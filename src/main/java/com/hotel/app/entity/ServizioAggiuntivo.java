package com.hotel.app.entity;

import com.hotel.app.entity.enums.TipoServizio;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "servizi_aggiuntivi")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServizioAggiuntivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoServizio tipo;

    @Column(nullable = false)
    private String nome;

    private String descrizione;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal prezzo;

    @Column(nullable = false)
    @Builder.Default
    private Boolean perGiorno = false; // se true, il prezzo è giornaliero

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "struttura_id", nullable = false)
    private Struttura struttura;

    @Column(nullable = false)
    @Builder.Default
    private Boolean attivo = true;
}
