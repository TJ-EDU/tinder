package com.eggedu.tinder.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eggedu.tinder.entities.Mascota;

@Repository
public interface MascotaRepository extends JpaRepository<Mascota, String>{
	
	@Query("SELECT c FROM Mascota c WHERE c.usuario.id = :id AND c.baja IS NULL")
	public List<Mascota> buscarMascotasPorUsuario(@Param("id") String id);
	
}
