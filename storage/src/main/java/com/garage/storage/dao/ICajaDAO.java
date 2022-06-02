package com.garage.storage.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.garage.storage.entity.Balda;
import com.garage.storage.entity.Caja;
import com.garage.storage.entity.Usuario;

@Repository
public interface ICajaDAO extends JpaRepository<Caja, Long> {
	Caja findByIdcaja(Long idCaja);
	List<Caja> findByUsuario(Usuario usuario);
	List<Caja> findByUsuarioAndBalda(Usuario usuario, Balda balda);
}
