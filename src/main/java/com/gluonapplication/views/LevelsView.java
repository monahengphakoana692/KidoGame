package com.gluonapplication.views;

import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
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

import javax.swing.*;

public class LevelsView extends View
{
    // Constants for view names
    public static final String LEVEL1 = "Level1";

    public static final String LEVEL2 = "Level2";
    public static final String LEVEL3 = "Level3";

    private static String L1Icon = "/L1.png";
    private static String L2Icon = "/L2.png";
    private static String L3Icon= "/L3.png";


    public   String getL1Icon() {
        return L1Icon;
    }

    public  void setL1Icon(String l1Icon) {
        L1Icon = l1Icon;
    }

    public   String getL2Icon() {
        return L2Icon;
    }

    public  void setL2Icon(String l2Icon) {
        L2Icon = l2Icon;
    }

    public  String getL3Icon() {
        return L3Icon;
    }

    public  void setL3Icon(String l3Icon) {
        L3Icon = l3Icon;
    }




    // Game instances
    private  LilothoGame lilothoGame;
    static VBox level1Box;
    static VBox level2Box;
    static VBox level3Box;
    static VBox mainContainer;

    public LevelsView()
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

        mainContainer.getChildren().addAll(level1Box, level2Box, level3Box);
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


    public void showGame(String viewName) {
        try {
            lilothoGame = new LilothoGame();

            if (getAppManager() != null) {
                // Check if view exists by attempting to switch to it
                try {
                    getAppManager().switchView(viewName);
                    switch (viewName) {
                        case LEVEL1:
                            lilothoGame.showLevel(0);
                            getAppManager().addViewFactory(viewName, () -> lilothoGame);
                            break;
                        case LEVEL2:
                            if(PrimaryView.getLevelnum().equals("1"))
                            {
                                lilothoGame.showLevel(1);
                                getAppManager().addViewFactory(viewName, () -> lilothoGame);
                            }else
                            {
                                showAlert("Boemo bo Koetsoe");
                            }
                            break;
                        case LEVEL3:
                            if(PrimaryView.getLevelnum().equals("2")) {
                                lilothoGame.showLevel(2);
                                getAppManager().addViewFactory(viewName, () -> lilothoGame);
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
            showAlert("Error switching view: 1" + e.getMessage());
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
        appBar.setTitleText("PAPALI KA LILOTHO - KAROLO EA: 1");
    }


}