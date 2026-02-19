package com.hotel.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tipi_camera")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoCamera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome; // es: Singola, Doppia, Suite, Familiare

    private String descrizione;

    @Column(nullable = false)
    private Integer capacitaMinima;

    @Column(nullable = false)
    private Integer capacitaMassima;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal prezzoBase;

    private Boolean haVista;

    private Boolean haBalcone;

    private Boolean haAriaCondizionata;

    @Builder.Default
    private Boolean attivo = true;
}
