package com.garage.storage.dto;

import com.garage.storage.entity.Balda;
import com.garage.storage.entity.Caja;
import com.garage.storage.entity.Categoria;
import com.garage.storage.entity.Usuario;

public class ObjetoDTO {

	private Long idobjeto;
	private Caja caja;
	private Balda balda;
	private String nombre;
	private String marca;
	private String descripcion;
	private byte[] foto;
	private int posicion = 0;
	private Categoria categoria;
	private Usuario usuarioObjeto;
	
	public Long getIdobjeto() {
		return idobjeto;
	}
	public void setIdobjeto(Long idobjeto) {
		this.idobjeto = idobjeto;
	}
	public Caja getCaja() {
		return caja;
	}
	public void setCaja(Caja caja) {
		this.caja = caja;
	}
	public Balda getBalda() {
		return balda;
	}
	public void setBalda(Balda balda) {
		this.balda = balda;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getMarca() {
		return marca;
	}
	public void setMarca(String marca) {
		this.marca = marca;
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
	public Categoria getCategoria() {
		return categoria;
	}
	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	public Usuario getUsuarioObjeto() {
		return usuarioObjeto;
	}
	public void setUsuarioObjeto(Usuario usuarioObjeto) {
		this.usuarioObjeto = usuarioObjeto;
	}
}
