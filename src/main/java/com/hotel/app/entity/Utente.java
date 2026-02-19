package com.hotel.app.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hotel.app.entity.enums.Ruolo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "utenti")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder


public class Utente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cognome;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Ruolo ruolo;

    @Column(nullable = false)
    private Boolean attivo;

    @Column(nullable = false)
    private LocalDateTime dataRegistrazione;

    // Struttura a cui è assegnato (solo per PERSONALE/GESTORE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "struttura_assegnata_id")
    private Struttura strutturaAssegnata;

    // Prenotazioni effettuate (solo per CLIENTE)
    @JsonIgnore
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Prenotazione> prenotazioni = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        dataRegistrazione = LocalDateTime.now();
    }
}

