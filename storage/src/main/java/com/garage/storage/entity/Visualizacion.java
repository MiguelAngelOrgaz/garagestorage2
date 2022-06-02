package com.garage.storage.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Table(name = "visualizacion")
public class Visualizacion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
    VisualizacionKey id;
	
	@ManyToOne
    @MapsId("idcategoria")
    @JoinColumn(name = "idcategoria")
	private Categoria categoria;
	
	@ManyToOne
    @MapsId("idusuario")
    @JoinColumn(name = "idusuario")
	private Usuario usuario;
	
	private char visible = 'S';

	public Visualizacion() {
		super();
	}

	public Visualizacion(Categoria categoria, Usuario usuario, char visible) {
		super();
		this.categoria = categoria;
		this.usuario = usuario;
		this.visible = visible;
	}

	public VisualizacionKey getId() {
		return id;
	}

	public void setId(VisualizacionKey id) {
		this.id = id;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Usuario getCliente() {
		return usuario;
	}

	public void setCliente(Usuario cliente) {
		this.usuario = cliente;
	}

	public char getVisible() {
		return visible;
	}

	public void setVisible(char visible) {
		this.visible = visible;
	}
}
