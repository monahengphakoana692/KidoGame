package com.gluonapplication.views;

import com.gluonhq.charm.glisten.mvc.View;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;

public class Catagories extends View {
    VBox Lilotho = null;
    Image image = null;
    Label label = null;
    ImageView imageView = null;
    public static final String LIlotho_VIEW = "Lilotho";

    public VBox getLilotho()
    {
        Lilotho = new VBox(10);
        Lilotho.setAlignment(Pos.TOP_CENTER); // Changed from CENTER to TOP_CENTER

        image = new Image("/background2.jpg");
        imageView = new ImageView(image);
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);
        SecondaryView secondaryView = new SecondaryView();
        imageView.setOnMouseClicked(event ->
        {
            ShowGame("Lilotho");
        });

        label = new Label("Lipapali ka LiLOTHO");
        label.setAlignment(Pos.CENTER);

        Lilotho.getChildren().addAll(imageView, label);
        Lilotho.setStyle("-fx-background-color: orange;");
        Lilotho.setMaxWidth(100);

        // Add padding to the VBox (top, right, bottom, left)
        Lilotho.setPadding(new javafx.geometry.Insets(10, 10, 0, 10)); // 10px from top and left

        return Lilotho;
    }

    public VBox getMaele()
    {
        Lilotho = new VBox(10);
        Lilotho.setAlignment(Pos.TOP_CENTER); // Changed from CENTER to TOP_CENTER

        image = new Image("/background2.jpg");
        imageView = new ImageView(image);
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);

        label = new Label("Lipapali ka LiLOTHO");
        label.setAlignment(Pos.CENTER);

        Lilotho.getChildren().addAll(imageView, label);
        Lilotho.setStyle("-fx-background-color: orange;");
        Lilotho.setMaxWidth(100);

        // Add padding to the VBox (top, right, bottom, left)
        Lilotho.setPadding(new javafx.geometry.Insets(10, 10, 0, 10)); // 10px from top and left

        return Lilotho;
    }

    public VBox getLipapali()
    {
        Lilotho = new VBox(10);
        Lilotho.setAlignment(Pos.TOP_CENTER); // Changed from CENTER to TOP_CENTER

        image = new Image("/background2.jpg");
        imageView = new ImageView(image);
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);



        label = new Label("Lipapali ka LiLOTHO");
        label.setAlignment(Pos.CENTER);

        Lilotho.getChildren().addAll(imageView, label);
        Lilotho.setStyle("-fx-background-color: orange;");
        Lilotho.setMaxWidth(100);

        // Add padding to the VBox (top, right, bottom, left)
        Lilotho.setPadding(new javafx.geometry.Insets(10, 10, 0, 10)); // 10px from top and left

        return Lilotho;
    }

    private void ShowGame(String title) {


       LilothoGame lilothoGame = new LilothoGame();

        getAppManager().addViewFactory(title.replaceAll("\\s+", ""), () -> lilothoGame);
        getAppManager().switchView(title.replaceAll("\\s+", ""));
    }

}