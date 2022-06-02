package com.garage.storage.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.garage.storage.entity.Balda;

@Repository
public interface IBaldaDAO extends JpaRepository<Balda, Long> {
	Balda findByIdbalda(Long idBalda);

}
