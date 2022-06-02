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
@Table(name = "estanteria")
public class Estanteria implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idestanteria;
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idestancia", nullable = false)
	private Estancia estancia;
	private String nombre;
	private String descripcion;
	
	@OneToMany(mappedBy = "estanteria", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Collection<Balda> baldas;
	
	
	

	public Estanteria(Long idestanteria, Estancia estancia, String nombre, String descripcion) {
		super();
		this.idestanteria = idestanteria;
		this.estancia = estancia;
		this.nombre = nombre;
		this.descripcion = descripcion;
	}

	public Estanteria() {
		super();
	}
	
	public Long getIdestanteria() {
		return idestanteria;
	}
	public void setIdestanteria(Long idestanteria) {
		this.idestanteria = idestanteria;
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

	public Estancia getEstancia() {
		return estancia;
	}

	public void setEstancia(Estancia estancia) {
		this.estancia = estancia;
	}
	public Collection<Balda> getBaldas() {
		return baldas;
	}

	public void setBaldas(Collection<Balda> baldas) {
		this.baldas = baldas;
	}
}
