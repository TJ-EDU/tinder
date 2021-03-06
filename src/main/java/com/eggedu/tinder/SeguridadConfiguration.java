package com.eggedu.tinder;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.eggedu.tinder.services.UsuarioService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SeguridadConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	public UsuarioService usuarioService;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(usuarioService).passwordEncoder(new BCryptPasswordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.headers().frameOptions().sameOrigin().and()
		
		.authorizeRequests()
		.antMatchers("/css/*", "/js/*","/img/*", "/vendor/*", "/login", "/registro")
		.permitAll().and().formLogin().
		loginPage("/login")
		.loginProcessingUrl("/logincheck")
		.usernameParameter("username")
		.passwordParameter("password")
		.defaultSuccessUrl("/inicio")
		.and()
		.logout().
		logoutUrl("/logout")
		.logoutSuccessUrl("/login?logout")
		.permitAll().and().csrf().disable()
		;
	}
}
