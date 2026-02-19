package com.hotel.app.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotel.app.entity.Ospite;
import com.hotel.app.entity.Prenotazione;

import com.hotel.app.repository.PrenotazioneRepository;

/**
 * Service semplificato per generazione report XML
 */
@Service
public class ReportService {

    private final PrenotazioneRepository prenotazioneRepository;

    public ReportService(PrenotazioneRepository prenotazioneRepository) {
        this.prenotazioneRepository = prenotazioneRepository;
    }

    @Transactional(readOnly = true)
    public String generaReportQuestura(LocalDate data, Long strutturaId) {
        List<Prenotazione> prenotazioni = trovaPrenotazioniPresenti(data, strutturaId);

        StringBuilder xml = new StringBuilder();
        aggiungiHeaderXML(xml, "reportQuestura", "report_questura.xsd");
        aggiungiTag(xml, "dataReport", formattaData(data), 1);
        xml.append("  <prenotazioni>\n");

        prenotazioni.forEach(p -> aggiungiPrenotazioneQuestura(xml, p));

        xml.append("  </prenotazioni>\n");
        aggiungiTag(xml, "totalePrenotazioni", String.valueOf(prenotazioni.size()), 1);
        aggiungiTag(xml, "totaleOspiti", String.valueOf(contaOspiti(prenotazioni)), 1);
        xml.append("</reportQuestura>");

        return xml.toString();
    }

    @Transactional(readOnly = true)
    public String generaReportTassaSoggiorno(LocalDate data, Long strutturaId) {
        List<Prenotazione> prenotazioni = trovaPrenotazioniPresenti(data, strutturaId);

        StringBuilder xml = new StringBuilder();
        aggiungiHeaderXML(xml, "reportTassaSoggiorno", "report_tassa_soggiorno.xsd");
        aggiungiTag(xml, "dataReport", formattaData(data), 1);
        xml.append("  <gruppi>\n");

        int[] totali = {0, 0, 0}; // ospiti, minori, altro
        prenotazioni.forEach(p -> aggiungiGruppoTassa(xml, p, totali));

        xml.append("  </gruppi>\n");
        aggiungiRiepilogoTassa(xml, totali);
        xml.append("</reportTassaSoggiorno>");

        return xml.toString();
    }

    // === METODI HELPER PER XML ===

    private void aggiungiHeaderXML(StringBuilder xml, String rootTag, String xsd) {
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<").append(rootTag).append(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
        xml.append(" xsi:noNamespaceSchemaLocation=\"").append(xsd).append("\">\n");
    }

    private void aggiungiTag(StringBuilder xml, String tag, String valore, int indent) {
        String indentStr = "  ".repeat(indent);
        xml.append(indentStr).append("<").append(tag).append(">")
           .append(escape(valore))
           .append("</").append(tag).append(">\n");
    }

    private void aggiungiPrenotazioneQuestura(StringBuilder xml, Prenotazione p) {
        xml.append("    <prenotazione>\n");
        aggiungiTag(xml, "codicePrenotazione", p.getCodicePrenotazione(), 3);
        aggiungiStruttura(xml, p);
        aggiungiTag(xml, "camera", p.getCamera().getNumero(), 3);
        aggiungiTag(xml, "dataCheckIn", formattaData(p.getDataCheckIn()), 3);
        aggiungiTag(xml, "dataCheckOut", formattaData(p.getDataCheckOut()), 3);

        xml.append("      <ospiti>\n");
        p.getOspiti().forEach(o -> aggiungiOspiteQuestura(xml, o));
        xml.append("      </ospiti>\n");
        xml.append("    </prenotazione>\n");
    }

    private void aggiungiStruttura(StringBuilder xml, Prenotazione p) {
        xml.append("      <struttura>\n");
        aggiungiTag(xml, "nome", p.getCamera().getStruttura().getNome(), 4);
        aggiungiTag(xml, "indirizzo", p.getCamera().getStruttura().getIndirizzo(), 4);
        aggiungiTag(xml, "citta", p.getCamera().getStruttura().getCitta(), 4);
        xml.append("      </struttura>\n");
    }

    private void aggiungiOspiteQuestura(StringBuilder xml, Ospite o) {
        xml.append("        <ospite>\n");
        aggiungiTag(xml, "nome", o.getNome(), 5);
        aggiungiTag(xml, "cognome", o.getCognome(), 5);
        aggiungiTag(xml, "dataNascita", formattaData(o.getDataNascita()), 5);
        aggiungiTag(xml, "luogoNascita", o.getLuogoNascita(), 5);
        aggiungiTag(xml, "cittadinanza", o.getCittadinanza(), 5);
        aggiungiTag(xml, "capogruppo", String.valueOf(o.getIsCapogruppo()), 5);

        if (o.getIsCapogruppo() && o.getTipoDocumento() != null) {
            aggiungiDocumento(xml, o);
        }

        xml.append("        </ospite>\n");
    }

    private void aggiungiDocumento(StringBuilder xml, Ospite o) {
        xml.append("          <documento>\n");
        aggiungiTag(xml, "tipo", String.valueOf(o.getTipoDocumento()), 6);
        aggiungiTag(xml, "numero", o.getNumeroDocumento(), 6);

        if (o.getEnteRilascioDocumento() != null) {
            aggiungiTag(xml, "enteRilascio", o.getEnteRilascioDocumento(), 6);
        }
        if (o.getDataRilascioDocumento() != null) {
            aggiungiTag(xml, "dataRilascio", formattaData(o.getDataRilascioDocumento()), 6);
        }
        if (o.getDataScadenzaDocumento() != null) {
            aggiungiTag(xml, "dataScadenza", formattaData(o.getDataScadenzaDocumento()), 6);
        }

        xml.append("          </documento>\n");
    }

    private void aggiungiGruppoTassa(StringBuilder xml, Prenotazione p, int[] totali) {
        Ospite capogruppo = p.getOspiti().stream()
            .filter(Ospite::getIsCapogruppo)
            .findFirst()
            .orElse(null);

        if (capogruppo == null) return;

        int ospiti = p.getOspiti().size();
        int esenti = (int) p.getOspiti().stream().filter(Ospite::getEsenteTassaSoggiorno).count();
        int minori = (int) p.getOspiti().stream()
            .filter(o -> o.getEsenteTassaSoggiorno() && isMinore(o))
            .count();

        totali[0] += ospiti;
        totali[1] += minori;
        totali[2] += (esenti - minori);

        xml.append("    <gruppo>\n");
        xml.append("      <capogruppo>\n");
        aggiungiTag(xml, "nome", capogruppo.getNome(), 4);
        aggiungiTag(xml, "cognome", capogruppo.getCognome(), 4);
        xml.append("      </capogruppo>\n");

        aggiungiTag(xml, "codicePrenotazione", p.getCodicePrenotazione(), 3);
        aggiungiTag(xml, "numeroOspiti", String.valueOf(ospiti), 3);
        aggiungiTag(xml, "numeroEsenti", String.valueOf(esenti), 3);

        if (esenti > 0) {
            aggiungiEsenzioni(xml, p);
        }

        xml.append("    </gruppo>\n");
    }

    private void aggiungiEsenzioni(StringBuilder xml, Prenotazione p) {
        xml.append("      <esenzioni>\n");
        p.getOspiti().stream()
            .filter(Ospite::getEsenteTassaSoggiorno)
            .forEach(o -> {
                xml.append("        <esenzione>\n");
                aggiungiTag(xml, "ospite", o.getNome() + " " + o.getCognome(), 5);
                String tipo = o.getTipoEsenzione() != null ?
                    String.valueOf(o.getTipoEsenzione()) : "NON_SPECIFICATO";
                aggiungiTag(xml, "tipo", tipo, 5);
                xml.append("        </esenzione>\n");
            });
        xml.append("      </esenzioni>\n");
    }

    private void aggiungiRiepilogoTassa(StringBuilder xml, int[] totali) {
        xml.append("  <riepilogo>\n");
        aggiungiTag(xml, "totaleOspiti", String.valueOf(totali[0]), 2);
        aggiungiTag(xml, "totaleEsentiMinori", String.valueOf(totali[1]), 2);
        aggiungiTag(xml, "totaleEsentiAltro", String.valueOf(totali[2]), 2);
        aggiungiTag(xml, "totaleImponibili", String.valueOf(totali[0] - totali[1] - totali[2]), 2);
        xml.append("  </riepilogo>\n");
    }

    // === QUERY E UTILITY ===

    private List<Prenotazione> trovaPrenotazioniPresenti(LocalDate data, Long strutturaId) {
        return prenotazioneRepository.findPrenotazioniPresenti(data, strutturaId);
    }

    private int contaOspiti(List<Prenotazione> prenotazioni) {
        return prenotazioni.stream().mapToInt(p -> p.getOspiti().size()).sum();
    }

    private boolean isMinore(Ospite o) {
        return "MINORE_12_ANNI".equals(String.valueOf(o.getTipoEsenzione()));
    }

    private String formattaData(LocalDate data) {
        return data.format(DateTimeFormatter.ISO_DATE);
    }

    private String escape(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&apos;");
    }
}
