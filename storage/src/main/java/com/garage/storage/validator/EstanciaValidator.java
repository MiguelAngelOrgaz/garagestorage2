package com.garage.storage.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.garage.storage.dto.EstanciaDTO;


@Component
public class EstanciaValidator implements Validator {
 
    @Override
    public boolean supports(Class<?> aClass) {
        return EstanciaDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        EstanciaDTO estancia = (EstanciaDTO) o;
 
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nombre", "campo.vacio");
        if (estancia.getNombre().length() > 45) {
            errors.rejectValue("nombre", "error.estancia.nombre.max");
        }
        if (estancia.getDescripcion().length() > 250) {
            errors.rejectValue("descripcion", "error.estancia.descripcion.max");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "descripcion", "campo.vacio");

    }
}
