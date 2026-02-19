let prenotazioneId = null;
let numeroOspiti = 0;

document.addEventListener('DOMContentLoaded', () => {
    verificaAutenticazione();

    // Ottieni ID prenotazione da URL parameter
    const urlParams = new URLSearchParams(window.location.search);
    prenotazioneId = urlParams.get('id');

    if (!prenotazioneId) {
        alert('Prenotazione non specificata');
        window.location.href = '/pages/cliente/mie-prenotazioni.html';
        return;
    }

    caricaDettagliPrenotazione();

    // Setup form submit
    document.getElementById('form-checkin').addEventListener('submit', (e) => {
        e.preventDefault();
        inviaCheckIn();
    });
});

function verificaAutenticazione() {
    const utenteJSON = localStorage.getItem('utente');
    if (!utenteJSON) {
        alert('Devi effettuare il login');
        window.location.href = '/pages/login.html';
        return;
    }

    const utente = JSON.parse(utenteJSON);
    if (utente.ruolo !== 'CLIENTE') {
        alert('Accesso non autorizzato');
        window.location.href = '/pages/login.html';
        return;
    }
}

function caricaDettagliPrenotazione() {
    const utenteJSON = localStorage.getItem('utente');
    const utente = JSON.parse(utenteJSON);

    fetch(`http://localhost:8080/api/prenotazioni/utente/${utente.id}`)
        .then(response => response.json())
        .then(prenotazioni => {
            const prenotazione = prenotazioni.find(p => p.id == prenotazioneId);

            if (!prenotazione) {
                alert('Prenotazione non trovata');
                window.location.href = '/pages/cliente/mie-prenotazioni.html';
                return;
            }

            numeroOspiti = prenotazione.numeroOspiti;

            // Mostra dettagli prenotazione
            document.getElementById('dettagli-prenotazione').innerHTML = `
                <p><strong>Codice:</strong> ${prenotazione.codicePrenotazione}</p>
                <p><strong>Struttura:</strong> ${prenotazione.strutturaNome}</p>
                <p><strong>Camera:</strong> ${prenotazione.cameraNumero} - ${prenotazione.tipoCameraNome}</p>
                <p><strong>Check-in:</strong> ${prenotazione.dataCheckIn}</p>
                <p><strong>Check-out:</strong> ${prenotazione.dataCheckOut}</p>
                <p><strong>Numero ospiti:</strong> ${numeroOspiti}</p>
            `;

            // Genera form per tutti gli ospiti
            generaFormOspiti(numeroOspiti);
        })
        .catch(err => {
            alert('Errore nel caricamento della prenotazione: ' + err.message);
            window.history.back();
        });
}

function generaFormOspiti(numero) {
    const container = document.getElementById('ospiti-container');
    container.innerHTML = '';

    for (let i = 0; i < numero; i++) {
        const isCapogruppo = (i === 0);
        const ospiteCard = creaFormOspite(i + 1, isCapogruppo);
        container.appendChild(ospiteCard);
    }
}

function creaFormOspite(numeroOspite, isCapogruppo) {
    const card = document.createElement('div');
    card.className = 'ospite-card';

    const titoloRuolo = isCapogruppo ? 'Capogruppo' : `Ospite ${numeroOspite}`;

    card.innerHTML = `
        <h3>${titoloRuolo}</h3>

        <div class="form-row">
            <div class="form-group">
                <label for="nome-${numeroOspite}">Nome *</label>
                <input type="text" id="nome-${numeroOspite}" name="nome" required>
            </div>
            <div class="form-group">
                <label for="cognome-${numeroOspite}">Cognome *</label>
                <input type="text" id="cognome-${numeroOspite}" name="cognome" required>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="data-nascita-${numeroOspite}">Data di Nascita *</label>
                <input type="date" id="data-nascita-${numeroOspite}" name="dataNascita" required>
            </div>
            <div class="form-group">
                <label for="cittadinanza-${numeroOspite}">Cittadinanza *</label>
                <input type="text" id="cittadinanza-${numeroOspite}" name="cittadinanza" required placeholder="es. Italiana">
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="luogo-nascita-${numeroOspite}">Luogo di Nascita *</label>
                <input type="text" id="luogo-nascita-${numeroOspite}" name="luogoNascita" required>
            </div>
            <div class="form-group">
                <label for="provincia-nascita-${numeroOspite}">Provincia (sigla)</label>
                <input type="text" id="provincia-nascita-${numeroOspite}" name="provinciaNascita" maxlength="2" placeholder="RM">
            </div>
        </div>

        ${isCapogruppo ? `
            <h4 style="margin-top: 20px; color: #2c5f2d;">Dati Documento (Obbligatori per Capogruppo)</h4>

            <div class="form-row">
                <div class="form-group">
                    <label for="tipo-documento-${numeroOspite}">Tipo Documento *</label>
                    <select id="tipo-documento-${numeroOspite}" name="tipoDocumento" required>
                        <option value="">Seleziona...</option>
                        <option value="CARTA_IDENTITA">Carta d'Identità</option>
                        <option value="PASSAPORTO">Passaporto</option>
                        <option value="PATENTE">Patente</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="numero-documento-${numeroOspite}">Numero Documento *</label>
                    <input type="text" id="numero-documento-${numeroOspite}" name="numeroDocumento" required>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="ente-rilascio-${numeroOspite}">Ente Rilascio *</label>
                    <input type="text" id="ente-rilascio-${numeroOspite}" name="enteRilascioDocumento" required placeholder="es. Comune di Roma">
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="data-rilascio-${numeroOspite}">Data Rilascio *</label>
                    <input type="date" id="data-rilascio-${numeroOspite}" name="dataRilascioDocumento" required>
                </div>
                <div class="form-group">
                    <label for="data-scadenza-${numeroOspite}">Data Scadenza *</label>
                    <input type="date" id="data-scadenza-${numeroOspite}" name="dataScadenzaDocumento" required>
                </div>
            </div>
        ` : ''}

        <input type="hidden" id="is-capogruppo-${numeroOspite}" value="${isCapogruppo}">
    `;

    return card;
}

function inviaCheckIn() {
    // Raccogli dati di tutti gli ospiti
    const ospiti = [];

    for (let i = 1; i <= numeroOspiti; i++) {
        const isCapogruppo = (i === 1);

        const ospite = {
            nome: document.getElementById(`nome-${i}`).value,
            cognome: document.getElementById(`cognome-${i}`).value,
            dataNascita: document.getElementById(`data-nascita-${i}`).value,
            cittadinanza: document.getElementById(`cittadinanza-${i}`).value,
            luogoNascita: document.getElementById(`luogo-nascita-${i}`).value,
            provinciaNascita: document.getElementById(`provincia-nascita-${i}`).value || null,
            isCapogruppo: isCapogruppo,
            esenteTassaSoggiorno: false,
            tipoEsenzione: null
        };

        // Aggiungi dati documento se capogruppo
        if (isCapogruppo) {
            ospite.tipoDocumento = document.getElementById(`tipo-documento-${i}`).value;
            ospite.numeroDocumento = document.getElementById(`numero-documento-${i}`).value;
            ospite.enteRilascioDocumento = document.getElementById(`ente-rilascio-${i}`).value;
            ospite.dataRilascioDocumento = document.getElementById(`data-rilascio-${i}`).value;
            ospite.dataScadenzaDocumento = document.getElementById(`data-scadenza-${i}`).value;
        } else {
            ospite.tipoDocumento = null;
            ospite.numeroDocumento = null;
            ospite.enteRilascioDocumento = null;
            ospite.dataRilascioDocumento = null;
            ospite.dataScadenzaDocumento = null;
        }

        // Calcola esenzione tassa (minori di 12 anni)
        const dataNascita = new Date(ospite.dataNascita);
        const oggi = new Date();
        let eta = oggi.getFullYear() - dataNascita.getFullYear();
        const mese = oggi.getMonth() - dataNascita.getMonth();
        if (mese < 0 || (mese === 0 && oggi.getDate() < dataNascita.getDate())) {
            eta--;
        }

        if (eta < 12) {
            ospite.esenteTassaSoggiorno = true;
            ospite.tipoEsenzione = 'MINORE_12_ANNI';
        }

        ospiti.push(ospite);
    }

    // Invia richiesta check-in con dati ospiti
    fetch(`http://localhost:8080/api/checkinout/checkin/${prenotazioneId}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(ospiti)
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        } else {
            return response.text().then(text => {
                throw new Error(text || 'Errore durante il check-in');
            });
        }
    })
    .then(() => {
        alert('Check-in effettuato con successo! Dati registrati correttamente.');
        window.location.href = '/pages/cliente/mie-prenotazioni.html';
    })
    .catch(err => {
        alert('Errore: ' + err.message);
    });
}
