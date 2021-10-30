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

import com.eggedu.tinder.entities.Usuario;
import com.eggedu.tinder.entities.Zona;
import com.eggedu.tinder.exception.ExceptionService;
import com.eggedu.tinder.repositories.ZonaRepository;
import com.eggedu.tinder.services.UsuarioService;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private ZonaRepository zonaRepository;
	
	@PreAuthorize("hasAnyRole('ROLE_USUARIO')")
	@GetMapping("/editar-perfil")
	public String editarPerfil(HttpSession session , @RequestParam String id, ModelMap model) {
		
		List<Zona> zonas = zonaRepository.findAll();
		model.put("zonas", zonas);
		
		Usuario login = (Usuario)session.getAttribute("usuariosession");
		if (login == null || !login.getId().equals(id)) {
			return "redirect:/inicio";
		}
		
		try {
			Usuario usuario = usuarioService.buscarPorId(id);
			model.addAttribute("perfil", usuario);
			
		} catch (ExceptionService ex) {
			model.addAttribute("error", ex.getMessage());
		}
		
		return "perfil.html";
	}
	
	@PreAuthorize("hasAnyRole('ROLE_USUARIO')")
	@PostMapping("/actualizar-perfil")
	public String registrar(ModelMap model, HttpSession session, MultipartFile archivo, @RequestParam String id, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String mail, @RequestParam String clave1, @RequestParam String clave2, @RequestParam String idZona ) {
		
		Usuario usuario = null;
		try {
			
			Usuario login = (Usuario)session.getAttribute("usuariosession");
			if (login == null || !login.getId().equals(id)) {
				return "redirect:/inicio";
			}
			
			usuario = usuarioService.buscarPorId(id);
			usuarioService.modificar(archivo, id, nombre, apellido, mail, clave1, clave2, idZona);
			session.setAttribute("usuariosession", usuario);
			
			return "redirect:/inicio";
		} catch (ExceptionService ex) {
			
			List<Zona> zonas = zonaRepository.findAll();
			model.put("zonas", zonas);
			model.put("error", ex.getMessage());
			model.put("perfil", usuario);
			
			return "perfil.html";
		}
		
	}
	
	
}
