package com.garage.storage.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Repository;

import com.garage.storage.entity.Objeto;
import com.garage.storage.entity.Usuario;
import com.garage.storage.util.Utils;

@Repository
public class ObjetoDAO {
	private static Logger logger = Logger.getLogger(ObjetoDAO.class);
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public List<Objeto> localizarObjeto(Long idObjeto, String nombre, Long idCaja, Long idHabitacion, Long idEstanteria,
			Long idBalda, Usuario usuario) {
		  logger.info("findAll");
		  StringBuilder  sqlQuery = new StringBuilder("SELECT o FROM Objeto as o LEFT JOIN o.caja.balda.estanteria as e");
		  sqlQuery.append(" WHERE o.usuarioObjeto =").append(usuario.getIdusuario());
		  if(idObjeto!=null || !Utils.esNuloVacio(nombre) || idCaja!=null || idHabitacion!=null || idEstanteria!=null || idBalda!=null)
		  {
			  if(idObjeto!=null)
				  sqlQuery.append(" AND o.idobjeto=").append(idObjeto);
			  if(!Utils.esNuloVacio(nombre))
				  sqlQuery.append(" AND UPPER(o.nombre) like '%").append(nombre.toUpperCase()).append("%'");
			  if(idCaja!=null)
				  sqlQuery.append(" AND o.caja.idcaja=").append(idCaja);
			  if(idHabitacion!=null)
				  sqlQuery.append(" AND e.estancia.idestancia=").append(idHabitacion);
			  if(idEstanteria!=null)
				  sqlQuery.append(" AND e.idestanteria=").append(idEstanteria);
			  if(idBalda!=null)
				  sqlQuery.append(" AND o.caja.balda.posicion=").append(idBalda);
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
