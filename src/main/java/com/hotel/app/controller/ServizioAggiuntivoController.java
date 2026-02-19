package com.hotel.app.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hotel.app.dto.response.ServizioAggiuntivoResponse;
import com.hotel.app.mapper.ServizioAggiuntivoMapper;
import com.hotel.app.service.ServizioAggiuntivoService;

@RestController
@RequestMapping("/api/servizi")
public class ServizioAggiuntivoController {

    private final ServizioAggiuntivoService servizioAggiuntivoService;

    public ServizioAggiuntivoController(ServizioAggiuntivoService servizioAggiuntivoService) {
        this.servizioAggiuntivoService = servizioAggiuntivoService;
    }

    @GetMapping
    public List<ServizioAggiuntivoResponse> getAllServizi() {
        return servizioAggiuntivoService.findAll().stream()
                .map(ServizioAggiuntivoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/struttura/{strutturaId}")
    public ResponseEntity<List<ServizioAggiuntivoResponse>> getServiziByStruttura(@PathVariable Long strutturaId) {
        List<ServizioAggiuntivoResponse> servizi = servizioAggiuntivoService.findByStrutturaId(strutturaId).stream()
                .map(ServizioAggiuntivoMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(servizi);
    }
}
