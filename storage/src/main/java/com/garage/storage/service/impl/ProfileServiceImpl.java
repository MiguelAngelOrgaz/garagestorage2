package com.garage.storage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.garage.storage.dao.IRolDAO;
import com.garage.storage.dao.IUsuarioDAO;
import com.garage.storage.entity.Rol;
import com.garage.storage.entity.Usuario;
import com.garage.storage.service.IProfileService;

@Service
public class ProfileServiceImpl implements IProfileService {

	public static final int MAX_FAILED_ATTEMPTS = 5;
	
	@Autowired
	private IUsuarioDAO usuarioDAO;
	@Autowired
    private IRolDAO rolDAO;
	@Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public void registrarUsuario(Usuario usuario) {
		usuario.setPassword(bCryptPasswordEncoder.encode(usuario.getPassword()));
		usuarioDAO.save(usuario);		
	}

	@Override
	public int modificarDatosPersona(Long idUsuario, String password, String email) {	
		password = bCryptPasswordEncoder.encode(password);
		return usuarioDAO.updateUser(idUsuario, password, email);
	}

	@Override
	public Usuario findByNombre(String username) {
		return usuarioDAO.findByNombre(username);
	}
	
	public Rol buscarRolPorNombre(String rol) {
		return rolDAO.findByNombre(rol);
	}

	@Override
	public Usuario consultarUsuario(Long id) {
		return usuarioDAO.findByIdusuario(id);
	}
	
	public void incrementarIntentosFallidos(Usuario user) {
        int newFailAttempts = user.getIntento() + 1;
        usuarioDAO.updateFailedAttempts(newFailAttempts, user.getIdusuario());
    }
     
    public void resetearIntentosFallidos(Long idUsuario) {
    	usuarioDAO.updateFailedAttempts(0, idUsuario);
    }
     
    public void lock(Usuario user) {
        user.setEsactivo(false);    
        usuarioDAO.save(user);
    }

	@Override
	public List<Usuario> listAllUsuarios() {
		return usuarioDAO.findAll();
	}

}
