package gui.utils;

import javafx.scene.Scene;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
/**
 * Classe utilitaire pour gérer les styles de l'application
 */
public class StyleManager {

    // Constantes de couleurs principales
    public static final String PRIMARY_COLOR = "#4a6baf";
    public static final String PRIMARY_DARK_COLOR = "#3a5999";
    public static final String PRIMARY_LIGHT_COLOR = "#5d7fc1";
    public static final String BACKGROUND_COLOR = "#f5f5f7";

    // Constantes de couleurs fonctionnelles
    public static final String SUCCESS_COLOR = "#2ecc71";
    public static final String WARNING_COLOR = "#f39c12";
    public static final String ERROR_COLOR = "#e74c3c";
    public static final String INFO_COLOR = "#3498db";

    // Constantes de texte
    public static final String TEXT_PRIMARY_COLOR = "#2c3e50";
    public static final String TEXT_SECONDARY_COLOR = "#7f8c8d";

    /**
     * Applique les styles globaux à la scène fournie
     * @param scene La scène à styliser
     */
    public static void applyStyles(Scene scene) {
        String css =
                ".root { -fx-font-family: 'Segoe UI', Arial, sans-serif; -fx-background-color: " + BACKGROUND_COLOR + "; }" +
                        ".table-view { -fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5); }" +
                        ".table-view .column-header { -fx-background-color: " + PRIMARY_COLOR + "; -fx-text-fill: white; }" +
                        ".title-label { -fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY_COLOR + "; }" +
                        ".subtitle { -fx-font-size: 16px; -fx-font-weight: normal; -fx-text-fill: " + TEXT_SECONDARY_COLOR + "; }" +
                        ".button { -fx-background-color: " + PRIMARY_COLOR + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 4; }" +
                        ".button:hover { -fx-background-color: " + PRIMARY_LIGHT_COLOR + "; }" +
                        ".action-button { -fx-min-width: 80px; }" +
                        ".text-field { -fx-background-radius: 4; }" +

                        // Styles pour les boutons de couleur
                        ".success-button { -fx-background-color: " + SUCCESS_COLOR + "; }" +
                        ".success-button:hover { -fx-background-color: derive(" + SUCCESS_COLOR + ", 20%); }" +
                        ".warning-button { -fx-background-color: " + WARNING_COLOR + "; }" +
                        ".warning-button:hover { -fx-background-color: derive(" + WARNING_COLOR + ", 20%); }" +
                        ".error-button { -fx-background-color: " + ERROR_COLOR + "; }" +
                        ".error-button:hover { -fx-background-color: derive(" + ERROR_COLOR + ", 20%); }" +

                        // Styles pour les boîtes de dialogue
                        ".dialog-pane { -fx-background-color: white; -fx-padding: 20; }" +
                        ".dialog-pane > .button-bar > .container { -fx-background-color: white; }" +

                        // Styles pour les cellules de tableau
                        ".table-cell { -fx-alignment: CENTER-LEFT; -fx-padding: 8; }" +
                        ".table-row-cell:odd { -fx-background-color: #f9f9f9; }" +
                        ".table-row-cell:hover { -fx-background-color: #e9f0f7; }";

        try {
            // Encode the entire CSS string and replace spaces with %20
            String encodedCSS = URLEncoder.encode(css, StandardCharsets.UTF_8.toString())
                    .replace("+", "%20");
            scene.getStylesheets().add("data:text/css," + encodedCSS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Crée un style inline pour un label de titre
     */
    public static String getTitleStyle() {
        return "-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY_COLOR + ";";
    }

    /**
     * Crée un style inline pour un label de sous-titre
     */
    public static String getSubtitleStyle() {
        return "-fx-font-size: 16px; -fx-font-weight: normal; -fx-text-fill: " + TEXT_SECONDARY_COLOR + ";";
    }

    /**
     * Crée un style inline pour un bouton primaire
     */
    public static String getPrimaryButtonStyle() {
        return "-fx-background-color: " + PRIMARY_COLOR + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 4;";
    }

    /**
     * Crée un style inline pour un bouton de succès
     */
    public static String getSuccessButtonStyle() {
        return "-fx-background-color: " + SUCCESS_COLOR + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 4;";
    }

    /**
     * Crée un style inline pour un bouton d'avertissement
     */
    public static String getWarningButtonStyle() {
        return "-fx-background-color: " + WARNING_COLOR + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 4;";
    }

    /**
     * Crée un style inline pour un bouton d'erreur
     */
    public static String getErrorButtonStyle() {
        return "-fx-background-color: " + ERROR_COLOR + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 4;";
    }

    /**
     * Crée un style pour une badge de type de document
     */
    public static String getDocumentTypeBadgeStyle(String type) {
        String color;
        switch (type.toLowerCase()) {
            case "livre":
                color = INFO_COLOR;
                break;
            case "magazine":
                color = SUCCESS_COLOR;
                break;
            case "article":
                color = "#9b59b6"; // Violet
                break;
            default:
                color = "#95a5a6"; // Gris
                break;
        }

        return "-fx-background-color: " + color + "; -fx-text-fill: white; -fx-padding: 5 10; -fx-background-radius: 4;";
    }

    /**
     * Crée un style pour un label de statut (disponible/emprunté)
     */
    public static String getStatusStyle(boolean disponible) {
        String color = disponible ? SUCCESS_COLOR : WARNING_COLOR;
        return "-fx-font-weight: bold; -fx-text-fill: " + color + ";";
    }

    /**
     * Crée un style pour une boîte de statut (conteneur)
     */
    public static String getStatusBoxStyle(boolean disponible) {
        String bgColor = disponible ? "#e8f8f5" : "#fef5e7";
        return "-fx-background-color: " + bgColor + "; -fx-background-radius: 5;";
    }
}