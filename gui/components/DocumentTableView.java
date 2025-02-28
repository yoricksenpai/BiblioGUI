package gui.components;

import gui.dialogs.DocumentDetailsDialog;
import gui.utils.NotificationManager;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.Document;
import model.Livre;
import model.Magazine;
import model.Article;

import java.util.Optional;

public class DocumentTableView {
    private final VBox component;
    private final TableView<Document> documentsTableView;
    private final ObservableList<Document> documents;

    public DocumentTableView(ObservableList<Document> documents) {
        this.documents = documents;
        this.component = new VBox(15);
        this.component.setPadding(new Insets(20));

        // Création des éléments d'interface
        this.documentsTableView = new TableView<>();
        setupTableView();

        this.component.getChildren().addAll(createHeaderBox(), documentsTableView);
        VBox.setVgrow(documentsTableView, Priority.ALWAYS);
    }

    public Node getComponent() {
        return component;
    }

    private HBox createHeaderBox() {
        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setSpacing(15);

        Label tableTitle = new Label("Liste des Documents");
        tableTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 18; -fx-text-fill: #2c3e50;");

        Label countLabel = new Label("0 documents");
        countLabel.setStyle("-fx-text-fill: #7f8c8d;");
        documents.addListener((ListChangeListener<Document>) c -> {
            countLabel.setText(documents.size() + " document" + (documents.size() > 1 ? "s" : ""));
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        ComboBox<String> filterCombo = new ComboBox<>();
        filterCombo.getItems().addAll("Tous", "Disponibles", "Empruntés", "Livres", "Magazines", "Articles");
        filterCombo.setValue("Tous");
        filterCombo.setPromptText("Filtrer par...");

        headerBox.getChildren().addAll(tableTitle, countLabel, spacer, filterCombo);
        return headerBox;
    }

    private void setupTableView() {
        documentsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        documentsTableView.getStyleClass().add("table-view");

        // Colonnes du tableau
        TableColumn<Document, String> titreCol = createTitreColumn();
        TableColumn<Document, String> auteurCol = createAuteurColumn();
        TableColumn<Document, Integer> anneeCol = createAnneeColumn();
        TableColumn<Document, String> typeCol = createTypeColumn();
        TableColumn<Document, Boolean> disponibleCol = createDisponibleColumn();
        TableColumn<Document, Void> actionsCol = createActionsColumn();

        // Ajouter les colonnes au tableau
        documentsTableView.getColumns().addAll(titreCol, auteurCol, anneeCol, typeCol, disponibleCol, actionsCol);

        // Définir les items du tableau
        documentsTableView.setItems(documents);
    }

    // Méthodes de création de colonnes
    private TableColumn<Document, String> createTitreColumn() {
        TableColumn<Document, String> titreCol = new TableColumn<>("Titre");
        titreCol.setCellValueFactory(cellData -> {
            Document doc = cellData.getValue();
            return Bindings.createStringBinding(() -> doc.titre);
        });
        return titreCol;
    }

    private TableColumn<Document, String> createAuteurColumn() {
        TableColumn<Document, String> auteurCol = new TableColumn<>("Auteur");
        auteurCol.setCellValueFactory(cellData -> {
            Document doc = cellData.getValue();
            return Bindings.createStringBinding(() -> doc.auteur);
        });
        return auteurCol;
    }

    private TableColumn<Document, Integer> createAnneeColumn() {
        TableColumn<Document, Integer> anneeCol = new TableColumn<>("Année");
        anneeCol.setCellValueFactory(cellData -> {
            Document doc = cellData.getValue();
            return Bindings.createObjectBinding(() -> doc.anneePublication);
        });
        return anneeCol;
    }

    private TableColumn<Document, String> createTypeColumn() {
        TableColumn<Document, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(cellData -> {
            Document doc = cellData.getValue();
            String type;
            if (doc instanceof Livre) type = "Livre";
            else if (doc instanceof Magazine) type = "Magazine";
            else if (doc instanceof Article) type = "Article";
            else {
                type = "Document";
            }
            return Bindings.createStringBinding(() -> type);
        });

        // Cellule colorée pour les types
        typeCol.setCellFactory(col -> new TableCell<Document, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("");
                } else {
                    setText(item);

                    // Styliser selon le type
                    String bgColor;
                    String textColor = "white";

                    switch (item) {
                        case "Livre":
                            bgColor = "#3498db"; // bleu
                            break;
                        case "Magazine":
                            bgColor = "#2ecc71"; // vert
                            break;
                        case "Article":
                            bgColor = "#9b59b6"; // violet
                            break;
                        default:
                            bgColor = "#95a5a6"; // gris
                            break;
                    }

                    setStyle("-fx-background-color: " + bgColor + "; -fx-text-fill: " + textColor + "; -fx-padding: 5 10; -fx-background-radius: 4;");
                    setAlignment(Pos.CENTER);
                }
            }
        });

        return typeCol;
    }

    private TableColumn<Document, Boolean> createDisponibleColumn() {
        TableColumn<Document, Boolean> disponibleCol = new TableColumn<>("Disponible");
        disponibleCol.setCellValueFactory(cellData -> {
            Document doc = cellData.getValue();
            return Bindings.createObjectBinding(() -> doc.disponible);
        });

        // Cellule avec indicateur visuel pour disponibilité
        disponibleCol.setCellFactory(col -> new TableCell<Document, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("");
                } else {
                    HBox box = new HBox(5);
                    box.setAlignment(Pos.CENTER);

                    Circle circle = new Circle(5);
                    circle.setFill(item ? Color.GREEN : Color.RED);

                    Label label = new Label(item ? "Disponible" : "Emprunté");
                    label.setStyle("-fx-text-fill: " + (item ? "#27ae60" : "#e74c3c") + ";");

                    box.getChildren().addAll(circle, label);
                    setGraphic(box);
                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                }
            }
        });

        return disponibleCol;
    }

    private TableColumn<Document, Void> createActionsColumn() {
        TableColumn<Document, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setCellFactory(param -> new TableCell<Document, Void>() {
            private final HBox actionsPane = new HBox(8);

            private final Button detailsBtn = createButton("📖", "Détails", "#3498db");
            private final Button emprunterBtn = createButton("📤", "Emprunter", "#2ecc71");
            private final Button rendreBtn = createButton("📥", "Rendre", "#f39c12");
            private final Button supprimerBtn = createButton("🗑️", "Supprimer", "#e74c3c");

            private Button createButton(String icon, String tooltip, String color) {
                Button btn = new Button(icon);
                btn.setTooltip(new Tooltip(tooltip));
                btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-size: 14px; -fx-min-width: 30px; -fx-min-height: 30px; -fx-max-width: 30px; -fx-max-height: 30px; -fx-background-radius: 15px;");
                return btn;
            }

            {
                // Configurer les boutons
                detailsBtn.setOnAction(event -> {
                    Document doc = getTableView().getItems().get(getIndex());
                    new DocumentDetailsDialog(doc, DocumentTableView.this::emprunterDocument, DocumentTableView.this::rendreDocument).show();
                });

                emprunterBtn.setOnAction(event -> {
                    Document doc = getTableView().getItems().get(getIndex());
                    emprunterDocument(doc);
                });

                rendreBtn.setOnAction(event -> {
                    Document doc = getTableView().getItems().get(getIndex());
                    rendreDocument(doc);
                });

                supprimerBtn.setOnAction(event -> {
                    Document doc = getTableView().getItems().get(getIndex());
                    supprimerDocument(doc);
                });

                // Ajouter les boutons au conteneur
                actionsPane.getChildren().addAll(detailsBtn, emprunterBtn, rendreBtn, supprimerBtn);
                actionsPane.setAlignment(Pos.CENTER);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Document doc = getTableView().getItems().get(getIndex());

                    // Activer/désactiver boutons selon disponibilité
                    emprunterBtn.setDisable(!doc.disponible);
                    rendreBtn.setDisable(doc.disponible);

                    setGraphic(actionsPane);
                }
            }
        });

        return actionsCol;
    }

    // Méthodes d'actions sur les documents
    public void emprunterDocument(Document document) {
        if (!document.disponible) {
            NotificationManager.showNotification("Document non disponible",
                    "Ce document n'est pas disponible pour l'emprunt.", "warning");
            return;
        }

        document.emprunterDocument();
        documentsTableView.refresh();

        NotificationManager.showNotification("Emprunt réussi",
                "Le document \"" + document.titre + "\" a été emprunté avec succès.", "success");
    }

    public void rendreDocument(Document document) {
        if (document.disponible) {
            NotificationManager.showNotification("Document déjà disponible",
                    "Ce document n'a pas été emprunté ou a déjà été rendu.", "warning");
            return;
        }

        document.rendreDocument();
        documentsTableView.refresh();

        NotificationManager.showNotification("Retour réussi",
                "Le document \"" + document.titre + "\" a été rendu avec succès.", "success");
    }

    private void supprimerDocument(Document document) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmation de suppression");
        confirmAlert.setHeaderText("Êtes-vous sûr de vouloir supprimer ce document ?");
        confirmAlert.setContentText("Titre: " + document.titre);

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            documents.remove(document);

            NotificationManager.showNotification("Suppression réussie",
                    "Le document \"" + document.titre + "\" a été supprimé avec succès.", "error");
        }
    }
}