package com.hotel.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hotel.app.entity.enums.TipoDocumento;
import com.hotel.app.entity.enums.TipoEsenzione;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "ospiti")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ospite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prenotazione_id", nullable = false)
    private Prenotazione prenotazione;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cognome;

    @Column(nullable = false)
    private LocalDate dataNascita;

    @Column(nullable = false)
    private String luogoNascita;

    private String provinciaNascita;

    @Column(nullable = false)
    private String cittadinanza;

    // Solo per il capogruppo
    @Column(nullable = false)
    @Builder.Default
    private Boolean isCapogruppo = false;

    // Dati documento (obbligatori solo per capogruppo)
    @Enumerated(EnumType.STRING)
    private TipoDocumento tipoDocumento;

    private String numeroDocumento;

    private String enteRilascioDocumento;

    private LocalDate dataRilascioDocumento;

    private LocalDate dataScadenzaDocumento;

    // Esenzione tassa di soggiorno
    @Builder.Default
    private Boolean esenteTassaSoggiorno = false;

    @Enumerated(EnumType.STRING)
    private TipoEsenzione tipoEsenzione;
}
