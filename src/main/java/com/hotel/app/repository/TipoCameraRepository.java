package com.hotel.app.repository;

import com.hotel.app.entity.TipoCamera;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TipoCameraRepository extends JpaRepository<TipoCamera, Long> {

}
