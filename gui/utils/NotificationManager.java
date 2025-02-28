package gui.utils;

import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class NotificationManager {

    // Types de notifications
    public static final String SUCCESS = "success";
    public static final String WARNING = "warning";
    public static final String ERROR = "error";
    public static final String INFO = "info";

    // Durée d'affichage par défaut en secondes (statique)
    private static double displayDuration = 5.0;

    public NotificationManager() {
        this(5.0); // Utilise la durée par défaut
    }

    public NotificationManager(double displayDuration) {
        NotificationManager.displayDuration = displayDuration;
    }

    /**
     * Affiche une notification avec le titre, le message et le type spécifiés
     */
    public static void showNotification(String title, String message, String type) {
        Stage notificationStage = new Stage();
        notificationStage.initStyle(StageStyle.UNDECORATED);
        notificationStage.setAlwaysOnTop(true);

        VBox content = new VBox(10);
        content.setPadding(new Insets(15));
        content.setAlignment(Pos.CENTER_LEFT);

        String bgColor, iconText;
        switch (type) {
            case SUCCESS:
                bgColor = "#d4edda";
                iconText = "✅";
                break;
            case WARNING:
                bgColor = "#fff3cd";
                iconText = "⚠️";
                break;
            case ERROR:
                bgColor = "#f8d7da";
                iconText = "❌";
                break;
            default: // INFO
                bgColor = "#d1ecf1";
                iconText = "ℹ️";
                break;
        }

        content.setStyle("-fx-background-color: " + bgColor + "; " +
                "-fx-background-radius: 5; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        HBox headerBox = new HBox(10);
        headerBox.setAlignment(Pos.CENTER_LEFT);

        Label iconLabel = new Label(iconText);
        iconLabel.setStyle("-fx-font-size: 24px;");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button closeButton = new Button("×");
        closeButton.setStyle("-fx-background-color: transparent; -fx-font-size: 18px;");
        closeButton.setOnAction(e -> notificationStage.close());

        headerBox.getChildren().addAll(iconLabel, titleLabel, spacer, closeButton);

        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(350);

        content.getChildren().addAll(headerBox, messageLabel);

        Scene scene = new Scene(content);
        notificationStage.setScene(scene);

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        notificationStage.setX(screenBounds.getMaxX() - 400);
        notificationStage.setY(screenBounds.getMaxY() - 150);

        PauseTransition delay = new PauseTransition(Duration.seconds(displayDuration));
        delay.setOnFinished(e -> notificationStage.close());
        delay.play();

        notificationStage.show();
    }

    public static void showSuccess(String title, String message) {
        showNotification(title, message, SUCCESS);
    }

    public static void showWarning(String title, String message) {
        showNotification(title, message, WARNING);
    }

    public static void showError(String title, String message) {
        showNotification(title, message, ERROR);
    }

    public static void showInfo(String title, String message) {
        showNotification(title, message, INFO);
    }
}