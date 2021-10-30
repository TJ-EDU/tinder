package com.eggedu.tinder.controllers;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class ErroresController implements ErrorController {

	@RequestMapping(value="/error", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView renderErrorPage(HttpServletRequest httpRequest) {
		
		ModelAndView errorPage = new ModelAndView("error");
		String errorMsg = "";
		int httpErrorCode = getErrorCode(httpRequest);
		
		switch (httpErrorCode) {
		case 400:
			errorMsg = "EL RECURSO SOLICITADO NO EXISTE.";
			break;
		case 401:
			errorMsg = "NO SE ENCUENTRA AUTORIZADO.";
		case 403:
			errorMsg = "NO TIENE PERMISOS PARA ACCEDER AL RECURSO.";
			break;
		case 404:
			errorMsg = "EL RECURSO SOLICITADO NO FUE ENCONTRADO.";
			break;
		case 500:
			errorMsg = "OCURRIÓ UN ERROR INTERNO.";
			break;
		
		}
		
		errorPage.addObject("codigo", httpErrorCode);
		errorPage.addObject("mensaje", errorMsg);
		
		return errorPage;
	}
	
	//@SuppressWarnings("rawtypes")
	private int getErrorCode(HttpServletRequest httpRequest) {
		
		Map mapa = httpRequest.getParameterMap();
		for(Object key : mapa.keySet()) {
			
			String[] valores = (String[]) mapa.get(key);
			for(String valor : valores) {
				System.out.println(key.toString() + ": " + valor);
			}
		}
		
		Enumeration<String> atributos = httpRequest.getAttributeNames();
		while (atributos.hasMoreElements()) {
			
			String key = atributos.nextElement();
			System.out.println(key + ": " + httpRequest.getAttribute(key));
		}
		
		return (Integer) httpRequest.getAttribute("javax.servlet.error.status_code");
		
	}
	
	
	public String getErrorPath() {
		return "/error";
	}
	
}
