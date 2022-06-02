package com.garage.storage.util;

import java.util.Iterator;

import com.garage.storage.entity.Rol;
import com.garage.storage.entity.Usuario;

public class Utils {
	
	private Utils() {
	    throw new IllegalStateException("Utils class");
	  }

	/**
     * Método que devuelve si una cadena está vacía o es nula
     * @param s Cadena de texto
     * @return boolean Indicando true si está vacía o nula, o false en caso contrario
     */
	public static boolean esNuloVacio(String s) {
	    return (s == null || s.isEmpty());
	}
	
	/**
     * Método que devuelve si un usuario tiene un determinado rol
     * @param usuario Usuario en el que se quiere buscar el rol
     * @param sRol Cadena de texto con el rol a buscar
     * @return boolean Indicando true si tiene el rol indicado o false en caso contrario
     */
	public static boolean tieneRol(Usuario usuario, String sRol)
	{
		boolean tieneRol = false;
		for (Iterator<Rol> it = usuario.getRoles().iterator(); it.hasNext() && !tieneRol; ) {
    		Rol rol = it.next();
            if(!esNuloVacio(sRol) && sRol.equals(rol.getNombre()))
            	tieneRol = true;
        }
		return tieneRol;
	}
}

