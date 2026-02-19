package com.hotel.app.mapper;

import com.hotel.app.dto.response.CameraResponse;
import com.hotel.app.entity.Camera;

public class CameraMapper {
    public static CameraResponse toResponse(Camera camera){
        return CameraResponse.builder()
                .id(camera.getId())
                .numero(camera.getNumero())
                .piano(camera.getPiano())
                .strutturaId(camera.getStruttura().getId())
                .strutturaNome(camera.getStruttura().getNome())
                .tipoCameraId(camera.getTipoCamera().getId())
                .tipoCameraNome(camera.getTipoCamera().getNome())
                .tipoCameraDescrizione(camera.getTipoCamera().getDescrizione())
                .capacitaMinima(camera.getTipoCamera().getCapacitaMinima())
                .capacitaMassima(camera.getTipoCamera().getCapacitaMassima())
                .prezzoBase(camera.getTipoCamera().getPrezzoBase())
                .haVista(camera.getTipoCamera().getHaVista())
                .haBalcone(camera.getTipoCamera().getHaBalcone())
                .haAriaCondizionata(camera.getTipoCamera().getHaAriaCondizionata())
                .stato(camera.getStato())
                .note(camera.getNote())
                .attiva(camera.getAttiva())
                .build();
    }
    
}
