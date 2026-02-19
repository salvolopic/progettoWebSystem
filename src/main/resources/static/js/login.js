document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('login-form');
    loginForm.addEventListener('submit', (e) => {
        e.preventDefault();
        effettuaLogin();
    });
});

function effettuaLogin() {
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    // Pulisci sessione precedente
    localStorage.removeItem('utente');

    fetch('http://localhost:8080/api/auth/logout', { method: 'POST' })
    .catch(() => {}) // ignora errori se non c'era sessione
    .finally(() => {
        fetch('http://localhost:8080/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password })
        })
        .then(response => {
            if (response.ok) return response.json();
            else throw new Error('Credenziali non valide');
        })
        .then(utente => {
            localStorage.setItem('utente', JSON.stringify(utente));

            if (utente.ruolo === 'CLIENTE') {
                window.location.href = '/pages/cliente/menu.html';
            } else if (utente.ruolo === 'PERSONALE') {
                window.location.href = '/pages/personale/camere.html';
            } else {
                window.location.href = '/pages/gestore/dashboard.html';
            }
        })
        .catch(err => alert('Errore: ' + err.message));
    });
}

