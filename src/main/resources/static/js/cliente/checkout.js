document.addEventListener('DOMContentLoaded', () => {
    const params = new URLSearchParams(window.location.search);
    const prenotazioneId = params.get('prenotazioneId');

    if (!prenotazioneId) {
        alert('Prenotazione non specificata');
        window.location.href = '/pages/cliente/mie-prenotazioni.html';
        return;
    }

    caricaPrenotazione(prenotazioneId);
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

// Carica i dettagli della prenotazione e li mostra
function caricaPrenotazione(prenotazioneId) {
    fetch(`http://localhost:8080/api/prenotazioni/${prenotazioneId}`)
    .then(response => {
        if (response.ok) return response.json();
        else throw new Error('Prenotazione non trovata');
    })
    .then(prenotazione => {
        document.getElementById('codice').textContent = prenotazione.codicePrenotazione;
        document.getElementById('camera').textContent = prenotazione.cameraNumero;
        document.getElementById('checkin-data').textContent = prenotazione.dataCheckIn;
        document.getElementById('checkout-data').textContent = prenotazione.dataCheckOut;
        document.getElementById('stato').textContent = prenotazione.stato;

        document.getElementById('btn-checkout').addEventListener('click', () => {
            effettuaCheckOut(prenotazioneId);
        });
    })
    .catch(err => alert('Errore: ' + err.message));
}

// Chiama il POST per effettuare il check-out
function effettuaCheckOut(prenotazioneId) {
    fetch(`http://localhost:8080/api/checkinout/checkout/${prenotazioneId}`, {
        method: 'POST'
    })
    .then(response => {
        if (response.ok) {
            alert('Check-out effettuato con successo!');
            window.location.href = '/pages/cliente/mie-prenotazioni.html';
        } else if (response.status === 400) {
            return response.text().then(msg => { throw new Error(msg); });
        } else {
            throw new Error('Prenotazione non trovata');
        }
    })
    .catch(err => alert('Errore: ' + err.message));
}
