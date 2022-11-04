package com.cursojava.curso.controlle;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cursojava.curso.dao.UsuarioDao;
import com.cursojava.curso.model.Usuario;
import com.cursojava.curso.utils.JWTUtil;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

@RestController
public class UsuarioController {
	
	@Autowired
	private UsuarioDao usuarioDao;
	
	@Autowired
	private JWTUtil jwtutil;
	
	@RequestMapping(value = "api/usuarios/{id}")
	public Usuario getUsuario(@PathVariable Long id){
		
		Usuario usuario = new Usuario();	
		usuario.setId(id);
		usuario.setNombre("Jaun");
		usuario.setApellido("Maolla");
		usuario.setEmail("askjdhajk@dhjdasd");
		usuario.setTelefono("65424154");
		usuario.setPassword("654321");
		
		return usuario;
	}
	
	@RequestMapping(value = "api/usuarios")
	public List<Usuario> getUsuarios(@RequestHeader(value="Authorization") String token){
		
		if (!validarToken(token)) {
			return null;
		}
		return usuarioDao.getUsuarios();
	}
	
	private boolean validarToken(String token) {
		String usuarioId = jwtutil.getKey(token);
			return usuarioId != null;
	}
	
	@RequestMapping(value = "api/usuarios", method = RequestMethod.POST)
	public void registrarUsuario(@RequestBody Usuario usuario){
		
		Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
		String hash = argon2.hash(1, 1024, 1, usuario.getPassword());
		usuario.setPassword(hash);
		
		usuarioDao.registrar(usuario);
	}

	@RequestMapping(value = "api/usuarios/{id}", method = RequestMethod.DELETE)
	public void eliminar(@RequestHeader(value="Authorization") String token, 
						 @PathVariable Long id){
		if (!validarToken(token)) {
			return;
			}
		
		usuarioDao.eliminar(id);		
	}

	
}
