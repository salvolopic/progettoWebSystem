package com.hotel.app.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotel.app.entity.Camera;
import com.hotel.app.entity.enums.StatoCamera;
import com.hotel.app.entity.enums.StatoPrenotazione;
import com.hotel.app.repository.CameraRepository;
import com.hotel.app.repository.PrenotazioneRepository;


@Service
public class CameraService {

    private final CameraRepository cameraRepository;
    private final PrenotazioneRepository prenotazioneRepository;

    public CameraService(CameraRepository cameraRepository, PrenotazioneRepository prenotazioneRepository) {
        this.cameraRepository = cameraRepository;
        this.prenotazioneRepository = prenotazioneRepository;
    }

    public Optional<Camera> findById(Long id) {
        return cameraRepository.findById(id);
    }

    public List<Camera> findAll() {
        return cameraRepository.findAll();
    }

    public List<Camera> findByStrutturaId(Long strutturaId) {
        return cameraRepository.findByStruttura_Id(strutturaId);
    }

    public List<Camera> findCamereDisponibili(Long strutturaId, LocalDate dataCheckIn, LocalDate dataCheckOut) {
       
        List<Camera> camereDisponibili = cameraRepository.findByStruttura_IdAndStato(strutturaId, StatoCamera.DISPONIBILE);

        return camereDisponibili.stream()
            .filter(camera -> {
                boolean hasOverlap = prenotazioneRepository.findAll().stream()
                    .filter(p -> p.getCamera().getId().equals(camera.getId()))
                    .filter(p -> p.getStato() != StatoPrenotazione.CANCELLATA &&
                                p.getStato() != StatoPrenotazione.CHECK_OUT_EFFETTUATO)
                    .anyMatch(p -> dataCheckIn.isBefore(p.getDataCheckOut()) &&
                                  dataCheckOut.isAfter(p.getDataCheckIn()));

                return !hasOverlap; // Include solo se NON ha sovrapposizioni
            })
            .collect(Collectors.toList());
    }

    public List<Camera> findCamereByStrutturaIdAndStato(Long strutturaId, StatoCamera stato) {
        return cameraRepository.findByStruttura_IdAndStato(strutturaId, stato);
    }

    @Transactional
    public void cambiaStatoCamera(Long id, StatoCamera nuovoStato) {
        Camera camera = cameraRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Camera non trovata con id: " + id));

        camera.setStato(nuovoStato);
        cameraRepository.save(camera);
    }

}
