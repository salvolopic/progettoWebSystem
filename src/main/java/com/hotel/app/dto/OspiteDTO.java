package com.hotel.app.dto;

import com.hotel.app.entity.enums.TipoDocumento;
import com.hotel.app.entity.enums.TipoEsenzione;

import java.time.LocalDate;

/**
 * - Report questura (dati anagrafici + documento)
 * - Report tassa soggiorno (dati per calcolo esenzioni)
 */
public class OspiteDTO {

    // Dati anagrafici base
    private String nome;
    private String cognome;
    private LocalDate dataNascita;
    private String luogoNascita;
    private String provinciaNascita;
    private String cittadinanza;

    // Dati documento (obbligatori per capogruppo)
    private TipoDocumento tipoDocumento;
    private String numeroDocumento;
    private String enteRilascioDocumento;
    private LocalDate dataRilascioDocumento;
    private LocalDate dataScadenzaDocumento;

    private Boolean isCapogruppo;

    private Boolean esenteTassaSoggiorno;
    private TipoEsenzione tipoEsenzione;


    public OspiteDTO() {
        this.isCapogruppo = false;
        this.esenteTassaSoggiorno = false;
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public LocalDate getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(LocalDate dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getLuogoNascita() {
        return luogoNascita;
    }

    public void setLuogoNascita(String luogoNascita) {
        this.luogoNascita = luogoNascita;
    }

    public String getProvinciaNascita() {
        return provinciaNascita;
    }

    public void setProvinciaNascita(String provinciaNascita) {
        this.provinciaNascita = provinciaNascita;
    }

    public String getCittadinanza() {
        return cittadinanza;
    }

    public void setCittadinanza(String cittadinanza) {
        this.cittadinanza = cittadinanza;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getEnteRilascioDocumento() {
        return enteRilascioDocumento;
    }

    public void setEnteRilascioDocumento(String enteRilascioDocumento) {
        this.enteRilascioDocumento = enteRilascioDocumento;
    }

    public LocalDate getDataRilascioDocumento() {
        return dataRilascioDocumento;
    }

    public void setDataRilascioDocumento(LocalDate dataRilascioDocumento) {
        this.dataRilascioDocumento = dataRilascioDocumento;
    }

    public LocalDate getDataScadenzaDocumento() {
        return dataScadenzaDocumento;
    }

    public void setDataScadenzaDocumento(LocalDate dataScadenzaDocumento) {
        this.dataScadenzaDocumento = dataScadenzaDocumento;
    }

    public Boolean getIsCapogruppo() {
        return isCapogruppo;
    }

    public void setIsCapogruppo(Boolean isCapogruppo) {
        this.isCapogruppo = isCapogruppo;
    }

    public Boolean getEsenteTassaSoggiorno() {
        return esenteTassaSoggiorno;
    }

    public void setEsenteTassaSoggiorno(Boolean esenteTassaSoggiorno) {
        this.esenteTassaSoggiorno = esenteTassaSoggiorno;
    }

    public TipoEsenzione getTipoEsenzione() {
        return tipoEsenzione;
    }

    public void setTipoEsenzione(TipoEsenzione tipoEsenzione) {
        this.tipoEsenzione = tipoEsenzione;
    }
}
