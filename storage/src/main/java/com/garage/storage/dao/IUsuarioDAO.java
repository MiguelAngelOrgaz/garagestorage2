package com.garage.storage.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.garage.storage.entity.Usuario;

@Repository
public interface IUsuarioDAO extends JpaRepository<Usuario, Long> {
	Usuario findByNombre(String nombre);
	Usuario findByIdusuario(Long id);
	List<Usuario> findByEsactivo(char esactivo);
	@Transactional
	@Modifying
	@Query("update Usuario usuario set usuario.password = :password, usuario.email=:email where usuario.idusuario = :id")
    int updateUser(@Param("id") Long usuarioId, @Param("password") String password, @Param("email") String email);
	@Transactional
	@Modifying
	@Query("UPDATE Usuario u SET u.intento = :intento WHERE u.idusuario = :id")
    public void updateFailedAttempts(@Param("intento") int failAttempts, @Param("id") Long usuarioId);
	@Transactional
	@Modifying
	@Query("UPDATE Usuario u SET u.intento = 0, u.esactivo='S' WHERE u.idusuario = :id")
	void reactivarUsuario(@Param("id") Long usuarioId);
}
