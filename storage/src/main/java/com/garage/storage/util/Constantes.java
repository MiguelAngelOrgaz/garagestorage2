package com.garage.storage.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;

public class Constantes {
	public static final String ROLUSUARIO = "ROLE_USUARIO";
	public static final String ROLADMINISTRADOR = "ROLE_ADMIN";
	public static final String TITULO = "tituloPagina";
	public static final String REDIRECT_PRINCIPAL = "redirect:/welcome";
	public static final String MODEL_ALLCATEGORIES = "categoriasAll";
	public static final String MODEL_ALLESTANTERIAS = "estanteriasAll";
	public static final String MODEL_ALLESTANCIAS = "estanciasAll";
	
	public static List<String> tiposArchivosHabilitados()
	{
		List<String> tipos = new ArrayList<>();
		tipos.add(MediaType.IMAGE_GIF_VALUE);
		tipos.add(MediaType.IMAGE_JPEG_VALUE);
		tipos.add(MediaType.IMAGE_PNG_VALUE);
		return tipos;		
	}
}
