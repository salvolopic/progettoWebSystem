package com.hotel.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "note_clienti")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotaCliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prenotazione_id", nullable = false)
    private Prenotazione prenotazione;

    @Column(nullable = false, length = 1000)
    private String testo;

    @Column(nullable = false)
    private LocalDateTime dataCreazione;

    @Builder.Default
    private Boolean lettaDalPersonale = false;

    private LocalDateTime dataLettura;

    // Chi ha letto la nota
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "letta_da_id")
    private Utente lettaDa;

    @PrePersist
    protected void onCreate() {
        dataCreazione = LocalDateTime.now();
    }
}
