package com.gluonapplication.views;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class HomeView
{
    VBox backgroundLayout = null;

    public VBox getBackgroundLayout()
    {
        backgroundLayout = new VBox(10);
        backgroundLayout.setAlignment(Pos.TOP_CENTER);
        Catagories catagories = new Catagories();

        backgroundLayout.setPrefHeight(600);
        backgroundLayout.setPrefWidth(350);

        backgroundLayout.setStyle("-fx-background-color: transparent; -fx-opacity:1;");
        backgroundLayout.getChildren().addAll(getTitle(),catagories.getLilotho());


        return  backgroundLayout;
    }

    public Label getTitle()
    {
        String title = "Khetha Papali eo u ka e bapalang";

        Label titledLabel = new Label(title);
        titledLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        return titledLabel;
    }



}