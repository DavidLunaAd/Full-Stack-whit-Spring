package com.cursojava.curso.controlle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cursojava.curso.dao.UsuarioDao;
import com.cursojava.curso.model.Usuario;
import com.cursojava.curso.utils.JWTUtil;


@RestController
public class AuthController {
	
	@Autowired
	private UsuarioDao usuarioDao;
	
	@Autowired
	private JWTUtil jwtutil;
	
	@RequestMapping(value = "api/login", method = RequestMethod.POST)
	public String login(@RequestBody Usuario usuario){
		
		Usuario usuarioLogueado = usuarioDao.obtenerUsurioPorCredenciales(usuario);
		
		if (usuarioLogueado != null) {
			String tokenJwt = jwtutil.create(String.valueOf(usuarioLogueado.getId()), usuarioLogueado.getEmail());
			return tokenJwt;
		}
		return "FAIL";
	}
}
