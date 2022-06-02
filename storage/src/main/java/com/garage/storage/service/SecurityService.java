package com.garage.storage.service;

import com.garage.storage.entity.Usuario;

public interface SecurityService {
	Usuario userAuthenticated();
	boolean isAuthenticated();
    void autoLogin(String username, String password); 
    boolean isTheUserAuthenticated(String name);
    String nameUserAuthenticated();
    boolean estanciaAccesible(Long idEstancia);
    boolean estanteriaAccesible(Long idEstanteria);
    boolean cajaAccesible(Long idCaja);
    boolean objetoAccesible(Long idObjeto);
    boolean categoriaAccesible(Long idCategoria);
}
