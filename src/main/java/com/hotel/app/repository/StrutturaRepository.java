package com.hotel.app.repository;

import com.hotel.app.entity.Struttura;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StrutturaRepository extends JpaRepository<Struttura, Long> {

}
