package com.garage.storage.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.garage.storage.dto.UsuarioDTO;
import com.garage.storage.service.IProfileService;

@Component
public class UserValidator implements Validator {
    @Autowired
    private IProfileService userService;

    @Override
    public boolean supports(Class<?> aClass) {
        return UsuarioDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UsuarioDTO user = (UsuarioDTO) o;
 
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nombre", "campo.vacio");
        if (user.getNombre().length() < 6 || user.getNombre().length() > 32) {
            errors.rejectValue("nombre", "Size.userForm.username");
        }
        if (user.getIdusuario()==null && userService.findByNombre(user.getNombre()) != null) {
            errors.rejectValue("nombre", "Duplicate.userForm.username");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "campo.vacio");
        if (user.getPassword().length() < 8 || user.getPassword().length() > 32) {
            errors.rejectValue("password", "Size.userForm.password");
        }

        if (!user.getPasswordConfirm().equals(user.getPassword())) {
            errors.rejectValue("passwordConfirm", "Diff.userForm.passwordConfirm");
        }
    }
}
