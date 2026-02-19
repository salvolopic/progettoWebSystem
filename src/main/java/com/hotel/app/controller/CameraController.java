package com.hotel.app.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.app.dto.response.CameraResponse;
import com.hotel.app.entity.enums.StatoCamera;
import com.hotel.app.mapper.CameraMapper;
import com.hotel.app.service.CameraService;


@RestController
@RequestMapping("/api/camere")
public class CameraController {

    private final CameraService cameraService;

    public CameraController(CameraService cameraService) {
        this.cameraService = cameraService;
    }

    @GetMapping
    public List<CameraResponse> getAllCamere() {
        return cameraService.findAll().stream()
                .map(CameraMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CameraResponse> getCameraById(@PathVariable Long id) {
        return cameraService.findById(id)
                .map(CameraMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/disponibili/{strutturaId}")
    public List<CameraResponse> getCamereDisponibili(
            @PathVariable Long strutturaId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataCheckIn,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataCheckOut) {

        if (dataCheckIn != null && dataCheckOut != null) {
            return cameraService.findCamereDisponibili(strutturaId, dataCheckIn, dataCheckOut).stream()
                    .map(CameraMapper::toResponse)
                    .collect(Collectors.toList());
        }

        return cameraService.findCamereByStrutturaIdAndStato(strutturaId, StatoCamera.DISPONIBILE).stream()
                .map(CameraMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/occupate/{strutturaId}")
    public List<CameraResponse> getCamereOccupate(@PathVariable Long strutturaId) {
        return cameraService.findCamereByStrutturaIdAndStato(strutturaId, StatoCamera.OCCUPATA).stream()
                .map(CameraMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/da-pulire/{strutturaId}")
    public List<CameraResponse> getCamereDaPulire(@PathVariable Long strutturaId) {
        return cameraService.findCamereByStrutturaIdAndStato(strutturaId, StatoCamera.DA_PULIRE).stream()
                .map(CameraMapper::toResponse)
                .collect(Collectors.toList());
    }

    @PostMapping("/{id}/pulizia-completata")
    public ResponseEntity<Void> segnalaPuliziaCompletata(@PathVariable Long id) {
        try {
            cameraService.cambiaStatoCamera(id, StatoCamera.DISPONIBILE);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
