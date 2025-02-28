package gui.dialogs;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.Article;
import model.Document;
import model.Livre;
import model.Magazine;

import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

public class DocumentDetailsDialog {
    private final Dialog<Void> dialog;
    private final Document document;
    private final Consumer<Document> emprunterHandler;
    private final Consumer<Document> rendreHandler;

    public DocumentDetailsDialog(Document document, Consumer<Document> emprunterHandler,
                                 Consumer<Document> rendreHandler) {
        this.document = document;
        this.emprunterHandler = emprunterHandler;
        this.rendreHandler = rendreHandler;

        dialog = new Dialog<>();
        dialog.setTitle("Détails du Document");
        dialog.getDialogPane().setPrefSize(550, 400);
        configureDialogContent();
    }

    public void show() {
        dialog.showAndWait();
    }

    private void configureDialogContent() {
        // Appliquer les styles pour la boîte de dialogue
        dialog.getDialogPane().getStylesheets().add("data:text/css," +
                ".dialog-pane { -fx-background-color: white; -fx-padding: 20; }" +
                ".dialog-pane > .button-bar > .container { -fx-background-color: white; }" +
                ".label-header { -fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50; }");

        VBox content = new VBox(20);
        content.setPadding(new Insets(10));

        // En-tête avec titre et type
        content.getChildren().add(createHeader());

        // Statut de disponibilité
        content.getChildren().add(createStatusBox());

        // Grille d'informations détaillées
        content.getChildren().add(createInfoGrid());

        // Boutons d'action
        content.getChildren().add(createActionBox());

        dialog.getDialogPane().setContent(content);

        // Ajouter le bouton de fermeture
        ButtonType fermerButton = new ButtonType("Fermer", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(fermerButton);
    }

    private HBox createHeader() {
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);

        String typeDocument;
        String iconType;
        String colorType;

        if (document instanceof Livre) {
            typeDocument = "Livre";
            iconType = "📚";
            colorType = "#3498db";
        } else if (document instanceof Magazine) {
            typeDocument = "Magazine";
            iconType = "📰";
            colorType = "#2ecc71";
        } else if (document instanceof Article) {
            typeDocument = "Article";
            iconType = "📄";
            colorType = "#9b59b6";
        } else {
            typeDocument = "Document";
            iconType = "📝";
            colorType = "#95a5a6";
        }

        Label typeIcon = new Label(iconType);
        typeIcon.setStyle("-fx-font-size: 36px;");

        VBox titleBox = new VBox(5);

        Label titleLabel = new Label(document.titre);
        titleLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Label typeLabel = new Label(typeDocument);
        typeLabel.setStyle("-fx-background-color: " + colorType + "; -fx-text-fill: white; -fx-padding: 5 10; -fx-background-radius: 4;");

        titleBox.getChildren().addAll(titleLabel, typeLabel);
        header.getChildren().addAll(typeIcon, titleBox);

        return header;
    }

    private HBox createStatusBox() {
        HBox statusBox = new HBox(10);
        statusBox.setAlignment(Pos.CENTER_LEFT);
        statusBox.setPadding(new Insets(10, 0, 10, 0));
        statusBox.setStyle("-fx-background-color: " + (document.disponible ? "#e8f8f5" : "#fef5e7") + "; -fx-background-radius: 5;");

        Circle statusCircle = new Circle(8);
        statusCircle.setFill(document.disponible ? Color.web("#27ae60") : Color.web("#f39c12"));

        Label statusLabel = new Label(document.disponible ? "Disponible" : "Actuellement emprunté");
        statusLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: " + (document.disponible ? "#27ae60" : "#f39c12") + ";");

        statusBox.getChildren().addAll(statusCircle, statusLabel);

        return statusBox;
    }

    private GridPane createInfoGrid() {
        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(20);
        infoGrid.setVgap(15);
        infoGrid.setPadding(new Insets(15));
        infoGrid.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 5;");

        // Style pour les labels et valeurs
        String labelStyle = "-fx-font-weight: bold; -fx-text-fill: #7f8c8d;";
        String valueStyle = "-fx-font-weight: bold; -fx-text-fill: #2c3e50;";

        addDetailRow(infoGrid, 0, "Auteur", document.auteur, labelStyle, valueStyle);
        addDetailRow(infoGrid, 1, "Année de publication", String.valueOf(document.anneePublication), labelStyle, valueStyle);
        addDetailRow(infoGrid, 2, "Date d'ajout", document.dateInsertion.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")), labelStyle, valueStyle);

        // Ajouter les informations spécifiques
        int row = 3;
        if (document instanceof Livre) {
            Livre livre = (Livre) document;
            addDetailRow(infoGrid, row++, "Nombre de pages", String.valueOf(livre.nombrePage), labelStyle, valueStyle);
        } else if (document instanceof Magazine) {
            Magazine magazine = (Magazine) document;
            addDetailRow(infoGrid, row++, "Mois de publication", magazine.moisPubli, labelStyle, valueStyle);
        } else if (document instanceof Article) {
            Article article = (Article) document;
            addDetailRow(infoGrid, row++, "Journal", article.journal, labelStyle, valueStyle);
        }

        return infoGrid;
    }

    private HBox createActionBox() {
        HBox actionBox = new HBox(15);
        actionBox.setAlignment(Pos.CENTER);

        Button actionButton;
        if (document.disponible) {
            actionButton = new Button("Emprunter ce document");
            actionButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold;");
            actionButton.setOnAction(e -> {
                emprunterHandler.accept(document);
                dialog.close();
            });
        } else {
            actionButton = new Button("Rendre ce document");
            actionButton.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-font-weight: bold;");
            actionButton.setOnAction(e -> {
                rendreHandler.accept(document);
                dialog.close();
            });
        }

        actionBox.getChildren().add(actionButton);

        return actionBox;
    }

    // Méthode utilitaire pour ajouter une ligne de détail
    private void addDetailRow(GridPane grid, int row, String label, String value, String labelStyle, String valueStyle) {
        Label labelNode = new Label(label + ":");
        labelNode.setStyle(labelStyle);

        Label valueNode = new Label(value);
        valueNode.setStyle(valueStyle);

        grid.add(labelNode, 0, row);
        grid.add(valueNode, 1, row);
    }
}