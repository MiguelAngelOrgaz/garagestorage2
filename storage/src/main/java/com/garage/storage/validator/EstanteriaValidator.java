package com.garage.storage.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.garage.storage.dto.EstanteriaDTO;


@Component
public class EstanteriaValidator implements Validator {
 
	@Override
    public boolean supports(Class<?> aClass) {
        return EstanteriaDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        EstanteriaDTO estanteria = (EstanteriaDTO) o;
 
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nombre", "campo.vacio");
        if (estanteria.getNombre().length() > 45) {
            errors.rejectValue("nombre", "error.estanteria.nombre.max");
        }
        if (estanteria.getDescripcion().length() > 250) {
            errors.rejectValue("descripcion", "error.estanteria.descripcion.max");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "descripcion", "campo.vacio");

    }
}
