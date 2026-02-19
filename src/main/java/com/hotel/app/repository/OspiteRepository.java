package com.hotel.app.repository;

import com.hotel.app.entity.Ospite;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OspiteRepository extends JpaRepository<Ospite, Long> {

    List<Ospite> findByPrenotazione_Id(Long prenotazioneId);

}
