package com.garage.storage.dto;

import java.util.Collection;

import com.garage.storage.entity.Balda;
import com.garage.storage.entity.Objeto;
import com.garage.storage.entity.Usuario;

public class CajaDTO {
	
	private Long idcaja;
	private Balda balda;
	private String descripcion;
	private byte[] foto;
	private int posicion = 0;
	private Usuario usuario;
    private Collection<Objeto> objetos;
	public Long getIdcaja() {
		return idcaja;
	}
	public void setIdcaja(Long idcaja) {
		this.idcaja = idcaja;
	}
	public Balda getBalda() {
		return balda;
	}
	public void setBalda(Balda balda) {
		this.balda = balda;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public byte[] getFoto() {
		return foto;
	}
	public void setFoto(byte[] foto) {
		this.foto = foto;
	}
	public int getPosicion() {
		return posicion;
	}
	public void setPosicion(int posicion) {
		this.posicion = posicion;
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

}
