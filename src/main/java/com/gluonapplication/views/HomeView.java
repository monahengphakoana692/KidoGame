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
    private VBox backgroundLayout = null;
    private Catagories categories;
    private VBox lilotho, maele, lipapali, liaparo, lijo;

    public VBox getBackgroundLayout() {
        backgroundLayout = new VBox(5);
        backgroundLayout.setAlignment(Pos.TOP_CENTER);
        categories = new Catagories();

        backgroundLayout.setPrefHeight(600);
        backgroundLayout.setPrefWidth(350);
        backgroundLayout.setStyle("-fx-background-color: transparent; -fx-opacity:1;");

        // Get all category boxes
        lilotho = categories.getLilotho();
        maele = categories.getMaele();
        lipapali = categories.getLipapali();
        liaparo = categories.getLiaparo();
        lijo = categories.getLijo();


        // Initially set them to invisible (they'll fade in)
        lilotho.setOpacity(0);
        maele.setOpacity(0);
        lipapali.setOpacity(0);
        liaparo.setOpacity(0);
        lijo.setOpacity(0);

        // Add all components to the layout
        backgroundLayout.getChildren().addAll(getTitle(), lilotho, maele, lipapali, liaparo, lijo);

        // Load saved progress (if any)
        loadProgress();

        // Animate the categories sequentially
        animateCategories(lilotho, maele, lipapali, liaparo, lijo);

        return backgroundLayout;
    }

    private void loadProgress() {
        // Here you would load saved progress from persistent storage
        // For now, we'll just initialize with some example values
        // Replace this with actual loading logic

        // Example progress (replace with your loading logic):
        categories.updateProgress(Catagories.LILOTHO_VIEW, getSavedProgress(Catagories.LILOTHO_VIEW));
        categories.updateProgress(Catagories.MAELE_VIEW, getSavedProgress(Catagories.MAELE_VIEW));
        categories.updateProgress(Catagories.LIPAPALI_VIEW, getSavedProgress(Catagories.LIPAPALI_VIEW));
        categories.updateProgress(Catagories.LIAPARO_VIEW, getSavedProgress(Catagories.LIAPARO_VIEW));
        categories.updateProgress(Catagories.LIJO_VIEW, getSavedProgress(Catagories.LIJO_VIEW));
    }

    private int getSavedProgress(String category) {
        // Replace this with actual loading from preferences/database
        // For now, returning 0 for all categories
        return Integer.parseInt(PrimaryView.getLevelnum());
    }

    public void updateCategoryProgress(String categoryName, int starsEarned) {
        // Update the progress in the Categories class
        categories.updateProgress(categoryName, starsEarned);

        // Save the progress (you would implement this)
        saveProgress(categoryName, starsEarned);

        // Refresh the view to show updated stars
        refreshCategoryViews();
    }

    private void saveProgress(String categoryName, int starsEarned) {
        // Implement your saving logic here
        // This could be to SharedPreferences, a database, or a file
    }

    private void refreshCategoryViews() {
        // Recreate the category views to show updated stars
        VBox newLilotho = categories.getLilotho();
        VBox newMaele = categories.getMaele();
        VBox newLipapali = categories.getLipapali();
        VBox newLiaparo = categories.getLiaparo();
        VBox newLijo = categories.getLijo();

        // Replace the old views with the new ones
        backgroundLayout.getChildren().set(1, newLilotho);
        backgroundLayout.getChildren().set(2, newMaele);
        backgroundLayout.getChildren().set(3, newLipapali);
        backgroundLayout.getChildren().set(4, newLiaparo);
        backgroundLayout.getChildren().set(5, newLijo);

        // Reapply animations to the new views
        animateCategories(newLilotho, newMaele, newLipapali, newLiaparo, newLijo);
    }

    // Rest of your existing methods (animateCategories, startPulseAnimation, getTitle) remain the same
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
                parallelTransition.setDelay(Duration.millis(100 * i));
            }

            sequentialTransition.getChildren().add(parallelTransition);

            // Adding continuous pulsing animation after the initial animation completes
            parallelTransition.setOnFinished(e -> {
                startPulseAnimation(category);
            });
        }

        sequentialTransition.play();
    }

    private void startPulseAnimation(VBox box) {
        // Create a gentle pulsing animation
        ScaleTransition pulse = new ScaleTransition(Duration.seconds(1.5), box);
        pulse.setFromX(1.0);
        pulse.setFromY(1.0);
        pulse.setToX(1.05);  // 5% scale up
        pulse.setToY(1.05);  // 5% scale up
        pulse.setAutoReverse(true);
        pulse.setCycleCount(ScaleTransition.INDEFINITE);

        // Optional: Add slight opacity variation for more effect
        FadeTransition fadePulse = new FadeTransition(Duration.seconds(1.5), box);
        fadePulse.setFromValue(1.0);
        fadePulse.setToValue(0.95);
        fadePulse.setAutoReverse(true);
        fadePulse.setCycleCount(FadeTransition.INDEFINITE);

        ParallelTransition pulseAnimation = new ParallelTransition(pulse, fadePulse);
        pulseAnimation.play();

        // Store the animation so we can stop it later if needed
        box.setUserData(pulseAnimation);
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