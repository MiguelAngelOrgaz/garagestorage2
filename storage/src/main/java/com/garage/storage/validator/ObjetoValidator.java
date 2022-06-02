package com.garage.storage.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import com.garage.storage.entity.Objeto;
import com.garage.storage.util.Constantes;


@Component
public class ObjetoValidator implements Validator {
 
	@Override
    public boolean supports(Class<?> aClass) {
        return Objeto.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
    	Objeto objeto = (Objeto) o;
 
    	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nombre", "campo.vacio");
        if (objeto.getNombre().length() > 45) {
            errors.rejectValue("nombre", "error.objeto.nombre.max");
        }        
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "descripcion", "campo.vacio");
        if (objeto.getDescripcion().length() > 45) {
            errors.rejectValue("descripcion", "error.objeto.descripcion.max");
        }
        if (objeto.getCategoria()==null || objeto.getCategoria().getIdcategoria()==null) {
            errors.rejectValue("categoria", "error.objeto.categoria");
        }

    }
    
    public void validate(Object o, MultipartFile imagen, Errors errors) {
        validate(o,errors);
        if(!imagen.isEmpty() && !Constantes.tiposArchivosHabilitados().contains(imagen.getContentType()))
				errors.rejectValue("foto", "error.caja.imagen");
    }
}
