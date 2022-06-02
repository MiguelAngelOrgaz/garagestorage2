package com.garage.storage.service;

import java.util.List;

import com.garage.storage.entity.Categoria;
import com.garage.storage.entity.Usuario;

public interface IAdministrationService {
	public void reactivarUsuario(Long idUsuario);
	public List<Usuario> listarUsuarios (char soloBloqueados);
	public List<Categoria> listarCategoriasSuperiores(Usuario usuario);
	public List<Categoria> listarCategoriasSuperioresVisibles(Usuario usuario);
	public List<Categoria> listarCategorias(Usuario usuario);
	public void nuevaCategoria(Categoria categoria);
	public Categoria obtenerCategoria(Long idCategoria);
	public int modificarCategoria(Categoria categoria);
	public int ocultarCategoria(Categoria categoria);
	public int mostrarCategoria(Categoria categoria);
}
