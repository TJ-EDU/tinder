package com.eggedu.tinder.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eggedu.tinder.entities.Zona;

@Repository
public interface ZonaRepository extends JpaRepository<Zona, String> {
	
}
