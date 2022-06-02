package com.garage.storage.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.garage.storage.entity.Visualizacion;
import com.garage.storage.entity.VisualizacionKey;

@Repository
public interface IVisualizacionDAO extends JpaRepository<Visualizacion, VisualizacionKey>{

}
