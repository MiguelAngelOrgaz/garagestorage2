package com.garage.storage.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.CascadeType;
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
@Table(name = "estancia")
public class Estancia implements Serializable{

	private static final long serialVersionUID = 1L;
	 
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idestancia;
	
	private String nombre;
	private String descripcion;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "idusuario", nullable = false)
    private Usuario usuario;

	@OneToMany(mappedBy = "estancia", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Collection<Estanteria> estanterias;
	
	
	public Estancia() {
		super();
	}

	public Estancia(Long idestancia, String nombre, String descripcion, Usuario usuario) {
		super();
		this.idestancia = idestancia;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.usuario = usuario;
	}

	public Long getIdestancia() {
		return idestancia;
	}

	public void setIdestancia(Long idestancia) {
		this.idestancia = idestancia;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Collection<Estanteria> getEstanterias() {
		return estanterias;
	}

	public void setEstanterias(Collection<Estanteria> estanterias) {
		this.estanterias = estanterias;
	}
	
	

}
