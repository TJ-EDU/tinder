package com.eggedu.tinder.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.eggedu.tinder.entities.Foto;
import com.eggedu.tinder.entities.Mascota;
import com.eggedu.tinder.entities.Usuario;
import com.eggedu.tinder.enums.Sexo;
import com.eggedu.tinder.enums.Tipo;
import com.eggedu.tinder.exception.ExceptionService;
import com.eggedu.tinder.repositories.MascotaRepository;
import com.eggedu.tinder.repositories.UsuarioRepository;

@Service
public class MascotaService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private MascotaRepository mascotaRepository;
	
	@Autowired
	private FotoService fotoService;
	
	@Transactional
	public void agregarMascota(MultipartFile archivo, String idUsuario, String nombre, Sexo sexo, Tipo tipo) throws ExceptionService {
		
		Usuario usuario = usuarioRepository.findById(idUsuario).get();
		
		validar(nombre, sexo);
		
		Mascota mascota = new Mascota();
		mascota.setNombre(nombre);
		mascota.setSexo(sexo);
		mascota.setAlta(new Date());
		mascota.setUsuario(usuario);
		mascota.setTipo(tipo);
		
		Foto foto = fotoService.guardar(archivo);
		mascota.setFoto(foto);
		
		mascotaRepository.save(mascota);
		
	}
	
	@Transactional
	public void modificar(MultipartFile archivo, String idUsuario, String idMascota, String nombre, Sexo sexo, Tipo tipo) throws ExceptionService{
		
		validar(nombre, sexo);
		
		Optional<Mascota> respuesta = mascotaRepository.findById(idMascota);
		
		if (respuesta.isPresent()) {
			
			Mascota mascota = respuesta.get();
			if (mascota.getUsuario().getId().equals(idUsuario)) {
				
				mascota.setNombre(nombre);
				mascota.setSexo(sexo);
				
				String idFoto = null;
				if (mascota.getFoto() != null) {
					
					idFoto = mascota.getFoto().getId();
				}
				
				Foto foto = fotoService.actualizar(idFoto, archivo);
				mascota.setFoto(foto);
				mascota.setTipo(tipo);
				
				mascotaRepository.save(mascota);
				
			}else {
				throw new ExceptionService("NO TIENE PERMISOS SUFICIENTES PARA REALIZAR LA OPERACIÓN");
			}
		}else {
			throw new ExceptionService("NO EXISTE UNA MASCOTA CON EL IDENTIFICADOR SOLICITADO");
		}
		
	}
	
	@Transactional
	public void eliminar(String idUsuario, String idMascota) throws ExceptionService{
		
		Optional<Mascota> respuesta = mascotaRepository.findById(idMascota);
		
		if (respuesta.isPresent()) {
			
			Mascota mascota = respuesta.get();
			if (mascota.getUsuario().getId().equals(idUsuario)) {
				
				mascota.setBaja(new Date());
				mascotaRepository.save(mascota);
				
			}
		}else {
			throw new ExceptionService("NO EXISTE UNA MASCOTA CON EL IDENTIFICADOR SOLICITADO");
		}
		
	}
	
	public void validar(String nombre, Sexo sexo) throws ExceptionService {
		
		if (nombre == null || nombre.isEmpty()) {
			throw new ExceptionService("EL NOMBRE DE LA MASCOTA NO PUEDE SER NULO O VACIO");
		}
		
		if (sexo == null) {
			throw new ExceptionService("EL SEXO DE LA MASCOTA NO PUEDE SER NULO");
		}
		
	}
	
	public Mascota buscarPorId(String id) throws ExceptionService{
		
		Optional<Mascota> respuesta = mascotaRepository.findById(id);
		if (respuesta.isPresent()) {
			
			return respuesta.get();
		}else {
			
			throw new ExceptionService("LA MASCOTA SOLICITADA NO EXISTE");
		}
		
	}
	
	public List<Mascota> buscarMascotasPorUsuario(String id){
		
		return mascotaRepository.buscarMascotasPorUsuario(id);
	}
	
}
