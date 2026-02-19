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
    numeroCamerePerStato();
    caricaPanoramica();
});

function caricaPanoramica() {
    fetch('http://localhost:8080/api/strutture')
    .then(response => response.json())
    .then(strutture => {
        const container = document.getElementById('panoramica-strutture');
        container.innerHTML = '';

        strutture.forEach(struttura => {
            Promise.all([
                fetch(`http://localhost:8080/api/camere/disponibili/${struttura.id}`).then(r => r.json()),
                fetch(`http://localhost:8080/api/camere/occupate/${struttura.id}`).then(r => r.json()),
                fetch(`http://localhost:8080/api/camere/da-pulire/${struttura.id}`).then(r => r.json())
            ]).then(([disponibili, occupate, daPulire]) => {
                const stelle = struttura.stelle ? '⭐'.repeat(struttura.stelle) : '';
                const panel = document.createElement('div');
                panel.className = 'struttura-panel';
                panel.innerHTML = `
                    <div class="struttura-panel-header">
                        <h3>🏨 ${struttura.nome}</h3>
                        <span class="struttura-stelle">${stelle} &nbsp;${struttura.citta}</span>
                    </div>
                    <div class="struttura-panel-body">
                        <div class="mini-stat">
                            <div class="mini-stat-dot disponibile"></div>
                            <div class="mini-stat-info">
                                <div class="mini-stat-num">${disponibili.length}</div>
                                <div class="mini-stat-label">Disponibili</div>
                            </div>
                        </div>
                        <div class="mini-stat">
                            <div class="mini-stat-dot occupata"></div>
                            <div class="mini-stat-info">
                                <div class="mini-stat-num">${occupate.length}</div>
                                <div class="mini-stat-label">Occupate</div>
                            </div>
                        </div>
                        <div class="mini-stat">
                            <div class="mini-stat-dot da-pulire"></div>
                            <div class="mini-stat-info">
                                <div class="mini-stat-num">${daPulire.length}</div>
                                <div class="mini-stat-label">Da Pulire</div>
                            </div>
                        </div>
                    </div>
                `;
                container.appendChild(panel);
            }).catch(() => {});
        });
    })
    .catch(err => console.error('Errore panoramica strutture:', err));
}

function numeroCamerePerStato() {
    fetch('http://localhost:8080/api/camere')
    .then(response => {
        if (response.ok) return response.json();
        else throw new Error('Errore durante il caricamento');
    })
    .then(camere => {
        const conteggio = {
            DISPONIBILE: 0,
            OCCUPATA: 0,
            DA_PULIRE: 0
        };

        camere.forEach(camera => {
            if (conteggio[camera.stato] !== undefined) {
                conteggio[camera.stato]++;
            }
        });

        document.getElementById('num-disponibili').textContent = conteggio.DISPONIBILE;
        document.getElementById('num-occupate').textContent = conteggio.OCCUPATA;
        document.getElementById('num-da-pulire').textContent = conteggio.DA_PULIRE;
    })
    .catch(err => alert('Errore: ' + err.message));
}
