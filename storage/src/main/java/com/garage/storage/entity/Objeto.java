package com.garage.storage.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "objeto")
public class Objeto implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idobjeto;
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "idcaja", nullable = true)
	private Caja caja;
	private String nombre;
	private String marca;
	private String descripcion;
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private byte[] foto;
	private int posicion = 0;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idcategoria", nullable = false)
	private Categoria categoria;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idusuario", nullable = false)
	private Usuario usuarioObjeto;
	
	
	public Objeto() {
		super();
	}
	public Objeto(Long idobjeto, Caja caja, String nombre, String marca, String descripcion,
			byte[] foto, int posicion) {
		super();
		this.idobjeto = idobjeto;
		this.caja = caja;
		this.nombre = nombre;
		this.marca = marca;
		this.descripcion = descripcion;
		this.foto = foto;
		this.posicion = posicion;
	}
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
