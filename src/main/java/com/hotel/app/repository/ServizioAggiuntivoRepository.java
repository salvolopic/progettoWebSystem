package com.hotel.app.repository;

import com.hotel.app.entity.ServizioAggiuntivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServizioAggiuntivoRepository extends JpaRepository<ServizioAggiuntivo, Long> {

    List<ServizioAggiuntivo> findByStruttura_IdAndAttivoTrue(Long strutturaId);

}
