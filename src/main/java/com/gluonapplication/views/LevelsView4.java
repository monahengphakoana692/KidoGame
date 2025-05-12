package com.gluonapplication.views;

import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import javax.swing.*;

public class LevelsView4 extends View
{
    // Constants for view names
    public static final String LEVEL1 = "Level1";

    public static final String LEVEL2 = "Level2";
    public static final String LEVEL3 = "Level3";

    private static String L1Icon = "/L1.png";
    private static String L2Icon = "/L2.png";
    private static String L3Icon= "/L3.png";


    public static  String getL1Icon() {
        return L1Icon;
    }

    public static void setL1Icon(String l1Icon) {
        L1Icon = l1Icon;
    }

    public static  String getL2Icon() {
        return L2Icon;
    }

    public static void setL2Icon(String l2Icon) {
        L2Icon = l2Icon;
    }

    public static String getL3Icon() {
        return L3Icon;
    }

    public static void setL3Icon(String l3Icon) {
        L3Icon = l3Icon;
    }

    // Game instances
    private LiaparoGame  liaparoGame;

    //private final MaeleGame maeleGame = new MaeleGame(); // You'll need to create this class
    //private final LipapaliGame lipapaliGame = new LipapaliGame(); // You'll need to create this class
    private VBox level1Box;
    private VBox level2Box;
    private VBox level3Box;
    private VBox mainContainer;

    public LevelsView4()
    {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        mainContainer = new VBox(20);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setMinHeight(Region.USE_PREF_SIZE);

        // Initialize level boxes
        refreshLevelBoxes();

        StackPane centerContainer = new StackPane();
        centerContainer.getChildren().add(mainContainer);
        StackPane.setAlignment(mainContainer, Pos.TOP_CENTER);

        scrollPane.setContent(centerContainer);
        setCenter(scrollPane);
    }

    public void refreshLevelBoxes() {
        // Clear existing boxes
        mainContainer.getChildren().clear();

        // Create new boxes with current icons
        level1Box = createCenteredLevelBox(getL1Icon(), "Boemo ba 1", LEVEL1);
        level2Box = createCenteredLevelBox(getL2Icon(), "Boemo ba 2", LEVEL2);
        level3Box = createCenteredLevelBox(getL3Icon(), "Boemo ba 3", LEVEL3);

        // Initially set them to invisible (they'll fade in)
        level1Box.setOpacity(0);
        level2Box.setOpacity(0);
        level3Box.setOpacity(0);

        // Add all level boxes to the layout
        mainContainer.getChildren().addAll(level1Box, level2Box, level3Box);

        // Animate the level boxes sequentially
        animateLevelBoxes(level1Box, level2Box, level3Box);
    }

    private void animateLevelBoxes(VBox... levelBoxes) {
        SequentialTransition sequentialTransition = new SequentialTransition();

        for (int i = 0; i < levelBoxes.length; i++) {
            VBox levelBox = levelBoxes[i];

            // Create a fade-in transition
            FadeTransition fadeIn = new FadeTransition(Duration.millis(600), levelBox);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);

            // Create a bounce effect that returns to original scale
            ScaleTransition bounce = new ScaleTransition(Duration.millis(400), levelBox);
            bounce.setFromX(0.8);  // Start slightly smaller
            bounce.setFromY(0.8);
            bounce.setToX(1.1);    // Grow slightly larger
            bounce.setToY(1.1);
            bounce.setCycleCount(2);
            bounce.setAutoReverse(true);

            // Final transition to ensure we end at exactly scale 1.0
            ScaleTransition finalScale = new ScaleTransition(Duration.millis(100), levelBox);
            finalScale.setToX(1.0);
            finalScale.setToY(1.0);

            // Combine all animations for this level box
            ParallelTransition fadeAndBounce = new ParallelTransition(fadeIn, bounce);
            SequentialTransition completeAnimation = new SequentialTransition(
                    fadeAndBounce,
                    finalScale
            );

            // Add delay between each level's animation
            if (i > 0) {
                completeAnimation.setDelay(Duration.millis(150 * i));
            }

            sequentialTransition.getChildren().add(completeAnimation);
        }

        sequentialTransition.play();
    }

    private VBox createCenteredLevelBox(String imagePath, String title, String viewName) {
        VBox levelBox = new VBox(10);
        levelBox.setAlignment(Pos.CENTER);
        levelBox.setStyle("-fx-background-color: yellow; -fx-background-radius: 10;");  // Fixed typo: background -> background-color
        levelBox.setPadding(new Insets(15));

        try {
            ImageView imageView = new ImageView(new Image(imagePath));
            imageView.setFitWidth(120);
            imageView.setFitHeight(120);
            imageView.setPreserveRatio(true);

            // Make the image view clickable
            imageView.setOnMouseClicked(e -> {
                System.out.println("Icon clicked: " + viewName);  // Debug output
                showGame(viewName);  // Call your existing method
            });

            // Visual feedback for clickable image
            imageView.setStyle("-fx-cursor: hand;");  // Show hand cursor on hover

            Label label = new Label(title);
            label.setStyle("-fx-font-weight: bold; -fx-text-fill: black; -fx-font-size: 14px;");

            levelBox.getChildren().addAll(imageView, label);
            levelBox.setOnMouseClicked(e -> showGame(viewName));  // Keep whole box clickable too

        } catch (Exception e) {
            System.err.println("Error loading image: " + e.getMessage());
        }

        return levelBox;
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


            liaparoGame = new LiaparoGame();

            if (getAppManager() != null) {
                // Check if view exists by attempting to switch to it
                try {
                    getAppManager().switchView(viewName);
                    switch (viewName) {
                        case LEVEL1:
                            if(PrimaryView.getLevelnum().equals("9")) {
                                liaparoGame.showLevel(10);
                                getAppManager().addViewFactory(viewName, () -> liaparoGame);
                            }else
                            {
                                showAlert("Boemo bo  Koetsoe");
                            }
                            break;
                        case LEVEL2:
                            if(PrimaryView.getLevelnum().equals("10"))
                            {
                                liaparoGame.showLevel(11);
                                getAppManager().addViewFactory(viewName, () -> liaparoGame);
                            }else
                            {
                                showAlert("Boemo bo Koetsoe");
                            }
                            break;
                        case LEVEL3:
                            if(PrimaryView.getLevelnum().equals("11")) {
                                liaparoGame.showLevel(12);
                                getAppManager().addViewFactory(viewName, () -> liaparoGame);
                            }else
                            {
                                showAlert("Boemo bo Koetsoe");
                            }
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

    @Override
    protected void updateAppBar(AppBar appBar) {
        appBar.setNavIcon(MaterialDesignIcon.ARROW_BACK.button(e -> {

            getAppManager().goHome();
        }));
        appBar.setTitleText("PAPALI KA LIAPARO - KAROLO EA: 4" );
    }


}