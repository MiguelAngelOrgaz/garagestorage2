package com.garage.storage.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.garage.storage.dto.CajaDTO;
import com.garage.storage.dto.ObjetoDTO;
import com.garage.storage.entity.Balda;
import com.garage.storage.entity.Caja;
import com.garage.storage.entity.Categoria;
import com.garage.storage.entity.Estancia;
import com.garage.storage.entity.Estanteria;
import com.garage.storage.entity.Objeto;
import com.garage.storage.entity.Usuario;
import com.garage.storage.service.IAdministrationService;
import com.garage.storage.service.IObjectManagementService;
import com.garage.storage.service.IShelfManagementService;
import com.garage.storage.service.SecurityService;
import com.garage.storage.util.Constantes;
import com.garage.storage.util.Utils;
import com.garage.storage.validator.CajaValidator;
import com.garage.storage.validator.ObjetoValidator;

@Controller
public class ObjectManagementController {
	
	private static final String CAJASFORM = "cajasForm";
	private static final String CAJAFORM = "cajaForm";
	private static final String OBJETOFORM = "objetoForm";
	private static final String NUEVACAJA = "nuevaCaja";
	private static final String CONTENT = "contentImage";
	private static final String REDIRECT_LISTAROBJETOS = "redirect:/listarObjetos";
	private static final String REDIRECT_LISTARCAJAS = "redirect:/listarCajas";
	private static final String TITULO_CAJAS = "boton.cabecera.cajas.title";
	private static final String TITULO_OBJETOS = "titulo.objeto.gestion";
	private static final String STRING_IDOBJETO = "idObjeto";
	private static final String ERROR_ADJUNTAR = "error.caja.adjuntar";
	private static final String FORM_IDCAJA = "buscadorIdCaja";
	private static final String FORM_BALDA = "buscadorBalda";
	private static final String FORM_HABITACION = "buscadorHabitacion";
	private static final String FORM_ESTANTERIA = "buscadorEstanteria";

	@Autowired
    private SecurityService securityService;
	
	@Autowired
    private IObjectManagementService objectManagementService;
	
	@Autowired
    private IShelfManagementService shelfManagementService;
	
	@Autowired
	private CajaValidator cajaValidator;
	
	@Autowired
	private ObjetoValidator objetoValidator;
	
	@Autowired
	private IAdministrationService administrationService;
	
	@Autowired
    private MessageSource mensajes;
	
	@Autowired
    private ModelMapper modelMapper;
	
	@GetMapping("/cajasParaBalda/{idBalda}")
    public String cajasParaBalda(@PathVariable("idBalda") Long idBalda,Model model) {
		Balda b =  shelfManagementService.obtenerBalda(idBalda);
		if(securityService.estanteriaAccesible(b.getEstanteria().getIdestanteria()))
		{
			List<Caja> listaCajas = objectManagementService.listarCajas(true,securityService.userAuthenticated());
			model.addAttribute(CAJASFORM, listaCajas);
			model.addAttribute("idBalda",idBalda);
			model.addAttribute("idEstanteria",b.getEstanteria().getIdestanteria());
			return "cajasSinColocar";
		}
		return Constantes.REDIRECT_PRINCIPAL;
    }
	
	@GetMapping("/cajasParaBalda/nuevaCaja/{idBalda}")
    public String nuevaCaja(@PathVariable("idBalda") Long idBalda, Model model) {
		Balda balda = shelfManagementService.obtenerBalda(idBalda);
		if(securityService.estanteriaAccesible(balda.getEstanteria().getIdestanteria()))
		{
	        model.addAttribute(CAJAFORM, convertToDto(new Caja()));
	        model.addAttribute("actionForm", "/cajasParaBalda/nuevaCaja");
	        model.addAttribute("idBalda",idBalda);
	        model.addAttribute("idEstanteria", balda.getEstanteria().getIdestanteria());
	        return NUEVACAJA;
		}
		return "redirect:/cajasParaBalda/"+idBalda; 
    }
	
    @PostMapping("/cajasParaBalda/nuevaCaja")
    public String nuevaCaja(@ModelAttribute(CAJAFORM) CajaDTO cajaForm, BindingResult bindingResult, 
    		@RequestParam("file") MultipartFile imagen, @ModelAttribute("idEstanteria") Long idEstanteria,
    		@ModelAttribute("idBalda") Long idBalda) {
  
    	if(securityService.estanteriaAccesible(idEstanteria))
		{
    		Caja caja = convertToEntity(cajaForm);
    		cajaValidator.validate(caja, imagen, bindingResult);
    		if (bindingResult.hasErrors()) {
              return NUEVACAJA;
    		}
    	
    		if(!imagen.isEmpty())
    		{  						
    			byte[] bytes;
				try {
					bytes = imagen.getBytes();
					caja.setFoto(bytes);
				} catch (IOException e) {
					bindingResult.rejectValue("foto", ERROR_ADJUNTAR);   			
				}
    		}
    		caja.setUsuario(securityService.userAuthenticated());
    		objectManagementService.nuevaCaja(caja);
		}
    	return "redirect:/cajasParaBalda/"+idBalda;
    }
    
    @GetMapping("/insertarEnBalda/{idBalda}/caja/{idCaja}")
    public String insertarEnBalda(@PathVariable("idBalda") Long idBalda, @PathVariable("idCaja") Long idCaja) {
    	Balda b =  shelfManagementService.obtenerBalda(idBalda);
    	Caja c = objectManagementService.obtenerCaja(idCaja);
		if(securityService.estanteriaAccesible(b.getEstanteria().getIdestanteria()) && 
				securityService.cajaAccesible(idCaja))
		{
			objectManagementService.insertarEnBalda(c, b);
			return "redirect:/obtenerEstanteria/"+b.getEstanteria().getIdestanteria();
		}
		return Constantes.REDIRECT_PRINCIPAL;
    }
    
    @GetMapping("/sacarDeBalda/{idCaja}")
    public String sacarDeBalda(@PathVariable("idCaja") Long idCaja) {
    	Caja c = objectManagementService.obtenerCaja(idCaja);
    	Balda b = c.getBalda();
		if(securityService.cajaAccesible(idCaja))
		{
			objectManagementService.sacarDeBalda(c);
			return "redirect:/obtenerEstanteria/"+b.getEstanteria().getIdestanteria();
		}
		return Constantes.REDIRECT_PRINCIPAL;
    }
    
    @GetMapping("/nuevaCaja")
    public String nuevaCaja(Model model) {
	    model.addAttribute(CAJAFORM, convertToDto(new Caja()));
	    return NUEVACAJA; 
    }

    @PostMapping("/nuevaCaja")
    public String nuevaCaja(@ModelAttribute(CAJAFORM) CajaDTO cajaForm, BindingResult bindingResult, 
    		@RequestParam("file") MultipartFile imagen) {
  
    	Caja caja = convertToEntity(cajaForm);
    	cajaValidator.validate(caja, imagen, bindingResult);
    	if (bindingResult.hasErrors()) {
    		return NUEVACAJA;
    	}
    	
    	if(!imagen.isEmpty())
    	{  						
    		byte[] bytes;
			try {
				bytes = imagen.getBytes();
				caja.setFoto(bytes);
			} catch (IOException e) {
				bindingResult.rejectValue("foto", ERROR_ADJUNTAR);   			
			}
    	}
    	caja.setUsuario(securityService.userAuthenticated());
    	objectManagementService.nuevaCaja(caja);
    	return REDIRECT_LISTARCAJAS;
    }
    
    @GetMapping("/modificarCaja/{idCaja}")
   	public String modificarCaja(@PathVariable("idCaja") Long idCaja, Model model) {
   	
    	Caja caja = objectManagementService.obtenerCaja(idCaja);
    	//Solo se pueden modificar cajas que sean del usuario
       	if(caja!=null && securityService.cajaAccesible(idCaja))
       	{
   	    	if(caja.getFoto()!=null)
   	    	{
   	    		byte[] encodeBase64 = Base64.getEncoder().encode(caja.getFoto());
   	    		String base64Encoded = new String(encodeBase64, StandardCharsets.UTF_8);
   	    		model.addAttribute(CONTENT, base64Encoded );
   	    	}
     		model.addAttribute(CAJAFORM, convertToDto(caja));
     		model.addAttribute(Constantes.TITULO, mensajes.getMessage(TITULO_CAJAS, null, LocaleContextHolder.getLocale()));
 	   		return "editarCaja";
       	}
       	return REDIRECT_LISTARCAJAS;	
   	}
       
       
       @PostMapping("/modificarCaja")
       public String modificarCaja(@ModelAttribute(CAJAFORM) CajaDTO cajaForm, BindingResult bindingResult, 
    		   String eliminarImagen, Model model, @RequestParam("file") MultipartFile imagen) {
    	  
    	   //Solo se pueden modificar cajas que sean del usuario
       	  if(securityService.cajaAccesible(cajaForm.getIdcaja()))
       	  {
       		  Caja caja = convertToEntity(cajaForm);
       		   String base64Encoded = null;
	    	   cajaValidator.validate(caja, bindingResult);
	    	   if("N".equals(eliminarImagen) && imagen.isEmpty())
    		   {
	        	    byte[] encodeBase64 = Base64.getEncoder().encode(caja.getFoto());
		       		base64Encoded = new String(encodeBase64, StandardCharsets.UTF_8);
    		   }
	    	   else
	    	   {
	    		   if(!imagen.isEmpty())
	    		   {
	    			   byte[] bytes;
	   					try {
		   					bytes = imagen.getBytes();
		   					caja.setFoto(bytes);
		   				} catch (IOException e) {
		   					bindingResult.rejectValue("foto", ERROR_ADJUNTAR);   			
		   				}
	    		   }
	    		   else
	    			   caja.setFoto(null);
	    	   }
	    	   if (bindingResult.hasErrors()) {
			       model.addAttribute(CONTENT, base64Encoded );
	               return "editarCaja";
	           }
	    	   objectManagementService.modificarCaja(caja);  
       	  }
           
           
           return REDIRECT_LISTARCAJAS;
       }
       
    @GetMapping("/obtenerCaja/{idCaja}")
   	public String obtenerCaja(@PathVariable("idCaja") Long idCaja, Model model) {
   	
       	Caja caja = objectManagementService.obtenerCaja(idCaja);
      //Solo se pueden consultar cajas que sean del usuario
       	if(caja!=null && securityService.cajaAccesible(idCaja))
       	{
   	    	
   	    	model.addAttribute(CAJAFORM, convertToDto(caja));
   	    	if(caja.getFoto()!=null)
   	   	    {
   	   	    	byte[] encodeBase64 = Base64.getEncoder().encode(caja.getFoto());
   	   	    	String base64Encoded = new String(encodeBase64, StandardCharsets.UTF_8);
   	   	    	model.addAttribute(CONTENT, base64Encoded );
   	   	    }	
   	    	model.addAttribute(Constantes.TITULO, mensajes.getMessage(TITULO_CAJAS, null, LocaleContextHolder.getLocale()));
   	    		   
   	    	return "detalleCaja";
       	}
       	return Constantes.REDIRECT_PRINCIPAL;	
   	}
    
    @GetMapping("/nuevoObjeto")
    public String nuevoObjeto(Model model) {
        model.addAttribute(OBJETOFORM, convertToDto(new Objeto()));
        List<Categoria> listaCategorias = administrationService.listarCategorias(securityService.userAuthenticated());
        model.addAttribute(Constantes.TITULO, mensajes.getMessage(TITULO_OBJETOS, null, LocaleContextHolder.getLocale()));
        model.addAttribute(Constantes.MODEL_ALLCATEGORIES, listaCategorias);
        return "nuevoObjeto";
    }
	
	 @PostMapping("/nuevoObjeto")
	 public String nuevoObjeto(@ModelAttribute(OBJETOFORM) ObjetoDTO objetoForm, BindingResult bindingResult,
			 @RequestParam("file") MultipartFile imagen, Model model) {
		 Objeto objeto = convertToEntity(objetoForm);
		 objetoValidator.validate(objeto, bindingResult);
	     if (bindingResult.hasErrors()) {
	    	 List<Categoria> listaCategorias = administrationService.listarCategorias(securityService.userAuthenticated());
	 		 model.addAttribute(Constantes.MODEL_ALLCATEGORIES, listaCategorias);
	    	 return "nuevoObjeto";
	     }
	     if(!imagen.isEmpty())
 		 {  						
 			byte[] bytes;
				try {
					bytes = imagen.getBytes();
					objeto.setFoto(bytes);
				} catch (IOException e) {
					bindingResult.rejectValue("foto", ERROR_ADJUNTAR);   			
				}
 		 }
	     objeto.setUsuarioObjeto(securityService.userAuthenticated());    
	     objectManagementService.nuevoObjeto(objeto);

	     return REDIRECT_LISTAROBJETOS;
	 }
	 
	 @GetMapping("/modificarObjeto/{idObjeto}")
	   	public String modificarObjeto(@PathVariable(STRING_IDOBJETO) Long idObjeto, Model model) {
	   	
	    	Objeto objeto = objectManagementService.obtenerObjeto(idObjeto);
	    	//Solo se pueden modificar objetos que sean del usuario
	       	if(objeto!=null && securityService.objetoAccesible(idObjeto))
	       	{
	   	    	if(objeto.getFoto()!=null)
	   	    	{
	   	    		byte[] encodeBase64 = Base64.getEncoder().encode(objeto.getFoto());
	   	    		String base64Encoded = new String(encodeBase64, StandardCharsets.UTF_8);
	   	    		model.addAttribute(CONTENT, base64Encoded );
	   	    	}
	   	    	List<Categoria> listaCategorias = administrationService.listarCategoriasSuperioresVisibles(securityService.userAuthenticated());
	   	    	model.addAttribute(Constantes.MODEL_ALLCATEGORIES, listaCategorias);
		    	model.addAttribute("actionForm", "/modificarObjeto");
	     		model.addAttribute(OBJETOFORM, convertToDto(objeto));
	     		model.addAttribute(Constantes.TITULO, mensajes.getMessage(TITULO_OBJETOS, null, LocaleContextHolder.getLocale()));
	 	   		return "editarObjeto";
	       	}
	       	return Constantes.REDIRECT_PRINCIPAL;	
	   	}
	       
	       
   @PostMapping("/modificarObjeto")
   public String modificarObjeto(@ModelAttribute(OBJETOFORM) ObjetoDTO objetoForm, BindingResult bindingResult, 
		   String eliminarImagen, Model model, @RequestParam("file") MultipartFile imagen) {
	  
	   //Solo se pueden modificar objetos que sean del usuario
   	  if(securityService.objetoAccesible(objetoForm.getIdobjeto()))
   	  {
   		  Objeto objeto = convertToEntity(objetoForm);
   		   String base64Encoded = null;
    	   objetoValidator.validate(objeto, bindingResult);
    	   if("N".equals(eliminarImagen) && imagen.isEmpty())
		   {
        	    byte[] encodeBase64 = Base64.getEncoder().encode(objeto.getFoto());
	       		base64Encoded = new String(encodeBase64, StandardCharsets.UTF_8);
		   }
    	   else
    	   {
    		   if(!imagen.isEmpty())
    		   {
    			   byte[] bytes;
   					try {
	   					bytes = imagen.getBytes();
	   					objeto.setFoto(bytes);
	   				} catch (IOException e) {
	   					bindingResult.rejectValue("foto", ERROR_ADJUNTAR);   			
	   				}
    		   }
    		   else
    			   objeto.setFoto(null);
    	   }
    	   if (bindingResult.hasErrors()) {
    		   List<Categoria> listaCategorias = administrationService.listarCategoriasSuperioresVisibles(securityService.userAuthenticated());
   	    		model.addAttribute(Constantes.MODEL_ALLCATEGORIES, listaCategorias);
		       model.addAttribute(CONTENT, base64Encoded );
               return "editarObjeto";
           }
    	   objectManagementService.modificarObjeto(objeto);  
    	   return REDIRECT_LISTAROBJETOS;
   	  }                 
      return Constantes.REDIRECT_PRINCIPAL;
   }
   
   @GetMapping("/obtenerObjeto/{idObjeto}")
   public String obtenerObjeto(@PathVariable(STRING_IDOBJETO) Long idObjeto, Model model) {
  	
      	Objeto objeto = objectManagementService.obtenerObjeto(idObjeto);
      //Solo se pueden consultar objetos que sean del usuario
      	if(objeto!=null && securityService.objetoAccesible(idObjeto))
      	{
  	    	model.addAttribute(OBJETOFORM, convertToDto(objeto));
  	    	if(objeto.getFoto()!=null)
  	   		{
  	   			byte[] encodeBase64 = Base64.getEncoder().encode(objeto.getFoto());
  	   			String base64Encoded = new String(encodeBase64, StandardCharsets.UTF_8);
  	   			model.addAttribute(CONTENT, base64Encoded );
  	   		}	
  	    model.addAttribute(Constantes.TITULO, mensajes.getMessage(TITULO_OBJETOS, null, LocaleContextHolder.getLocale()));
  	    return "detalleObjeto";
  	    }
      	return Constantes.REDIRECT_PRINCIPAL;	
  	}
   
   @GetMapping("/listarObjetos")
   public String listarObjetos(Model model) {
	   Usuario usuario = securityService.userAuthenticated();
	   Collection<Objeto> objetos = usuario.getObjetos();
	   Collection<Estancia> estancias = usuario.getEstancias();
	   List<Estanteria> estanterias = new ArrayList<>();
	
	   for (Estancia estancia : estancias) {
	        estanterias.addAll(estancia.getEstanterias());
	   }
	   model.addAttribute("objetosForm", objetos);
	   model.addAttribute(Constantes.MODEL_ALLESTANCIAS, estancias);
	   model.addAttribute(Constantes.MODEL_ALLESTANTERIAS, estanterias);
	   model.addAttribute(Constantes.TITULO, mensajes.getMessage(TITULO_OBJETOS, null, LocaleContextHolder.getLocale()));
	   return "listaObjetos";
   }
   
   @PostMapping("/listarObjetos")
   public String listarObjetos(@ModelAttribute(FORM_IDCAJA) String buscadorIdCaja, 
		   @ModelAttribute("buscadorIdObjeto") String buscadorIdObjeto, @ModelAttribute(FORM_HABITACION) String buscadorHabitacion,
		   @ModelAttribute(FORM_BALDA) String buscadorBalda, @ModelAttribute("buscadorNombre") String buscadorNombre, 
		   @ModelAttribute(FORM_ESTANTERIA) String buscadorEstanteria, Model model) {
	   Usuario usuario = securityService.userAuthenticated();
	   Collection<Estancia> estancias = usuario.getEstancias();
	   List<Estanteria> estanterias = new ArrayList<>();
	
	   for (Estancia estancia : estancias) {
	        estanterias.addAll(estancia.getEstanterias());
	   }
	   model.addAttribute(FORM_IDCAJA,buscadorIdCaja);
	   model.addAttribute("buscadorIdObjeto",buscadorIdObjeto);
	   model.addAttribute(FORM_HABITACION,buscadorHabitacion);
	   model.addAttribute(FORM_BALDA,buscadorBalda);
	   model.addAttribute("buscadorNombre",buscadorNombre);
	   model.addAttribute(FORM_ESTANTERIA,buscadorEstanteria);
	   model.addAttribute(Constantes.MODEL_ALLESTANCIAS, estancias);
	   model.addAttribute(Constantes.MODEL_ALLESTANTERIAS, estanterias);
	   model.addAttribute(Constantes.TITULO, mensajes.getMessage(TITULO_OBJETOS, null, LocaleContextHolder.getLocale()));
	   
	   List<Objeto> listaObjetos = objectManagementService.localizarObjeto
			   (Utils.esNuloVacio(buscadorIdObjeto)?null:Long.parseLong(buscadorIdObjeto), buscadorNombre, 
				Utils.esNuloVacio(buscadorIdCaja)?null:Long.parseLong(buscadorIdCaja), 
				Utils.esNuloVacio(buscadorHabitacion)?null:Long.parseLong(buscadorHabitacion), 
				Utils.esNuloVacio(buscadorEstanteria)?null:Long.parseLong(buscadorEstanteria), 
				Utils.esNuloVacio(buscadorBalda)?null:Long.parseLong(buscadorBalda), usuario);
	   model.addAttribute("objetosForm", listaObjetos);
   	return "listaObjetos";
   }
   
   @GetMapping("/listarCajas")
   public String listarCajas(Model model) {
	   Usuario usuario = securityService.userAuthenticated();
	   Collection<Caja> cajas = usuario.getCajas();
	   Collection<Estancia> estancias = usuario.getEstancias();
	   List<Estanteria> estanterias = new ArrayList<>();
	
	   for (Estancia estancia : estancias) {
	        estanterias.addAll(estancia.getEstanterias());
	   }
	   model.addAttribute(CAJASFORM, cajas);
	   model.addAttribute(Constantes.MODEL_ALLESTANCIAS, estancias);
	   model.addAttribute(Constantes.MODEL_ALLESTANTERIAS, estanterias);
	   model.addAttribute(Constantes.TITULO, mensajes.getMessage(TITULO_CAJAS, null, LocaleContextHolder.getLocale()));
	   return "listaCajas";
   }
   
   @PostMapping("/listarCajas")
   public String listarCajas(@ModelAttribute(FORM_IDCAJA) String buscadorIdCaja, 
		   @ModelAttribute(FORM_HABITACION) String buscadorHabitacion,
		   @ModelAttribute(FORM_BALDA) String buscadorBalda, @ModelAttribute("buscadorDescripcion") String buscadorDescripcion, 
		   @ModelAttribute(FORM_ESTANTERIA) String buscadorEstanteria, Model model) {
	   Usuario usuario = securityService.userAuthenticated();
	   Collection<Estancia> estancias = usuario.getEstancias();
	   List<Estanteria> estanterias = new ArrayList<>();
	
	   for (Estancia estancia : estancias) {
	        estanterias.addAll(estancia.getEstanterias());
	   }
	   model.addAttribute(FORM_IDCAJA,buscadorIdCaja);
	   model.addAttribute(FORM_HABITACION,buscadorHabitacion);
	   model.addAttribute(FORM_BALDA,buscadorBalda);
	   model.addAttribute("buscadorDescripcion",buscadorDescripcion);
	   model.addAttribute(FORM_ESTANTERIA,buscadorEstanteria);
	   model.addAttribute(Constantes.MODEL_ALLESTANCIAS, estancias);
	   model.addAttribute(Constantes.MODEL_ALLESTANTERIAS, estanterias);
	   model.addAttribute(Constantes.TITULO, mensajes.getMessage(TITULO_CAJAS, null, LocaleContextHolder.getLocale()));
	   
	   List<Caja> listaCajas = objectManagementService.localizarCaja
			   (Utils.esNuloVacio(buscadorIdCaja)?null:Long.parseLong(buscadorIdCaja), buscadorDescripcion, 
				Utils.esNuloVacio(buscadorHabitacion)?null:Long.parseLong(buscadorHabitacion), 
				Utils.esNuloVacio(buscadorEstanteria)?null:Long.parseLong(buscadorEstanteria), 
				Utils.esNuloVacio(buscadorBalda)?null:Long.parseLong(buscadorBalda), usuario);
	   model.addAttribute(CAJASFORM, listaCajas);
   	return "listaCajas";
   }
   
   @GetMapping("/insertarEnCaja/{idObjeto}")
   public String insertarEnCaja(@PathVariable(STRING_IDOBJETO) Long idObjeto, Model model) {
	   if(securityService.objetoAccesible(idObjeto))
	   {
		   Usuario usuario = securityService.userAuthenticated();
		   Collection<Caja> cajas = usuario.getCajas();
		   Collection<Estancia> estancias = usuario.getEstancias();
		   List<Estanteria> estanterias = new ArrayList<>();
		
		   for (Estancia estancia : estancias) {
		        estanterias.addAll(estancia.getEstanterias());
		   }
		   model.addAttribute(STRING_IDOBJETO,idObjeto);
		   model.addAttribute(CAJASFORM, cajas);
		   model.addAttribute(Constantes.MODEL_ALLESTANCIAS, estancias);
		   model.addAttribute(Constantes.MODEL_ALLESTANTERIAS, estanterias);
		   model.addAttribute(Constantes.TITULO, mensajes.getMessage(TITULO_OBJETOS, null, LocaleContextHolder.getLocale()));
		   return "insertarEnCaja";
	   }
	   return REDIRECT_LISTARCAJAS; 
   }
   
   @PostMapping("/insertarEnCaja")
   public String insertarEnCaja(@ModelAttribute(FORM_IDCAJA) String buscadorIdCaja, 
		   @ModelAttribute(FORM_HABITACION) String buscadorHabitacion,
		   @ModelAttribute(FORM_BALDA) String buscadorBalda, @ModelAttribute("buscadorDescripcion") String buscadorDescripcion, 
		   @ModelAttribute(FORM_ESTANTERIA) String buscadorEstanteria, 
		   @ModelAttribute(STRING_IDOBJETO) String idObjeto, Model model) {
	   Usuario usuario = securityService.userAuthenticated();
	   Collection<Estancia> estancias = usuario.getEstancias();
	   List<Estanteria> estanterias = new ArrayList<>();
	
	   for (Estancia estancia : estancias) {
	        estanterias.addAll(estancia.getEstanterias());
	   }
	   model.addAttribute(FORM_IDCAJA,buscadorIdCaja);
	   model.addAttribute(FORM_HABITACION,buscadorHabitacion);
	   model.addAttribute(FORM_BALDA,buscadorBalda);
	   model.addAttribute("buscadorDescripcion",buscadorDescripcion);
	   model.addAttribute(FORM_ESTANTERIA,buscadorEstanteria);
	   model.addAttribute(Constantes.MODEL_ALLESTANCIAS, estancias);
	   model.addAttribute(Constantes.MODEL_ALLESTANTERIAS, estanterias);
	   model.addAttribute(STRING_IDOBJETO,idObjeto);
	   
	   List<Caja> listaCajas = objectManagementService.localizarCaja
			   (Utils.esNuloVacio(buscadorIdCaja)?null:Long.parseLong(buscadorIdCaja), buscadorDescripcion, 
				Utils.esNuloVacio(buscadorHabitacion)?null:Long.parseLong(buscadorHabitacion), 
				Utils.esNuloVacio(buscadorEstanteria)?null:Long.parseLong(buscadorEstanteria), 
				Utils.esNuloVacio(buscadorBalda)?null:Long.parseLong(buscadorBalda), usuario);
	   model.addAttribute(CAJASFORM, listaCajas);
   	return "insertarEnCaja";
   }
   
   @GetMapping("/insertarObjetoEnCaja/{idObjeto}/{idCaja}")
   public String insertarEnCaja(@PathVariable(STRING_IDOBJETO) Long idObjeto, @PathVariable("idCaja") Long idCaja, Model model) {
	   if(securityService.objetoAccesible(idObjeto) && securityService.cajaAccesible(idCaja))
	   {
		   Objeto objeto = objectManagementService.obtenerObjeto(idObjeto);
		   Caja caja = objectManagementService.obtenerCaja(idCaja);
		   
		   objeto.setCaja(caja);
		   objectManagementService.modificarObjeto(objeto);
		   
		   Usuario usuario = securityService.userAuthenticated();
		   Collection<Caja> cajas = usuario.getCajas();
		   Collection<Estancia> estancias = usuario.getEstancias();
		   List<Estanteria> estanterias = new ArrayList<>();
		
		   for (Estancia estancia : estancias) {
		        estanterias.addAll(estancia.getEstanterias());
		   }
		   model.addAttribute(STRING_IDOBJETO,idObjeto);
		   model.addAttribute(CAJASFORM, cajas);
		   model.addAttribute(Constantes.MODEL_ALLESTANCIAS, estancias);
		   model.addAttribute(Constantes.MODEL_ALLESTANTERIAS, estanterias);
		   return REDIRECT_LISTAROBJETOS;
	   }
	   return REDIRECT_LISTARCAJAS; 
   }
   
   @GetMapping("/sacarObjetoDeCaja/{idObjeto}")
   public String sacarObjetoDeCaja(@PathVariable(STRING_IDOBJETO) Long idObjeto, Model model) {
	   if(securityService.objetoAccesible(idObjeto))
	   {
		   Objeto objeto = objectManagementService.obtenerObjeto(idObjeto);		   
		   objeto.setCaja(null);
		   objectManagementService.modificarObjeto(objeto);
		   
		   Usuario usuario = securityService.userAuthenticated();
		   Collection<Caja> cajas = usuario.getCajas();
		   Collection<Estancia> estancias = usuario.getEstancias();
		   List<Estanteria> estanterias = new ArrayList<>();
		
		   for (Estancia estancia : estancias) {
		        estanterias.addAll(estancia.getEstanterias());
		   }
		   model.addAttribute(STRING_IDOBJETO,idObjeto);
		   model.addAttribute(CAJASFORM, cajas);
		   model.addAttribute(Constantes.MODEL_ALLESTANCIAS, estancias);
		   model.addAttribute(Constantes.MODEL_ALLESTANTERIAS, estanterias);
		   return REDIRECT_LISTAROBJETOS;
	   }
	   return Constantes.REDIRECT_PRINCIPAL; 
   }
   
   @GetMapping("/eliminarCaja/{idCaja}")
   public String eliminarCaja(@PathVariable("idCaja") Long idCaja, Model model) {
	   if(securityService.cajaAccesible(idCaja))
	   {
		   Caja caja = objectManagementService.obtenerCaja(idCaja);		   
		   objectManagementService.eliminarCaja(caja);
		   return REDIRECT_LISTARCAJAS;
	   }
	   return Constantes.REDIRECT_PRINCIPAL; 
   }
   
   @GetMapping("/eliminarObjeto/{idObjeto}")
   public String eliminarObjeto(@PathVariable(STRING_IDOBJETO) Long idObjeto, Model model) {
	   if(securityService.objetoAccesible(idObjeto))
	   {
		   Objeto objeto = objectManagementService.obtenerObjeto(idObjeto);	
		   objectManagementService.eliminarObjeto(objeto);
		   return REDIRECT_LISTAROBJETOS;
	   }
	   return Constantes.REDIRECT_PRINCIPAL; 
   }
   
   /**
    * Método que convierte un Entity a un DTO
    * @param caja Caja Entity que se quiere convertir
    * @return CajaDTO caja DTO convertida
    */
   private CajaDTO convertToDto(Caja caja) {
   	return modelMapper.map(caja, CajaDTO.class);
   }
   
   /**
    * Método que convierte un DTO a un Entity
    * @param cajaDTO CajaDTO que se quiere convertir
    * @return Caja objeto entity convertido
    */
   private Caja convertToEntity(CajaDTO cajaDTO) throws ParseException {
   	return modelMapper.map(cajaDTO, Caja.class);
   }
   
   /**
    * Método que convierte un Entity a un DTO
    * @param objeto Objeto Entity que se quiere convertir
    * @return ObjetoDTO objeto DTO convertido
    */
   private ObjetoDTO convertToDto(Objeto objeto) {
   	return modelMapper.map(objeto, ObjetoDTO.class);
   }
   
   /**
    * Método que convierte un DTO a un Entity
    * @param objetoDTO ObjetoDTO que se quiere convertir
    * @return Objeto objeto entity convertido
    */
   private Objeto convertToEntity(ObjetoDTO objetoDTO) throws ParseException {
   	return modelMapper.map(objetoDTO, Objeto.class);
   }
}
