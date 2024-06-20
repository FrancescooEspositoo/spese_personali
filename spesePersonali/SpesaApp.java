package spesePersonali;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


public class SpesaApp extends JFrame{
	private SpesaCRUD spesaCRUD;
	private CategoriaCRUD categoriaCRUD;
	private JTextField idSpesaDeleteField, idSpesaField, nomeSpesaField, importoSpesaField, dataField, idCategoriaField;
	private JTextField nomeCategoriaField, descrizioneField;
	private JTextField nuovaSpesaField, nuovoImportoField, nuovaDataField, nuovoIdCatField;
	private JTextField idCategoriaDeleteField;
	private JTextField nuovaIdCategoriaField, nuovaCategoriaField, nuovaDescrizioneField;
	JComboBox<String> idCategoriaComboBox;
	public SpesaApp(){
		spesaCRUD = new SpesaCRUD();
		categoriaCRUD = new CategoriaCRUD();
		//schermata iniziale
		setupDatabase();

		setTitle("Gestione Spese Personali");
		setSize(1000, 700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JTabbedPane tabbedPane = new JTabbedPane();


		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		setupRiepilogo(mainPanel);
		tabbedPane.addTab("Ripilogo Spese", mainPanel);

		JPanel categoriePanel = new JPanel();
		categoriePanel.setLayout(new BorderLayout());
		setupCategoria(categoriePanel);
		tabbedPane.addTab("Categorie", categoriePanel);

		JPanel spesePanel = new JPanel();
		spesePanel.setLayout(new BorderLayout());
		setupSpesePanel(spesePanel);
		tabbedPane.addTab("Spese", spesePanel);

		JPanel deletePanel = new JPanel();
		deletePanel.setLayout(new BorderLayout());
		setupDelete(deletePanel);
		tabbedPane.addTab("Elimina Spese/Categorie", deletePanel);

		JPanel updatePanel = new JPanel();
		updatePanel.setLayout(new BorderLayout());
		setupUpdate(updatePanel);
		tabbedPane.addTab("Modifica Spese", updatePanel);

		JPanel updateCategoriaPanel = new JPanel();
		updateCategoriaPanel.setLayout(new BorderLayout());
		setupUpdateCategoria(updateCategoriaPanel);
		tabbedPane.addTab("Modifica Categoria", updateCategoriaPanel);

		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new BorderLayout());
		setupSearch(searchPanel);
		tabbedPane.addTab("Ricerca Data/Categoria", searchPanel);
		add(tabbedPane);

		JPanel reportPanel = new JPanel();
		reportPanel.setLayout(new BorderLayout());
		setupMensile(reportPanel);
		tabbedPane.addTab("Report Mensili/Annuali", reportPanel);
		add(tabbedPane);
	}

	//schermata iniziale
	private void setupDatabase() {
		JPanel credentialsPanel = new JPanel(new GridLayout(3, 2));
		JTextField userField = new JTextField();
		JPasswordField passwordField = new JPasswordField();

		credentialsPanel.add(new JLabel("Database Username:"));
		credentialsPanel.add(userField);
		credentialsPanel.add(new JLabel("Database Password:"));
		credentialsPanel.add(passwordField);

		int result = JOptionPane.showConfirmDialog(null, credentialsPanel, "Enter Database Credentials", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			String user = userField.getText();
			String password = new String(passwordField.getPassword());

			DatabaseConnection.setCredentials(user, password);
			DatabaseSetup.main(new String[]{user, password});
		} else {
			System.exit(0);
		}
	}
	private void setupRiepilogo(JPanel panel){
		JTextArea outputAreaSpese = new JTextArea();
		JScrollPane scrollPaneSpese = new JScrollPane(outputAreaSpese);
		panel.add(scrollPaneSpese, BorderLayout.CENTER);
		readSpesaNuova(outputAreaSpese);
		JPanel buttonPanel = new JPanel();
		
		buttonPanel.setLayout(new FlowLayout());
		JButton btnRead = new JButton("Scarica PDF");
		btnRead.setBackground(Color.GREEN);
		btnRead.setForeground(Color.BLACK); 
		Pdf pdf = new Pdf();
		btnRead.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pdf.salvaPdf();
			}
		});
		buttonPanel.add(btnRead);

		panel.add(buttonPanel, BorderLayout.SOUTH);

	}
	private void setupSpesePanel(JPanel panel) {
		JTextArea outputAreaSpese = new JTextArea();
		JScrollPane scrollPaneSpese = new JScrollPane(outputAreaSpese);
		panel.add(scrollPaneSpese, BorderLayout.CENTER);

		JPanel inputPanelSpesa = new JPanel();
		inputPanelSpesa.setLayout(new GridLayout(7, 2));
		inputPanelSpesa.add(new JLabel());
		inputPanelSpesa.add(new JLabel());
		inputPanelSpesa.add(new JLabel("Nome Spesa:"));
		nomeSpesaField = new JTextField();
		inputPanelSpesa.add(nomeSpesaField);

		inputPanelSpesa.add(new JLabel("Importo:"));
		importoSpesaField = new JTextField();
		inputPanelSpesa.add(importoSpesaField);

		inputPanelSpesa.add(new JLabel("Data (YYYY-MM-DD):"));
		dataField = new JTextField();
		inputPanelSpesa.add(dataField);

		inputPanelSpesa.add(new JLabel("Categoria:"));
		idCategoriaComboBox = new JComboBox<>();

		//aggiuta le categorie disponibili 
		popolaComboBoxCategorie(); // Metodo per popolare la JComboBox con le categorie dal database
		inputPanelSpesa.add(idCategoriaComboBox);


		JButton btnAddSpesa = new JButton("Aggiungi Spesa");
		btnAddSpesa.setBackground(Color.GREEN);
		btnAddSpesa.setForeground(Color.BLACK); 
		btnAddSpesa.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addSpesa(outputAreaSpese);
				JOptionPane.showMessageDialog(panel, "Aggiunta Spesa avvenuta con successo.");
			}
		});
		JButton btnResetSpesa = new JButton("Reset");
		btnResetSpesa.setBackground(Color.LIGHT_GRAY);
		btnResetSpesa.setForeground(Color.BLACK);
		btnResetSpesa.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Reset dei campi
				nomeSpesaField.setText("");
				importoSpesaField.setText("");
				dataField.setText("");
				idCategoriaComboBox.setSelectedIndex(0); 
				

			}
		});
		inputPanelSpesa.add(new JLabel());
		inputPanelSpesa.add(new JLabel());
		inputPanelSpesa.add(btnAddSpesa);
		inputPanelSpesa.add(btnResetSpesa);
		panel.add(inputPanelSpesa, BorderLayout.NORTH);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());

		JButton btnRead = new JButton("Visualizza Spese");
		btnRead.setBackground(Color.PINK);
		btnRead.setForeground(Color.BLACK);
		btnRead.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				readSpesaNuova(outputAreaSpese);
				//readSpesa(outputAreaSpese);
			}
		});
		buttonPanel.add(btnRead);

		panel.add(buttonPanel, BorderLayout.SOUTH);
		initializeComboBox();
	}
	private void setupCategoria(JPanel panel) {
		JTextArea outputAreaCategoria = new JTextArea();
		JScrollPane scrollPaneCategoria = new JScrollPane(outputAreaCategoria);
		panel.add(scrollPaneCategoria, BorderLayout.CENTER);

		JPanel inputPanelCategoria = new JPanel();
		inputPanelCategoria.setLayout(new GridLayout(5, 2));
		inputPanelCategoria.add(new JLabel());
		inputPanelCategoria.add(new JLabel());
		inputPanelCategoria.add(new JLabel("Nome Categoria:"));
		nomeCategoriaField = new JTextField();
		inputPanelCategoria.add(nomeCategoriaField);

		inputPanelCategoria.add(new JLabel("Descrizione:"));
		descrizioneField = new JTextField();
		inputPanelCategoria.add(descrizioneField);



		JButton btnAddCategoria = new JButton("Aggiungi Categoria");
		btnAddCategoria.setBackground(Color.GREEN);
		btnAddCategoria.setForeground(Color.BLACK);
		btnAddCategoria.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addCategoria(outputAreaCategoria);
				updateCategoriaComboBox(); //aggiornamento combobox
				JOptionPane.showMessageDialog(panel, "Aggiunta Categoria avvenuta con successo.");
			}
		});
		JButton btnResetCategoria = new JButton("Reset");
		btnResetCategoria.setBackground(Color.LIGHT_GRAY);
		btnResetCategoria.setForeground(Color.BLACK);
		btnResetCategoria.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				nomeCategoriaField.setText("");
				descrizioneField.setText("");
			}
		});
		inputPanelCategoria.add(new JLabel());
		inputPanelCategoria.add(new JLabel());
		inputPanelCategoria.add(btnAddCategoria);
		inputPanelCategoria.add(btnResetCategoria);
		panel.add(inputPanelCategoria, BorderLayout.NORTH);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		
		JButton btnRead = new JButton("Visualizza Categorie");
		btnRead.setBackground(Color.PINK);
		btnRead.setForeground(Color.BLACK);
		btnRead.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				readCategoria(outputAreaCategoria);
			}
		});
		buttonPanel.add(btnRead);

		panel.add(buttonPanel, BorderLayout.SOUTH);
	}
	private void setupDelete(JPanel panel){
		JTextArea outputAreaDelete = new JTextArea();
		JScrollPane scrollPaneDelete = new JScrollPane(outputAreaDelete);
		panel.add(scrollPaneDelete, BorderLayout.CENTER);

		JPanel inputPanelDelete = new JPanel();
		inputPanelDelete.setLayout(new GridLayout(8, 4));
		inputPanelDelete.add(new JLabel());
		inputPanelDelete.add(new JLabel());
		inputPanelDelete.add(new JLabel("ID Spesa:"));
		idSpesaDeleteField = new JTextField();
		inputPanelDelete.add(idSpesaDeleteField);

		JButton btnAdd = new JButton("Elimina Spesa");
		btnAdd.setBackground(Color.RED);
		btnAdd.setForeground(Color.BLACK);
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				deleteSpesa(outputAreaDelete);
				
				JOptionPane.showMessageDialog(panel, "Eliminazione avvenuta con successo.");
			}
		});
		JButton btnReset= new JButton("Reset");
		btnReset.setBackground(Color.LIGHT_GRAY);
		btnReset.setForeground(Color.BLACK);
		btnReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				idSpesaDeleteField.setText("");
			}
		});
		inputPanelDelete.add(new JLabel());
		inputPanelDelete.add(new JLabel());
		inputPanelDelete.add(btnAdd);
		inputPanelDelete.add(btnReset);

		//due placeholder vuoti
		inputPanelDelete.add(new Label());
		inputPanelDelete.add(new Label());
		//campi per la categoria
		inputPanelDelete.add(new JLabel("ID Categoria:"));
		idCategoriaDeleteField = new JTextField();
		inputPanelDelete.add(idCategoriaDeleteField);
		
		//bottone per elimina categoria
		JButton btnAddCategoria = new JButton("Elimina Categoria");
		btnAddCategoria.setBackground(Color.RED);
		btnAddCategoria.setForeground(Color.BLACK);
		btnAddCategoria.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				deleteCategoria(outputAreaDelete);
				updateCategoriaComboBox(); //aggiornamento combobox
				JOptionPane.showMessageDialog(panel, "Eliminazione avvenuta con successo.");
			}
		});
		JButton btnResetCategoria= new JButton("Reset");
		btnResetCategoria.setBackground(Color.LIGHT_GRAY);
		btnResetCategoria.setForeground(Color.BLACK);
		btnResetCategoria.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				idCategoriaDeleteField.setText("");
			}
		});
		inputPanelDelete.add(new JLabel());
		inputPanelDelete.add(new JLabel());
		inputPanelDelete.add(btnAddCategoria);
		inputPanelDelete.add(btnResetCategoria);
		panel.add(inputPanelDelete, BorderLayout.NORTH);

		JPanel buttonPanel = new JPanel();

		buttonPanel.setLayout(new FlowLayout());
		
		JButton btnReadCategoria = new JButton("Visualizza Categoria");
		btnReadCategoria.setBackground(Color.PINK);
		btnReadCategoria.setForeground(Color.BLACK);
		btnReadCategoria.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				readCategoria(outputAreaDelete);
			}
		});
		JButton btnRead = new JButton("Visualizza Spesa");
		btnRead.setBackground(Color.PINK);
		btnRead.setForeground(Color.BLACK);
		btnRead.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				readSpesa(outputAreaDelete);
			}
		});
		buttonPanel.add(btnRead);
		buttonPanel.add(btnReadCategoria);
		panel.add(buttonPanel, BorderLayout.SOUTH);
		
	}

	private void setupUpdate(JPanel panel) {
		JTextArea outputAreaUpdate = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(outputAreaUpdate);
		panel.add(scrollPane, BorderLayout.CENTER);

		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new GridLayout(8, 2));
		inputPanel.add(new JLabel());
		inputPanel.add(new JLabel());
		inputPanel.add(new JLabel("ID Spesa:"));
		idSpesaField = new JTextField();
		inputPanel.add(idSpesaField);

		inputPanel.add(new JLabel("Nome Spesa:"));
		nuovaSpesaField = new JTextField();
		inputPanel.add(nuovaSpesaField);

		inputPanel.add(new JLabel("Importo:"));
		nuovoImportoField = new JTextField();
		inputPanel.add(nuovoImportoField);

		inputPanel.add(new JLabel("Data (YYYY-MM-DD):"));
		nuovaDataField = new JTextField();
		inputPanel.add(nuovaDataField);

		inputPanel.add(new JLabel("ID Categoria:"));
		nuovoIdCatField = new JTextField();
		inputPanel.add(nuovoIdCatField);

		JButton btnAdd = new JButton("Modifica Spesa");
		btnAdd.setBackground(Color.GREEN);
		btnAdd.setForeground(Color.BLACK); 
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateSpesa(outputAreaUpdate);
				JOptionPane.showMessageDialog(panel, "Modifica Spesa avvenuta con successo.");
			}
		});
		JButton btnReset = new JButton("Reset");
		btnReset.setBackground(Color.LIGHT_GRAY);
		btnReset.setForeground(Color.BLACK);
		btnReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				//reset campi
				idSpesaField.setText("");
				nuovaSpesaField.setText("");
				nuovoImportoField.setText("");
				nuovaDataField.setText("");
				nuovoIdCatField.setText("");		
			}
		});
		inputPanel.add(new JLabel());
		inputPanel.add(new JLabel());
		inputPanel.add(btnAdd);
		inputPanel.add(btnReset);
		panel.add(inputPanel, BorderLayout.NORTH);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());

		JButton btnRead = new JButton("Visualizza Spese");
		btnRead.setBackground(Color.PINK);
		btnRead.setForeground(Color.BLACK);
		btnRead.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				readSpesa(outputAreaUpdate);
			}
		});
		buttonPanel.add(btnRead);

		panel.add(buttonPanel, BorderLayout.SOUTH);
	}

	private void setupUpdateCategoria(JPanel panel) {

		JTextArea outputAreaUpdate = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(outputAreaUpdate);
		panel.add(scrollPane, BorderLayout.CENTER);

		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new GridLayout(6, 2));
		inputPanel.add(new JLabel());
		inputPanel.add(new JLabel());
		inputPanel.add(new JLabel("ID Categoria:"));
		nuovaIdCategoriaField = new JTextField();
		inputPanel.add(nuovaIdCategoriaField);

		inputPanel.add(new JLabel("Nome Categoria:"));
		nuovaCategoriaField= new JTextField();
		inputPanel.add(nuovaCategoriaField);

		inputPanel.add(new JLabel("Descrizione:"));
		nuovaDescrizioneField= new JTextField();
		inputPanel.add(nuovaDescrizioneField);

		JButton btnAdd = new JButton("Modifica Categoria");
		btnAdd.setBackground(Color.GREEN);
		btnAdd.setForeground(Color.BLACK); 
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateCategoria(outputAreaUpdate);
				updateCategoriaComboBox();
				JOptionPane.showMessageDialog(panel, "Modifica Categoria avvenuta con successo.");
			}
		});
		JButton btnReset = new JButton("Reset");
		btnReset.setBackground(Color.LIGHT_GRAY);
		btnReset.setForeground(Color.BLACK);
		btnReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				//reset campi
				nuovaIdCategoriaField.setText("");
				nuovaCategoriaField.setText("");
				nuovaDescrizioneField.setText("");

			}
		});
		inputPanel.add(new JLabel());
		inputPanel.add(new JLabel());
		inputPanel.add(btnAdd);
		inputPanel.add(btnReset);
		panel.add(inputPanel, BorderLayout.NORTH);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());

		JButton btnRead = new JButton("Visualizza Categorie");
		btnRead.setBackground(Color.PINK);
		btnRead.setForeground(Color.BLACK);
		btnRead.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				readCategoria(outputAreaUpdate);
			}
		});
		buttonPanel.add(btnRead);

		panel.add(buttonPanel, BorderLayout.SOUTH);
	}
	private void setupSearch(JPanel panel) {
		JTextArea outputAreaSearch = new JTextArea();
		JScrollPane scrollPaneSearch = new JScrollPane(outputAreaSearch);
		panel.add(scrollPaneSearch, BorderLayout.CENTER);

		JPanel inputPanelSearch = new JPanel();
		inputPanelSearch.setLayout(new GridLayout(4, 2));
		inputPanelSearch.add(new JLabel());
		inputPanelSearch.add(new JLabel());
		inputPanelSearch.add(new JLabel("Ricerca Data o Categoria:"));
		JTextField searchField = new JTextField();
		inputPanelSearch.add(searchField);

		JButton btnSearch = new JButton("Cerca");
		btnSearch.setBackground(Color.GREEN);
		btnSearch.setForeground(Color.BLACK);
		btnSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String searchTerm = searchField.getText();
				searchSpesa(outputAreaSearch, searchTerm);
			}
		});
		JButton btnReset = new JButton("Reset");
		btnReset.setBackground(Color.LIGHT_GRAY);
		btnReset.setForeground(Color.BLACK);
		btnReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//reset dei campi
				searchField.setText("");

			}
		});
		inputPanelSearch.add(new JLabel());
		inputPanelSearch.add(new JLabel());
		inputPanelSearch.add(btnSearch);
		inputPanelSearch.add(btnReset);

		panel.add(inputPanelSearch, BorderLayout.NORTH);
	}


	private void setupMensile(JPanel panel) {
		JTextArea outputAreaReport = new JTextArea();
		JScrollPane scrollPaneReport = new JScrollPane(outputAreaReport);
		panel.add(scrollPaneReport, BorderLayout.CENTER);

		JPanel inputPanelReport = new JPanel();
		inputPanelReport.setLayout(new GridLayout(2, 2));
		inputPanelReport.add(new JLabel());
		inputPanelReport.add(new JLabel());
		
		JButton btnMensile = new JButton("Report Mensile");
		btnMensile.setBackground(Color.GRAY);
		btnMensile.setForeground(Color.BLACK);
		btnMensile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				visualizzaReportMensile(outputAreaReport);
			}
		});
		
		JButton btnAnnuale = new JButton("Report Annuale");
		btnAnnuale.setBackground(Color.GRAY);
		btnAnnuale.setForeground(Color.BLACK);
		btnAnnuale.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				visualizzaReportAnnuale(outputAreaReport);
			}
		});

		inputPanelReport.add(btnMensile);
		
		inputPanelReport.add(btnAnnuale);

		panel.add(inputPanelReport, BorderLayout.NORTH);
	}
	private void popolaComboBoxCategorie() {
		java.util.List<Categoria> categorie = categoriaCRUD.getAllCategoria(); // Esempio di metodo per ottenere le categorie dal database
		for (Categoria categoria : categorie) {
			
			idCategoriaComboBox.addItem(categoria.getNomeCategoria()); // Aggiunge il nome della categoria alla JComboBox
		}
	}

	
	private void updateCategoriaComboBox() {
		idCategoriaComboBox.removeAllItems(); // Rimuove tutti gli elementi esistenti
		java.util.List<Categoria> categorie = categoriaCRUD.getAllCategoria();
		for (Categoria categoria : categorie) {
			idCategoriaComboBox.addItem(categoria.getNomeCategoria());
		}
	}
	private void initializeComboBox() {
		updateCategoriaComboBox();
	}

	private void searchSpesa(JTextArea outputAreaSearch, String searchTerm) {

		java.util.List<Spesa> spese = spesaCRUD.searchSpesa(searchTerm);
		outputAreaSearch.setText("");

		for (Spesa s : spese) {
			outputAreaSearch.append("ID Spesa: " + s.getIdSpesa() + ", Nome Spesa: " + s.getNomeSpesa() +
					", Importo: " + s.getImporto() + ", Data: " + s.getData() +
					" |  ID Categoria: " + s.getIdCategoria() + ", Nome Categoria: " + s.getCategoria().getNomeCategoria()  +", Descrizione Categoria: " +s.getCategoria().getDescrizione() + "\n");
		}
	}


	//	private void addSpesa(JTextArea outputAreaSpese) {
	//		
	//		String nomeSpesa = nomeSpesaField.getText();
	//		String importoText = importoSpesaField.getText();
	//		String data = dataField.getText();
	////		String idCategoriaText = idCategoriaField.getText();
	//	    Categoria selectedCategoria = (Categoria) idCategoriaComboBox.getSelectedItem();
	//		
	//	    System.out.println("Nome Spesa: " + nomeSpesa);
	//	    System.out.println("Importo: " + importoText);
	//	    System.out.println("Data: " + data);
	//	    System.out.println("ID Categoria: " + selectedCategoria);
	//	    // Verifica che nessuno dei campi sia vuoto
	//	    if (nomeSpesa.isEmpty() || importoText.isEmpty() || data.isEmpty() || selectedCategoria == null) {
	//	        outputAreaSpese.setText("Tutti i campi devono essere compilati!\n");
	//	        return;
	//	    }
	//
	//	    try {
	//	        // Converti i campi di testo in valori numerici
	//	        float importo = Float.parseFloat(importoText);
	//	        
	//
	//	        // Crea un oggetto Spesa e aggiungilo al database
	//	        Spesa spesa = new Spesa(nomeSpesa, importo, data, selectedCategoria.getIdCategoria());
	//	        spesaCRUD.addSpesa(spesa);
	//	        
	////	        outputAreaSpese.setText("Spesa aggiunta con successo!\n");
	//	    } catch (NumberFormatException e) {
	//	        outputAreaSpese.setText("Importo e ID Categoria devono essere numeri validi!\n");
	//	    }
	//	}


	private void addSpesa(JTextArea outputAreaSpese) {
		String nomeSpesa = nomeSpesaField.getText();
		String importoText = importoSpesaField.getText();
		String data = dataField.getText();
		String nomeCategoria = (String) idCategoriaComboBox.getSelectedItem();

		// Verifica che nessuno dei campi sia vuoto
		if (nomeSpesa.isEmpty() || importoText.isEmpty() || data.isEmpty() || nomeCategoria.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Tutti i campi devono essere compilati");
//			outputAreaSpese.setText("Tutti i campi devono essere compilati!\n");
			return;
		}

		try {
			// Converti il campo importo in valore numerico
			float importo = Float.parseFloat(importoText);
			// Ottieni l'id della categoria dal nome
			int idCategoria = categoriaCRUD.getIdCategoriaFromNome(nomeCategoria);
			// Crea un oggetto Spesa e aggiungilo al database
			Spesa spesa = new Spesa(nomeSpesa, importo, data, idCategoria);
			spesaCRUD.addSpesa(spesa);


		} catch (NumberFormatException e) {
			outputAreaSpese.setText("Importo deve essere un numero valido!\n");
		}
	}



	private void readSpesa(JTextArea outputAreaSpese) {
		java.util.List<Spesa> spese = spesaCRUD.getAllSpesa();
		outputAreaSpese.setText("");
		for(Spesa s : spese) {
			outputAreaSpese.append("ID Spesa: " + s.getIdSpesa() + ", Nome Spesa: " + s.getNomeSpesa()+ ", Importo: " + "€"+s.getImporto() + ", Data: " + s.getData() + ", ID Categoria: "+ s.getIdCategoria() +"\n");

		}
	}

	private void updateSpesa(JTextArea outputAreaUpdate) {

		String idSpesaText = idSpesaField.getText();
		String nomeSpesa = nuovaSpesaField.getText();
		String importoText = nuovoImportoField.getText();
		String data = nuovaDataField.getText();
		String idCategoriaText = nuovoIdCatField.getText();

		// Verifica che nessuno dei campi sia vuoto
		if (idSpesaText.isEmpty()|| nomeSpesa.isEmpty() || importoText.isEmpty() || data.isEmpty() || idCategoriaText.isEmpty()) {
			outputAreaUpdate.setText("Tutti i campi devono essere compilati!\n");
			return;
		}

		try {
			// Converti i campi di testo in valori numerici
			float importo = Float.parseFloat(importoText);
			int idCategoria = Integer.parseInt(idCategoriaText);
			int idSpesa = Integer.parseInt(idSpesaText);

			// Crea un oggetto Spesa e aggiungilo al database
			Spesa spesa = new Spesa(idSpesa,nomeSpesa, importo, data, idCategoria);
			spesaCRUD.updateSpesa(spesa);
			//	        outputAreaUpdate.setText("Spesa modificata con successo!\n");
		} catch (NumberFormatException e) {
			outputAreaUpdate.setText("Importo e ID Categoria devono essere numeri validi!\n");
		}
	}

	private void updateCategoria(JTextArea outputAreaUpdate) {
		String idCategoriaText = nuovaIdCategoriaField.getText();
		String nomeCategoria = nuovaCategoriaField.getText();
		String descrizione = nuovaDescrizioneField.getText();


		// Verifica che nessuno dei campi sia vuoto
		if (idCategoriaText.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Tutti i campi devono essere compilati!");
//			outputAreaUpdate.setText("Tutti i campi devono essere compilati!\n");
			return;
		}

		try {
			// Converti i campi di testo in valori numerici

			int idCategoria = Integer.parseInt(idCategoriaText);


			// Crea un oggetto Spesa e aggiungilo al database
			Categoria categoria = new Categoria(idCategoria, nomeCategoria, descrizione);

			categoriaCRUD.updateCategoria(categoria);
			//	        outputAreaUpdate.setText("Spesa modificata con successo!\n");
		} catch (NumberFormatException e) {
			outputAreaUpdate.setText("Importo e ID Categoria devono essere numeri validi!\n");
		}
	}
	private void deleteSpesa(JTextArea outputAreaDelete) {
		int idSpesa = Integer.parseInt(idSpesaDeleteField.getText());
		spesaCRUD.deleteSpesa(idSpesa);
	}

	private void addCategoria(JTextArea outputAreaCategoria) {
		String nomeCategoria = nomeCategoriaField.getText();
		String descrizione = descrizioneField.getText();
		System.out.println("nome categoria: " + nomeCategoria);
		Categoria categoria = new Categoria(nomeCategoria,descrizione);
		categoriaCRUD.addCategoria(categoria);
		
	}
	private void readCategoria(JTextArea outputAreaCategoria) {
		java.util.List<Categoria> categorie = categoriaCRUD.getAllCategoria();
		outputAreaCategoria.setText("");
		for(Categoria c : categorie) {
			outputAreaCategoria.append("ID Categoria: " + c.getIdCategoria() + ", Nome Categoria: " + c.getNomeCategoria() + ", Descrizione: " + c.getDescrizione()+"\n");

		}
	}
	private void deleteCategoria(JTextArea outputAreaDelete) {
		int idCategoria = Integer.parseInt(idCategoriaDeleteField.getText());
		categoriaCRUD.deleteCategoria(idCategoria);
	}

	public void readSpesaNuova(JTextArea outputAreaSpese) {
		java.util.List<Spesa> spese = spesaCRUD.getAllSpesaNuova();
		outputAreaSpese.setText("");
		for(Spesa s : spese) {
			outputAreaSpese.append("ID: " + s.getIdSpesa() + ", Nome Spesa: " + s.getNomeSpesa()+ ", Importo: " + s.getImporto()+"€" + ", Data: " + s.getData() + ", Nome Categoria: "+ s.getCategoria().getNomeCategoria()+ ", Descrizione: " + s.getCategoria().getDescrizione() +"\n");
		}
	}
	
	//metodo per la visualizzazione report mensile
	public void visualizzaReportMensile(JTextArea outputAreaMensile) {
		Map<YearMonth, BigDecimal> reportMensile = spesaCRUD.generateReportMensile();
		outputAreaMensile.setText("");
		for (Map.Entry<YearMonth, BigDecimal> entry : reportMensile.entrySet()) {
			outputAreaMensile.append("Mese: " + entry.getKey() + " -- Totale spese: " + entry.getValue() + "\n" );
		}
	}
	//metodo per la visualizzazione report annuale
	public void visualizzaReportAnnuale(JTextArea outputAreaAnnuale) {
		Map<Integer, BigDecimal> reportAnnuale = spesaCRUD.generateReportAnnuale();
		outputAreaAnnuale.setText("");
		for (Map.Entry<Integer, BigDecimal> entry : reportAnnuale.entrySet()) {
			outputAreaAnnuale.append("Anno: " + entry.getKey() + " -- Totale spese: " + entry.getValue() + "\n");
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new SpesaApp().setVisible(true);
			}
		});

	}

}
