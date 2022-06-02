package com.garage.storage.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.garage.storage.dao.IBaldaDAO;
import com.garage.storage.dao.IEstanciaDAO;
import com.garage.storage.dao.IEstanteriaDAO;
import com.garage.storage.dao.IUsuarioDAO;
import com.garage.storage.entity.Balda;
import com.garage.storage.entity.Caja;
import com.garage.storage.entity.Estancia;
import com.garage.storage.entity.Estanteria;
import com.garage.storage.entity.Usuario;
import com.garage.storage.service.IShelfManagementService;

@Service
public class ShelfManagementServiceImpl implements IShelfManagementService {

	private static final Logger logger = LoggerFactory.getLogger(ShelfManagementServiceImpl.class);
	
	@Autowired
	private IUsuarioDAO usuarioDAO;
	
	@Autowired
	private IEstanciaDAO estanciaDAO;
	
	@Autowired
	private IEstanteriaDAO estanteriaDAO;
	
	@Autowired
	private IBaldaDAO baldaDAO;
	
	@Override
	public List<Estancia> listarHabitaciones(Long idUsuario) {
		Usuario usuario = usuarioDAO.findByIdusuario(idUsuario);
		return new ArrayList<>(usuario.getEstancias());
	}

	@Override
	public Estancia obtenerHabitacion(Long idHabitacion) {
		return estanciaDAO.findByIdestancia(idHabitacion);
	}

	@Override
	public void nuevaHabitacion(Estancia estancia, String nombre) {
		Usuario usuario = usuarioDAO.findByNombre(nombre);
		estancia.setUsuario(usuario); 
		estanciaDAO.save(estancia);		
	}

	@Override
	public int modificarHabitacion(Estancia estancia) {
		estanciaDAO.save(estancia);
		return 0;
	}

	@Override
	public void nuevaEstanteria(Estanteria estanteria) {
		estanteriaDAO.save(estanteria);	
	}

	@Override
	public Estanteria obtenerEstanteria(Long idEstanteria) {
		return estanteriaDAO.findByIdestanteria(idEstanteria);
	}

	@Override
	public int modificarEstanteria(Estanteria estanteria) {
		estanteriaDAO.save(estanteria);
		return 0;
	}

	@Override
	public void nuevaBalda(Balda balda) {
		baldaDAO.save(balda);
	}

	@Override
	public Balda obtenerBalda(Long idBalda) {
		return baldaDAO.findByIdbalda(idBalda);
	}

	@Override
	public int eliminarBalda(Balda balda) {	
		try {
			//Saca todos las cajas que tuviera antes de eliminar
			for(Caja caja:balda.getCajas())
			{
				caja.setBalda(null);
				caja.setPosicion(0);
			}
			//Hay que posicionar las baldas superiores un nivel más abajo
			int posicion = balda.getPosicion();
			Collection<Balda> baldas = balda.getEstanteria().getBaldas();
			baldaDAO.delete(balda);
			for(Balda b:baldas)
			{
				if(b.getPosicion()>posicion)
				{
					b.setPosicion(b.getPosicion()-1);
					baldaDAO.save(b);
				}
			}
		}catch (Exception e)
		{
			logger.error(String.format("Error al eliminar balda. %s",e.getMessage()));
			return -1;
		}
		return 0;
	}

	@Override
	public int modificarBalda(Balda balda) {
		baldaDAO.save(balda);
		return 0;
	}

	@Override
	public int eliminaEstanteria(Estanteria estanteria) {
		try {
			//Eliminar baldas, sacando las cajas que hubiera
			for(Balda balda:estanteria.getBaldas())
			{
				eliminarBalda(balda);
			}
			estanteriaDAO.delete(estanteria);			
		}catch (Exception e)
		{
			logger.error(String.format("Error al eliminar estanteria. %s",e.getMessage()));
			return -1;
		}
		return 0;
	}

	@Override
	public int eliminarHabitacion(Estancia estancia) {
		try {
			//Eliminar estanterias que tuviera
			for(Estanteria estanteria:estancia.getEstanterias())
			{
				eliminaEstanteria(estanteria);
			}
			estanciaDAO.delete(estancia);			
		}catch (Exception e)
		{
			logger.error(String.format("Error al eliminar estancia. %s",e.getMessage()));
			return -1;
		}
		return 0;
	}

}
