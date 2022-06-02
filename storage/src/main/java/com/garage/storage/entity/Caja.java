package com.garage.storage.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "caja")
public class Caja implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idcaja;
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "idbalda", nullable = true)
	private Balda balda;
	private String descripcion;
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private byte[] foto;
	private int posicion = 0;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idusuario", nullable = false)
	private Usuario usuario;
	
	@OneToMany(mappedBy = "caja", fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST)
    private Collection<Objeto> objetos;
	
	
	public Caja() {
		super();
	}
	public Caja(Long idcaja, Balda balda, String descripcion, byte[] foto, int posicion, Usuario usuario) {
		super();
		this.idcaja = idcaja;
		this.balda = balda;
		this.descripcion = descripcion;
		this.foto = foto;
		this.posicion = posicion;
		this.usuario = usuario;
	}
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

