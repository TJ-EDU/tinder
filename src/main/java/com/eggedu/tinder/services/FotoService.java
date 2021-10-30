package com.eggedu.tinder.services;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.eggedu.tinder.entities.Foto;
import com.eggedu.tinder.exception.ExceptionService;
import com.eggedu.tinder.repositories.FotoRepository;

@Service
public class FotoService {
	
	@Autowired
	private FotoRepository fotoRepository;
	
	//exce service
	
	@Transactional
	public Foto guardar(MultipartFile archivo) throws ExceptionService{
		
		if ( archivo != null && !archivo.isEmpty() ) {
			
			try {
				
				Foto foto = new Foto();
				foto.setMime(archivo.getContentType());
				foto.setNombre(archivo.getName());
				foto.setContenido(archivo.getBytes());
				
				return fotoRepository.save(foto);
				
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			
		}
		
		return null;
	}
	
	@Transactional
	public Foto actualizar(String idFoto, MultipartFile archivo) throws ExceptionService{
		
		if (archivo != null) {
			
			try {
				
				Foto foto = new Foto();
				if (idFoto != null) {
					
					Optional<Foto> respuesta = fotoRepository.findById(idFoto);
					if (respuesta.isPresent()) {
						
						foto = respuesta.get();
					}
					
				}
				
				foto.setMime(archivo.getContentType());
				foto.setNombre(archivo.getName());
				foto.setContenido(archivo.getBytes());
				
				return fotoRepository.save(foto);
				
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			
		}
		
		return null;
	}
	
	
}
