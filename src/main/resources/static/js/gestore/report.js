document.addEventListener('DOMContentLoaded', () => {
    verificaAutenticazione();
    caricaStrutture();

    // Imposta data di oggi come default
    const oggi = new Date().toISOString().split('T')[0];
    document.getElementById('data-questura').value = oggi;
    document.getElementById('data-tassa').value = oggi;

    // Setup form handlers
    document.getElementById('form-questura').addEventListener('submit', (e) => {
        e.preventDefault();
        scaricaReportQuestura();
    });

    document.getElementById('form-tassa').addEventListener('submit', (e) => {
        e.preventDefault();
        scaricaReportTassaSoggiorno();
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
    if (utente.ruolo !== 'GESTORE') {
        alert('Accesso non autorizzato - solo per gestori');
        window.location.href = '/pages/login.html';
        return;
    }
}

function caricaStrutture() {
    fetch('http://localhost:8080/api/strutture')
        .then(response => response.json())
        .then(strutture => {
            const selectQuestura = document.getElementById('struttura-questura');
            const selectTassa = document.getElementById('struttura-tassa');

            strutture.forEach(s => {
                const option1 = document.createElement('option');
                option1.value = s.id;
                option1.textContent = s.nome;
                selectQuestura.appendChild(option1);

                const option2 = document.createElement('option');
                option2.value = s.id;
                option2.textContent = s.nome;
                selectTassa.appendChild(option2);
            });
        })
        .catch(err => {
            console.error('Errore caricamento strutture:', err);
        });
}

function scaricaReportQuestura() {
    const data = document.getElementById('data-questura').value;
    const strutturaId = document.getElementById('struttura-questura').value;

    if (!data) {
        alert('Seleziona una data');
        return;
    }

    // Costruisci URL
    let url = `http://localhost:8080/api/report/questura?data=${data}`;
    if (strutturaId) {
        url += `&strutturaId=${strutturaId}`;
    }

    // Mostra messaggio di caricamento
    const btn = event.submitter;
    const originalText = btn.textContent;
    btn.textContent = 'Generazione in corso...';
    btn.disabled = true;

    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('Errore nella generazione del report');
            }
            return response.blob();
        })
        .then(blob => {
            // Crea link per download
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = `report_questura_${data}.xml`;
            document.body.appendChild(a);
            a.click();
            window.URL.revokeObjectURL(url);
            document.body.removeChild(a);

            alert('Report scaricato con successo!');
        })
        .catch(err => {
            alert('Errore: ' + err.message);
        })
        .finally(() => {
            btn.textContent = originalText;
            btn.disabled = false;
        });
}

function scaricaReportTassaSoggiorno() {
    const data = document.getElementById('data-tassa').value;
    const strutturaId = document.getElementById('struttura-tassa').value;

    if (!data) {
        alert('Seleziona una data');
        return;
    }

    // Costruisci URL
    let url = `http://localhost:8080/api/report/tassa-soggiorno?data=${data}`;
    if (strutturaId) {
        url += `&strutturaId=${strutturaId}`;
    }

    // Mostra messaggio di caricamento
    const btn = event.submitter;
    const originalText = btn.textContent;
    btn.textContent = 'Generazione in corso...';
    btn.disabled = true;

    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('Errore nella generazione del report');
            }
            return response.blob();
        })
        .then(blob => {
            // Crea link per download
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = `report_tassa_soggiorno_${data}.xml`;
            document.body.appendChild(a);
            a.click();
            window.URL.revokeObjectURL(url);
            document.body.removeChild(a);

            alert('Report scaricato con successo!');
        })
        .catch(err => {
            alert('Errore: ' + err.message);
        })
        .finally(() => {
            btn.textContent = originalText;
            btn.disabled = false;
        });
}
