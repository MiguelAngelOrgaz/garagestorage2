package com.garage.storage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.garage.storage.dao.ObjetoDAO;
import com.garage.storage.dao.CajaDAO;
import com.garage.storage.dao.ICajaDAO;
import com.garage.storage.dao.IObjetoDAO;
import com.garage.storage.entity.Balda;
import com.garage.storage.entity.Caja;
import com.garage.storage.entity.Objeto;
import com.garage.storage.entity.Usuario;
import com.garage.storage.service.IObjectManagementService;

@Service
public class ObjectManagementServiceImpl implements IObjectManagementService {

	@Autowired
	private ICajaDAO cajaDAO;
	
	@Autowired
	private IObjetoDAO objetoDAO;
	
	@Autowired
	private ObjetoDAO objetoDAO2;
	
	@Autowired
	private CajaDAO cajaDAO2;
	
	@Override
	public List<Caja> listarCajas(boolean sinUbicar,Usuario usuario) {
		if(sinUbicar)
			return cajaDAO.findByUsuarioAndBalda(usuario, null);
		return cajaDAO.findByUsuario(usuario);
	}

	@Override
	public Caja obtenerCaja(Long idCaja) {
		return cajaDAO.findByIdcaja(idCaja);
	}

	@Override
	public void nuevaCaja(Caja caja) {
		cajaDAO.save(caja);		
	}

	@Override
	public int modificarCaja(Caja caja) {
		cajaDAO.save(caja);
		return 0;
	}

	@Override
	public void nuevoObjeto(Objeto objeto) {
		objetoDAO.save(objeto);
		
	}

	@Override
	public Objeto obtenerObjeto(Long idObjeto) {
		return objetoDAO.findByIdobjeto(idObjeto);
	}

	@Override
	public int modificarObjeto(Objeto objeto) {
		objetoDAO.save(objeto);
		return 0;
	}

	@Override
	public List<Objeto> listarObjetos(Usuario usuario) {
		return objetoDAO.findByUsuarioObjeto(usuario);
	}

	@Override
	public List<Objeto> localizarObjeto(Long idObjeto, String nombre, Long idCaja, Long idHabitacion, Long idEstanteria,
			Long idBalda, Usuario usuario) {
		return objetoDAO2.localizarObjeto(idObjeto, nombre, idCaja, idHabitacion, idEstanteria, idBalda, usuario);
	}

	@Override
	public List<Caja> localizarCaja(Long idCaja, String descripcion, Long idHabitacion, Long idEstanteria,
			Long idBalda, Usuario usuario) {
		return cajaDAO2.localizarCaja(idCaja, descripcion, idHabitacion, idEstanteria, idBalda, usuario);
	}

	@Override
	public int eliminarCaja(Caja caja) {
		//Sacar todos los objetos de la caja antes de eliminarla
		for(Objeto objeto:caja.getObjetos())
		{
			objeto.setCaja(null);
			this.modificarObjeto(objeto);
		}
		cajaDAO.delete(caja);
		return 0;
	}

	@Override
	public int eliminarObjeto(Objeto objeto) {
		objetoDAO.delete(objeto);
		return 0;
	}

	@Override
	public void insertarEnBalda(Caja caja, Balda balda) {
		int posicion = 1;
		if(balda.getCajas()!=null && !balda.getCajas().isEmpty())
			posicion += balda.getCajas().size();
		caja.setBalda(balda);
		caja.setPosicion(posicion);
		this.modificarCaja(caja);
	}

	@Override
	public void sacarDeBalda(Caja caja) {
		caja.setBalda(null);
		caja.setPosicion(0);
		this.modificarCaja(caja);
	}

}
