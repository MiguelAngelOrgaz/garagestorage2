package com.garage.storage.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.garage.storage.entity.Rol;

@Repository
public interface IRolDAO extends JpaRepository<Rol, Long> {
	public Rol findByNombre(String nombre);
}
