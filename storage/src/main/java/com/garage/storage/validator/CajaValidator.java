package com.garage.storage.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import com.garage.storage.entity.Caja;
import com.garage.storage.util.Constantes;


@Component
public class CajaValidator implements Validator {
 
	@Override
    public boolean supports(Class<?> aClass) {
        return Caja.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Caja caja = (Caja) o;
 
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "descripcion", "campo.vacio");
        if (caja.getDescripcion().length() > 45) {
            errors.rejectValue("descripcion", "error.caja.descripcion.max");
        }

    }
    
    public void validate(Object o, MultipartFile imagen, Errors errors) {
        validate(o,errors);
        if(!imagen.isEmpty() && !Constantes.tiposArchivosHabilitados().contains(imagen.getContentType()))
				errors.rejectValue("foto", "error.caja.imagen");
    }
}
