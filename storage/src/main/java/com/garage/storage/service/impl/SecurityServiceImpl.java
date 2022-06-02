package com.garage.storage.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.garage.storage.dao.ICajaDAO;
import com.garage.storage.dao.ICategoriaDAO;
import com.garage.storage.dao.IEstanciaDAO;
import com.garage.storage.dao.IEstanteriaDAO;
import com.garage.storage.dao.IObjetoDAO;
import com.garage.storage.dao.IUsuarioDAO;
import com.garage.storage.entity.Caja;
import com.garage.storage.entity.Categoria;
import com.garage.storage.entity.Estancia;
import com.garage.storage.entity.Estanteria;
import com.garage.storage.entity.Objeto;
import com.garage.storage.entity.Usuario;
import com.garage.storage.service.SecurityService;

@Service
public class SecurityServiceImpl implements SecurityService{
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private IEstanteriaDAO estanteriaDAO;
    
    @Autowired
    private ICajaDAO cajaDAO;
    
    @Autowired
    private IObjetoDAO objetoDAO;
    
    @Autowired
    private ICategoriaDAO categoriaDAO;
    
    @Autowired
    private IUsuarioDAO usuarioDAO;
    
    @Autowired
    private IEstanciaDAO estanciaDAO;

    private static final Logger logger = LoggerFactory.getLogger(SecurityServiceImpl.class);

    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.
            isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }

    @Override
    public void autoLogin(String username, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

        authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        if (usernamePasswordAuthenticationToken.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            logger.debug("Auto login successfully!");
        }
    }

	@Override
	public boolean isTheUserAuthenticated(String name) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return(authentication.getName().equals(name));
	}

	@Override
	public String nameUserAuthenticated() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication.getName();
	}
	
	public boolean estanteriaAccesible(Long idEstanteria)
	{
		Estanteria estanteria = estanteriaDAO.findByIdestanteria(idEstanteria);
		String username = this.nameUserAuthenticated();
    	return username.equals(estanteria.getEstancia().getUsuario().getNombre());
	}

	@Override
	public boolean cajaAccesible(Long idCaja) {
		Caja caja = cajaDAO.findByIdcaja(idCaja);
		String username = this.nameUserAuthenticated();
    	return username.equals(caja.getUsuario().getNombre());
	}

	@Override
	public Usuario userAuthenticated() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return usuarioDAO.findByNombre(authentication.getName());
	}

	@Override
	public boolean objetoAccesible(Long idObjeto) {
		Objeto objeto = objetoDAO.findByIdobjeto(idObjeto);
		String username = this.nameUserAuthenticated();
    	return username.equals(objeto.getUsuarioObjeto().getNombre());
	}

	@Override
	public boolean categoriaAccesible(Long idCategoria) {
		Categoria categoria = categoriaDAO.findByIdcategoria(idCategoria);
		String username = this.nameUserAuthenticated();
    	return username.equals(categoria.getUsuario().getNombre());
	}

	@Override
	public boolean estanciaAccesible(Long idEstancia) {
		Estancia estancia = estanciaDAO.findByIdestancia(idEstancia);
		String username = this.nameUserAuthenticated();
    	return username.equals(estancia.getUsuario().getNombre());
	}
}
