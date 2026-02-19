document.addEventListener('DOMContentLoaded', () => {
    caricaMiePrenotazioni();
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

function caricaMiePrenotazioni() {
    fetch('http://localhost:8080/api/prenotazioni')
    .then(response => {
        if (response.ok) return response.json();
        else throw new Error('Errore durante il recupero delle prenotazioni');
    })
    .then(prenotazioni => {
        // Filtra solo le prenotazioni del cliente loggato
        const miePrenotazioni = prenotazioni.filter(p => p.clienteId === utenteLoggato.id);

        const lista = document.getElementById('lista-mie-prenotazioni');
        lista.innerHTML = '';

        miePrenotazioni.forEach(prenotazione => {
            const li = document.createElement('li');

            // Genera i bottoni in base allo stato
            let bottoni = '';

            if (prenotazione.stato === 'CONFERMATA') {
                bottoni += `<button class="btn" onclick="effettuaCheckIn(${prenotazione.id})">Check-In</button> `;
                bottoni += `<button class="btn btn-danger" onclick="eliminaPrenotazione(${prenotazione.id})">Elimina</button> `;
            } else if (prenotazione.stato === 'CHECK_IN_EFFETTUATO') {
                bottoni += `<button class="btn" onclick="effettuaCheckOut(${prenotazione.id})">Check-Out</button> `;
                bottoni += `<button class="btn" onclick="vaiANote(${prenotazione.id})">Note</button>`;
            }

            li.innerHTML = `
                <strong>Codice: ${prenotazione.codicePrenotazione}</strong><br>
                Struttura: ${prenotazione.strutturaNome} |
                Camera: ${prenotazione.cameraNumero} |
                Check-in: ${prenotazione.dataCheckIn} |
                Check-out: ${prenotazione.dataCheckOut}<br>
                Stato: ${prenotazione.stato} |
                Totale: €${prenotazione.costoTotale}<br>
                ${bottoni}
            `;
            lista.appendChild(li);
        });
    })
    .catch(err => alert('Errore: ' + err.message));
}

function eliminaPrenotazione(prenotazioneId){
    if (!confirm('Vuoi eliminare questa prenotazione?')){
        return;
    }
    fetch(`http://localhost:8080/api/prenotazioni/${prenotazioneId}`, {
        method: 'DELETE'
    })
    .then(response => {
        if (response.ok) {
            alert('Prenotazione eliminata con successo!');
            caricaMiePrenotazioni(); // Ricarica la lista
        } else {
            throw new Error('Errore durante l\'eliminazione');
        }
    })
    .catch(err => {
        alert('Errore: ' + err.message);
    })
}

function vaiANote(prenotazioneId) {
    window.location.href = `/pages/cliente/note.html?prenotazioneId=${prenotazioneId}`;
}

function effettuaCheckIn(prenotazioneId) {
    // Reindirizza al form di check-in con dati anagrafici
    window.location.href = `/pages/cliente/checkin-form.html?id=${prenotazioneId}`;
}

function effettuaCheckOut(prenotazioneId) {
    if (!confirm('Vuoi effettuare il check-out per questa prenotazione?')) {
        return;
    }

    fetch(`http://localhost:8080/api/checkinout/checkout/${prenotazioneId}`, {
        method: 'POST'
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        } else if (response.status === 400) {
            return response.text().then(text => {
                throw new Error(text);
            });
        } else {
            throw new Error('Errore durante il check-out');
        }
    })
    .then(() => {
        alert('Check-out effettuato con successo!');
        caricaMiePrenotazioni(); // Ricarica la lista
    })
    .catch(err => {
        console.error('Errore:', err);
        alert('Errore: ' + err.message);
    });
}

