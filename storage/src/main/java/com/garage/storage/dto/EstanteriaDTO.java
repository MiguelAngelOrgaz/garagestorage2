package com.garage.storage.dto;

import java.util.Collection;
import com.garage.storage.entity.Balda;
import com.garage.storage.entity.Estancia;

public class EstanteriaDTO {

	private Long idestanteria;
	private Estancia estancia;
	private String nombre;
	private String descripcion;
    private Collection<Balda> baldas;
    
	public Long getIdestanteria() {
		return idestanteria;
	}
	public void setIdestanteria(Long idestanteria) {
		this.idestanteria = idestanteria;
	}
	public Estancia getEstancia() {
		return estancia;
	}
	public void setEstancia(Estancia estancia) {
		this.estancia = estancia;
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
	public Collection<Balda> getBaldas() {
		return baldas;
	}
	public void setBaldas(Collection<Balda> baldas) {
		this.baldas = baldas;
	}
    
}
