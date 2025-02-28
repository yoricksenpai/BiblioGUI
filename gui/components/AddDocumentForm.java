package gui.components;

import gui.utils.NotificationManager;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.Document;
import model.Livre;
import model.Magazine;
import model.Article;

public class AddDocumentForm {
    private final TitledPane component;
    private final ObservableList<Document> documents;

    // Champs du formulaire
    private TextField newTitreField, newAuteurField, newAnneeField;
    private TextField newNombrePagesField, newMoisPubliField, newJournalField;
    private ComboBox<String> typeComboBox;

    public AddDocumentForm(ObservableList<Document> documents) {
        this.documents = documents;
        VBox pane = new VBox(10);
        pane.setPadding(new Insets(10));

        component = createFormPane();
        pane.getChildren().add(component);
    }

    public Node getComponent() {
        return component;
    }

    private TitledPane createFormPane() {
        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));
        vbox.setStyle("-fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        // Section type de document
        HBox typeBox = createTypeSelectionBox();

        // Formulaire avec champs
        GridPane formGrid = createFormGrid();

        // Champs spécifiques
        GridPane specificGrid = createSpecificFieldsGrid();

        // Bouton de création
        Button createBtn = new Button("Créer le document");
        createBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 5;");
        createBtn.setMaxWidth(Double.MAX_VALUE);
        createBtn.setOnAction(e -> createDocument());

        vbox.getChildren().addAll(typeBox, formGrid, specificGrid, createBtn);

        TitledPane titledPane = new TitledPane("Ajouter un nouveau document", vbox);
        titledPane.setCollapsible(true);
        titledPane.setStyle("-fx-font-weight: bold;");

        return titledPane;
    }

    private HBox createTypeSelectionBox() {
        HBox typeBox = new HBox(10);
        typeBox.setAlignment(Pos.CENTER_LEFT);

        Label typeLabel = new Label("Type de document:");
        typeLabel.setStyle("-fx-font-weight: bold;");

        typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll("Document", "Livre", "Magazine", "Article");
        typeComboBox.setValue("Document");
        typeComboBox.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(typeComboBox, Priority.ALWAYS);

        // Configuration des cellules avec icônes
        typeComboBox.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    String icon = "";
                    switch (item) {
                        case "Livre": icon = "📚 "; break;
                        case "Magazine": icon = "📰 "; break;
                        case "Article": icon = "📄 "; break;
                        default: icon = "📝 "; break;
                    }
                    setText(icon + item);
                }
            }
        });

        // Configuration pour la valeur sélectionnée
        typeComboBox.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    String icon = "";
                    switch (item) {
                        case "Livre": icon = "📚 "; break;
                        case "Magazine": icon = "📰 "; break;
                        case "Article": icon = "📄 "; break;
                        default: icon = "📝 "; break;
                    }
                    setText(icon + item);
                }
            }
        });

        typeBox.getChildren().addAll(typeLabel, typeComboBox);
        return typeBox;
    }

    private GridPane createFormGrid() {
        GridPane formGrid = new GridPane();
        formGrid.setHgap(15);
        formGrid.setVgap(15);
        formGrid.setPadding(new Insets(20, 0, 20, 0));

        // Style pour les labels
        String labelStyle = "-fx-font-weight: bold;";

        Label titreLabel = new Label("Titre:");
        titreLabel.setStyle(labelStyle);

        newTitreField = createStyledTextField("Saisissez le titre du document");

        Label auteurLabel = new Label("Auteur:");
        auteurLabel.setStyle(labelStyle);

        newAuteurField = createStyledTextField("Nom de l'auteur");

        Label anneeLabel = new Label("Année:");
        anneeLabel.setStyle(labelStyle);

        newAnneeField = createStyledTextField("Année de publication");

        formGrid.add(titreLabel, 0, 0);
        formGrid.add(newTitreField, 1, 0);
        formGrid.add(auteurLabel, 0, 1);
        formGrid.add(newAuteurField, 1, 1);
        formGrid.add(anneeLabel, 0, 2);
        formGrid.add(newAnneeField, 1, 2);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(20);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(80);
        formGrid.getColumnConstraints().addAll(col1, col2);

        return formGrid;
    }

    private GridPane createSpecificFieldsGrid() {
        GridPane specificGrid = new GridPane();
        specificGrid.setHgap(15);
        specificGrid.setVgap(15);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(20);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(80);
        specificGrid.getColumnConstraints().addAll(col1, col2);

        newNombrePagesField = createStyledTextField("Nombre de pages");
        newMoisPubliField = createStyledTextField("Ex: Janvier, Février, etc.");
        newJournalField = createStyledTextField("Nom du journal ou de la revue");

        // Label dynamique
        final Label[] dynamicLabel = {new Label()};
        dynamicLabel[0].setStyle("-fx-font-weight: bold;");

        // Gérer l'affichage des champs spécifiques selon le type sélectionné
        typeComboBox.setOnAction(e -> {
            specificGrid.getChildren().clear();

            String selectedType = typeComboBox.getValue();
            switch (selectedType) {
                case "Livre":
                    dynamicLabel[0].setText("Nombre de pages:");
                    specificGrid.add(dynamicLabel[0], 0, 0);
                    specificGrid.add(newNombrePagesField, 1, 0);
                    break;
                case "Magazine":
                    dynamicLabel[0].setText("Mois de publication:");
                    specificGrid.add(dynamicLabel[0], 0, 0);
                    specificGrid.add(newMoisPubliField, 1, 0);
                    break;
                case "Article":
                    dynamicLabel[0].setText("Journal:");
                    specificGrid.add(dynamicLabel[0], 0, 0);
                    specificGrid.add(newJournalField, 1, 0);
                    break;
            }
        });

        return specificGrid;
    }

    private TextField createStyledTextField(String promptText) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        textField.setStyle("-fx-padding: 8; -fx-background-radius: 5;");
        return textField;
    }

    private void createDocument() {
        try {
            // Récupération des valeurs de base
            String titre = newTitreField.getText().trim();
            String auteur = newAuteurField.getText().trim();

            // Validation des champs requis
            if (titre.isEmpty() || auteur.isEmpty() || newAnneeField.getText().trim().isEmpty()) {
                NotificationManager.showNotification("Champs manquants",
                        "Veuillez remplir tous les champs obligatoires.", "error");
                return;
            }

            // Vérification si un document avec le même titre existe déjà
            for (Document doc : documents) {
                if (doc.titre.equalsIgnoreCase(titre)) {
                    NotificationManager.showNotification("Titre en double",
                            "Un document avec ce titre existe déjà dans la bibliothèque.", "error");
                    return;
                }
            }

            // Conversion et validation de l'année
            int annee;
            try {
                annee = Integer.parseInt(newAnneeField.getText().trim());
                if (annee <= 0) {
                    NotificationManager.showNotification("Année invalide",
                            "L'année de publication doit être un nombre positif.", "error");
                    return;
                }
            } catch (NumberFormatException e) {
                NotificationManager.showNotification("Année invalide",
                        "L'année de publication doit être un nombre entier.", "error");
                return;
            }

            // Création du document selon le type
            Document newDoc = null;
            String type = typeComboBox.getValue();

            switch (type) {
                case "Document":
                    newDoc = new Document(titre, auteur, annee, true);
                    break;

                case "Livre":
                    String nbPagesStr = newNombrePagesField.getText().trim();
                    if (nbPagesStr.isEmpty()) {
                        NotificationManager.showNotification("Champ manquant",
                                "Veuillez indiquer le nombre de pages du livre.", "error");
                        return;
                    }

                    try {
                        int nbPages = Integer.parseInt(nbPagesStr);
                        if (nbPages <= 0) {
                            NotificationManager.showNotification("Nombre de pages invalide",
                                    "Le nombre de pages doit être positif.", "error");
                            return;
                        }
                        newDoc = new Livre(titre, auteur, annee, true, nbPages);
                    } catch (NumberFormatException e) {
                        NotificationManager.showNotification("Nombre de pages invalide",
                                "Le nombre de pages doit être un nombre entier.", "error");
                        return;
                    }
                    break;

                case "Magazine":
                    String moisPubli = newMoisPubliField.getText().trim();
                    if (moisPubli.isEmpty()) {
                        NotificationManager.showNotification("Champ manquant",
                                "Veuillez indiquer le mois de publication du magazine.", "error");
                        return;
                    }
                    newDoc = new Magazine(titre, auteur, annee, true, moisPubli);
                    break;

                case "Article":
                    String journal = newJournalField.getText().trim();
                    if (journal.isEmpty()) {
                        NotificationManager.showNotification("Champ manquant",
                                "Veuillez indiquer le nom du journal de l'article.", "error");
                        return;
                    }
                    newDoc = new Article(titre, auteur, annee, true, journal);
                    break;
            }

            // Ajout du document à la liste
            if (newDoc != null) {
                documents.add(newDoc);

                // Réinitialiser les champs
                newTitreField.clear();
                newAuteurField.clear();
                newAnneeField.clear();
                newNombrePagesField.clear();
                newMoisPubliField.clear();
                newJournalField.clear();

                NotificationManager.showNotification("Document créé",
                        "Le document \"" + newDoc.titre + "\" a été créé avec succès.", "success");
            }

        } catch (Exception e) {
            NotificationManager.showNotification("Erreur",
                    "Une erreur s'est produite lors de la création du document: " + e.getMessage(), "error");
        }
    }
}