package com.hotel.app.repository;

import com.hotel.app.entity.RegistroPulizie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistroPulizieRepository extends JpaRepository<RegistroPulizie, Long> {

    List<RegistroPulizie> findByCamera_Id(Long cameraId);

    List<RegistroPulizie> findByPersonale_Id(Long personaleId);

}
