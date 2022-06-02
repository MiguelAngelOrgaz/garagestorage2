package com.garage.storage.service;

import java.util.List;

import com.garage.storage.entity.Balda;
import com.garage.storage.entity.Caja;
import com.garage.storage.entity.Objeto;
import com.garage.storage.entity.Usuario;


public interface IObjectManagementService {
	public List<Caja> listarCajas(boolean sinUbicar,Usuario usuario);
	public Caja obtenerCaja(Long idCaja);
	public void nuevaCaja(Caja caja);
	public int modificarCaja(Caja caja);
	public int eliminarCaja(Caja caja);
	public void nuevoObjeto(Objeto objeto);
	public Objeto obtenerObjeto(Long idObjeto);
	public int modificarObjeto(Objeto objeto);
	public int eliminarObjeto(Objeto objeto);
	public List<Objeto> listarObjetos(Usuario usuario);
	public List<Objeto> localizarObjeto(Long idObjeto, String nombre, Long idCaja, Long idHabitacion, Long idEstanteria, Long idBalda, Usuario usuario);
	public List<Caja> localizarCaja(Long idCaja, String descripcion, Long idHabitacion, Long idEstanteria, Long idBalda, Usuario usuario);
	public void insertarEnBalda(Caja caja, Balda balda);
	public void sacarDeBalda(Caja caja);
}
