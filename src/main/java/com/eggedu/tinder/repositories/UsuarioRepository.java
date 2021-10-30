package com.eggedu.tinder.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eggedu.tinder.entities.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {
	
	@Query("SELECT c FROM Usuario c WHERE c.email = :email")
	public Usuario buscarPorEmail(@Param("email") String email);
	
}
