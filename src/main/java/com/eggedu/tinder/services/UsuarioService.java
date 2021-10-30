package com.eggedu.tinder.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.eggedu.tinder.entities.Foto;
import com.eggedu.tinder.entities.Usuario;
import com.eggedu.tinder.entities.Zona;
import com.eggedu.tinder.exception.ExceptionService;
import com.eggedu.tinder.repositories.UsuarioRepository;
import com.eggedu.tinder.repositories.ZonaRepository;

@Service
public class UsuarioService implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private ZonaRepository zonaRepository;

	@Autowired
	private FotoService fotoService;

	@Autowired
	private NotificacionService notificacionService;

	@Transactional
	public void registrar(MultipartFile archivo, String nombre, String apellido, String email, String clave,
			String clave2, String idZona) throws ExceptionService {

		// deprecado use getById
		Zona zona = zonaRepository.getOne(idZona);
		// Zona zona = zonaRepository.getById(idZona);

		validar(nombre, apellido, email, clave, clave2, zona);

		Usuario usuario = new Usuario();
		usuario.setNombre(nombre);
		usuario.setApellido(apellido);
		usuario.setEmail(email);
		usuario.setZona(zona);

		String encriptada = new BCryptPasswordEncoder().encode(clave);
		usuario.setClave(encriptada);

		usuario.setAlta(new Date());

		Foto foto = fotoService.guardar(archivo);
		usuario.setFoto(foto);

		usuarioRepository.save(usuario);

		// notificacionService.enviar("Bienvenidos al Tinder de Mascota", "Tinder de
		// Mascota", usuario.getEmail());
	}

	@Transactional
	public void modificar(MultipartFile archivo, String id, String nombre, String apellido, String email, String clave,
			String clave2, String idZona) throws ExceptionService {

		// deprecado use getById
		Zona zona = zonaRepository.getOne(idZona);
		// Zona zona = zonaRepository.getById(idZona);

		validar(nombre, apellido, email, clave, clave2, zona);

		Optional<Usuario> respuesta = usuarioRepository.findById(id);

		if (respuesta.isPresent()) {

			Usuario usuario = respuesta.get();
			usuario.setNombre(nombre);
			usuario.setApellido(apellido);
			usuario.setEmail(email);
			usuario.setZona(zona);
			
			String encriptada = new BCryptPasswordEncoder().encode(clave);
			usuario.setClave(encriptada);

			String idFoto = null;
			if (usuario.getFoto() != null) {

				idFoto = usuario.getFoto().getId();
			}

			Foto foto = fotoService.actualizar(idFoto, archivo);
			usuario.setFoto(foto);

			usuarioRepository.save(usuario);

		} else {
			throw new ExceptionService("NO SE ENCONTRÓ EL USUARIO SOLICITADO");
		}

	}

	@Transactional
	public void deshabilitar(String id) throws ExceptionService {

		Optional<Usuario> respuesta = usuarioRepository.findById(id);

		if (respuesta.isPresent()) {

			Usuario usuario = respuesta.get();
			usuario.setBaja(new Date());

			usuarioRepository.save(usuario);

		} else {
			throw new ExceptionService("NO SE ENCONTRO EL USUARIO SOLICITADO");
		}

	}

	@Transactional
	public void habilitar(String id) throws ExceptionService {

		Optional<Usuario> respuesta = usuarioRepository.findById(id);

		if (respuesta.isPresent()) {

			Usuario usuario = respuesta.get();
			usuario.setBaja(null);

			usuarioRepository.save(usuario);

		} else {
			throw new ExceptionService("NO SE ENCONTRO EL USUARIO SOLICITADO");
		}

	}

	public void validar(String nombre, String apellido, String email, String clave, String clave2, Zona zona)
			throws ExceptionService {

		if (nombre == null || nombre.isEmpty()) {
			throw new ExceptionService("EL NOMBRE DEL USUARIO NO PUEDE SER VACIO");
		}

		if (apellido == null || apellido.isEmpty()) {
			throw new ExceptionService("EL APELLIDO DEL USUARIO NO PUEDE SER VACIO");
		}

		if (email == null || email.isEmpty()) {
			throw new ExceptionService("EL EMAIL DEL USUARIO NO PUEDE SER VACIO");
		}

		if (clave == null || clave.isEmpty() || clave.length() <= 6) {
			throw new ExceptionService("LA CLAVE NO PUEDE SER VACIA Y TIENE QUE TENER MAS DE 6 DIGITOS");
		}

		if (!clave.equals(clave2)) {
			throw new ExceptionService("LAS CLAVES DEBEN SER IGUALES");
		}

		if (zona == null) {
			throw new ExceptionService("NO SE ENCONTRO LA ZONA SOLICITADA");
		}
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		Usuario usuario = usuarioRepository.buscarPorEmail(email);
		
		if (usuario != null) {
			System.out.println("USUARIO EMAIL => " + usuario.getEmail());
			List<GrantedAuthority> permisos = new ArrayList<>();

			GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_USUARIO");
			permisos.add(p1);
			
			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			HttpSession session = attr.getRequest().getSession(true);
			session.setAttribute("usuariosession", usuario);
			
			User user = new User(usuario.getEmail(), usuario.getClave(), permisos);

			return user;
		} else {
			return null;
		}

	}
	
	public Usuario buscarPorId(String idUser) throws ExceptionService {
		
		Usuario usuario = usuarioRepository.findById(idUser).get();
		if (usuario != null) {
			
			return usuario;
		}else {

			throw new ExceptionService("NO SE ENCONTRO EL USUARIO SOLICITADO");
		}
		
	}
	


}
