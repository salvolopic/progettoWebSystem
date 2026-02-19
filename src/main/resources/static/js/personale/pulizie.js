document.addEventListener('DOMContentLoaded', () => {
    caricaCamereDaPulire();
});

function caricaCamereDaPulire() {
    fetch('http://localhost:8080/api/camere')
    .then(response => {
        if (response.ok) return response.json();
        else throw new Error('Errore durante il recupero delle camere da pulire');
    })
    .then(camere => {
        const listaCamere = document.getElementById('lista-camere');
        listaCamere.innerHTML = '';
        // Filtra le camere che necessitano di pulizia stato === 'DA_PULIRE'
        camere = camere.filter(camera => camera.stato === 'DA_PULIRE');

        camere.forEach(element => {
            const li = document.createElement('li');
            li.innerHTML = `
                Camera: ${element.numero} - 
                Stato: ${element.stato} - 
                <button onclick="registraPulizia(${element.id})">Segna come pulita</button>
            `;
            listaCamere.appendChild(li);
        });
    })
    .catch(err => alert('Errore: ' + err.message));
}

const personaleId = 1; // ID del personale di pulizia loggato

//Registra pulizia 
function registraPulizia(cameraId) {
    fetch(`http://localhost:8080/api/pulizie?cameraId=${cameraId}&personaleId=${personaleId}`, {
        method: 'POST'
    })
    .then(response => {
        if ( response.ok) {
            alert('Pulizia registrata con successo');
            caricaCamereDaPulire();
        } else {
            throw new Error('Errore durante la registrazione della pulizia');
        }
    })
    .catch(err => alert('Errore: ' + err.message));
    }