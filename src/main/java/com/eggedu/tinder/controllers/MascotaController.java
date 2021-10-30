package com.eggedu.tinder.controllers;


import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.eggedu.tinder.entities.Mascota;
import com.eggedu.tinder.entities.Usuario;

import com.eggedu.tinder.enums.Sexo;
import com.eggedu.tinder.enums.Tipo;
import com.eggedu.tinder.exception.ExceptionService;

import com.eggedu.tinder.services.MascotaService;




@PreAuthorize("hasAnyRole('ROLE_USUARIO')")
@Controller
@RequestMapping("/mascota")
public class MascotaController {
	
	
	@Autowired
	private MascotaService mascotaService;
	
	@GetMapping("/mis-mascotas")
	public String misMascotas(HttpSession session , @RequestParam(required = false) String id, ModelMap model) {
		
		Usuario login = (Usuario) session.getAttribute("usuariosession");
		if (login == null) {
			return "/redirect:/login";
		}
		
		List<Mascota> mascotas = mascotaService.buscarMascotasPorUsuario(login.getId());
		model.put("mascotas", mascotas);
		
		return "mascotas";
	
	}
	
	@GetMapping("/editar-perfil")
	public String editarPerfil(HttpSession session , @RequestParam(required = false) String id, @RequestParam(required = false) String accion, ModelMap model) {
		
		if (accion == null) {
			accion = "Crear";
		}
		Usuario login = (Usuario) session.getAttribute("usuariosession");
		if (login == null) {
			return "/redirect:/login";
		}
		
		Mascota mascota = new Mascota();
		if (id != null && !id.isEmpty()) {
			
			try {
				
				mascota = mascotaService.buscarPorId(id);
			} catch (ExceptionService e) {
				
				e.printStackTrace();
			}
			
		}
		
		model.put("perfil", mascota);
		model.put("accion", accion);
		model.put("sexos", Sexo.values());
		model.put("tipos", Tipo.values());
		
		return "mascota.html";
	}
	
	@PreAuthorize("hasAnyRole('ROLE_USUARIO')")
	@PostMapping("/actualizar-perfil")
	public String actualizar(ModelMap model, HttpSession session, MultipartFile archivo, @RequestParam String id, @RequestParam String nombre, @RequestParam Sexo sexo, @RequestParam Tipo tipo ) {
			
		Usuario login = (Usuario) session.getAttribute("usuariosession");
		if (login == null) {
			return "/redirect:/login";
			
		}
		
		try {
		
			if ( id == null || id.isEmpty() ) {
				
				mascotaService.agregarMascota(archivo, login.getId(), nombre, sexo, tipo);
			}else {
				
				mascotaService.modificar(archivo, login.getId(), id, nombre, sexo, tipo);
			}
			
			return "redirect:/inicio";
		} catch (ExceptionService ex) {
			
			Mascota mascota = new Mascota();
			mascota.setId(id);
			mascota.setNombre(nombre);
			mascota.setSexo(sexo);
			mascota.setTipo(tipo);
			
			model.put("accion", "Actualizar");
			model.put("sexos", Sexo.values());
			model.put("tipos", Tipo.values());
			model.put("error", ex.getMessage());
			model.put("perfil", mascota);
			
			return "mascota.html";
		}
		
	}
	
	
	@PostMapping("/eliminar-perfil")
	public String eliminar(HttpSession session, @RequestParam String id) {
		
		try {
			
			Usuario login = (Usuario) session.getAttribute("usuariosession");
			mascotaService.eliminar(login.getId(), id);
			
		} catch (ExceptionService e) {
			
			e.printStackTrace();
		}
		
		return "redirect:/mascota/mis-mascotas";
	}
	
	
}
