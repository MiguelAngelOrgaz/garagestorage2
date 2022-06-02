package com.garage.storage.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.garage.storage.entity.Estanteria;

@Repository
public interface IEstanteriaDAO extends JpaRepository<Estanteria, Long> {
	Estanteria findByNombre(String nombre);
	Estanteria findByIdestanteria(Long idEstanteria);
}
