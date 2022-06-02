package com.garage.storage.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "usuario")
public class Usuario implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idusuario;

    private String nombre;
    
    private String password;
    
    private String email;
    
    private char esactivo='S';
    
	private int intento = 0;

    @ManyToMany
    private Set<Rol> roles;
    
    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Collection<Estancia> estancias;
    
    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Collection<Caja> cajas;
    
    @OneToMany(mappedBy = "usuarioObjeto", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Collection<Objeto> objetos;
    
    @OneToMany(mappedBy="usuario")
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

    public Set<Rol> getRoles() {
        return roles;
    }

    public void setRoles(Set<Rol> roles) {
        this.roles = roles;
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
	
	@Override
    public boolean equals(Object obj) {
		if (obj == null)
		    return false;
		if (this.getClass() != obj.getClass())
		    return false;
		return this.idusuario.equals(((Usuario)obj).getIdusuario());
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
