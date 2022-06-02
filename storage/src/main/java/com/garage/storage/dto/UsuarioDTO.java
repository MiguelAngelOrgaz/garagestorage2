package com.garage.storage.dto;

import java.util.Collection;
import java.util.Set;

import com.garage.storage.entity.Caja;
import com.garage.storage.entity.Estancia;
import com.garage.storage.entity.Objeto;
import com.garage.storage.entity.Rol;
import com.garage.storage.entity.Visualizacion;

public class UsuarioDTO {

	private Long idusuario;

    private String nombre;
    
    private String password;
    
    private String email;
    
    private char esactivo='S';
    
	private int intento = 0;

    private String passwordConfirm;

    private Set<Rol> roles;
    
    private Collection<Estancia> estancias;
    
    private Collection<Caja> cajas;

    private Collection<Objeto> objetos;
    
    private Collection<Visualizacion> visualizacion;

	public Long getIdusuario() {
		return idusuario;
	}

	public void setIdusuario(Long idusuario) {
		this.idusuario = idusuario;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean getEsactivo() {
		return esactivo == 'S';
	}

	public void setEsactivo(boolean esactivo) {
		this.esactivo = esactivo ? 'S':'N';
	}

	public int getIntento() {
		return intento;
	}

	public void setIntento(int intento) {
		this.intento = intento;
	}

	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

	public Set<Rol> getRoles() {
		return roles;
	}

	public void setRoles(Set<Rol> roles) {
		this.roles = roles;
	}

	public Collection<Estancia> getEstancias() {
		return estancias;
	}

	public void setEstancias(Collection<Estancia> estancias) {
		this.estancias = estancias;
	}

	public Collection<Caja> getCajas() {
		return cajas;
	}

	public void setCajas(Collection<Caja> cajas) {
		this.cajas = cajas;
	}

	public Collection<Objeto> getObjetos() {
		return objetos;
	}

	public void setObjetos(Collection<Objeto> objetos) {
		this.objetos = objetos;
	}

	public Collection<Visualizacion> getVisualizacion() {
		return visualizacion;
	}

	public void setVisualizacion(Collection<Visualizacion> visualizacion) {
		this.visualizacion = visualizacion;
	}
    
}
