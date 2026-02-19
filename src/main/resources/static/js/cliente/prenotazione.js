document.addEventListener('DOMContentLoaded', () => {
    const params = new URLSearchParams(window.location.search);
    const cameraId = params.get('cameraId');

    if (!cameraId) {
        alert('Camera non specificata');
        window.location.href = '/pages/cliente/ricerca.html';
        return;
    }

    caricaCamera(cameraId);
});

function getUtenteLoggato() {
    const utenteJSON = localStorage.getItem('utente');
    if (utenteJSON) return JSON.parse(utenteJSON);
    return null;
}

const utenteLoggato = getUtenteLoggato();
if (!utenteLoggato || utenteLoggato.ruolo !== 'CLIENTE') {
    window.location.href = '/pages/login.html';
}

// Recupera i dettagli della camera e li mostra nella pagina
function caricaCamera(cameraId) {
    fetch(`http://localhost:8080/api/camere/${cameraId}`)
    .then(response => {
        if (response.ok) return response.json();
        else throw new Error('Camera non trovata');
    })
    .then(camera => {
        document.getElementById('camera-numero').textContent = camera.numero;
        document.getElementById('camera-tipo').textContent = camera.tipoCameraNome;
        document.getElementById('camera-prezzo').textContent = `€${camera.prezzoBase.toFixed(2)} per notte`;

        // Attiva il form solo dopo aver caricato la camera
        const form = document.getElementById('prenotazione-form');
        form.addEventListener('submit', (e) => {
            e.preventDefault();
            creaPrenotazione(cameraId);
        });
    })
    .catch(err => alert('Errore: ' + err.message));
}

// Costruisce il PrenotazioneRequest e fa il POST
function creaPrenotazione(cameraId) {
    const dataCheckIn = document.getElementById('data-checkin').value;
    const dataCheckOut = document.getElementById('data-checkout').value;
    const numeroOspiti = document.getElementById('numero-ospiti').value;

    const prenotazione = {
        clienteId: utenteLoggato.id,
        cameraId: parseInt(cameraId),
        dataCheckIn: dataCheckIn,
        dataCheckOut: dataCheckOut,
        numeroOspiti: parseInt(numeroOspiti)
    };

    fetch('http://localhost:8080/api/prenotazioni', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(prenotazione)
    })
    .then(response => {
        if (response.ok) return response.json();
        else throw new Error('Errore durante la creazione della prenotazione');
    })
    .then(risultato => {
        alert('Prenotazione creata! Codice: ' + risultato.codicePrenotazione);
        window.location.href = '/pages/cliente/mie-prenotazioni.html';
    })
    .catch(err => alert('Errore: ' + err.message));
}
