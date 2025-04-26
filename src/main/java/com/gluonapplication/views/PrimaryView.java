package com.gluonapplication.views;

import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.Icon;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class PrimaryView extends View {

    public PrimaryView()
    {

        getStylesheets().add(PrimaryView.class.getResource("primary.css").toExternalForm());

        Image backgroundImage = new Image("/background.jpg");
        HomeView homeView = new HomeView();
        ImageView imageView = new ImageView(backgroundImage);
        imageView.setPreserveRatio(false);
        StackPane background1 = new StackPane();
        background1.setMaxWidth(200);
        background1.setMaxHeight(600);
        background1.setStyle("-fx-background-color:yellow;");

        imageView.setFitWidth(315);
        imageView.setFitHeight(600);

        background1.getChildren().addAll(imageView,homeView.getBackgroundLayout());
        background1.setPadding(new javafx.geometry.Insets(10, 10, 0, 10));

        VBox controls = new VBox(background1);
        controls.setAlignment(Pos.CENTER);

        setCenter(controls);
    }

    @Override
    protected void updateAppBar(AppBar appBar) {
        appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> getAppManager().getDrawer().open()));
        appBar.setTitleText("Primary");
        appBar.getActionItems().add(MaterialDesignIcon.SEARCH.button(e -> System.out.println("Search")));
    }



}