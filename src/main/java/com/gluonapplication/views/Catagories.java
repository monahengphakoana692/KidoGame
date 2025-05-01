package com.gluonapplication.views;

import com.gluonhq.charm.glisten.mvc.View;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class Catagories extends View {
    // Constants for view names
    public static final String LILOTHO_VIEW = "Lilotho";

    public static final String MAELE_VIEW = "Maele";
    public static final String LIPAPALI_VIEW = "Lipapali";

    // Game instances
    private  LilothoGame lilothoGame;
    //private final MaeleGame maeleGame = new MaeleGame(); // You'll need to create this class
    //private final LipapaliGame lipapaliGame = new LipapaliGame(); // You'll need to create this class

    public VBox getLilotho() {
        return createCategoryBox(
                "/background2.jpg", // Unique image for each category
                "Lipapali ka LiLOTHO",
                LILOTHO_VIEW,
                "-fx-background-color: #FFA500;" // Orange
        );
    }

    public VBox getMaele() {
        return createCategoryBox(
                "/background2.jpg",
                "Lipapali ka Maele",
                MAELE_VIEW,
                "-fx-background-color: #4CAF50;" // Green
        );
    }

    public VBox getLipapali() {
        return createCategoryBox(
                "/background2.jpg",
                "Lipapali tse Ling",
                LIPAPALI_VIEW,
                "-fx-background-color: #2196F3;" // Blue
        );
    }

    private VBox createCategoryBox(String imagePath, String title, String viewName, String style) {
        VBox categoryBox = new VBox(10);
        categoryBox.setAlignment(Pos.TOP_CENTER);
        categoryBox.setPadding(new Insets(10));
        categoryBox.setMaxWidth(150);
        categoryBox.setStyle(style);

        try {
            Image image = new Image(imagePath);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(150);
            imageView.setFitHeight(150);
            imageView.setPreserveRatio(true);
            imageView.setOnMouseClicked(event -> showGame(viewName));

            Label label = new Label(title);
            label.setAlignment(Pos.CENTER);
            label.setStyle("-fx-font-weight: bold;");

            categoryBox.getChildren().addAll(imageView, label);
        } catch (Exception e) {
            showAlert("Error loading category: " + e.getMessage());
        }

        return categoryBox;
    }

    public void showGame(String viewName) {
        try {
            lilothoGame = new LilothoGame();
            if (getAppManager() != null) {
                // Check if view exists by attempting to switch to it
                try {
                    getAppManager().switchView(viewName);
                    switch (viewName) {
                        case LILOTHO_VIEW:
                            getAppManager().addViewFactory(viewName, () -> lilothoGame);
                            break;
                        case MAELE_VIEW:
                            // getAppManager().addViewFactory(viewName, () -> maeleGame);
                            break;
                        case LIPAPALI_VIEW:
                            // getAppManager().addViewFactory(viewName, () -> lipapaliGame);
                            break;
                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e.toString());
                }
            }
        } catch (Exception e) {
            showAlert("Error switching view: " + e.getMessage());
        }
    }

    private void showAlert(String error) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An error occurred");
        alert.setContentText(error);
        alert.showAndWait();
    }


}