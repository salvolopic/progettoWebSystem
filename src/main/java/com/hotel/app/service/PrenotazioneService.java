package com.hotel.app.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotel.app.dto.request.PrenotazioneRequest;
import com.hotel.app.dto.response.PrenotazioneResponse;
import com.hotel.app.entity.Camera;
import com.hotel.app.entity.Prenotazione;
import com.hotel.app.entity.Utente;
import com.hotel.app.entity.enums.StatoPrenotazione;
import com.hotel.app.mapper.PrenotazioneMapper;
import com.hotel.app.repository.CameraRepository;
import com.hotel.app.repository.PrenotazioneRepository;
import com.hotel.app.repository.UtenteRepository;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class PrenotazioneService {

    private final PrenotazioneRepository prenotazioneRepository;
    private final UtenteRepository utenteRepository;
    private final CameraRepository cameraRepository;

    public PrenotazioneService(PrenotazioneRepository prenotazioneRepository,
                               UtenteRepository utenteRepository,
                               CameraRepository cameraRepository) {
        this.prenotazioneRepository = prenotazioneRepository;
        this.utenteRepository = utenteRepository;
        this.cameraRepository = cameraRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Prenotazione> findById(Long id) {
        return prenotazioneRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Prenotazione> findByCodice(String codice) {
        return prenotazioneRepository.findByCodicePrenotazione(codice);
    }

    @Transactional(readOnly = true)
    public List<Prenotazione> findByClienteId(Long clienteId) {
        return prenotazioneRepository.findByCliente_Id(clienteId);
    }

    @Transactional(readOnly = true)
    public List<Prenotazione> findAll() {
        return prenotazioneRepository.findAll();
    }

    @Transactional
    public PrenotazioneResponse creaPrenotazione(PrenotazioneRequest request) {
        Prenotazione prenotazione = PrenotazioneMapper.fromRequest(request);

        Utente cliente = utenteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente non trovato"));

        Camera camera = cameraRepository.findById(request.getCameraId())
                .orElseThrow(() -> new IllegalArgumentException("Camera non trovata"));

        prenotazione.setCliente(cliente);
        prenotazione.setCamera(camera);

        prenotazione.setCodicePrenotazione(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        prenotazione.setStato(StatoPrenotazione.CONFERMATA);

        calcolaCosti(prenotazione);

        Prenotazione prenotazioneSalvata = prenotazioneRepository.save(prenotazione);
        return PrenotazioneMapper.toResponse(prenotazioneSalvata);
    }

    private void calcolaCosti(Prenotazione prenotazione) {
        long giorni = ChronoUnit.DAYS.between(prenotazione.getDataCheckIn(), prenotazione.getDataCheckOut());
        if (giorni <= 0) giorni = 1;

        BigDecimal costoCamera = prenotazione.getCamera().getTipoCamera().getPrezzoBase()
                .multiply(BigDecimal.valueOf(giorni));
        prenotazione.setCostoCamera(costoCamera);

        if (prenotazione.getCostoServizi() == null) {
            prenotazione.setCostoServizi(BigDecimal.ZERO);
        }

        prenotazione.setCostoTotale(costoCamera.add(prenotazione.getCostoServizi()));
    }

    public Prenotazione salvaPrenotazione(Prenotazione prenotazione) {
        return prenotazioneRepository.save(prenotazione);
    }

    @Transactional
    public void cancellaPrenotazione(Long id) {
        Prenotazione prenotazione = prenotazioneRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Prenotazione non trovata con id: " + id));

        prenotazione.setStato(StatoPrenotazione.CANCELLATA);
        prenotazioneRepository.save(prenotazione);
    }
}
