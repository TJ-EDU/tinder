package com.eggedu.tinder.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eggedu.tinder.entities.Mascota;
import com.eggedu.tinder.entities.Usuario;
import com.eggedu.tinder.exception.ExceptionService;
import com.eggedu.tinder.services.MascotaService;
import com.eggedu.tinder.services.UsuarioService;

@Controller
@RequestMapping("/foto")
public class FotoController {

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private MascotaService mascotaService;

	@GetMapping("/usuario/{id}")
	public ResponseEntity<byte[]> fotoUsuario(@PathVariable String id) {

		try {

			Usuario usuario = usuarioService.buscarPorId(id);
			if (usuario.getFoto() == null) {
				throw new ExceptionService("EL USUARIO NO TIENE UNA FOTO ASIGNADA");
			}
			
			byte[] foto = usuario.getFoto().getContenido();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_JPEG);

			return new ResponseEntity<>(foto, headers, HttpStatus.OK);
		} catch (ExceptionService e) {

			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}
	
	@GetMapping("/mascota/{id}")
	public ResponseEntity<byte[]> fotoMascota(@PathVariable String id) {

		try {

			Mascota mascota = mascotaService.buscarPorId(id);
			if (mascota.getFoto() == null) {
				throw new ExceptionService("LA MASCOTA NO TIENE UNA FOTO ASIGNADA");
			}
			
			byte[] foto = mascota.getFoto().getContenido();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_JPEG);

			return new ResponseEntity<>(foto, headers, HttpStatus.OK);
		} catch (ExceptionService e) {

			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}

}
