package com.garage.storage.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.garage.storage.entity.Usuario;
import com.garage.storage.service.impl.ProfileServiceImpl;

@Component
public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	@Autowired
    private ProfileServiceImpl userService;
     
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
    	UserDetails userDetails =  (UserDetails) authentication.getPrincipal();
        Usuario user = userService.findByNombre(userDetails.getUsername());
        if (user.getIntento() > 0) {
            userService.resetearIntentosFallidos(user.getIdusuario());
        }        
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
