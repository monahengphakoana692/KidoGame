package com.gluonapplication.views;

import com.gluonhq.charm.glisten.mvc.View;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class HomeView extends View {
    VBox backgroundLayout = null;

    public VBox getBackgroundLayout() {
        backgroundLayout = new VBox(10);
        backgroundLayout.setAlignment(Pos.TOP_CENTER);
        Catagories catagories = new Catagories();

        backgroundLayout.setPrefHeight(600);
        backgroundLayout.setPrefWidth(350);
        backgroundLayout.setStyle("-fx-background-color: transparent; -fx-opacity:1;");

        // Get all category boxes
        VBox lilotho = catagories.getLilotho();
        VBox maele = catagories.getMaele();
        VBox lipapali = catagories.getLipapali();
        VBox liaparo = catagories.getLiaparo();
        VBox lijo = catagories.getLijo();

        // Initially set them to invisible (they'll fade in)
        lilotho.setOpacity(0);
        maele.setOpacity(0);
        lipapali.setOpacity(0);
        liaparo.setOpacity(0);
        lijo.setOpacity(0);

        // Add all components to the layout
        backgroundLayout.getChildren().addAll(getTitle(), lilotho, maele, lipapali, liaparo, lijo);

        // Animate the categories sequentially
        animateCategories(lilotho, maele, lipapali, liaparo, lijo);

        return backgroundLayout;
    }

    private void animateCategories(VBox... categories) {
        SequentialTransition sequentialTransition = new SequentialTransition();

        // Add an initial 5-second delay before any animations start
        javafx.animation.PauseTransition initialDelay = new javafx.animation.PauseTransition(Duration.seconds(5));
        sequentialTransition.getChildren().add(initialDelay);

        for (int i = 0; i < categories.length; i++) {
            VBox category = categories[i];

            // Create a fade-in transition
            FadeTransition fadeIn = new FadeTransition(Duration.millis(600), category);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);

            // Create a slide-up transition
            TranslateTransition slideUp = new TranslateTransition(Duration.millis(600), category);
            slideUp.setFromY(50);
            slideUp.setToY(0);

            // Create a scale transition for a slight "pop" effect
            ScaleTransition scale = new ScaleTransition(Duration.millis(400), category);
            scale.setFromX(0.9);
            scale.setFromY(0.9);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.setDelay(Duration.millis(200));

            // Combine all animations for this category
            ParallelTransition parallelTransition = new ParallelTransition(fadeIn, slideUp, scale);

            // Add a delay between each category's animation
            if (i > 0) {
                parallelTransition.setDelay(Duration.millis(200 * i));
            }

            sequentialTransition.getChildren().add(parallelTransition);
        }

        sequentialTransition.play();
    }

    public Label getTitle() {
        String title = "Khetha Papali eo u ka e bapalang";
        Label titledLabel = new Label(title);
        titledLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        titledLabel.setOpacity(0); // Start invisible

        // Add animation to title with 5-second delay
        FadeTransition fadeIn = new FadeTransition(Duration.millis(900), titledLabel);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setDelay(Duration.seconds(5)); // 5-second delay
        fadeIn.play();

        return titledLabel;
    }
}