document.addEventListener('DOMContentLoaded', () => {
    verificaAutenticazione();
    mostraInfoUtente();
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

function mostraInfoUtente() {
    const utenteJSON = localStorage.getItem('utente');
    if (utenteJSON) {
        const utente = JSON.parse(utenteJSON);
        const userInfo = document.getElementById('user-info');
        userInfo.innerHTML = `
            <strong>${utente.nome} ${utente.cognome}</strong><br>
            Email: ${utente.email}
        `;
    }
}

function logout() {
    fetch('http://localhost:8080/api/auth/logout', { method: 'POST' })
    .finally(() => {
        localStorage.removeItem('utente'); //ripulisco i dati dal server
        localStorage.removeItem('userId');
        window.location.href = '/pages/login.html';
    });
}
