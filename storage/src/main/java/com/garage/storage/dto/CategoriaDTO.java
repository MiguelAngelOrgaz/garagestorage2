package com.garage.storage.dto;

import java.util.Collection;

import com.garage.storage.entity.Categoria;
import com.garage.storage.entity.Objeto;
import com.garage.storage.entity.Usuario;
import com.garage.storage.entity.Visualizacion;

public class CategoriaDTO {
	
	private Long idcategoria;
    private Categoria categoriapadre;
    private Collection<Categoria> categoriashijas;
	private String nombre;
	private char esfija;
    private Usuario usuario;
    private Collection<Visualizacion> visualizacion;
    private Collection<Objeto> objetos;
	private boolean visible;
	
	public Long getIdcategoria() {
		return idcategoria;
	}
	public void setIdcategoria(Long idcategoria) {
		this.idcategoria = idcategoria;
	}
	public Categoria getCategoriapadre() {
		return categoriapadre;
	}
	public void setCategoriapadre(Categoria categoriapadre) {
		this.categoriapadre = categoriapadre;
	}
	public Collection<Categoria> getCategoriashijas() {
		return categoriashijas;
	}
	public void setCategoriashijas(Collection<Categoria> categoriashijas) {
		this.categoriashijas = categoriashijas;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public char getEsfija() {
		return esfija;
	}
	public void setEsfija(char esfija) {
		this.esfija = esfija;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public Collection<Visualizacion> getVisualizacion() {
		return visualizacion;
	}
	public void setVisualizacion(Collection<Visualizacion> visualizacion) {
		this.visualizacion = visualizacion;
	}
	public Collection<Objeto> getObjetos() {
		return objetos;
	}
	public void setObjetos(Collection<Objeto> objetos) {
		this.objetos = objetos;
	}
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

}
