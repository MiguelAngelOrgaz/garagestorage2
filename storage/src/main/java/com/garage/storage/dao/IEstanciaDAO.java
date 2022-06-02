package com.garage.storage.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.garage.storage.entity.Estancia;

@Repository
public interface IEstanciaDAO extends JpaRepository<Estancia, Long> {
	Estancia findByNombre(String nombre);
	Estancia findByIdestancia(Long idEstancia);
}
