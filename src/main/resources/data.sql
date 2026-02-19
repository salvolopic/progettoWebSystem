-- Strutture
INSERT INTO strutture (id, nome, indirizzo, citta, cap, provincia, telefono, email, descrizione, stelle, attiva)
VALUES
    (1, 'Hotel Bellissimo', 'Via Roma 1', 'Roma', '00100', 'RM', '0612345678', 'hotel@bellissimo.it', 'Un hotel nel cuore di Roma', 4, true),
    (2, 'Hotel Mare Blu', 'Lungomare Marconi 45', 'Rimini', '47921', 'RN', '0541987654', 'info@mareblu.it', 'Hotel di lusso fronte mare con spa e piscina', 5, true),
    (3, 'Hotel Montagna Verde', 'Via Dolomiti 12', 'Cortina d''Ampezzo', '32043', 'BL', '0436876543', 'contatti@montagnaverde.it', 'Accogliente hotel di montagna con vista sulle Dolomiti', 3, true)
ON CONFLICT (id) DO NOTHING;

-- Tipi Camera
INSERT INTO tipi_camera (id, nome, descrizione, capacita_minima, capacita_massima, prezzo_base, ha_vista_mare, ha_balcone, ha_aria_condizionata, attivo)
VALUES
    (1, 'Singola', 'Camera singola confortevole', 1, 2, 80.00, false, false, true, true),
    (2, 'Doppia', 'Camera doppia spaziosa', 2, 4, 120.00, false, true, true, true),
    (3, 'Suite', 'Suite lussuosa con vista', 2, 3, 200.00, true, true, true, true)
ON CONFLICT (id) DO NOTHING;

-- Camere
INSERT INTO camere (id, numero, piano, struttura_id, tipo_camera_id, stato, attiva)
VALUES
    -- Hotel Bellissimo (Struttura 1)
    (1, '101', 1, 1, 1, 'DISPONIBILE', true),
    (2, '102', 1, 1, 1, 'DISPONIBILE', true),
    (3, '201', 2, 1, 2, 'DISPONIBILE', true),
    (4, '202', 2, 1, 2, 'DISPONIBILE', true),
    (5, '301', 3, 1, 3, 'DISPONIBILE', true),
    -- Hotel Mare Blu (Struttura 2)
    (6, '101', 1, 2, 1, 'DISPONIBILE', true),
    (7, '102', 1, 2, 1, 'DISPONIBILE', true),
    (8, '201', 2, 2, 2, 'DISPONIBILE', true),
    (9, '202', 2, 2, 2, 'DISPONIBILE', true),
    (10, '301', 3, 2, 3, 'DISPONIBILE', true),
    (11, '302', 3, 2, 3, 'DISPONIBILE', true),
    -- Hotel Montagna Verde (Struttura 3)
    (12, '1', 1, 3, 1, 'DISPONIBILE', true),
    (13, '2', 1, 3, 1, 'DISPONIBILE', true),
    (14, '3', 2, 3, 2, 'DISPONIBILE', true),
    (15, '4', 2, 3, 2, 'DISPONIBILE', true),
    (16, '5', 3, 3, 3, 'DISPONIBILE', true)
ON CONFLICT (id) DO NOTHING;

-- Utenti di sistema (GESTORE e PERSONALE) - password = "password123" (BCrypt hash)
INSERT INTO utenti (id, username, password, nome, cognome, email, ruolo, attivo, data_registrazione, struttura_assegnata_id)
VALUES
    -- GESTORE DELLA CATENA (vede TUTTI gli hotel)
    (1, 'gestore', '$2a$10$WynYsLg.QYoKgvTAMGofx.GowjP2cU5CepfYn1fUT44EeggI0KOKi', 'Mario', 'Rossi', 'gestore@hotelchain.it', 'GESTORE', true, CURRENT_TIMESTAMP, NULL),
    -- Personale Hotel Bellissimo (Struttura 1)
    (2, 'personale1', '$2a$10$WynYsLg.QYoKgvTAMGofx.GowjP2cU5CepfYn1fUT44EeggI0KOKi', 'Luigi', 'Verdi', 'personale@bellissimo.it', 'PERSONALE', true, CURRENT_TIMESTAMP, 1),
    -- Personale Hotel Mare Blu (Struttura 2)
    (3, 'personale2', '$2a$10$WynYsLg.QYoKgvTAMGofx.GowjP2cU5CepfYn1fUT44EeggI0KOKi', 'Anna', 'Bianchi', 'personale@mareblu.it', 'PERSONALE', true, CURRENT_TIMESTAMP, 2),
    -- Personale Hotel Montagna Verde (Struttura 3)
    (4, 'personale3', '$2a$10$WynYsLg.QYoKgvTAMGofx.GowjP2cU5CepfYn1fUT44EeggI0KOKi', 'Marco', 'Neri', 'personale@montagnaverde.it', 'PERSONALE', true, CURRENT_TIMESTAMP, 3)
ON CONFLICT (username) DO NOTHING;

-- Servizi Aggiuntivi (prezzi variabili per servizio)
INSERT INTO servizi_aggiuntivi (id, tipo, nome, descrizione, prezzo, per_giorno, struttura_id, attivo)
VALUES
    -- Hotel Bellissimo (Struttura 1)
    (1, 'SPA', 'Accesso SPA', 'Accesso illimitato alla spa con sauna, bagno turco e idromassaggio', 25.00, false, 1, true),
    (2, 'TRANSFER', 'Servizio Transfer', 'Transfer privato da/per aeroporto o stazione', 30.00, false, 1, true),
    (3, 'COLAZIONE', 'Colazione', 'Colazione continentale a buffet', 12.00, true, 1, true),
    (4, 'MEZZA_PENSIONE', 'Mezza Pensione', 'Colazione e cena incluse', 35.00, true, 1, true),
    (5, 'PENSIONE_COMPLETA', 'Pensione Completa', 'Colazione, pranzo e cena inclusi', 50.00, true, 1, true),
    (6, 'PARCHEGGIO', 'Parcheggio', 'Parcheggio privato custodito 24h', 10.00, true, 1, true),
    (7, 'MINIBAR', 'Minibar Premium', 'Minibar rifornito giornalmente con prodotti premium', 15.00, true, 1, true),
    (8, 'LAVANDERIA', 'Servizio Lavanderia', 'Servizio di lavanderia express (24h)', 20.00, false, 1, true),
    (9, 'PALESTRA', 'Accesso Palestra', 'Accesso alla palestra attrezzata 24h', 15.00, false, 1, true),
    (10, 'PISCINA', 'Accesso Piscina', 'Accesso alla piscina coperta riscaldata', 18.00, false, 1, true),
    -- Hotel Mare Blu (Struttura 2)
    (11, 'SPA', 'Spa Luxury', 'Centro benessere con massaggi e trattamenti estetici', 35.00, false, 2, true),
    (12, 'TRANSFER', 'Transfer Aeroporto', 'Navetta privata aeroporto Rimini', 25.00, false, 2, true),
    (13, 'COLAZIONE', 'Colazione Buffet', 'Ricca colazione a buffet vista mare', 15.00, true, 2, true),
    (14, 'MEZZA_PENSIONE', 'Mezza Pensione', 'Colazione e cena con menù pesce', 40.00, true, 2, true),
    (15, 'PENSIONE_COMPLETA', 'Pensione Completa', 'Tutti i pasti inclusi con specialità romagnole', 60.00, true, 2, true),
    (16, 'PARCHEGGIO', 'Parcheggio Coperto', 'Garage privato custodito', 12.00, true, 2, true),
    (17, 'PISCINA', 'Ombrellone e Lettini', 'Postazione spiaggia privata', 20.00, true, 2, true),
    (18, 'TRANSFER', 'Escursioni in Barca', 'Tour in barca lungo la riviera', 45.00, false, 2, true),
    -- Hotel Montagna Verde (Struttura 3)
    (19, 'TRANSFER', 'Transfer Stazione', 'Navetta da/per stazione di Cortina', 20.00, false, 3, true),
    (20, 'COLAZIONE', 'Colazione Alpina', 'Colazione con prodotti tipici di montagna', 10.00, true, 3, true),
    (21, 'MEZZA_PENSIONE', 'Mezza Pensione', 'Colazione e cena tipica montana', 30.00, true, 3, true),
    (22, 'PENSIONE_COMPLETA', 'Pensione Completa', 'Tutti i pasti con specialità locali', 45.00, true, 3, true),
    (23, 'PARCHEGGIO', 'Parcheggio', 'Parcheggio esterno gratuito', 0.00, true, 3, true),
    (24, 'PALESTRA', 'Skipass Giornaliero', 'Skipass per impianti Cortina', 50.00, true, 3, true),
    (25, 'PARCHEGGIO', 'Deposito Sci', 'Deposito attrezzatura sci riscaldato', 5.00, true, 3, true),
    (26, 'TRANSFER', 'Guida Escursionistica', 'Guida per trekking e escursioni', 40.00, false, 3, true)
ON CONFLICT (id) DO NOTHING;

-- Utente cliente di test per poter testare check-in
INSERT INTO utenti (id, username, password, nome, cognome, email, ruolo, attivo, data_registrazione, struttura_assegnata_id)
VALUES (5, 'cliente', '$2a$10$WynYsLg.QYoKgvTAMGofx.GowjP2cU5CepfYn1fUT44EeggI0KOKi', 'Giovanni', 'Rossi', 'cliente@test.it', 'CLIENTE', true, CURRENT_TIMESTAMP, NULL)
ON CONFLICT (username) DO NOTHING;

-- Prenotazione di test con stato CONFERMATA per testare check-in
INSERT INTO prenotazioni (id, codice_prenotazione, cliente_id, camera_id, data_check_in, data_check_out, numero_ospiti, costo_totale, stato, data_creazione, data_modifica)
VALUES (1, 'PRE-001-2026', 5, 1, CURRENT_DATE, CURRENT_DATE + INTERVAL '3 days', 2, 360.00, 'CONFERMATA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

-- Reset sequences per evitare conflitti con auto-increment
SELECT setval('strutture_id_seq', (SELECT MAX(id) FROM strutture));
SELECT setval('tipi_camera_id_seq', (SELECT MAX(id) FROM tipi_camera));
SELECT setval('camere_id_seq', (SELECT MAX(id) FROM camere));
SELECT setval('utenti_id_seq', (SELECT MAX(id) FROM utenti));
SELECT setval('servizi_aggiuntivi_id_seq', (SELECT MAX(id) FROM servizi_aggiuntivi));
SELECT setval('prenotazioni_id_seq', (SELECT MAX(id) FROM prenotazioni));
