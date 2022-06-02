package com.garage.storage.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Repository;

import com.garage.storage.entity.Caja;
import com.garage.storage.entity.Usuario;
import com.garage.storage.util.Utils;

@Repository
public class CajaDAO {
	private static Logger logger = Logger.getLogger(CajaDAO.class);
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public List<Caja> localizarCaja(Long idCaja, String descripcion, Long idHabitacion, Long idEstanteria, Long idBalda, Usuario usuario) 
	{
		  logger.info("findAll");
		  StringBuilder  sqlQuery = new StringBuilder("SELECT c FROM Caja as c LEFT JOIN c.balda.estanteria as e");
		  sqlQuery.append(" WHERE c.usuario=").append(usuario.getIdusuario());
		  if(!Utils.esNuloVacio(descripcion) || idCaja!=null || idHabitacion!=null || idEstanteria!=null || idBalda!=null)
		  { 
			  if(idCaja!=null)
				  sqlQuery.append(" AND c.idcaja=").append(idCaja);
			  if(!Utils.esNuloVacio(descripcion))
				  sqlQuery.append(" AND UPPER(c.descripcion) like '%").append(descripcion.toUpperCase()).append("%'");		
			  if(idHabitacion!=null)
				  sqlQuery.append(" AND e.estancia.idestancia=").append(idHabitacion);
			  if(idEstanteria!=null)
				  sqlQuery.append(" AND e.idestanteria=").append(idEstanteria);
			  if(idBalda!=null)
				  sqlQuery.append(" AND c.balda.idbalda=").append(idBalda);
		  }
		  Query query = getEntityManager().createQuery(sqlQuery.toString());
		  return query.getResultList();
		}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	
}
