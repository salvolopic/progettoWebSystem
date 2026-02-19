package com.hotel.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "strutture")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Struttura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String indirizzo;

    @Column(nullable = false)
    private String citta;

    private String cap;

    private String provincia;

    @Column(nullable = false)
    private String telefono;

    private String email;

    private String descrizione;

    private Integer stelle;

    @JsonIgnore
    @OneToMany(mappedBy = "struttura", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Camera> camere = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "struttura", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ServizioAggiuntivo> serviziDisponibili = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "strutturaAssegnata")
    @Builder.Default
    private List<Utente> personale = new ArrayList<>();

    @Column(nullable = false)
    @Builder.Default
    private Boolean attiva = true;
}
