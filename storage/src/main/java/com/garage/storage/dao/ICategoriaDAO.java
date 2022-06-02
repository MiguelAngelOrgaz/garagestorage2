package com.garage.storage.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.garage.storage.entity.Categoria;
import com.garage.storage.entity.Usuario;

@Repository
public interface ICategoriaDAO extends JpaRepository<Categoria, Long> {
	Categoria findByIdcategoria(Long idCategoria);
	List<Categoria> findByCategoriapadre(Categoria padre);
	@Query("select c from Categoria c inner join Visualizacion v on c=v.categoria WHERE c.categoriapadre is null and (c.usuario=:usuario or c.esfija='S')")
	List<Categoria> findCategoriasPadresUsuario(Usuario usuario);
	@Query("select c from Categoria c inner join Visualizacion v on c=v.categoria WHERE v.visible = 'S' and c.categoriapadre is null and (c.usuario=:usuario or c.esfija='S')")
	List<Categoria> findCategoriasVisiblesPadresUsuario(Usuario usuario);
	@Query("select c from Categoria c inner join Visualizacion v on c=v.categoria WHERE v.visible = 'S' and (c.usuario=:usuario or c.esfija='S')")
	List<Categoria> findCategoriasUsuario(Usuario usuario);
}
