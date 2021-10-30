package com.eggedu.tinder.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.eggedu.tinder.entities.Zona;
import com.eggedu.tinder.exception.ExceptionService;
import com.eggedu.tinder.services.UsuarioService;
import com.eggedu.tinder.services.ZonaService;



@Controller
@RequestMapping("/")
public class PortalController {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private ZonaService zonaService;
	
	@GetMapping("/")
	public String index() {
		return "index.html";
	}
	
	//@PreAuthorize("hasAnyRole('Role_Usuario_Registrado')")
	@PreAuthorize("hasAnyRole('ROLE_USUARIO')")
	@GetMapping("/inicio")
	public String inicio() {
		
		return "inicio.html";
	}
	
	@GetMapping("/login")
	public String login(@RequestParam(required=false) String error , @RequestParam(required=false) String logout,  ModelMap modelo){
		
		if (error != null) {
			modelo.put("error", "NOMBRE DE USUARIO O CLAVE INCORRECTOS");
		}
		
		if (logout != null) {
			modelo.put("logout", "HA SALIDO CORRECTAMENTE DE LA PLATAFORMA");
		}
		
		return "login.html";
	}
	
	@GetMapping("/registro")
	public String registro(ModelMap modelo) {
		
		List<Zona> zonas = zonaService.obtenerZonas();
		modelo.put("zonas", zonas);
		
		return "registro.html";
	}
	
	@PostMapping("/registrar")
	public String registrar(ModelMap modelo, MultipartFile archivo, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String mail, @RequestParam String clave1, @RequestParam String clave2,  @RequestParam String idZona) {
		
		try {
			usuarioService.registrar(archivo, nombre, apellido, mail, clave1, clave2, idZona);
		} catch (ExceptionService e) {
			//recargar el combo
			List<Zona> zonas = zonaService.obtenerZonas();
			modelo.put("zonas", zonas);
			
			modelo.put("error", e.getMessage());
			modelo.put("nombre", nombre);
			modelo.put("apellido", apellido);
			modelo.put("mail", mail);
			modelo.put("clave1", clave1);
			modelo.put("clave2", clave2);
			
			return "registro.html";
			
		}
		
		modelo.put("titulo", "Bienvenido a Tinder de Mascotas");
		modelo.put("descripcion", "Tu usuario fue registrado de manera satisfactoria");
		
		return "exito.html";
	}
	
	
}
