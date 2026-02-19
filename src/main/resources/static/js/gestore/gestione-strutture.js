function getUtenteLoggato() {
    const utenteJSON = localStorage.getItem('utente');
    if (utenteJSON) return JSON.parse(utenteJSON);
    return null;
}

const utenteLoggato = getUtenteLoggato();
if (!utenteLoggato || utenteLoggato.ruolo !== 'GESTORE') {
    window.location.href = '/pages/login.html';
}

document.addEventListener('DOMContentLoaded', () => {
    caricaStrutture();
});

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
            lista.innerHTML = '<p style="text-align: center; padding: 40px; color: #999;">Nessuna struttura presente</p>';
            return;
        }

        strutture.forEach(struttura => {
            const card = document.createElement('div');
            card.className = 'room-card';

            const statoAttiva = struttura.attiva
                ? '<span style="color: #4caf50; font-weight: bold;">Attiva</span>'
                : '<span style="color: #f44336; font-weight: bold;">Non Attiva</span>';

            card.innerHTML = `
                <h3>${struttura.nome} ${statoAttiva}</h3>
                <p><strong>Indirizzo:</strong> ${struttura.indirizzo}, ${struttura.citta} ${struttura.cap || ''} (${struttura.provincia || 'N/A'})</p>
                <p><strong>Stelle:</strong> ${'⭐'.repeat(struttura.stelle || 0) || 'Nessuna classificazione'}</p>
                <p><strong>Telefono:</strong> ${struttura.telefono}</p>
                ${struttura.email ? `<p><strong>Email:</strong> ${struttura.email}</p>` : ''}
                ${struttura.descrizione ? `<p><strong>Descrizione:</strong> ${struttura.descrizione}</p>` : ''}
            `;
            lista.appendChild(card);
        });
    })
    .catch(err => alert('Errore: ' + err.message));
}
