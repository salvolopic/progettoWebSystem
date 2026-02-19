package com.hotel.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "registro_pulizie")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistroPulizie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camera_id", nullable = false)
    private Camera camera;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personale_id", nullable = false)
    private Utente personale;

    @Column(nullable = false)
    private LocalDateTime dataOra;

    private String note;

    @PrePersist
    protected void onCreate() {
        dataOra = LocalDateTime.now();
    }
}
