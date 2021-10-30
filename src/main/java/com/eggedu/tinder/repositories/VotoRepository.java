package com.eggedu.tinder.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eggedu.tinder.entities.Voto;

@Repository
public interface VotoRepository extends JpaRepository<Voto, String> {
	
	@Query("SELECT c FROM Voto c WHERE c.mascota1.id = :id ORDER BY c.fecha DESC")
	public List<Voto> buscarVotosPropios(@Param("id") String id);
	
	@Query("SELECT c FROM Voto c WHERE c.mascota2.id = :id ORDER BY c.fecha DESC")
	public List<Voto> buscarVotosRecibidos(@Param("id") String id);
	
}
