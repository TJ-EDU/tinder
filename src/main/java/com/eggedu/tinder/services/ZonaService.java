package com.eggedu.tinder.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eggedu.tinder.entities.Zona;
import com.eggedu.tinder.repositories.ZonaRepository;

@Service
public class ZonaService {
	
	@Autowired
	private ZonaRepository zonaRepository;
	
	public List<Zona> obtenerZonas() {
		return zonaRepository.findAll();
	}
	
}
