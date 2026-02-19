package com.hotel.app.repository;

import com.hotel.app.entity.Prenotazione;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;


import java.util.List;
import java.util.Optional;

@Repository
public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {

    Optional<Prenotazione> findByCodicePrenotazione(String codicePrenotazione);

    List<Prenotazione> findByCliente_Id(Long clienteId);

    @Query("SELECT DISTINCT p FROM Prenotazione p " +
           "JOIN FETCH p.camera c " +
           "JOIN FETCH c.struttura s " +
           "LEFT JOIN FETCH p.ospiti " +
           "WHERE p.stato IN ('CHECK_IN_EFFETTUATO', 'CHECK_OUT_EFFETTUATO') " +
           "AND p.dataCheckIn <= :data " +
           "AND p.dataCheckOut >= :data " +
           "AND (p.stato <> 'CHECK_OUT_EFFETTUATO' OR p.dataCheckOutEffettivo IS NULL OR CAST(p.dataCheckOutEffettivo AS LocalDate) >= :data) " +
           "AND (:strutturaId IS NULL OR c.struttura.id = :strutturaId)")
    List<Prenotazione> findPrenotazioniPresenti(@Param("data") LocalDate data, @Param("strutturaId") Long strutturaId);

}
