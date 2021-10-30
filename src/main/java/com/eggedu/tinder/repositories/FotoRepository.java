package com.eggedu.tinder.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eggedu.tinder.entities.Foto;

@Repository
public interface FotoRepository extends JpaRepository<Foto, String> {
	
	//no le puso repository
}
