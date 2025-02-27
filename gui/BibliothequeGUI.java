package gui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Document;
import model.Livre;
import model.Magazine;
import model.Article;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class BibliothequeGUI extends Application {

    // Liste pour stocker tous les documents
    private final ObservableList<Document> documents = FXCollections.observableArrayList();
    
    // Composants d'interface
    private ListView<String> documentsListView;
    private TextArea outputArea;
    private TextField titreField, auteurField, anneeField, disponibleField, dateInsertionField;
    private TextField nombrePagesField, moisPubliField, journalField;
    private TextField rechercheField;
    
    @Override
    public void start(Stage primaryStage) {
        // Initialisation des documents
        initialiserDocuments();
        
        // Création de l'interface
        BorderPane root = new BorderPane();
        
        // Section gauche: Liste des documents
        VBox leftPane = createLeftPane();
        root.setLeft(leftPane);
        
        // Section centrale: Détails et actions
        VBox centerPane = createCenterPane();
        root.setCenter(centerPane);
        
        // Section droite: Zone de sortie
        VBox rightPane = createRightPane();
        root.setRight(rightPane);
        
        // Configuration de la scène
        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setTitle("Gestion de Bibliothèque");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Charger la liste initiale
        updateDocumentsList();
    }
    
    private void initialiserDocuments() {
        // Création des documents comme dans Main.java
        documents.add(new Document("L'art subtil de séduire", "Mark Manson", 2019, true));
        documents.add(new Document("L'intelligence emotionnelle", "Ryan Andrews", 2014, true));
        documents.add(new Document("Plus Malin que le diable", "Napoléon Hill", 1938, false));
        documents.add(new Livre("Comment analyser les gens", "Robert Mercier", 2016, true, 256));
        documents.add(new Magazine("Tout est possible", "Mary Forleo", 2012, true, "Fevrier"));
        documents.add(new Article("L'avenir de l'IA", "Jean Dupont", 2023, true, "Journal of Computer Science"));
    }
    
    private VBox createLeftPane() {
        VBox pane = new VBox(10);
        pane.setPadding(new Insets(10));
        pane.setPrefWidth(250);
        
        Label titleLabel = new Label("Documents");
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
        
        documentsListView = new ListView<>();
        documentsListView.setPrefHeight(500);
        documentsListView.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> onDocumentSelected(newVal));
        
        pane.getChildren().addAll(titleLabel, documentsListView);
        return pane;
    }
    
    private VBox createCenterPane() {
        VBox pane = new VBox(15);
        pane.setPadding(new Insets(10));
        pane.setPrefWidth(350);
        
        // Section des détails du document
        TitledPane detailsPane = createDetailsPane();
        
        // Section des actions
        TitledPane actionsPane = createActionsPane();
        
        // Section pour rechercher et supprimer des documents
        TitledPane recherchePane = createRecherchePane();
        
        // Section pour créer de nouveaux documents
        TitledPane creationPane = createCreationPane();
        
        pane.getChildren().addAll(detailsPane, actionsPane, recherchePane, creationPane);
        return pane;
    }
    
    private TitledPane createDetailsPane() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        
        // Champs pour les détails
        titreField = new TextField();
        titreField.setEditable(false);
        auteurField = new TextField();
        auteurField.setEditable(false);
        anneeField = new TextField();
        anneeField.setEditable(false);
        disponibleField = new TextField();
        disponibleField.setEditable(false);
        dateInsertionField = new TextField(); // Nouveau champ
        dateInsertionField.setEditable(false);
        nombrePagesField = new TextField();
        nombrePagesField.setEditable(false);
        moisPubliField = new TextField();
        moisPubliField.setEditable(false);
        journalField = new TextField();
        journalField.setEditable(false);
        
        // Ajout des champs à la grille
        int row = 0;
        grid.add(new Label("Titre:"), 0, row);
        grid.add(titreField, 1, row++);
        grid.add(new Label("Auteur:"), 0, row);
        grid.add(auteurField, 1, row++);
        grid.add(new Label("Année:"), 0, row);
        grid.add(anneeField, 1, row++);
        grid.add(new Label("Disponible:"), 0, row);
        grid.add(disponibleField, 1, row++);
        grid.add(new Label("Date d'insertion:"), 0, row); // Nouveau label
        grid.add(dateInsertionField, 1, row++);
        grid.add(new Label("Nombre de pages:"), 0, row);
        grid.add(nombrePagesField, 1, row++);
        grid.add(new Label("Mois de publication:"), 0, row);
        grid.add(moisPubliField, 1, row++);
        grid.add(new Label("Journal:"), 0, row);
        grid.add(journalField, 1, row++);
        
        // Configuration des colonnes
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(40);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(60);
        grid.getColumnConstraints().addAll(col1, col2);
        
        TitledPane titledPane = new TitledPane("Détails du document", grid);
        titledPane.setCollapsible(false);
        return titledPane;
    }
    
    private TitledPane createActionsPane() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        
        Button afficherInfosBtn = new Button("Afficher les informations");
        afficherInfosBtn.setMaxWidth(Double.MAX_VALUE);
        afficherInfosBtn.setOnAction(e -> executeAfficherInfos());
        
        Button emprunterBtn = new Button("Emprunter le document");
        emprunterBtn.setMaxWidth(Double.MAX_VALUE);
        emprunterBtn.setOnAction(e -> executeEmprunter());
        
        Button rendreBtn = new Button("Rendre le document");
        rendreBtn.setMaxWidth(Double.MAX_VALUE);
        rendreBtn.setOnAction(e -> executeRendre());
        
        vbox.getChildren().addAll(afficherInfosBtn, emprunterBtn, rendreBtn);
        
        TitledPane titledPane = new TitledPane("Actions", vbox);
        titledPane.setCollapsible(false);
        return titledPane;
    }
    
    private TitledPane createRecherchePane() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        
        // Champ de recherche
        rechercheField = new TextField();
        rechercheField.setPromptText("Titre du document");
        
        // Bouton de recherche
        Button rechercherBtn = new Button("Rechercher");
        rechercherBtn.setMaxWidth(Double.MAX_VALUE);
        rechercherBtn.setOnAction(e -> executeRechercher());
        
        // Bouton de suppression
        Button supprimerBtn = new Button("Supprimer");
        supprimerBtn.setMaxWidth(Double.MAX_VALUE);
        supprimerBtn.setOnAction(e -> executeSupprimer());
        
        vbox.getChildren().addAll(rechercheField, rechercherBtn, supprimerBtn);
        
        TitledPane titledPane = new TitledPane("Rechercher/Supprimer un document", vbox);
        titledPane.setCollapsible(false);
        return titledPane;
    }
    
    private TitledPane createCreationPane() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        
        ComboBox<String> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll("Document", "Livre", "Magazine", "Article");
        typeComboBox.setValue("Document");
        
        TextField newTitreField = new TextField();
        newTitreField.setPromptText("Titre");
        TextField newAuteurField = new TextField();
        newAuteurField.setPromptText("Auteur");
        TextField newAnneeField = new TextField();
        newAnneeField.setPromptText("Année de publication");
        
        // Champs spécifiques qui apparaissent selon le type
        TextField newNombrePagesField = new TextField();
        newNombrePagesField.setPromptText("Nombre de pages");
        TextField newMoisPubliField = new TextField();
        newMoisPubliField.setPromptText("Mois de publication");
        TextField newJournalField = new TextField();
        newJournalField.setPromptText("Nom du journal");
        
        // Afficher/cacher les champs spécifiques selon le type sélectionné
        VBox specificFields = new VBox(10);
        typeComboBox.setOnAction(e -> {
            specificFields.getChildren().clear();
            switch (typeComboBox.getValue()) {
                case "Livre":
                    specificFields.getChildren().add(newNombrePagesField);
                    break;
                case "Magazine":
                    specificFields.getChildren().add(newMoisPubliField);
                    break;
                case "Article":
                    specificFields.getChildren().add(newJournalField);
                    break;
            }
        });
        
        Button createBtn = new Button("Créer");
        createBtn.setMaxWidth(Double.MAX_VALUE);
        createBtn.setOnAction(e -> {
            try {
                String titre = newTitreField.getText();
                String auteur = newAuteurField.getText();
                int annee = Integer.parseInt(newAnneeField.getText());

                Document newDoc = null;
                switch (typeComboBox.getValue()) {
                    case "Document":
                        newDoc = new Document(titre, auteur, annee, true);
                        break;
                    case "Livre":
                        int nombrePages = Integer.parseInt(newNombrePagesField.getText());
                        newDoc = new Livre(titre, auteur, annee, true, nombrePages);
                        break;
                    case "Magazine":
                        String moisPubli = newMoisPubliField.getText();
                        newDoc = new Magazine(titre, auteur, annee, true, moisPubli);
                        break;
                    case "Article":
                        String journal = newJournalField.getText();
                        newDoc = new Article(titre, auteur, annee, true, journal);
                        break;
                }

                if (newDoc != null) {
                    createDocument(newDoc); // Utilise la nouvelle méthode de création

                    // Réinitialiser les champs
                    newTitreField.clear();
                    newAuteurField.clear();
                    newAnneeField.clear();
                    newNombrePagesField.clear();
                    newMoisPubliField.clear();
                    newJournalField.clear();
                }
            } catch (NumberFormatException ex) {
                log("Erreur: Veuillez entrer des valeurs numériques valides pour l'année et le nombre de pages.");
            }
        });
        vbox.getChildren().addAll(
            new Label("Type de document:"), 
            typeComboBox,
            newTitreField, 
            newAuteurField, 
            newAnneeField,
            specificFields,
            createBtn
        );
        
        TitledPane titledPane = new TitledPane("Créer un nouveau document", vbox);
        titledPane.setCollapsible(true);
        titledPane.setExpanded(false);
        return titledPane;
    }
    
    private VBox createRightPane() {
        VBox pane = new VBox(10);
        pane.setPadding(new Insets(10));
        pane.setPrefWidth(350);
        
        Label titleLabel = new Label("Sortie");
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
        
        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setPrefHeight(500);
        
        Button clearBtn = new Button("Effacer");
        clearBtn.setOnAction(e -> outputArea.clear());
        
        pane.getChildren().addAll(titleLabel, outputArea, clearBtn);
        return pane;
    }
    
    private void updateDocumentsList() {
        documentsListView.getItems().clear();
        for (Document doc : documents) {
            String displayText = doc.titre;
            if (doc instanceof Livre) {
                displayText += " (Livre)";
            } else if (doc instanceof Magazine) {
                displayText += " (Magazine)";
            } else if (doc instanceof Article) {
                displayText += " (Article)";
            } else {
                displayText += " (Document)";
            }
            documentsListView.getItems().add(displayText);
        }
    }
    
    private void onDocumentSelected(String selectedItem) {
        if (selectedItem == null) return;
        
        int index = documentsListView.getSelectionModel().getSelectedIndex();
        if (index >= 0 && index < documents.size()) {
            Document doc = documents.get(index);
            
            // Réinitialiser tous les champs spécifiques
            nombrePagesField.setText("");
            moisPubliField.setText("");
            journalField.setText("");
            
            // Afficher les informations de base
            titreField.setText(doc.titre);
            auteurField.setText(doc.auteur);
            anneeField.setText(String.valueOf(doc.anneePublication));
            disponibleField.setText(doc.disponible ? "Oui" : "Non");
            
            // Afficher la date d'insertion
            dateInsertionField.setText(doc.dateInsertion.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            
            // Afficher les informations spécifiques
            if (doc instanceof Livre) {
                Livre livre = (Livre) doc;
                nombrePagesField.setText(String.valueOf(livre.nombrePage));
            } else if (doc instanceof Magazine) {
                Magazine magazine = (Magazine) doc;
                moisPubliField.setText(magazine.moisPubli);
            } else if (doc instanceof Article) {
                Article article = (Article) doc;
                journalField.setText(article.journal);
            }
        }
    }
    
    private void executeAfficherInfos() {
        int index = documentsListView.getSelectionModel().getSelectedIndex();
        if (index >= 0 && index < documents.size()) {
            Document doc = documents.get(index);
            
            ConsoleCapture.captureOutput(() -> doc.afficherinfos());
            log(ConsoleCapture.getOutput());
        } else {
            log("Veuillez sélectionner un document.");
        }
    }
    
    private void executeEmprunter() {
        int index = documentsListView.getSelectionModel().getSelectedIndex();
        if (index >= 0 && index < documents.size()) {
            Document doc = documents.get(index);
            
            ConsoleCapture.captureOutput(() -> doc.emprunterDocument());
            log(ConsoleCapture.getOutput());
            
            // Mettre à jour l'affichage
            disponibleField.setText(doc.disponible ? "Oui" : "Non");
        } else {
            log("Veuillez sélectionner un document.");
        }
    }
    
    private void executeRendre() {
        int index = documentsListView.getSelectionModel().getSelectedIndex();
        if (index >= 0 && index < documents.size()) {
            Document doc = documents.get(index);
            
            ConsoleCapture.captureOutput(() -> doc.rendreDocument());
            log(ConsoleCapture.getOutput());
            
            // Mettre à jour l'affichage
            disponibleField.setText(doc.disponible ? "Oui" : "Non");
        } else {
            log("Veuillez sélectionner un document.");
        }
    }
    
    // Méthode pour exécuter la recherche
    private void executeRechercher() {
        String titreRecherche = rechercheField.getText().trim();
        if (titreRecherche.isEmpty()) {
            log("Veuillez entrer un titre à rechercher.");
            return;
        }
        
        // Convertir ObservableList en ArrayList pour la méthode de recherche
        ArrayList<Document> docList = new ArrayList<>(documents);
        
        // Utiliser la méthode statique rechercherDocument de la classe Document
        ConsoleCapture.captureOutput(() -> Document.rechercherDocument(docList, titreRecherche));
        log(ConsoleCapture.getOutput());
        
        // Sélectionner le document dans la liste s'il a été trouvé
        for (int i = 0; i < documents.size(); i++) {
            if (documents.get(i).titre.equals(titreRecherche)) {
                documentsListView.getSelectionModel().select(i);
                documentsListView.scrollTo(i);
                break;
            }
        }
    }
    private void createDocument(Document doc) {
        documents.add(doc); // Ajoute le document à la liste ObservableList
        updateDocumentsList(); // Actualise la liste des documents affichée dans l'interface
        log("Document créé: " + doc.titre + " (Date d'insertion: " +
                doc.dateInsertion.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ")");
    }
    // Méthode pour exécuter la suppression
    private void executeSupprimer() {
        String titreSupprimer = rechercheField.getText().trim();
        if (titreSupprimer.isEmpty()) {
            // Si le champ est vide, utiliser le document sélectionné
            int index = documentsListView.getSelectionModel().getSelectedIndex();
            if (index >= 0 && index < documents.size()) {
                titreSupprimer = documents.get(index).titre;
            } else {
                log("Veuillez sélectionner un document ou entrer un titre à supprimer.");
                return;
            }
        }
        
        // Convertir ObservableList en ArrayList pour la méthode de suppression
        ArrayList<Document> docList = new ArrayList<>(documents);
        
        // Utiliser la méthode statique supprimerDocument de la classe Document
        String finalTitreSupprimer = titreSupprimer;
        ConsoleCapture.captureOutput(() -> Document.supprimerDocument(docList, finalTitreSupprimer));
        String result = ConsoleCapture.getOutput();
        log(result);
        
        // Si le document a été supprimé, mettre à jour notre ObservableList
        if (result.contains("a été supprimé")) {
            // Mettre à jour la liste des documents
            for (int i = 0; i < documents.size(); i++) {
                if (documents.get(i).titre.equals(titreSupprimer)) {
                    documents.remove(i);
                    break;
                }
            }
            updateDocumentsList();
            
            // Réinitialiser les champs de détails
            titreField.clear();
            auteurField.clear();
            anneeField.clear();
            disponibleField.clear();
            dateInsertionField.clear();
            nombrePagesField.clear();
            moisPubliField.clear();
            journalField.clear();
        }
    }
    
    private void log(String message) {
        outputArea.appendText(message + "\n");
    }
}