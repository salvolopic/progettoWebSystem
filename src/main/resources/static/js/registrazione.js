document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('registrazione-form');

    form.addEventListener('submit', (e) => {
        e.preventDefault();
        effettuaRegistrazione();
    });
});

function effettuaRegistrazione() {
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const nome = document.getElementById('nome').value;
    const cognome = document.getElementById('cognome').value;
    const email = document.getElementById('email').value;

    const nuovoUtente = {
        username: username,
        password: password,
        nome: nome,
        cognome: cognome,
        email: email,
        ruolo: 'CLIENTE'
    };

    fetch('http://localhost:8080/api/auth/registrazione', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(nuovoUtente)
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        } else if (response.status === 400) {
            throw new Error('Dati non validi o utente già esistente');
        } else {
            throw new Error('Errore durante la registrazione');
        }
    })
    .then(() => {
        alert('Registrazione completata!');
        window.location.href = '/pages/login.html';
    })
    .catch(err => {
        console.error('Errore:', err);
        alert('Errore: ' + err.message);
    });
}
