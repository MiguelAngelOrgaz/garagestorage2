package com.garage.storage.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class VisualizacionKey implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "idcategoria")
	Long idcategoria;

    @Column(name = "idusuario")
    Long idusuario;

	public VisualizacionKey() {
		super();
	}

	public VisualizacionKey(Long idcategoria, Long idusuario) {
		super();
		this.idcategoria = idcategoria;
		this.idusuario = idusuario;
	}

	public Long getIdcategoria() {
		return idcategoria;
	}

	public void setIdcategoria(Long idcategoria) {
		this.idcategoria = idcategoria;
	}

	public Long getIdusuario() {
		return idusuario;
	}

	public void setIdusuario(Long idusuario) {
		this.idusuario = idusuario;
	}
}
