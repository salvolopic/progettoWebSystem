function getUtenteLoggato() {
    const utenteJSON = localStorage.getItem('utente');
    if (utenteJSON) return JSON.parse(utenteJSON);
    return null;
}

const utenteLoggato = getUtenteLoggato();
if (!utenteLoggato || utenteLoggato.ruolo !== 'CLIENTE') {
    window.location.href = '/pages/login.html';
}

let prenotazioneId;

document.addEventListener('DOMContentLoaded', () => {
    const params = new URLSearchParams(window.location.search);
    prenotazioneId = params.get('prenotazioneId');

    if (!prenotazioneId) {
        alert('Prenotazione non specificata');
        window.location.href = '/pages/cliente/mie-prenotazioni.html';
        return;
    }

    caricaNoteEsistenti();

    document.getElementById('form-nota').addEventListener('submit', (e) => {
        e.preventDefault();
        inviaNuovaNota();
    });
});

function caricaNoteEsistenti() {
    fetch(`http://localhost:8080/api/notacliente/prenotazione/${prenotazioneId}`)
    .then(response => {
        if (response.ok) return response.json();
        else throw new Error('Errore nel caricamento delle note');
    })
    .then(note => {
        const listaNote = document.getElementById('lista-note');
        listaNote.innerHTML = '';
        note.forEach(nota => {
            const li = document.createElement('li');
            li.innerHTML = `
                Nota: ${nota.testo} -
                Data: ${new Date(nota.dataCreazione).toLocaleString()}
            `;
            listaNote.appendChild(li);
        });
    })
    .catch(err => alert('Errore: ' + err.message));
}

function inviaNuovaNota() {
    const testo = document.getElementById('testo-nota').value;

    fetch(`http://localhost:8080/api/notacliente/${prenotazioneId}`, {
        method: 'POST',
        headers: { 'Content-Type': 'text/plain' },
        body: testo
    })
    .then(response => {
        if (response.ok) {
            document.getElementById('testo-nota').value = '';
            caricaNoteEsistenti();
        } else {
            throw new Error('Errore nell\'invio della nota');
        }
    })
    .catch(err => alert('Errore: ' + err.message));
}
