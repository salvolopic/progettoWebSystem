package com.hotel.app.repository;

import com.hotel.app.entity.Camera;
import com.hotel.app.entity.enums.StatoCamera;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CameraRepository extends JpaRepository<Camera, Long> {

    List<Camera> findByStruttura_Id(Long strutturaId);

    List<Camera> findByStruttura_IdAndStato(Long strutturaId, StatoCamera stato);
}
