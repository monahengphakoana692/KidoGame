package com.gluonapplication.views;

import com.gluonhq.charm.glisten.animation.BounceInRightTransition;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.FloatingActionButton;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import static java.lang.Long.toHexString;

public class SecondaryView extends View {
    private static final double CATEGORY_WIDTH = 280; // Wider boxes for better touch targets
    private static final double LEVEL_ITEM_SPACING = 15;

    public SecondaryView() {
        getStylesheets().add(SecondaryView.class.getResource("secondary.css").toExternalForm());

        // Main container with more vertical spacing
        VBox mainContainer = new VBox(20);
        mainContainer.setAlignment(Pos.TOP_CENTER);
        mainContainer.setPadding(new Insets(20, 10, 40, 10)); // More padding on top/bottom

        // Title with larger font and padding
        Label titleLabel = new Label("TLHALOSO YA BOITSHITSHO");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        titleLabel.setPadding(new Insets(0, 0, 15, 0));

        // Scrollable content area
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // No horizontal scrolling
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        // Content container inside scroll pane
        VBox contentBox = new VBox(25);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPadding(new Insets(10));

        // Category sections - now in vertical layout for mobile
        VBox sectionCategories = new VBox(25);
        sectionCategories.setAlignment(Pos.TOP_CENTER);

        // Individual category boxes with more spacing
        VBox lilothoBox = createCategoryBox("Lilotho",
                PrimaryView.getLilothoStats(),
                Color.LIGHTBLUE);
        lilothoBox.setMinWidth(CATEGORY_WIDTH);

        VBox maeleBox = createCategoryBox("Maele",
                PrimaryView.getMaeleStats(),
                Color.LIGHTCORAL);
        maeleBox.setMinWidth(CATEGORY_WIDTH);

        VBox lipapaliBox = createCategoryBox("Lipapali",
                PrimaryView.getLipapaliStats(),
                Color.LIGHTGREEN);
        lipapaliBox.setMinWidth(CATEGORY_WIDTH);

        sectionCategories.getChildren().addAll(lilothoBox, maeleBox, lipapaliBox);

        // Overall statistics with more padding
        VBox overallStats = createOverallStatsBox();
        overallStats.setMinWidth(CATEGORY_WIDTH + 40);

        contentBox.getChildren().addAll(sectionCategories, overallStats);
        scrollPane.setContent(contentBox);

        mainContainer.getChildren().addAll(titleLabel, scrollPane);
        setCenter(mainContainer);
        setShowTransitionFactory(BounceInRightTransition::new);

        // Larger floating action button
        FloatingActionButton refreshButton = new FloatingActionButton(
                MaterialDesignIcon.REFRESH.text,
                e -> refreshStatistics()
        );
        //refreshButton.setSize(FloatingActionButton.Size.LARGE);
        refreshButton.showOn(this);
    }

    private VBox createCategoryBox(String categoryName, PrimaryView.CategoryStats stats, Color bgColor) {
        VBox categoryBox = new VBox(15); // Increased spacing
        categoryBox.setAlignment(Pos.TOP_CENTER);
        categoryBox.setStyle("-fx-background-color: " + toHexString(bgColor) + "; " +
                "-fx-padding: 20; " +
                "-fx-background-radius: 15;");

        // Category title with larger font
        Label title = new Label(categoryName);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setPadding(new Insets(0, 0, 10, 0));

        // Level progress indicators with more spacing
        VBox levelsBox = new VBox(LEVEL_ITEM_SPACING);
        for (int i = 0; i < stats.levelStats.length; i++) {
            PrimaryView.LevelStat level = stats.levelStats[i];

            HBox levelBox = new HBox(10);
            levelBox.setAlignment(Pos.CENTER_LEFT);

            Label levelLabel = new Label(String.format("Boemo %d:", i+1));
            levelLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            levelLabel.setMinWidth(80);

            ProgressBar pb = new ProgressBar(level.completionPercentage() / 100.0);
            pb.setPrefWidth(120);
            pb.setPrefHeight(20);

            Label percentLabel = new Label(String.format("%.1f%%", level.completionPercentage()));
            percentLabel.setFont(Font.font("Arial", 14));

            levelBox.getChildren().addAll(levelLabel, pb, percentLabel);
            levelsBox.getChildren().add(levelBox);
        }

        // Category statistics with larger fonts
        VBox statsBox = new VBox(10);
        statsBox.setAlignment(Pos.CENTER_LEFT);
        statsBox.setPadding(new Insets(15, 0, 0, 0));

        Label totalLabel = new Label("Lipotso: " + stats.totalQuestions);
        totalLabel.setFont(Font.font("Arial", 14));

        Label correctLabel = new Label("Tse nepahetseng: " + stats.correctAnswers);
        correctLabel.setFont(Font.font("Arial", 14));

        Label wrongLabel = new Label("Tse fosahetseng: " + stats.wrongAnswers);
        wrongLabel.setFont(Font.font("Arial", 14));

        Label percentageLabel = new Label("Boemo: " + String.format("%.1f%%", stats.successPercentage()));
        percentageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        statsBox.getChildren().addAll(
                new Separator(),
                totalLabel,
                correctLabel,
                wrongLabel,
                percentageLabel
        );

        categoryBox.getChildren().addAll(title, levelsBox, statsBox);
        return categoryBox;
    }
    private VBox createOverallStatsBox() {
        VBox overallBox = new VBox(15);
        overallBox.setAlignment(Pos.CENTER);
        overallBox.setStyle("-fx-background-color: orange; " +
                "-fx-padding: 25; " +
                "-fx-background-radius: 15;");

        PrimaryView.OverallStats stats = PrimaryView.getOverallStats();

        Label title = new Label("Kakaretso");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setPadding(new Insets(0, 0, 10, 0));

        // Larger progress bar
        ProgressBar overallProgress = new ProgressBar(stats.overallPercentage() / 100.0);
        overallProgress.setPrefWidth(220);
        overallProgress.setPrefHeight(25);
        overallProgress.setStyle("-fx-accent: #4a6baf;");

        // Stats in grid layout for better organization
        GridPane statsGrid = new GridPane();
        statsGrid.setHgap(15);
        statsGrid.setVgap(10);
        statsGrid.setPadding(new Insets(15, 0, 0, 0));

        Label totalLabel = new Label("Lipotso tsohle:");
        totalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        Label totalValue = new Label(String.valueOf(stats.totalQuestions));
        totalValue.setFont(Font.font("Arial", 14));

        Label correctLabel = new Label("Tse nepahetseng:");
        correctLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        Label correctValue = new Label(String.valueOf(stats.totalCorrect));
        correctValue.setFont(Font.font("Arial", 14));
        correctValue.setTextFill(Color.GREEN);

        Label wrongLabel = new Label("Tse fosahetseng:");
        wrongLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        Label wrongValue = new Label(String.valueOf(stats.totalWrong));
        wrongValue.setFont(Font.font("Arial", 14));
        wrongValue.setTextFill(Color.RED);

        Label percentageLabel = new Label("Boemo bohle:");
        percentageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        Label percentageValue = new Label(String.format("%.1f%%", stats.overallPercentage()));
        percentageValue.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        statsGrid.addRow(0, totalLabel, totalValue);
        statsGrid.addRow(1, correctLabel, correctValue);
        statsGrid.addRow(2, wrongLabel, wrongValue);
        statsGrid.addRow(3, percentageLabel, percentageValue);

        overallBox.getChildren().addAll(title, overallProgress, statsGrid);
        return overallBox;
    }


    private void refreshStatistics() {
        // Implement refresh logic here
        // This would update all the statistics from your data source
    }

    private String toHexString(Color color) {
        return String.format("#%02X%02X%02X",
                (int)(color.getRed() * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255));
    }

    @Override
    protected void updateAppBar(AppBar appBar) {
        appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> getAppManager().getDrawer().open()));
        appBar.setTitleText("LITLALEHO");
        appBar.getActionItems().add(MaterialDesignIcon.FAVORITE.button(e -> System.out.println("Favorite")));
    }
    
}
