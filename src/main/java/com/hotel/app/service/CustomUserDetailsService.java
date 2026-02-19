package com.hotel.app.service;


import com.hotel.app.repository.UtenteRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


import org.springframework.security.core.userdetails.User;
import com.hotel.app.entity.Utente;




import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UtenteRepository utenteRepository;  // INIETTATO, non statico!

    public CustomUserDetailsService(UtenteRepository utenteRepository) {
        this.utenteRepository = utenteRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        // Recupera l'utente dal database
        Utente utente = utenteRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato"));
        
        // Converti Utente in UserDetails (oggetto di Spring Security)
        return User.builder()
            .username(utente.getUsername())
            .password(utente.getPassword())
            .roles(utente.getRuolo().name())
            .build();
    }
}




