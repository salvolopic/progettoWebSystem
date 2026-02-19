package com.hotel.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hotel.app.entity.enums.StatoPrenotazione;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "prenotazioni")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prenotazione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String codicePrenotazione;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Utente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camera_id", nullable = false)
    private Camera camera;

    @Column(nullable = false)
    private LocalDate dataCheckIn;

    @Column(nullable = false)
    private LocalDate dataCheckOut;

    private LocalDateTime dataCheckInEffettivo;

    private LocalDateTime dataCheckOutEffettivo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatoPrenotazione stato = StatoPrenotazione.CONFERMATA;

    @Column(nullable = false)
    private Integer numeroOspiti;

    @Column(precision = 10, scale = 2)
    private BigDecimal costoCamera;

    @Column(precision = 10, scale = 2)
    private BigDecimal costoServizi;

    @Column(precision = 10, scale = 2)
    private BigDecimal costoTotale;

    @JsonIgnore
    @OneToMany(mappedBy = "prenotazione", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Ospite> ospiti = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "prenotazione", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PrenotazioneServizio> serviziPrenotati = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "prenotazione", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<NotaCliente> note = new ArrayList<>();

    private LocalDateTime dataCreazione;

    private LocalDateTime dataModifica;

    @PrePersist
    protected void onCreate() {
        dataCreazione = LocalDateTime.now();
        dataModifica = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        dataModifica = LocalDateTime.now();
    }
}
