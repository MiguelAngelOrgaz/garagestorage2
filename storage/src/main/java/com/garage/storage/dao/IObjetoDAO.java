package com.garage.storage.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.garage.storage.entity.Balda;
import com.garage.storage.entity.Objeto;
import com.garage.storage.entity.Usuario;

@Repository
public interface IObjetoDAO extends JpaRepository<Objeto, Long> {
	Objeto findByIdobjeto(Long idObjeto);
	List<Objeto> findByUsuarioObjeto(Usuario usuario);
}
