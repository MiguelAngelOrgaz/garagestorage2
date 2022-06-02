package com.garage.storage.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.garage.storage.entity.Usuario;
import com.garage.storage.service.impl.ProfileServiceImpl;

@Component
public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	 @Autowired
	    private ProfileServiceImpl userService;
	 
	 	private boolean expired = false;
	 	private boolean lock = false;
	     
	    public CustomLoginFailureHandler() {
		super();
	}

		@Override
	    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
	            AuthenticationException exception) throws IOException, ServletException {
	        String nombre = request.getParameter("username");
	        Usuario user = userService.findByNombre(nombre);
	         
	        if (user != null) {
	            if (user.getEsactivo()) {
	                if (user.getIntento() < ProfileServiceImpl.MAX_FAILED_ATTEMPTS - 1) {
	                    userService.incrementarIntentosFallidos(user);
	                } else {
	                    userService.lock(user);
	                    setExpired(true);
	                }
	            } 
	            else
	            	setLock(true);
	             
	        }
	         
	        if(this.isExpired())
	        	super.setDefaultFailureUrl("/login?expired");
	        else if(this.isLock())
	        	super.setDefaultFailureUrl("/login?lock");
	        else
	        	super.setDefaultFailureUrl("/login?error");
	        super.onAuthenticationFailure(request, response, exception);
	    }

		public boolean isExpired() {
			return expired;
		}

		public void setExpired(boolean expired) {
			this.expired = expired;
		}

		public boolean isLock() {
			return lock;
		}

		public void setLock(boolean lock) {
			this.lock = lock;
		}
}
