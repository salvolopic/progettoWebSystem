// Variabili globali per tracciare la selezione
let strutturaSelezionata = null;
let cameraSelezionata = null;
let serviziSelezionati = [];
let currentStep = 1;

document.addEventListener('DOMContentLoaded', () => {
    verificaAutenticazione();
    caricaStrutture();

    // Setup form date
    document.getElementById('form-date').addEventListener('submit', (e) => {
        e.preventDefault();
        caricaCamereDisponibili();
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

function caricaStrutture() {
    fetch('http://localhost:8080/api/strutture')
    .then(response => {
        if (response.ok) return response.json();
        else throw new Error('Errore durante il caricamento delle strutture');
    })
    .then(strutture => {
        const lista = document.getElementById('lista-strutture');
        lista.innerHTML = '';

        if (strutture.length === 0) {
            lista.innerHTML = '<p>Nessuna struttura disponibile</p>';
            return;
        }

        strutture.forEach(struttura => {
            const card = document.createElement('div');
            card.className = 'room-card';
            card.innerHTML = `
                <h3>${struttura.nome}</h3>
                <p><strong>Indirizzo:</strong> ${struttura.indirizzo}, ${struttura.citta} (${struttura.provincia})</p>
                <p><strong>Stelle:</strong> ${'⭐'.repeat(struttura.stelle)}</p>
                <p>${struttura.descrizione || ''}</p>
                <p><strong>Contatti:</strong> ${struttura.telefono} - ${struttura.email}</p>
            `;
            card.onclick = () => selezionaStruttura(struttura);
            lista.appendChild(card);
        });
    })
    .catch(err => alert('Errore: ' + err.message));
}

function selezionaStruttura(struttura) {
    strutturaSelezionata = struttura;
    vaiAStep(2);
}

function caricaCamereDisponibili() {
    const dataCheckIn = document.getElementById('data-checkin').value;
    const dataCheckOut = document.getElementById('data-checkout').value;
    const numeroOspiti = document.getElementById('numero-ospiti').value;

    // Validazione date
    if (new Date(dataCheckIn) >= new Date(dataCheckOut)) {
        alert('La data di check-out deve essere successiva al check-in');
        return;
    }

    // Costruisci URL con parametri di date per filtrare disponibilità effettiva
    const url = `http://localhost:8080/api/camere/disponibili/${strutturaSelezionata.id}?dataCheckIn=${dataCheckIn}&dataCheckOut=${dataCheckOut}`;

    fetch(url)
    .then(response => {
        if (response.ok) return response.json();
        else throw new Error('Errore durante il caricamento delle camere');
    })
    .then(camere => {
        const lista = document.getElementById('lista-camere');
        lista.innerHTML = '';

        if (camere.length === 0) {
            lista.innerHTML = '<p>Nessuna camera disponibile per le date selezionate</p>';
            return;
        }

        camere.forEach(camera => {
            const card = document.createElement('div');
            card.className = 'room-card';
            card.innerHTML = `
                <h3>Camera ${camera.numero}</h3>
                <p><strong>Tipo:</strong> ${camera.tipoCameraNome}</p>
                <p><strong>Piano:</strong> ${camera.piano}</p>
                <p><strong>Capacità:</strong> ${camera.capacitaMinima} - ${camera.capacitaMassima} persone</p>
                <p><strong>Descrizione:</strong> ${camera.tipoCameraDescrizione || ''}</p>
                <p><strong>Caratteristiche:</strong></p>
                <ul>
                    ${camera.haVista ? '<li>Vista</li>' : ''}
                    ${camera.haBalcone ? '<li>Balcone</li>' : ''}
                    ${camera.haAriaCondizionata ? '<li>Aria condizionata</li>' : ''}
                </ul>
                <p><strong>Prezzo:</strong> €${camera.prezzoBase.toFixed(2)} per notte</p>
            `;
            card.onclick = (e) => selezionaCamera(camera, e);
            lista.appendChild(card);
        });

        vaiAStep(3);
    })
    .catch(err => alert('Errore: ' + err.message));
}

function selezionaCamera(camera, event) {
    // Rimuovi selezione precedente
    document.querySelectorAll('.room-card').forEach(card => {
        card.classList.remove('selected');
    });

    // Aggiungi selezione alla nuova camera
    event.target.closest('.room-card').classList.add('selected');

    cameraSelezionata = camera;

    // Carica servizi disponibili per questa struttura
    caricaServizi();
}

function caricaServizi() {
    fetch(`http://localhost:8080/api/servizi/struttura/${strutturaSelezionata.id}`)
    .then(response => {
        if (response.ok) return response.json();
        else throw new Error('Errore durante il caricamento dei servizi');
    })
    .then(servizi => {
        const lista = document.getElementById('lista-servizi');
        lista.innerHTML = '';

        if (servizi.length === 0) {
            lista.innerHTML = '<p>Nessun servizio aggiuntivo disponibile per questa struttura.</p>';
        } else {
            lista.innerHTML = '<p style="margin-bottom: 20px;"><strong>Seleziona i servizi aggiuntivi che desideri:</strong></p>';

            servizi.forEach(servizio => {
                const serviceDiv = document.createElement('div');
                serviceDiv.className = 'service-item';

                const infoDiv = document.createElement('div');
                infoDiv.style.flex = '1';
                infoDiv.innerHTML = `
                    <strong>${servizio.nome}</strong> - €${servizio.prezzo.toFixed(2)} ${servizio.perGiorno ? 'al giorno' : 'una tantum'}<br>
                    <small style="color: #666;">${servizio.descrizione}</small>
                `;

                const checkbox = document.createElement('input');
                checkbox.type = 'checkbox';
                checkbox.id = `servizio-${servizio.id}`;
                checkbox.value = servizio.id;
                checkbox.onchange = () => toggleServizio(servizio, checkbox.checked);

                serviceDiv.appendChild(infoDiv);
                serviceDiv.appendChild(checkbox);
                lista.appendChild(serviceDiv);
            });
        }

        vaiAStep(4);
    })
    .catch(err => {
        console.error('Errore:', err);
        const lista = document.getElementById('lista-servizi');
        lista.innerHTML = '<p>Errore nel caricamento dei servizi. Puoi continuare senza servizi aggiuntivi.</p>';
        vaiAStep(4);
    });
}

function toggleServizio(servizio, selected) {
    if (selected) {
        serviziSelezionati.push(servizio);
    } else {
        serviziSelezionati = serviziSelezionati.filter(s => s.id !== servizio.id);
    }
}

function vaiARiepilogo() {
    // Calcola costo totale
    const dataCheckIn = document.getElementById('data-checkin').value;
    const dataCheckOut = document.getElementById('data-checkout').value;
    const numeroOspiti = document.getElementById('numero-ospiti').value;

    const giorni = calcolaGiorni(dataCheckIn, dataCheckOut);
    const costoCamera = cameraSelezionata.prezzoBase * giorni;

    // Calcola costo servizi considerando se sono giornalieri o una tantum
    let costoServizi = 0;
    serviziSelezionati.forEach(s => {
        const prezzoServizio = parseFloat(s.prezzo);
        if (s.perGiorno) {
            costoServizi += prezzoServizio * giorni;
        } else {
            costoServizi += prezzoServizio;
        }
    });

    const costoTotale = costoCamera + costoServizi;

    // Popola riepilogo
    document.getElementById('summary-struttura').innerHTML = `
        <strong>${strutturaSelezionata.nome}</strong><br>
        ${strutturaSelezionata.indirizzo}, ${strutturaSelezionata.citta}
    `;

    document.getElementById('summary-camera').innerHTML = `
        <strong>Camera ${cameraSelezionata.numero}</strong> - ${cameraSelezionata.tipoCameraNome}<br>
        €${cameraSelezionata.prezzoBase.toFixed(2)} x ${giorni} notti = €${costoCamera.toFixed(2)}
    `;

    document.getElementById('summary-date').innerHTML = `
        Check-in: ${dataCheckIn}<br>
        Check-out: ${dataCheckOut}<br>
        Numero ospiti: ${numeroOspiti}<br>
        Durata: ${giorni} notti
    `;

    const serviziDiv = document.getElementById('summary-servizi');
    if (serviziSelezionati.length === 0) {
        serviziDiv.innerHTML = '<p>Nessun servizio aggiuntivo selezionato</p>';
    } else {
        let serviziHTML = '<ul style="list-style: none; padding: 0;">';
        serviziSelezionati.forEach(s => {
            const prezzoServizio = parseFloat(s.prezzo);
            const costoTotaleServizio = s.perGiorno ? prezzoServizio * giorni : prezzoServizio;
            serviziHTML += `
                <li style="margin-bottom: 8px;">
                    <strong>${s.nome}:</strong> €${prezzoServizio.toFixed(2)}
                    ${s.perGiorno ? `x ${giorni} giorni` : 'una tantum'}
                    = <strong>€${costoTotaleServizio.toFixed(2)}</strong>
                </li>
            `;
        });
        serviziHTML += '</ul>';
        serviziDiv.innerHTML = serviziHTML;
    }

    document.getElementById('summary-costo').textContent = `€${costoTotale.toFixed(2)}`;

    vaiAStep(5);
}

function confermaPrenotazione() {
    const utenteJSON = localStorage.getItem('utente');
    const utente = JSON.parse(utenteJSON);

    const dataCheckIn = document.getElementById('data-checkin').value;
    const dataCheckOut = document.getElementById('data-checkout').value;
    const numeroOspiti = document.getElementById('numero-ospiti').value;

    const prenotazione = {
        clienteId: utente.id,
        cameraId: cameraSelezionata.id,
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
        alert('Prenotazione creata con successo! Codice: ' + risultato.codicePrenotazione);
        window.location.href = '/pages/cliente/mie-prenotazioni.html';
    })
    .catch(err => alert('Errore: ' + err.message));
}

function calcolaGiorni(dataCheckIn, dataCheckOut) {
    const inizio = new Date(dataCheckIn);
    const fine = new Date(dataCheckOut);
    const differenza = fine - inizio;
    return Math.ceil(differenza / (1000 * 60 * 60 * 24));
}

function vaiAStep(stepNumber) {
    // Nascondi tutti gli step
    document.querySelectorAll('.step').forEach(step => {
        step.classList.remove('active');
    });

    // Mostra lo step corrente
    document.getElementById(`step-${stepNumber}`).classList.add('active');

    currentStep = stepNumber;

    window.scrollTo({ top: 0, behavior: 'smooth' });
}

function tornaStep(stepNumber) {
    vaiAStep(stepNumber);
}
