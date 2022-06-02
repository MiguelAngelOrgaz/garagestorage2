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

import com.garage.storage.dto.CategoriaDTO;
import com.garage.storage.entity.Categoria;
import com.garage.storage.entity.Usuario;
import com.garage.storage.service.IAdministrationService;
import com.garage.storage.service.SecurityService;
import com.garage.storage.util.Constantes;
import com.garage.storage.validator.CategoriaValidator;

/**
 * Esta clase es el controlador de la capa de presentación relacionada con la administración
 * @author: Miguel Ángel Orgaz 
 */
@Controller
public class AdministrationController {
	private static final String REDIRECT_LISTARCATEGORIAS = "redirect:/listarCategorias";
	private static final String TITULO_CATEGORIA = "titulo.categorias";
	
	@Autowired
	private IAdministrationService administrationService;
	
	@Autowired
    private SecurityService securityService;
	
	@Autowired
	private CategoriaValidator categoriaValidator;
	
	@Autowired
    private MessageSource mensajes;
	
	@Autowired
    private ModelMapper modelMapper;
	/**
     * Prepara el modelo y selecciona la vista para reactivar usuarios
     * @param model El modelo que se pasará a la vista. userForm con la lista de usuarios bloqueados y tituloPagina
     * @return El nombre de la vista para reactivar usuarios
     */	
	@GetMapping("/reactivarUsuario")
    public String reactivarUsuario(Model model) {
		List<Usuario> listaUsuarios = administrationService.listarUsuarios('N');
        model.addAttribute("userForm", listaUsuarios);
        model.addAttribute(Constantes.TITULO, mensajes.getMessage("reactivar.titulo", null, LocaleContextHolder.getLocale()));
        return "reactivarUsuario";
    }
	
	/**
     * Método que recibe los datos del usuario desde la vista para reactivar usuarios, llamando al servicio correspondiente
     * de la capa de negocio
     * @param idUsuario El identificador del usuario a reactivar
     * @return El nombre de la vista para reactivar usuarios
     */	
	@GetMapping("/reactivarUsuario/{id}")
    public String reactivarUsuario(@PathVariable("id") Long idUsuario) {	
		administrationService.reactivarUsuario(idUsuario);
        return "redirect:/reactivarUsuario";
    }
	
	/**
     * Prepara el modelo y selecciona la vista para mostrar las categorías
     * @param model El modelo que se pasará a la vista. categoriasForm con la lista de categorías y tituloPagina
     * @return El nombre de la vista que lista las categorías
     */	
	@GetMapping("/listarCategorias")
    public String listarCategorias(Model model) {
		List<Categoria> listaCategorias = administrationService.listarCategoriasSuperiores(securityService.userAuthenticated());
		model.addAttribute("categoriasForm", listaCategorias);
		model.addAttribute(Constantes.TITULO, mensajes.getMessage(TITULO_CATEGORIA, null, LocaleContextHolder.getLocale()));
		return "listaCategorias";
    }
	
	/**
     * Prepara el modelo y selecciona la vista para crear una nueva cateogría
     * @param model El modelo que se pasará a la vista. categoriasAll con la lista de categorías, categoriasForm y tituloPagina
     * @return El nombre de la vista para crear una categoría
     */
	@GetMapping("/nuevaCategoria")
    public String nuevaCategoria(Model model) {
		List<Categoria> listaCategorias = administrationService.listarCategoriasSuperioresVisibles(securityService.userAuthenticated());
		model.addAttribute(Constantes.MODEL_ALLCATEGORIES, listaCategorias);
        model.addAttribute("categoriaForm", new Categoria());
        model.addAttribute(Constantes.TITULO, mensajes.getMessage(TITULO_CATEGORIA, null, LocaleContextHolder.getLocale()));
        return "nuevaCategoria";
    }
	
	/**
     * Método que recibe los datos de la categoría nueva, llamando al servicio de la capa de negocio que crea una categoría
     * @param categoriasForm Con todos los datos de la neuva categoría
     * @param bindingResult Almacena los posibles errores de validación
     * @return Redirección a la lista de categorías
     */	
	 @PostMapping("/nuevaCategoria")
	 public String nuevaCategoria(@ModelAttribute("categoriaForm") CategoriaDTO categoriaForm, BindingResult bindingResult) {
		 Categoria categoria = convertToEntity(categoriaForm);
		 categoriaValidator.validate(categoria, bindingResult);
	     if (bindingResult.hasErrors()) {
	    	 return "nuevaCategoria";
	     }
	     Object admin = Constantes.ROLADMINISTRADOR;
	     if(securityService.userAuthenticated().getRoles().contains(admin))
	    	 categoria.setEsfija('S');
	     else
	    	 categoria.setEsfija('N');
	     categoria.setUsuario(securityService.userAuthenticated());    
	     administrationService.nuevaCategoria(categoria);
	     return REDIRECT_LISTARCATEGORIAS;
	 }
	 
	 
	 /**
	 * Prepara el modelo y selecciona la vista para modificar la categoría seleccionada
	 * @param model El modelo que se pasará a la vista.
	 * @param idCategoria Identificador de la categoría que se quiere modificar
	 * @return El nombre de la vista para modificar categorias
	 */		 
	@GetMapping("/modificarCategoria/{idCategoria}")
	public String modificarCategoria(@PathVariable("idCategoria") Long idCategoria, Model model) {	
    	Categoria categoria = administrationService.obtenerCategoria(idCategoria);	
    	//Solo se pueden modificar categorias que sean del usuario
    	if(categoria!=null && securityService.categoriaAccesible(idCategoria))
    	{
		 	List<Categoria> listaCategorias = administrationService.listarCategoriasSuperioresVisibles(securityService.userAuthenticated());
		 	model.addAttribute(Constantes.MODEL_ALLCATEGORIES, listaCategorias);
		 	model.addAttribute("categoriaForm", convertToDto(categoria));
	    	model.addAttribute("tituloPagina", mensajes.getMessage(TITULO_CATEGORIA, null, LocaleContextHolder.getLocale()));
	    	return "editarCategoria";
    	}
    	return Constantes.REDIRECT_PRINCIPAL;	
	}
    
	/**
     * Método que recibe los datos de la categoría a modificar, llamando al servicio de la capa de negocio que modifica una categoría
     * @param categoriasForm Con todos los datos de la categoría a modificar
     * @param bindingResult Almacena los posibles errores de validación
     * @param model Modelo con los datos para pasar la pantalla de editarCategoria en caso de fallo
     * @return Redirección a la lista de categorías si ha ido bien
     */	
    @PostMapping("/modificarCategoria")
    public String modificarCategoria(@ModelAttribute("categoriaForm") CategoriaDTO categoriaForm, BindingResult bindingResult, 
    		Model model) {    	
    	Categoria categoria = convertToEntity(categoriaForm);
    	categoriaValidator.validate(categoria, bindingResult);
        if (bindingResult.hasErrors()) {
        	List<Categoria> listaCategorias = administrationService.listarCategoriasSuperioresVisibles(securityService.userAuthenticated());
    		model.addAttribute(Constantes.MODEL_ALLCATEGORIES, listaCategorias);
            return "editarCategoria";
        }
        //Solo se pueden modificar categorías que sean del usuario
    	if(securityService.categoriaAccesible(categoriaForm.getIdcategoria()))
    	{ 		
    		administrationService.modificarCategoria(categoria);
    		return "redirect:listarCategorias";
    	}       
        return Constantes.REDIRECT_PRINCIPAL;
    }
    
    /**
     * Método que recibe el id de la categoría, llamando al servicio de la capa de negocio que la oculta
     * @param idCategoria Con el idenetificador de la categoría a ocultar
     * @return Redirección a la lista de categorías si ha ido bien
     */	
    @GetMapping("/ocultarCategoria/{idCategoria}")
	public String ocultarCategoria(@PathVariable("idCategoria") Long idCategoria) {	
    	Categoria categoria = administrationService.obtenerCategoria(idCategoria);	
    	//Solo se pueden modificar categorias que sean del usuario
    	if(categoria!=null && securityService.categoriaAccesible(idCategoria))
    	{
	   		administrationService.ocultarCategoria(categoria);
	   		return REDIRECT_LISTARCATEGORIAS;
	   	}
    	return Constantes.REDIRECT_PRINCIPAL;	
	}
    
    /**
     * Método que recibe el id de la categoría, llamando al servicio de la capa de negocio que la muestra
     * @param idCategoria Con el idenetificador de la categoría a ocultar
     * @return Redirección a la lista de categorías si ha ido bien
     */
    @GetMapping("/mostrarCategoria/{idCategoria}")
	public String mostrarCategoria(@PathVariable("idCategoria") Long idCategoria, Model model) {	
    	Categoria categoria = administrationService.obtenerCategoria(idCategoria);	
    	//Solo se pueden modificar categorias que sean del usuario
    	if(categoria!=null && securityService.categoriaAccesible(idCategoria))
    	{
	   		administrationService.mostrarCategoria(categoria);
	   		return REDIRECT_LISTARCATEGORIAS;
	  	}
    	return Constantes.REDIRECT_PRINCIPAL;	
	}
    
    /**
     * Método que convierte un Entity a un DTO
     * @param cat Categoría Entity que se quiere convertir
     * @return CategoriaDTO objeto DTO convertido
     */
    private CategoriaDTO convertToDto(Categoria cat) {
    	return modelMapper.map(cat, CategoriaDTO.class);
    }
    
    /**
     * Método que convierte un DTO a un Entity
     * @param categoriaDTO CategoriaDTO que se quiere convertir
     * @return Categoria objeto entity convertido
     */
    private Categoria convertToEntity(CategoriaDTO categoriaDTO) throws ParseException {
    	return modelMapper.map(categoriaDTO, Categoria.class);
    }
}
