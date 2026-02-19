package com.hotel.app.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotel.app.entity.Camera;
import com.hotel.app.entity.RegistroPulizie;
import com.hotel.app.entity.Utente;
import com.hotel.app.entity.enums.StatoCamera;
import com.hotel.app.repository.CameraRepository;
import com.hotel.app.repository.RegistroPulizieRepository;
import com.hotel.app.repository.UtenteRepository;


@Service
public class PulizieService {

    private final RegistroPulizieRepository registroPulizieRepository;
    private final CameraRepository cameraRepository;
    private final UtenteRepository utenteRepository;

    public PulizieService(RegistroPulizieRepository registroPulizieRepository,
                          CameraRepository cameraRepository,
                          UtenteRepository utenteRepository) {
        this.registroPulizieRepository = registroPulizieRepository;
        this.cameraRepository = cameraRepository;
        this.utenteRepository = utenteRepository;
    }

    @Transactional
    public RegistroPulizie registraPulizia(Long cameraId, Long personaleId, String note) {
        Camera camera = cameraRepository.findById(cameraId)
                .orElseThrow(() -> new IllegalArgumentException("Camera non trovata"));

        Utente personale = utenteRepository.findById(personaleId)
                .orElseThrow(() -> new IllegalArgumentException("Personale non trovato"));

        // Crea registro pulizia
        RegistroPulizie registro = new RegistroPulizie();
        registro.setCamera(camera);
        registro.setPersonale(personale);
        registro.setDataOra(LocalDateTime.now());
        registro.setNote(note);

        // Cambia stato camera da DA_PULIRE a DISPONIBILE
        camera.setStato(StatoCamera.DISPONIBILE);
        cameraRepository.save(camera);

        return registroPulizieRepository.save(registro);
    }

    public List<RegistroPulizie> findByCameraId(Long cameraId) {
        return registroPulizieRepository.findByCamera_Id(cameraId);
    }

}
