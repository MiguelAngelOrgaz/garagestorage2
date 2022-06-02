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
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name = "balda")
public class Balda implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idbalda;
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idestanteria", nullable = false)
	private Estanteria estanteria;
	private String descripcion;
	private char llena = 'N';
	private int posicion = 0;
	
	@OneToMany(mappedBy = "balda", fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST)
	@OrderBy("posicion ASC")
    private Collection<Caja> cajas;
	
	
	public Balda() {
		super();
	}
	public Balda(Long idbalda, Estanteria estanteria, String descripcion, char llena, int posicion) {
		super();
		this.idbalda = idbalda;
		this.estanteria = estanteria;
		this.descripcion = descripcion;
		this.llena = llena;
		this.posicion = posicion;
	}
	public Long getIdbalda() {
		return idbalda;
	}
	public void setIdbalda(Long idbalda) {
		this.idbalda = idbalda;
	}
	public Estanteria getEstanteria() {
		return estanteria;
	}
	public void setEstanteria(Estanteria estanteria) {
		this.estanteria = estanteria;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public boolean getLlena() {
		return llena=='S';
	}
	public void setLlena(boolean llena) {
		this.llena = llena ? 'S':'N';
	}
	public int getPosicion() {
		return posicion;
	}
	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}
	public Collection<Caja> getCajas() {
		return cajas;
	}
	public void setCajas(Collection<Caja> cajas) {
		this.cajas = cajas;
	}
	
	

}
