package com.hotel.app.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.app.service.ReportService;


@RestController
@RequestMapping("/api/report")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * Genera il report giornaliero per la questura in formato XML
     * Contiene i dati di tutti gli ospiti presenti in una data specifica
     *
     * @param data La data per cui generare il report (formato: YYYY-MM-DD)
     * @param strutturaId ID della struttura (opzionale, se null genera per tutte)
     * @return XML con i dati degli ospiti
     */
    @GetMapping(value = "/questura", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> generaReportQuestura(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam(required = false) Long strutturaId) {

        try {
            String xmlContent = reportService.generaReportQuestura(data, strutturaId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_XML);
            headers.setContentDispositionFormData("attachment",
                "report_questura_" + data + ".xml");

            return ResponseEntity.ok()
                .headers(headers)
                .body(xmlContent);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body("<errore>Errore nella generazione del report: " + e.getMessage() + "</errore>");
        }
    }

    /**
     * Genera il report tassa di soggiorno in formato XML
     * Contiene il riepilogo delle presenze con eventuali esenzioni
     *
     * @param data La data per cui generare il report (formato: YYYY-MM-DD)
     * @param strutturaId ID della struttura (opzionale, se null genera per tutte)
     * @return XML con il riepilogo tasse
     */
    @GetMapping(value = "/tassa-soggiorno", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> generaReportTassaSoggiorno(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam(required = false) Long strutturaId) {

        try {
            String xmlContent = reportService.generaReportTassaSoggiorno(data, strutturaId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_XML);
            headers.setContentDispositionFormData("attachment",
                "report_tassa_soggiorno_" + data + ".xml");

            return ResponseEntity.ok()
                .headers(headers)
                .body(xmlContent);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body("<errore>Errore nella generazione del report: " + e.getMessage() + "</errore>");
        }
    }
}
