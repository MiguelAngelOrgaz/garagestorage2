package com.garage.storage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.garage.storage.dao.ICategoriaDAO;
import com.garage.storage.dao.IUsuarioDAO;
import com.garage.storage.dao.IVisualizacionDAO;
import com.garage.storage.entity.Categoria;
import com.garage.storage.entity.Usuario;
import com.garage.storage.entity.Visualizacion;
import com.garage.storage.entity.VisualizacionKey;
import com.garage.storage.service.IAdministrationService;

@Service
public class AdministrationServiceImpl implements IAdministrationService {

	@Autowired
	private IUsuarioDAO usuarioDAO;
	
	@Autowired
	private ICategoriaDAO categoriaDAO;
	
	@Autowired
	private IVisualizacionDAO visualizacionDAO;
	
	@Override
	public void reactivarUsuario(Long idUsuario) {
		usuarioDAO.reactivarUsuario(idUsuario);		
	}

	@Override
	public List<Usuario> listarUsuarios(char soloBloqueados) {
		if(soloBloqueados=='N')
			return usuarioDAO.findByEsactivo(soloBloqueados);
		else
			return usuarioDAO.findAll();
	}

	@Override
	public List<Categoria> listarCategoriasSuperiores(Usuario usuario) {
		return categoriaDAO.findCategoriasPadresUsuario(usuario);
	}

	@Override
	public void nuevaCategoria(Categoria categoria) {
		Categoria c = categoriaDAO.save(categoria);
		Visualizacion v = new Visualizacion();
		v.setCategoria(c);
		v.setCliente(c.getUsuario());
		v.setVisible('S');
		v.setId(new VisualizacionKey(c.getIdcategoria(), v.getCliente().getIdusuario()));
		visualizacionDAO.save(v);
	}

	@Override
	public List<Categoria> listarCategorias(Usuario usuario) {
		return categoriaDAO.findCategoriasUsuario(usuario);
	}

	@Override
	public Categoria obtenerCategoria(Long idCategoria) {
		return categoriaDAO.findByIdcategoria(idCategoria);
		
	}

	@Override
	public int modificarCategoria(Categoria categoria) {
		categoriaDAO.save(categoria);
		return 0;
	}

	@Override
	public List<Categoria> listarCategoriasSuperioresVisibles(Usuario usuario) {
		return categoriaDAO.findCategoriasVisiblesPadresUsuario(usuario);
	}

	@Override
	public int ocultarCategoria(Categoria categoria) {
		for (Visualizacion v : categoria.getVisualizacion()) {
			if (v.getCliente().getIdusuario().equals(categoria.getUsuario().getIdusuario())) {
	            v.setVisible('N');
	            visualizacionDAO.save(v);
	            return 0;
	        }
		}
		return -1;
	}

	@Override
	public int mostrarCategoria(Categoria categoria) {
		for (Visualizacion v : categoria.getVisualizacion()) {
			if (v.getCliente().getIdusuario().equals(categoria.getUsuario().getIdusuario())) {
	            v.setVisible('S');
	            visualizacionDAO.save(v);
	            return 0;
	        }
		}
		return -1;
	}

}
