package com.garage.storage.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.garage.storage.dao.IUsuarioDAO;
import com.garage.storage.entity.Rol;
import com.garage.storage.entity.Usuario;


@Service
public class UserDetailsServiceImpl implements UserDetailsService{
    @Autowired
    private IUsuarioDAO userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        Usuario user = userRepository.findByNombre(username);
        if (user == null) throw new UsernameNotFoundException(username);

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Rol role : user.getRoles()){
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getNombre()));
        }
        
        //Ponemos un intento más del que tuviera

        return new org.springframework.security.core.userdetails.User(user.getNombre(), user.getPassword(), true, true, true, user.getEsactivo(),  grantedAuthorities);
    }
}
