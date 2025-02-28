package gui;

import gui.components.AddDocumentForm;
import gui.components.DocumentTableView;
import gui.utils.StyleManager;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Document;
import model.Livre;
import model.Magazine;
import model.Article;

public class BibliothequeGUI extends Application {

    // Liste pour stocker tous les documents
    private final ObservableList<Document> documents = FXCollections.observableArrayList();

    // Composants principaux
    private DocumentTableView documentTableView;
    private AddDocumentForm addDocumentForm;

    @Override
    public void start(Stage primaryStage) {
        // Initialisation des documents
        initialiserDocuments();

        // Création de l'interface
        BorderPane root = new BorderPane();

        // Barre de menu en haut
        VBox topPane = createTopPane();
        root.setTop(topPane);

        // Tableau des documents au centre
        documentTableView = new DocumentTableView(documents);
        root.setCenter(documentTableView.getComponent());

        // Formulaire de création en bas
        addDocumentForm = new AddDocumentForm(documents);
        root.setBottom(addDocumentForm.getComponent());

        // Configuration de la scène
        Scene scene = new Scene(root, 1000, 600);
        StyleManager.applyStyles(scene);
        primaryStage.setTitle("Gestion de Bibliothèque");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initialiserDocuments() {
        // Création des documents comme dans l'ancienne version
        documents.add(new Document("L'art subtil de séduire", "Mark Manson", 2019, true));
        documents.add(new Document("L'intelligence emotionnelle", "Ryan Andrews", 2014, true));
        documents.add(new Document("Plus Malin que le diable", "Napoléon Hill", 1938, false));
        documents.add(new Livre("Comment analyser les gens", "Robert Mercier", 2016, true, 256));
        documents.add(new Magazine("Tout est possible", "Mary Forleo", 2012, true, "Fevrier"));
        documents.add(new Article("L'avenir de l'IA", "Jean Dupont", 2023, true, "Journal of Computer Science"));
    }

    private VBox createTopPane() {
        VBox pane = new VBox(10);
        pane.setPadding(new Insets(20, 30, 10, 30));
        pane.setStyle("-fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setSpacing(15);

        // Icône de la bibliothèque
        Label iconLabel = new Label("📚");
        iconLabel.setStyle("-fx-font-size: 32px;");

        VBox titleBox = new VBox(5);
        Label titleLabel = new Label("Gestion de Bibliothèque");
        titleLabel.getStyleClass().add("title-label");

        Label subtitleLabel = new Label("Système de gestion documentaire");
        subtitleLabel.getStyleClass().add("subtitle");

        titleBox.getChildren().addAll(titleLabel, subtitleLabel);

        // Barre de recherche à droite
        TextField searchField = new TextField();
        searchField.setPromptText("Rechercher un document...");
        searchField.setPrefWidth(250);

        Button searchButton = new Button("Rechercher");
        searchButton.getStyleClass().add("action-button");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        headerBox.getChildren().addAll(iconLabel, titleBox, spacer, searchField, searchButton);

        pane.getChildren().add(headerBox);
        return pane;
    }

    public static void main(String[] args) {
        launch(args);
    }
}