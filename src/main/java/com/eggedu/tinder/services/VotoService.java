package com.eggedu.tinder.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eggedu.tinder.entities.Mascota;
import com.eggedu.tinder.entities.Voto;
import com.eggedu.tinder.exception.ExceptionService;
import com.eggedu.tinder.repositories.MascotaRepository;
import com.eggedu.tinder.repositories.VotoRepository;

@Service
public class VotoService {
	
	@Autowired
	private VotoRepository votoRepository;
	
	@Autowired
	private MascotaRepository mascotaRepository;
	
	//@Autowired
	//private NotificacionService notificacionService;
	
	public void votar(String idUsuario, String idMascota1, String idMascota2) throws ExceptionService{
		
		Voto voto = new Voto();
		voto.setFecha(new Date());
		
		if (idMascota1.equals(idMascota2)) {
			throw new ExceptionService("NO PUEDE VOTARSE A SI MISMO");
		}
		
		Optional<Mascota> respuesta = mascotaRepository.findById(idMascota1);
		if (respuesta.isPresent()) {
			
			Mascota mascota1 = respuesta.get();
			if (mascota1.getUsuario().getId().equals(idUsuario)) {
				
				voto.setMascota1(mascota1);
			}else {
				throw new ExceptionService("NO TIENE PERMISOS PARA REALIZAR LA OPERACIÓN SOLICITADA");
			}
			
		}else {
			throw new ExceptionService("NO EXISTE UNA MASCOTA VINCULADA A ESE IDENTIFICADOR");
		}
		
		Optional<Mascota> respuesta2 = mascotaRepository.findById(idMascota2);
		if (respuesta2.isPresent()) {
			
			Mascota mascota2 = respuesta2.get();
			voto.setMascota2(mascota2);
			
			//notificacionService.enviar("Tu mascota a sido votada", "Tinder de Mascota", mascota2.getUsuario().getEmail());
			
		}else {
			throw new ExceptionService("NO EXISTE UNA MASCOTA VINCULADA CON ESE IDENTIFICADOR");
		}
		
		votoRepository.save(voto);
		
	}
	
	public void responder(String idUsuario, String idVoto) throws ExceptionService{
		
		Optional<Voto> respuesta = votoRepository.findById(idVoto);
		if (respuesta.isPresent()) {
			
			Voto voto = respuesta.get();
			voto.setRespuesta(new Date());
			
			if (voto.getMascota2().getUsuario().getId().equals(idUsuario)) {
				
				//notificacionService.enviar("Tu voto fue correspondido", "Tinder de Mascota", voto.getMascota1().getUsuario().getEmail());
				votoRepository.save(voto);				

			}else {
				throw new ExceptionService("NO TIENE PERMISOS PARA REALIZAR LA OPERACIÓN");
			}
			
		}else {
			throw new ExceptionService("NO EXISTE EL VOTO SOLICITADO");
		}
		
	}
	
}
