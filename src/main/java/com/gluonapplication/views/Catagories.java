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
    public static final String LIAPARO_VIEW = "Liaparo";
    public static final String LIJO_VIEW = "Lijo";
    // Game instances
    private  LevelsView levelsView;
    private  LevelsView2 levelsView2;
    private  LevelsView3 levelsView3;
    private  LevelsView4 levelsView4;
    private  LevelsView5 levelsView5;

    public VBox getLilotho() {
        return createCategoryBox(
                "/background2.jpg", // Unique image for each category
                "papali ka LiLOTHO",
                LILOTHO_VIEW,
                "-fx-background-color: #FFA500;" // Orange
        );
    }

    public VBox getMaele() {
        return createCategoryBox(
                "/background2.jpg",
                "papali ka Maele",
                MAELE_VIEW,
                "-fx-background-color: #4CAF50;" // Green
        );
    }

    public VBox getLipapali() {
        return createCategoryBox(
                "/background2.jpg",
                "papali ka Lipapali",
                LIPAPALI_VIEW,
                "-fx-background-color: #2196F3;" // Blue
        );
    }

    public VBox getLiaparo() {
        return createCategoryBox(
                "/background2.jpg",
                "papali ka liaparo",
                LIAPARO_VIEW,
                "-fx-background-color: #2196F3;" // Blue
        );
    }

    public VBox getLijo() {
        return createCategoryBox(
                "/background2.jpg",
                "papali ka lijo",
                LIJO_VIEW,
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
            levelsView = new LevelsView();
            levelsView2 = new LevelsView2();
            levelsView3 = new LevelsView3();
            levelsView4 = new LevelsView4();
            levelsView5 = new LevelsView5();
            if (getAppManager() != null) {
                // Check if view exists by attempting to switch to it
                try {
                    getAppManager().switchView(viewName);
                    switch (viewName) {
                        case LILOTHO_VIEW:
                            getAppManager().addViewFactory(viewName, () -> levelsView);
                            break;
                        case MAELE_VIEW:
                            getAppManager().addViewFactory(viewName, () -> levelsView2);
                            break;
                        case LIPAPALI_VIEW:
                            getAppManager().addViewFactory(viewName, () -> levelsView3);
                            break;
                        case LIAPARO_VIEW:
                            getAppManager().addViewFactory(viewName, () -> levelsView4);
                            break;
                        case LIJO_VIEW:
                            getAppManager().addViewFactory(viewName, () -> levelsView5);
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