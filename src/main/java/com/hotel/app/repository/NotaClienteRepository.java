package com.hotel.app.repository;

import com.hotel.app.entity.NotaCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotaClienteRepository extends JpaRepository<NotaCliente, Long> {

    List<NotaCliente> findByPrenotazione_Id(Long prenotazioneId);

    List<NotaCliente> findByLettaDalPersonaleFalse();

}
