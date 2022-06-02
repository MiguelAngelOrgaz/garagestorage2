package com.garage.storage.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "categoria")
public class Categoria implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idcategoria;
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "categoriapadre", nullable = true)
    private Categoria categoriapadre;
    @OneToMany(mappedBy="categoriapadre")
    private Collection<Categoria> categoriashijas;
	private String nombre;
	private char esfija;
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "usuario", nullable = false)
    private Usuario usuario;
	@OneToMany(mappedBy="categoria")
    private Collection<Visualizacion> visualizacion;
	
	@OneToMany(mappedBy="categoria")
    private Collection<Objeto> objetos;
	
	@SuppressWarnings("unused")
	private transient boolean visible;
	
	public Categoria(Long idcategoria, Categoria categoriapadre, String nombre, char esfija, Usuario usuario) {
		super();
		this.idcategoria = idcategoria;
		this.categoriapadre = categoriapadre;
		this.nombre = nombre;
		this.esfija = esfija;
		this.usuario = usuario;
	}

	public Categoria() {
		super();
	}

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

	public Collection<Objeto> getObjetos() {
		return objetos;
	}

	public void setObjetos(Collection<Objeto> objetos) {
		this.objetos = objetos;
	}

	public Collection<Visualizacion> getVisualizacion() {
		return visualizacion;
	}

	public void setVisualizacion(Collection<Visualizacion> visualizacion) {
		this.visualizacion = visualizacion;
	}

	public boolean isVisible() {
		for (Visualizacion v : this.getVisualizacion()) {
	        if (v.getCliente().getIdusuario().equals(this.getUsuario().getIdusuario())) {
	            return v.getVisible()=='S';
	        }
	    }
		return false;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	

}
