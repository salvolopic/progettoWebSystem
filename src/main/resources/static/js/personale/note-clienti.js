document.addEventListener('DOMContentLoaded', () => {
    caricaNote();
});

function caricaNote() {
    fetch('http://localhost:8080/api/notacliente/nonlette')
    .then(response => {
        if (response.ok) return response.json();
        else throw new Error('Errore durante il recupero delle note');
    })
    .then(note => {
        const tbody = document.querySelector('#note-clienti-table tbody');
        tbody.innerHTML = '';

        if (note.length === 0) {
            tbody.innerHTML = '<tr><td colspan="3">Nessuna nota non letta</td></tr>';
            return;
        }

        note.forEach(nota => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${nota.clienteNome} ${nota.clienteCognome} (Pren. ${nota.prenotazioneId})</td>
                <td>${nota.testo}</td>
                <td>${new Date(nota.dataCreazione).toLocaleString()}
                    <button onclick="segnaComeLetta(${nota.id})">Segna come letta</button>
                </td>
            `;
            tbody.appendChild(tr);
        });
    })
    .catch(err => alert('Errore: ' + err.message));
}

function segnaComeLetta(notaId) {

    fetch(`http://localhost:8080/api/notacliente/${notaId}/letta`, {
        method: 'PUT'
    })
    .then(response => {
        if (response.ok) {
            caricaNote();
        } else {
            throw new Error('Errore durante l\'aggiornamento');
        }
    })
    .catch(err => alert('Errore: ' + err.message));
}
