package com.hotel.app.repository;

import com.hotel.app.entity.PrenotazioneServizio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrenotazioneServizioRepository extends JpaRepository<PrenotazioneServizio, Long> {

    List<PrenotazioneServizio> findByPrenotazione_Id(Long prenotazioneId);

}
