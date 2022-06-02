package com.garage.storage.service;

import java.util.List;

import com.garage.storage.entity.Rol;
import com.garage.storage.entity.Usuario;

public interface IProfileService {

	public void registrarUsuario (Usuario usuario);
	public int modificarDatosPersona (Long idUsuario, String password, String email);
	public Usuario findByNombre(String username);
	public Rol buscarRolPorNombre(String rol);
	public Usuario consultarUsuario(Long id);
	public List<Usuario> listAllUsuarios();
}
