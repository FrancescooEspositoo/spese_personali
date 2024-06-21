Gestione Spese Personali con Java e MySQL
Questo progetto consiste in un'applicazione Java che si connette a un database MySQL per gestire i dati delle spese personali. L'applicazione offre funzionalità CRUD (Create, Read, Update, Delete) per le spese e le categorie.

Requisiti
Java Development Kit (JDK) versione 8 o successiva
MySQL Server
Librerie JDBC per MySQL (solitamente fornite dal driver MySQL)

Importare il progetto in un IDE Java come Eclipse.

Assicurarsi che il server MySQL sia in esecuzione sulla propria macchina locale o sia accessibile.

Configurare le credenziali di accesso al database nel file DatabaseConnection.java.

Eseguire il file DatabaseSetup.java per creare il database e le tabelle necessarie.

Eseguire l'applicazione eseguendo la classe SpesaApp.java.

Utilizzo
Una volta avviata l'applicazione ci sarà un primo pannello con il riepilogo delle spese dove potrai scaricare anche il PDF, sarà possibile poi:
Aggiungere, visualizzare, modificare ed eliminare spese e categorie utilizzando l'interfaccia grafica.

Inserire manualmente prima i dati richiesti nella categoria, come nome categoria e descrizione.
Successivamente inserire manualmente i dati richiesti, come nome spesa, importo spesa, data e il nome della categoria.
Le operazioni di modifica ed eliminazione richiederanno l'inserimento dell'ID del record corrispondente.
Nota: È stato implementato un pannello ulteriore per il report mensile o annuale delle spese.

Struttura del Progetto
Il progetto è strutturato come segue:

src/SpesePersonali: Contiene il codice sorgente Java.
SpeseApp.java: Classe principale dell'applicazione che gestisce l'interfaccia grafica e le interazioni con il database.
Spese.java: Classe che rappresenta una spesa.
Categoria.java: Classe che rappresenta una categoria.
SpeseCRUD.java: Classe per la gestione dei metodi CRUD delle spese e l'accesso ai dati del database.
DatabaseConnection.java: Classe per la gestione della connessione al database.
DatabaseSetup.java: Classe per la creazione delle tabelle e l'inserimento di dati di esempio nel database.
Pdf.java: Classe per la creazione della scrittura delle spese in pdf.

Contributi
Sono benvenuti i contributi per migliorare questo progetto.