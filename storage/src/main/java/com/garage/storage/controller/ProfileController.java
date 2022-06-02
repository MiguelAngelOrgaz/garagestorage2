package com.garage.storage.controller;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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

import com.garage.storage.dto.UsuarioDTO;
import com.garage.storage.entity.Rol;
import com.garage.storage.entity.Usuario;
import com.garage.storage.service.IProfileService;
import com.garage.storage.service.SecurityService;
import com.garage.storage.util.Constantes;
import com.garage.storage.util.Utils;
import com.garage.storage.validator.UserValidator;

@Controller
public class ProfileController {
	
	private static final String ERROR = "error";
	private static final String VISTA_MODIFICARUSUARIO = "modifyUser";
	private static final String TITULO_USUARIO_MODIFICAR = "titulo.usuario.modificar";
	private static final String USERFORM = "userForm";

	@Autowired
	private IProfileService profileService;
	
	@Autowired
    private SecurityService securityService;
	
	@Autowired
    private MessageSource mensajes;
	
	@Autowired
    private UserValidator userValidator;
	
	@Autowired
    private ModelMapper modelMapper;
	
	/**
     * Prepara el modelo y selecciona la vista para registrar usuarios
     * @param model El modelo que se pasará a la vista. El userForm como objeto UsusarioDTO
     * @return El nombre de la vista para reactivar usuarios
     */	
	@GetMapping("/register")
    public String registrarUsuario(Model model) {
        if (securityService.isAuthenticated()) {
            return "redirect:/";
        }

        model.addAttribute(USERFORM, convertToDto(new Usuario()));

        return "register";
    }

	/**
     * Método que recibe los datos del usuario desde la vista para registrar usuarios, llamando al servicio correspondiente
     * de la capa de negocio, y logando al usuario
     * @param userForm El objeto UsuarioDTO con los datos que ha rellenado el usuario
     * @param bindingResult Almacena los posibles errores de validación
     * @return La redirección hacia la página principal
     */	
    @PostMapping("/register")
    public String register(@ModelAttribute(USERFORM) UsuarioDTO userForm, BindingResult bindingResult) {
        Usuario usuario = convertToEntity(userForm);
    	userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "register";
        }
        
        //Al crear un nuevo usuario se pone el rol de Usuario.
        Rol rol = profileService.buscarRolPorNombre(Constantes.ROLUSUARIO);
        Set<Rol> sRol = new HashSet<>();
        sRol.add(rol);
        usuario.setRoles(sRol);
        
        profileService.registrarUsuario(usuario);

        securityService.autoLogin(usuario.getNombre(), userForm.getPassword());

        return Constantes.REDIRECT_PRINCIPAL;
    }
	

    /**
     * Prepara el modelo y selecciona la vista para login de usuarios
     * @param model El modelo que se pasará a la vista. 
     * @param error Llegará en caso de que se hubiera producido algún error en el login
     * @param logout Llegará en caso de que se hubiera realizado logout satisfactoriamente
     * @param expired Llegará si el usuario ha introducido 5 veces mal el password
     * @param lock Llegará si el usuario está bloqueado
     * @return El nombre de la vista del login
     */	
    @GetMapping("/login")
    public String login(Model model, String error, String logout, String expired, String lock) {
        if (securityService.isAuthenticated()) {
            return "redirect:/";
        }

        if (error != null)
            model.addAttribute(ERROR, mensajes.getMessage("login.error", null, LocaleContextHolder.getLocale()));
        if (expired != null)
        	model.addAttribute(ERROR, mensajes.getMessage("login.expired", null, LocaleContextHolder.getLocale()));
        if (lock != null)
        	model.addAttribute(ERROR, mensajes.getMessage("login.lock", null, LocaleContextHolder.getLocale()));
        if (logout != null)
            model.addAttribute("message", mensajes.getMessage("logout.OK", null, LocaleContextHolder.getLocale()));

        model.addAttribute(Constantes.TITULO, mensajes.getMessage("titulo.login", null, LocaleContextHolder.getLocale()));
        
        return "login";
    }

    /**
     * Prepara el modelo y selecciona la vista para la página principal
     * @param model El modelo que se pasará a la vista. 
     * @return El nombre de la vista de la página principal
     */	
    @GetMapping({"/", "/welcome"})
    public String welcome(Model model) {
        return "welcome";
    }

    /**
     * Prepara el modelo y selecciona la vista para modificar un usuario concreto. Es llamado solamente por los administradores
     * @param model El modelo que se pasará a la vista. 
     * @param id ID del usuario a modificar
     * @return El nombre de la vista para modificar usuario
     */	
    @GetMapping("/modificarDatosUsuario/{id}")
	public String modificarDatosUsuario(@PathVariable("id") Long idUsuario, Model model) {
		
    	//Se mira si el usuario autenticado es administrador
    	Usuario usuarioAutenticado = securityService.userAuthenticated();	 
    	boolean esAdmin = false;
    	for (Iterator<Rol> it = usuarioAutenticado.getRoles().iterator(); it.hasNext() && !esAdmin; ) {
    		Rol rol = it.next();
            if (Constantes.ROLADMINISTRADOR.equals(rol.getNombre()))
                esAdmin = true;
        }
    	
    	//Si el usuario no es administrador
    	if(!esAdmin)
    		return Constantes.REDIRECT_PRINCIPAL;
    	
    	
    	Usuario usuario = profileService.consultarUsuario(idUsuario); 	
    	model.addAttribute(USERFORM, convertToDto(usuario));
		model.addAttribute(Constantes.TITULO, mensajes.getMessage(TITULO_USUARIO_MODIFICAR, null, LocaleContextHolder.getLocale()));
		return VISTA_MODIFICARUSUARIO;
		
	}
    
    /**
     * Prepara el modelo y selecciona la vista para modificar el usuario logado o la lista deusuario en caso de ser administrador
     * @param model El modelo que se pasará a la vista. 
     * @return El nombre de la vista para modificar usuario o la lista de usuario en caso de ser administrador
     */	
    @GetMapping("/modificarDatosUsuario")
	public String modificarDatosUsuario(Model model) {
		
    	Usuario usuarioAutenticado = securityService.userAuthenticated();	  	
    	boolean esAdmin = Utils.tieneRol(usuarioAutenticado, Constantes.ROLADMINISTRADOR);
    	if(esAdmin)
    	{
    		List<Usuario> listaUsuarios = profileService.listAllUsuarios();
    		model.addAttribute(USERFORM, listaUsuarios);
    		model.addAttribute("tituloPagina", mensajes.getMessage(TITULO_USUARIO_MODIFICAR, null, LocaleContextHolder.getLocale()));
    		return "listaUsuarios";
    	}
		model.addAttribute(USERFORM, convertToDto(usuarioAutenticado));
		model.addAttribute("tituloPagina", mensajes.getMessage(TITULO_USUARIO_MODIFICAR, null, LocaleContextHolder.getLocale()));
		return VISTA_MODIFICARUSUARIO;
		
	}
    
    /**
    * Método que recibe los datos del usuario desde la vista para modificar usuario, llamando al servicio correspondiente
    * de la capa de negocio, y modificando los datos de usuario
    * @param userForm El objeto UsuarioDTO con los datos que ha rellenado el usuario
    * @param bindingResult Almacena los posibles errores de validación
    * @param redirectAttrs para pasar a la pantalla principal el resultado al modificar el usuario.
    * @return La redirección hacia la página principal
    */	
    @PostMapping("/modificarDatosUsuario")
    public String modificarDatosUsuario(@ModelAttribute(USERFORM) UsuarioDTO userForm, BindingResult bindingResult, RedirectAttributes redirectAttrs) {
        userValidator.validate(userForm, bindingResult);
        
        if (bindingResult.hasErrors()) {
            return VISTA_MODIFICARUSUARIO;
        }
           
        //Si el usuario que se está modificando no es el del propio usuario, y éste no es administrador, sale.
        Usuario usuarioAutenticado = securityService.userAuthenticated();	
        if(userForm!=null && (userForm.getIdusuario().equals(usuarioAutenticado.getIdusuario()) || Utils.tieneRol(usuarioAutenticado, Constantes.ROLADMINISTRADOR)))
        {
        	profileService.modificarDatosPersona(userForm.getIdusuario(), userForm.getPassword(), userForm.getEmail());
        	redirectAttrs.addFlashAttribute("mensaje", mensajes.getMessage("mensaje.usuario.modificado", null, LocaleContextHolder.getLocale()));       	
        }
        return Constantes.REDIRECT_PRINCIPAL;
    }
	
    /**
     * Método que convierte un Entity a un DTO
     * @param usuario Usuario Entity que se quiere convertir
     * @return UsuarioDTO usuario DTO convertido
     */
    private UsuarioDTO convertToDto(Usuario usuario) {
    	return modelMapper.map(usuario, UsuarioDTO.class);
    }
    
    /**
     * Método que convierte un DTO a un Entity
     * @param usuarioDTO UsuarioDTO que se quiere convertir
     * @return Usuario usuario entity convertido
     */
    private Usuario convertToEntity(UsuarioDTO usuarioDTO) throws ParseException {
    	return modelMapper.map(usuarioDTO, Usuario.class);
    }
}
