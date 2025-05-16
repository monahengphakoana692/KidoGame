package com.gluonapplication.views;

import com.gluonhq.charm.glisten.mvc.View;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import java.util.HashMap;
import java.util.Map;

public class Catagories extends View {
    // Constants for view names
    public static final String LILOTHO_VIEW = "Lilotho";
    public static final String MAELE_VIEW = "Maele";
    public static final String LIPAPALI_VIEW = "Lipapali";
    public static final String LIAPARO_VIEW = "Liaparo";
    public static final String LIJO_VIEW = "Lijo";

    // Game instances
    private LevelsView levelsView;
    private LevelsView2 levelsView2;
    private LevelsView3 levelsView3;
    private LevelsView4 levelsView4;
    private LevelsView5 levelsView5;

    private HBox starContainer;

    // Track progress for each category (0-3 stars)
    private Map<String, Integer> categoryProgress = new HashMap<>();

    public Catagories() {
        // Initialize default progress (0 stars for all categories)
        categoryProgress.put(LILOTHO_VIEW, 0);
        categoryProgress.put(MAELE_VIEW, 0);
        categoryProgress.put(LIPAPALI_VIEW, 0);
        categoryProgress.put(LIAPARO_VIEW, 0);
        categoryProgress.put(LIJO_VIEW, 0);
    }

    public VBox getLilotho() {
        return createCategoryBox(
                "/background2.jpg",
                "papali ka LiLOTHO",
                LILOTHO_VIEW,
                "-fx-background-color: #FFA500;"
        );
    }

    public VBox getMaele() {
        return createCategoryBox(
                "/background2.jpg",
                "papali ka Maele",
                MAELE_VIEW,
                "-fx-background-color: #4CAF50;"
        );
    }

    public VBox getLipapali() {
        return createCategoryBox(
                "/background2.jpg",
                "papali ka Lipapali",
                LIPAPALI_VIEW,
                "-fx-background-color: #2196F3;"
        );
    }

    public VBox getLiaparo() {
        return createCategoryBox(
                "/background2.jpg",
                "papali ka liaparo",
                LIAPARO_VIEW,
                "-fx-background-color: #84c5f8;"
        );
    }

    public VBox getLijo() {
        return createCategoryBox(
                "/background2.jpg",
                "papali ka lijo",
                LIJO_VIEW,
                "-fx-background-color: #dfd80f;"
        );
    }

    public void setStarContainer(HBox starContainer) {
        this.starContainer = starContainer;
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

            // Add hover animation
            imageView.setOnMouseEntered(e -> {
                ScaleTransition scale = new ScaleTransition(Duration.millis(200), imageView);
                scale.setToX(1.05);
                scale.setToY(1.05);
                scale.play();
            });

            imageView.setOnMouseExited(e -> {
                ScaleTransition scale = new ScaleTransition(Duration.millis(200), imageView);
                scale.setToX(1.0);
                scale.setToY(1.0);
                scale.play();
            });

            imageView.setOnMouseClicked(event -> {
                ScaleTransition click = new ScaleTransition(Duration.millis(100), imageView);
                click.setToX(0.95);
                click.setToY(0.95);
                click.setAutoReverse(true);
                click.setCycleCount(2);
                click.setOnFinished(e -> showGame(viewName));
                click.play();
            });

            Label label = new Label(title);
            label.setAlignment(Pos.CENTER);
            label.setStyle("-fx-font-weight: bold;");

            // Add hover animation to label
            label.setOnMouseEntered(e -> label.setStyle("-fx-font-weight: bold; -fx-text-fill: white;"));
            label.setOnMouseExited(e -> label.setStyle("-fx-font-weight: bold; -fx-text-fill: black;"));

            // Create star rating based on progress
            starContainer = createStarRating(viewName);

            categoryBox.getChildren().addAll(imageView, label, starContainer);
        } catch (Exception e) {
            showAlert("Error loading category: " + e.getMessage());
        }

        return categoryBox;
    }

    public HBox createStarRating(String viewName) {
        HBox starContainer = new HBox(2);
        starContainer.setAlignment(Pos.CENTER);

        int progress = categoryProgress.getOrDefault(viewName, Integer.parseInt(PrimaryView.getLevelnum()));

        for (int i = 0; i < 3; i++)
        {
            ImageView star = new ImageView();

            switch (progress)
            {
                case 0:
                    star.setImage(new Image("/L2.png"));
                    star.setFitWidth(20);
                    star.setFitHeight(20);
                    starContainer.getChildren().add(star);
                    break;
                case 1:
                    star.setImage(new Image("/win2.png"));
                    star.setFitWidth(20);
                    star.setFitHeight(20);
                    starContainer.getChildren().add(star);
                    break;
            }
        }

        return starContainer;
    }

    // Method to update progress for a category
    public void updateProgress(String viewName, int starsEarned) {
        if (starsEarned >= 0 && starsEarned <= 3) {
            categoryProgress.put(viewName, starsEarned);
            // Here you would typically save this progress to persistent storage
        }
    }

    public void showGame(String viewName) {
        try {
            // Initialize the appropriate view based on category
            switch (viewName) {
                case LILOTHO_VIEW:
                    levelsView = new LevelsView();
                    break;
                case MAELE_VIEW:
                    levelsView2 = new LevelsView2();
                    break;
                case LIPAPALI_VIEW:
                    levelsView3 = new LevelsView3();
                    break;
                case LIAPARO_VIEW:
                    levelsView4 = new LevelsView4();
                    break;
                case LIJO_VIEW:
                    levelsView5 = new LevelsView5();
                    break;
            }

            if (getAppManager() != null) {
                try {
                    getAppManager().switchView(viewName);
                    // Add view factory for each category
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