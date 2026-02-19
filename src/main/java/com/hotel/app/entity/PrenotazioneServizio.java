package com.hotel.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "prenotazioni_servizi")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrenotazioneServizio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prenotazione_id", nullable = false)
    private Prenotazione prenotazione;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servizio_id", nullable = false)
    private ServizioAggiuntivo servizio;

    @Column(nullable = false)
    @Builder.Default
    private Integer quantita = 1;

    private LocalDate dataInizio;

    private LocalDate dataFine;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal prezzoUnitario;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal prezzoTotale;

    private String note;
}
