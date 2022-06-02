package com.garage.storage.dto;

import java.util.Collection;
import com.garage.storage.entity.Estanteria;
import com.garage.storage.entity.Usuario;

public class EstanciaDTO {

	private Long idestancia;
	private String nombre;
	private String descripcion;
    private Usuario usuario;
    private Collection<Estanteria> estanterias;
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
