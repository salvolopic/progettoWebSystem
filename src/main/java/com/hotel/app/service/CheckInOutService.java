package com.hotel.app.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotel.app.dto.OspiteDTO;
import com.hotel.app.entity.Ospite;
import com.hotel.app.entity.Prenotazione;
import com.hotel.app.entity.enums.StatoCamera;
import com.hotel.app.entity.enums.StatoPrenotazione;
import com.hotel.app.repository.OspiteRepository;

@Service
public class CheckInOutService {

    private final PrenotazioneService prenotazioneService;
    private final CameraService cameraService;
    private final OspiteRepository ospiteRepository;

    public CheckInOutService(PrenotazioneService prenotazioneService, CameraService cameraService, OspiteRepository ospiteRepository) {
        this.prenotazioneService = prenotazioneService;
        this.cameraService = cameraService;
        this.ospiteRepository = ospiteRepository;
    }

    public Prenotazione trovaPrenotazione(Long prenotazioneId) {
        return prenotazioneService.findById(prenotazioneId).orElse(null);
    }

    @Transactional
    public Prenotazione effettuaCheckIn(Long prenotazioneId, List<OspiteDTO> ospiti) {
        Prenotazione prenotazione = prenotazioneService.findById(prenotazioneId)
                .orElseThrow(() -> new IllegalArgumentException("Prenotazione non trovata"));

        if (prenotazione.getStato() != StatoPrenotazione.CONFERMATA) {
            throw new IllegalStateException("Check-in non consentito: la prenotazione deve essere confermata");
        }

        // Validazione data: check-in possibile solo dalla data prevista
        LocalDate oggi = LocalDate.now();
        if (oggi.isBefore(prenotazione.getDataCheckIn())) {
            throw new IllegalStateException("Check-in non consentito: è possibile effettuarlo solo a partire dal " + prenotazione.getDataCheckIn());
        }

        // Validazione ospiti: deve esserci almeno un capogruppo
        long capigruppo = ospiti.stream().filter(OspiteDTO::getIsCapogruppo).count();
        if (capigruppo == 0) {
            throw new IllegalArgumentException("Deve essere presente almeno un capogruppo");
        }
        if (capigruppo > 1) {
            throw new IllegalArgumentException("Può esserci un solo capogruppo");
        }

        // Crea gli ospiti e associali alla prenotazione
        for (OspiteDTO dto : ospiti) {
            Ospite ospite = new Ospite();
            ospite.setNome(dto.getNome());
            ospite.setCognome(dto.getCognome());
            ospite.setDataNascita(dto.getDataNascita());
            ospite.setLuogoNascita(dto.getLuogoNascita());
            ospite.setProvinciaNascita(dto.getProvinciaNascita());
            ospite.setCittadinanza(dto.getCittadinanza());
            ospite.setTipoDocumento(dto.getTipoDocumento());
            ospite.setNumeroDocumento(dto.getNumeroDocumento());
            ospite.setEnteRilascioDocumento(dto.getEnteRilascioDocumento());
            ospite.setDataRilascioDocumento(dto.getDataRilascioDocumento());
            ospite.setDataScadenzaDocumento(dto.getDataScadenzaDocumento());
            ospite.setIsCapogruppo(dto.getIsCapogruppo());
            ospite.setEsenteTassaSoggiorno(dto.getEsenteTassaSoggiorno());
            ospite.setTipoEsenzione(dto.getTipoEsenzione());
            ospite.setPrenotazione(prenotazione);

            ospiteRepository.save(ospite);
        }

        prenotazione.setStato(StatoPrenotazione.CHECK_IN_EFFETTUATO);
        prenotazione.setDataCheckInEffettivo(LocalDateTime.now());

        cameraService.cambiaStatoCamera(prenotazione.getCamera().getId(), StatoCamera.OCCUPATA);

        return prenotazioneService.salvaPrenotazione(prenotazione);
    }

    @Transactional
    public Prenotazione effettuaCheckOut(Long prenotazioneId) {
        Prenotazione prenotazione = prenotazioneService.findById(prenotazioneId)
                .orElseThrow(() -> new IllegalArgumentException("Prenotazione non trovata"));

        if (prenotazione.getStato() != StatoPrenotazione.CHECK_IN_EFFETTUATO) {
            throw new IllegalStateException("Check-out non consentito: deve essere effettuato prima il check-in");
        }

        if(prenotazione.getDataCheckOutEffettivo() != null) throw new IllegalStateException("Check-out già effettuato");


        prenotazione.setStato(StatoPrenotazione.CHECK_OUT_EFFETTUATO);
        prenotazione.setDataCheckOutEffettivo(LocalDateTime.now());

        cameraService.cambiaStatoCamera(prenotazione.getCamera().getId(), StatoCamera.DA_PULIRE);

        return prenotazioneService.salvaPrenotazione(prenotazione);
    }

    @Scheduled(cron = "0 0 12 * * *")
    @Transactional
    public void effettuaCheckOutAutomaticoGiornaliero() {
        LocalDate oggi = LocalDate.now();

        List<Prenotazione> prenotazioniAttive = prenotazioneService.findAll().stream()
                .filter(p -> p.getStato() == StatoPrenotazione.CHECK_IN_EFFETTUATO)
                .filter(p -> p.getDataCheckOutEffettivo() == null)
                .filter(p -> oggi.isAfter(p.getDataCheckOut()))
                .toList();

        int checkoutEffettuati = 0;

        for (Prenotazione prenotazione : prenotazioniAttive) {
            prenotazione.setStato(StatoPrenotazione.CHECK_OUT_EFFETTUATO);
            prenotazione.setDataCheckOutEffettivo(LocalDateTime.now());
            cameraService.cambiaStatoCamera(prenotazione.getCamera().getId(), StatoCamera.DA_PULIRE);
            prenotazioneService.salvaPrenotazione(prenotazione);
            checkoutEffettuati++;
        }

        if (checkoutEffettuati > 0) {
            System.out.println("Check-out automatico: " + checkoutEffettuati + " prenotazioni chiuse");
        }
    }
}
