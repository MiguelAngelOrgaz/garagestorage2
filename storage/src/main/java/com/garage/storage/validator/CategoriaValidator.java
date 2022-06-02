package com.garage.storage.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.garage.storage.entity.Categoria;


@Component
public class CategoriaValidator implements Validator {
 
	@Override
    public boolean supports(Class<?> aClass) {
        return Categoria.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Categoria categoria = (Categoria) o;
 
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nombre", "campo.vacio");
        if (categoria.getNombre().length() > 45) {
            errors.rejectValue("nombre", "error.categoria.nombre.max");
        }
        
        if(categoria.getIdcategoria()!=null && categoria.getCategoriapadre()!=null && categoria.getIdcategoria().equals(categoria.getCategoriapadre().getIdcategoria()))
        {
        	errors.rejectValue("categoriapadre", "error.categoria.padre.igual");
        }
        
        if(categoria.getCategoriashijas()!=null && !categoria.getCategoriashijas().isEmpty() && categoria.getCategoriapadre()!=null)
        {
        	errors.rejectValue("categoriapadre", "error.categoria.padre.hijas");
        }

    }
}
