package com.hotel.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hotel.app.entity.enums.StatoCamera;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "camere")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Camera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String numero; // es: "101", "202A"

    @Column(nullable = false)
    private Integer piano;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "struttura_id", nullable = false)
    private Struttura struttura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_camera_id", nullable = false)
    private TipoCamera tipoCamera;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatoCamera stato = StatoCamera.DISPONIBILE;

    private String note;

    @JsonIgnore
    @OneToMany(mappedBy = "camera", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Prenotazione> prenotazioni = new ArrayList<>();

    @Column(nullable = false)
    @Builder.Default
    private Boolean attiva = true;
}
