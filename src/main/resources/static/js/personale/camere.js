let strutturaId = null;
let tabCorrente = 'occupate';

document.addEventListener('DOMContentLoaded', () => {
    verificaAutenticazione();
    caricaDati();
});

function verificaAutenticazione() {
    const utenteJSON = localStorage.getItem('utente');
    if (!utenteJSON) {
        alert('Devi effettuare il login');
        window.location.href = '/pages/login.html';
        return;
    }

    const utente = JSON.parse(utenteJSON);
    if (utente.ruolo !== 'PERSONALE') {
        alert('Accesso non autorizzato - solo per personale');
        window.location.href = '/pages/login.html';
        return;
    }

    // Ottieni struttura assegnata
    strutturaId = utente.strutturaAssegnataId;
    if (!strutturaId) {
        alert('Nessuna struttura assegnata al tuo account');
        return;
    }
}

function caricaDati() {
    // Carica info struttura
    fetch(`http://localhost:8080/api/strutture/${strutturaId}`)
        .then(response => response.json())
        .then(struttura => {
            document.getElementById('nome-struttura').textContent = struttura.nome;
        })
        .catch(err => console.error('Errore caricamento struttura:', err));

    // Carica camere per il tab corrente
    caricaCamere();
}

function mostraTab(tab, e) {
    // Aggiorna bottoni
    document.querySelectorAll('.tab-button').forEach(btn => {
        btn.classList.remove('active');
    });
    e.target.classList.add('active');

    // Nascondi tutti i tab
    document.querySelectorAll('.tab-content').forEach(content => {
        content.classList.remove('active');
    });

    // Mostra tab selezionato
    document.getElementById(`tab-${tab}`).classList.add('active');

    tabCorrente = tab;
    caricaCamere();
}

function caricaCamere() {
    let url = '';

    switch(tabCorrente) {
        case 'occupate':
            url = `http://localhost:8080/api/camere/occupate/${strutturaId}`;
            break;
        case 'libere':
            url = `http://localhost:8080/api/camere/disponibili/${strutturaId}`;
            break;
        case 'da-pulire':
            url = `http://localhost:8080/api/camere/da-pulire/${strutturaId}`;
            break;
    }

    fetch(url)
        .then(response => response.json())
        .then(camere => {
            const lista = document.getElementById(`lista-${tabCorrente}`);
            lista.innerHTML = '';

            if (camere.length === 0) {
                lista.innerHTML = '<p style="text-align: center; color: #999; padding: 40px;">Nessuna camera in questo stato</p>';
                return;
            }

            if (tabCorrente === 'occupate') {
                caricaCamereOccupateConDettagli(camere, lista);
            } else if (tabCorrente === 'da-pulire') {
                caricaCamereDaPulire(camere, lista);
            } else {
                caricaCamereLibere(camere, lista);
            }
        })
        .catch(err => {
            console.error('Errore:', err);
            alert('Errore nel caricamento delle camere: ' + err.message);
        });
}

function caricaCamereOccupateConDettagli(camere, lista) {
    // Carica tutte le prenotazioni per trovare i dettagli
    fetch('http://localhost:8080/api/prenotazioni')
        .then(response => response.json())
        .then(prenotazioni => {
            camere.forEach(camera => {
                // Trova prenotazione attiva per questa camera
                const prenotazione = prenotazioni.find(p =>
                    p.cameraId === camera.id &&
                    p.stato === 'CHECK_IN_EFFETTUATO'
                );

                const card = document.createElement('div');
                card.className = 'camera-card';

                let dettagli = `
                    <h3>Camera ${camera.numero}</h3>
                    <p><strong>Tipo:</strong> ${camera.tipoCameraNome}</p>
                    <p><strong>Piano:</strong> ${camera.piano}</p>
                `;

                if (prenotazione) {
                    dettagli += `
                        <div class="prenotazione-info">
                            <h4>Dettagli Prenotazione</h4>
                            <p><strong>Codice:</strong> ${prenotazione.codicePrenotazione}</p>
                            <p><strong>Check-in:</strong> ${prenotazione.dataCheckIn}</p>
                            <p><strong>Check-out previsto:</strong> ${prenotazione.dataCheckOut}</p>
                            <p><strong>Ospiti:</strong> ${prenotazione.numeroOspiti}</p>
                            <button class="btn-secondary" onclick="visualizzaNote(${prenotazione.id})">Visualizza Note Cliente</button>
                        </div>
                    `;
                }

                card.innerHTML = dettagli;
                lista.appendChild(card);
            });
        })
        .catch(err => console.error('Errore caricamento prenotazioni:', err));
}

function caricaCamereDaPulire(camere, lista) {
    camere.forEach(camera => {
        const card = document.createElement('div');
        card.className = 'camera-card';
        card.innerHTML = `
            <h3>Camera ${camera.numero}</h3>
            <p><strong>Tipo:</strong> ${camera.tipoCameraNome}</p>
            <p><strong>Piano:</strong> ${camera.piano}</p>
            <button class="btn-primary" onclick="segnalaPuliziaCompletata(${camera.id})">Pulizia Completata</button>
        `;
        lista.appendChild(card);
    });
}

function caricaCamereLibere(camere, lista) {
    fetch('http://localhost:8080/api/prenotazioni')
        .then(response => response.json())
        .then(prenotazioni => {
            camere.forEach(camera => {
                const prenotazione = prenotazioni.find(p =>
                    p.cameraId === camera.id &&
                    p.stato === 'CONFERMATA'
                );

                const card = document.createElement('div');
                card.className = 'camera-card';

                let content = `
                    <h3>Camera ${camera.numero}</h3>
                    <p><strong>Tipo:</strong> ${camera.tipoCameraNome}</p>
                    <p><strong>Piano:</strong> ${camera.piano}</p>
                `;

                if (prenotazione) {
                    content += `
                        <p style="color: #ff9800; font-weight: bold;">📅 Camera prenotata</p>
                        <div class="prenotazione-info">
                            <p><strong>Codice:</strong> ${prenotazione.codicePrenotazione}</p>
                            <p><strong>Check-in previsto:</strong> ${prenotazione.dataCheckIn}</p>
                            <p><strong>Ospiti:</strong> ${prenotazione.numeroOspiti}</p>
                        </div>
                    `;
                } else {
                    content += `<p style="color: #4caf50; font-weight: bold;">✓ Pronta per essere occupata</p>`;
                }

                card.innerHTML = content;
                lista.appendChild(card);
            });
        })
        .catch(err => console.error('Errore caricamento prenotazioni:', err));
}

function segnalaPuliziaCompletata(cameraId) {
    if (!confirm('Confermi che la pulizia della camera è stata completata?')) {
        return;
    }

    fetch(`http://localhost:8080/api/camere/${cameraId}/pulizia-completata`, {
        method: 'POST'
    })
    .then(response => {
        if (response.ok) {
            alert('Pulizia segnalata con successo! La camera è ora disponibile.');
            caricaCamere(); // Ricarica la lista
        } else {
            throw new Error('Errore durante la segnalazione');
        }
    })
    .catch(err => {
        alert('Errore: ' + err.message);
    });
}

function visualizzaNote(prenotazioneId) {
    fetch(`http://localhost:8080/api/notacliente/prenotazione/${prenotazioneId}`)
        .then(response => response.json())
        .then(note => {
            const body = document.getElementById('note-modal-body');
            if (note.length === 0) {
                body.innerHTML = '<p class="nota-empty">Nessuna nota presente per questa prenotazione.</p>';
            } else {
                body.innerHTML = note.map(nota => {
                    const data = new Date(nota.dataCreazione).toLocaleString('it-IT');
                    return `
                        <div class="nota-item">
                            <div class="nota-item-data">${data}</div>
                            <div class="nota-item-testo">${nota.testo}</div>
                        </div>`;
                }).join('');
            }
            document.getElementById('note-modal').style.display = 'flex';
        })
        .catch(err => {
            alert('Errore nel caricamento delle note: ' + err.message);
        });
}

function chiudiModal() {
    document.getElementById('note-modal').style.display = 'none';
}
