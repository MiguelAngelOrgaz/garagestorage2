package com.garage.storage.service;

import java.util.List;

import com.garage.storage.entity.Balda;
import com.garage.storage.entity.Estancia;
import com.garage.storage.entity.Estanteria;

public interface IShelfManagementService {
	public List<Estancia> listarHabitaciones(Long idUsuario);
	public Estancia obtenerHabitacion(Long idHabitacion);
	public void nuevaHabitacion(Estancia estancia, String nombreUsuario);
	public int modificarHabitacion(Estancia estancia);
	public int eliminarHabitacion(Estancia estancia);
	public void nuevaEstanteria(Estanteria estanteria);
	public Estanteria obtenerEstanteria(Long idEstanteria);
	public int eliminaEstanteria(Estanteria estanteria);
	public Balda obtenerBalda(Long idBalda);
	public int modificarEstanteria(Estanteria estanteria);
	public void nuevaBalda(Balda balda);
	public int eliminarBalda(Balda balda);
	public int modificarBalda(Balda balda);
}
