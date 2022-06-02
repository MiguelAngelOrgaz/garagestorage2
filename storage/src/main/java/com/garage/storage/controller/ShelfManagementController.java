package com.garage.storage.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.garage.storage.dto.EstanciaDTO;
import com.garage.storage.dto.EstanteriaDTO;
import com.garage.storage.entity.Balda;
import com.garage.storage.entity.Estancia;
import com.garage.storage.entity.Estanteria;
import com.garage.storage.entity.Usuario;
import com.garage.storage.service.IShelfManagementService;
import com.garage.storage.service.SecurityService;
import com.garage.storage.util.Constantes;
import com.garage.storage.util.Utils;
import com.garage.storage.validator.EstanciaValidator;
import com.garage.storage.validator.EstanteriaValidator;

@Controller
public class ShelfManagementController {
	
	private static final String ESTANCIAFORM = "estanciaForm";
	private static final String ESTANTERIAFORM = "estanteriaForm";
	private static final String REDIRECT_OBTENERESTANTERIA = "redirect:/obtenerEstanteria/";
	private static final String REDIRECT_LISTARHABITACIONES = "redirect:/listarHabitaciones/";
	private static final String TITULO_HABITACIONES = "titulo.habitaciones";
	@Autowired
	private IShelfManagementService shelfManagementService;
	
	@Autowired
    private SecurityService securityService;
	
	@Autowired
    private EstanciaValidator estanciaValidator;
	
	@Autowired
    private EstanteriaValidator estanteriaValidator;
	
	@Autowired
    private MessageSource mensajes;
	
	@Autowired
    private ModelMapper modelMapper;
	
	@GetMapping("/listarHabitaciones")
    public String listarHabitaciones(Model model) {
		Usuario user = securityService.userAuthenticated();
		List<Estancia> listaEstancias = shelfManagementService.listarHabitaciones(user.getIdusuario());
		model.addAttribute(ESTANCIAFORM, listaEstancias);
		model.addAttribute(Constantes.TITULO, mensajes.getMessage(TITULO_HABITACIONES, null, LocaleContextHolder.getLocale()));
		return "listaHabitaciones";
    }
	
	@GetMapping("/nuevaHabitacion")
    public String nuevaHabitacion(Model model) {
        model.addAttribute(ESTANCIAFORM, convertToDto(new Estancia()));
        model.addAttribute(Constantes.TITULO, mensajes.getMessage(TITULO_HABITACIONES, null, LocaleContextHolder.getLocale()));
        return "nuevaHabitacion";
    }

    @PostMapping("/nuevaHabitacion")
    public String nuevaHabitacion(@ModelAttribute(ESTANCIAFORM) EstanciaDTO estanciaForm, BindingResult bindingResult) {
    	estanciaValidator.validate(estanciaForm, bindingResult);
        if (bindingResult.hasErrors()) {
            return "nuevaHabitacion";
        }
        Estancia estancia = convertToEntity(estanciaForm);   
        shelfManagementService.nuevaHabitacion(estancia, securityService.nameUserAuthenticated());

        return REDIRECT_LISTARHABITACIONES;
    }
     
    @GetMapping("/modificarHabitacion/{idHabitacion}")
	public String modificarHabitacion(@PathVariable("idHabitacion") Long idHabitacion, Model model) {
	
    	Estancia estancia = shelfManagementService.obtenerHabitacion(idHabitacion);
    	//Solo se pueden modificar habitaciones que sean del usuario
    	if(estancia!=null && securityService.estanciaAccesible(idHabitacion))
    	{
    		model.addAttribute(Constantes.TITULO, mensajes.getMessage(TITULO_HABITACIONES, null, LocaleContextHolder.getLocale()));
	    	model.addAttribute(ESTANCIAFORM, convertToDto(estancia));
	    	return "modificarHabitacion";	    	
    	}
    	return Constantes.REDIRECT_PRINCIPAL;	
	}
    
    
    @PostMapping("/modificarHabitacion")
    public String modificarHabitacion(@ModelAttribute(ESTANCIAFORM) EstanciaDTO estanciaForm, BindingResult bindingResult) {
    	estanciaValidator.validate(estanciaForm, bindingResult);
        
        if (bindingResult.hasErrors()) {
            return "modificarHabitacion";
        }
        Estancia estancia = convertToEntity(estanciaForm);
        Estancia estanciaOld = shelfManagementService.obtenerHabitacion(estanciaForm.getIdestancia());
        //Solo se pueden modificar habitaciones que sean del usuario
    	if(securityService.estanciaAccesible(estanciaOld.getIdestancia()))
    	{
    		estancia.setUsuario(estanciaOld.getUsuario());
    		shelfManagementService.modificarHabitacion(estancia);
    		return "redirect:listarHabitaciones";
    	}
        
        
        return Constantes.REDIRECT_PRINCIPAL;
    }
    
    
    @GetMapping("/listarEstanterias/{id}")
    public String listarEstanterias(@PathVariable("id") Long id,Model model) {
    	Estancia estancia = shelfManagementService.obtenerHabitacion(id);
    	if(estancia!=null && securityService.estanciaAccesible(id))
    	{	
			model.addAttribute("estanciaId", estancia.getIdestancia());
			model.addAttribute("estanciaNombre", estancia.getNombre());
			model.addAttribute(ESTANTERIAFORM, estancia.getEstanterias());
			model.addAttribute(Constantes.TITULO, mensajes.getMessage("titulo.estanterias", null, LocaleContextHolder.getLocale()));
			return "listaEstanterias";
    	}
		return Constantes.REDIRECT_PRINCIPAL;
    }
    
    @GetMapping("/eliminarHabitacion/{idHabitacion}")
    public String eliminarHabitacion(@PathVariable("idHabitacion") Long idHabitacion, RedirectAttributes redirectAttrs) {
    	Estancia estancia = shelfManagementService.obtenerHabitacion(idHabitacion);
    	if(estancia!=null && securityService.estanciaAccesible(idHabitacion))
    	{				
			int eliminar = shelfManagementService.eliminarHabitacion(estancia);
			if (eliminar<0)
				redirectAttrs.addFlashAttribute("mensaje", mensajes.getMessage("mensaje.error.eliminar.estancia", null, LocaleContextHolder.getLocale()));
			return REDIRECT_LISTARHABITACIONES;
		}
		return Constantes.REDIRECT_PRINCIPAL;
    }  
    
    @GetMapping("/nuevaEstanteria/{id}")
    public String nuevaEstanteria(@PathVariable("id") Long id,Model model) {
    	Estancia estancia = shelfManagementService.obtenerHabitacion(id);
    	Estanteria estanteria = new Estanteria();
    	estanteria.setEstancia(estancia);
        model.addAttribute(ESTANTERIAFORM, convertToDto(estanteria));
        model.addAttribute(Constantes.TITULO, mensajes.getMessage("titulo.nuevaEstanteria", null, LocaleContextHolder.getLocale()));
        return "nuevaEstanteria";
    }

    @PostMapping("/nuevaEstanteria")
    public String nuevaEstanteria(@ModelAttribute(ESTANTERIAFORM) EstanteriaDTO estanteriaForm, BindingResult bindingResult) {
    	estanteriaValidator.validate(estanteriaForm, bindingResult);
        if (bindingResult.hasErrors()) {
            return "nuevaEstanteria";
        }
		if(securityService.isTheUserAuthenticated(estanteriaForm.getEstancia().getUsuario().getNombre()))  
		{
			Estanteria estanteria = convertToEntity(estanteriaForm);
			shelfManagementService.nuevaEstanteria(estanteria);
			return "redirect:/listarEstanterias/"+estanteria.getEstancia().getIdestancia();
		}
    	
        return Constantes.REDIRECT_PRINCIPAL;
    }
    
    @GetMapping("/modificarEstanteria/{idEstanteria}")
   	public String modificarEstanteria(@PathVariable("idEstanteria") Long idEstanteria, Model model) {
   	
    	Estanteria estanteria = shelfManagementService.obtenerEstanteria(idEstanteria);
       	
       	if(estanteria!=null)
       	{
   	    	//Solo se pueden modificar estanterías que sean del usuario
   	    	String username = securityService.nameUserAuthenticated();
   	    	if(username.equals(estanteria.getEstancia().getUsuario().getNombre()))
   	    	{
   	    			model.addAttribute(ESTANTERIAFORM, convertToDto(estanteria));
   	    			return "editarEstanteria";
   	    	}
       	}
       	return Constantes.REDIRECT_PRINCIPAL;	
   	}
       
       
       @PostMapping("/modificarEstanteria")
       public String modificarEstanteria(@ModelAttribute(ESTANTERIAFORM) EstanteriaDTO estanteriaForm, BindingResult bindingResult) {
    	   estanteriaValidator.validate(estanteriaForm, bindingResult);
           
           if (bindingResult.hasErrors()) {
               return "editarEstanteria";
           }
           Estanteria estanteria = convertToEntity(estanteriaForm);
           Estanteria estanteriaOld = shelfManagementService.obtenerEstanteria(estanteriaForm.getIdestanteria());
           //Solo se pueden modificar habitaciones que sean del usuario
	       	String username = securityService.nameUserAuthenticated();
	       	if(username.equals(estanteriaOld.getEstancia().getUsuario().getNombre()))
	       	{
	       		estanteria.setEstancia(estanteriaOld.getEstancia());
	       		shelfManagementService.modificarEstanteria(estanteria);
	       		return "redirect:listarEstanterias/"+estanteria.getEstancia().getIdestancia();
	       	}
           
           
           return Constantes.REDIRECT_PRINCIPAL;
       }

       @GetMapping("/obtenerEstanteria/{idEstanteria}")
       public String obtenerEstanteria(@PathVariable("idEstanteria") Long idEstanteria,Model model) {
	       	Estanteria estanteria = shelfManagementService.obtenerEstanteria(idEstanteria);
	       	if(securityService.estanteriaAccesible(idEstanteria))
	       	{	
	       		model.addAttribute(Constantes.TITULO, mensajes.getMessage("title.estanteria.detalle", null, LocaleContextHolder.getLocale()));
	   			model.addAttribute("estanteria", convertToDto(estanteria));
	   			return "detalleEstanteria";
	       	}
	   		return Constantes.REDIRECT_PRINCIPAL;
       }
       
       @PostMapping("/nuevaBalda")
       public String nuevaBalda(@ModelAttribute("idEstanteria") String idEstanteria) {
    	   
    	   if(!Utils.esNuloVacio(idEstanteria) && securityService.estanteriaAccesible(Long.parseLong(idEstanteria)))
    	   {
    		   Estanteria estanteria = shelfManagementService.obtenerEstanteria(Long.parseLong(idEstanteria));
    		   int posicion = 1;
    		   if(estanteria.getBaldas()!=null && !estanteria.getBaldas().isEmpty())
    			   posicion+=estanteria.getBaldas().size();
    		   Balda balda = new Balda();
    		   balda.setPosicion(posicion);
    		   balda.setEstanteria(estanteria);
    		   shelfManagementService.nuevaBalda(balda);
    		   return REDIRECT_OBTENERESTANTERIA+idEstanteria;
    	   }
                    
           return Constantes.REDIRECT_PRINCIPAL;
       }
       
       @GetMapping("/eliminarBalda/{idBalda}")
       public String eliminarBalda(@PathVariable("idBalda") Long idBalda, RedirectAttributes redirectAttrs) {
       	Balda b = shelfManagementService.obtenerBalda(idBalda);
   		if(securityService.estanteriaAccesible(b.getEstanteria().getIdestanteria()))
   		{ 			
   			int eliminar = shelfManagementService.eliminarBalda(b);
   			if (eliminar<0)
   				redirectAttrs.addFlashAttribute("mensaje", mensajes.getMessage("mensaje.error.eliminar.balda", null, LocaleContextHolder.getLocale()));
   			return REDIRECT_OBTENERESTANTERIA+b.getEstanteria().getIdestanteria();
   		}
   		return Constantes.REDIRECT_PRINCIPAL;
       }  
       
       @GetMapping("/baldaLlena/{idBalda}")
       public String baldaLlena(@PathVariable("idBalda") Long idBalda) {
       	Balda b = shelfManagementService.obtenerBalda(idBalda);
   		if(securityService.estanteriaAccesible(b.getEstanteria().getIdestanteria()))
   		{ 		
   			b.setLlena(!b.getLlena());
   			shelfManagementService.modificarBalda(b);
   			return REDIRECT_OBTENERESTANTERIA+b.getEstanteria().getIdestanteria();
   		}
   		return Constantes.REDIRECT_PRINCIPAL;
       } 
       
       @GetMapping("/eliminarEstanteria/{idEstanteria}")
       public String eliminarEstanteria(@PathVariable("idEstanteria") Long idEstanteria, RedirectAttributes redirectAttrs) {
       	Estanteria estanteria = shelfManagementService.obtenerEstanteria(idEstanteria);
       	Long idEstancia = estanteria.getEstancia().getIdestancia();
   		if(securityService.estanteriaAccesible(estanteria.getIdestanteria()))
   		{ 			
   			int eliminar = shelfManagementService.eliminaEstanteria(estanteria);
   			if (eliminar<0)
   				redirectAttrs.addFlashAttribute("mensaje", mensajes.getMessage("mensaje.error.eliminar.estanteria", null, LocaleContextHolder.getLocale()));
   			return "redirect:/listarEstanterias/"+idEstancia;
   		}
   		return Constantes.REDIRECT_PRINCIPAL;
       }  
   	
       /**
        * Método que convierte un Entity a un DTO
        * @param objeto Estanteria Entity que se quiere convertir
        * @return EstanteriaDTO estanteria DTO convertido
        */
       private EstanteriaDTO convertToDto(Estanteria estanteria) {
       	return modelMapper.map(estanteria, EstanteriaDTO.class);
       }
       
       /**
        * Método que convierte un DTO a un Entity
        * @param estanteriaDTO EstanteriaDTO que se quiere convertir
        * @return Estanteria estanteria entity convertido
        */
       private Estanteria convertToEntity(EstanteriaDTO estanteriaDTO) throws ParseException {
       	return modelMapper.map(estanteriaDTO, Estanteria.class);
       }
       
       /**
        * Método que convierte un Entity a un DTO
        * @param objeto Estanteria Entity que se quiere convertir
        * @return EstanciaDTO estanteria DTO convertido
        */
       private EstanciaDTO convertToDto(Estancia estancia) {
       	return modelMapper.map(estancia, EstanciaDTO.class);
       }
       
       /**
        * Método que convierte un DTO a un Entity
        * @param estanteriaDTO EstanteriaDTO que se quiere convertir
        * @return Estanteria estanteria entity convertido
        */
       private Estancia convertToEntity(EstanciaDTO estanciaDTO) throws ParseException {
       	return modelMapper.map(estanciaDTO, Estancia.class);
       }
}
