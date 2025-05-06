package com.gluonapplication.views;

import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class PrimaryView extends View
{
    private static String Levelnum = "0";
    public static final String[] basicLevel = {"/L1.png","/L2.png","/L3.png"};
    public static final String[] AdvancedLevel = {"/win1.png","/win2.png","/win3.png"};

    public PrimaryView()
    {

        getStylesheets().add(PrimaryView.class.getResource("primary.css").toExternalForm());

        Image backgroundImage = new Image("/background.jpg");
        HomeView homeView = new HomeView();
        ImageView imageView = new ImageView(backgroundImage);
        imageView.setPreserveRatio(false);
        StackPane background1 = new StackPane();
        background1.setMaxWidth(200);
        background1.setMaxHeight(700);
        background1.setStyle("-fx-background-color:yellow;");

        imageView.setFitWidth(315);
        imageView.setFitHeight(900);

        background1.getChildren().addAll(imageView,homeView.getBackgroundLayout());
        background1.setPadding(new javafx.geometry.Insets(10, 10, 0, 10));

        VBox controls = new VBox(background1);
        controls.setAlignment(Pos.CENTER);

        setCenter(controls);

        ScrollPane scrollPane = new ScrollPane(controls);
        scrollPane.setFitToWidth(true); // Makes content fit viewport width
        scrollPane.setFitToHeight(true); // Makes content fit viewport height
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPannable(true); // Allows panning (touch scrolling)


        // Set the ScrollPane as the center of the View
        setCenter(scrollPane);
    }

    @Override
    protected void updateAppBar(AppBar appBar) {
        appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> getAppManager().getDrawer().open()));
        appBar.setTitleText("Primary");
        appBar.getActionItems().add(new Label("BOEMO: "+Levelnum));
    }

    public static void setLevelnum(String levelnum)

    {
        Levelnum = levelnum;
    }

    public static String getLevelnum()
    {
        return Levelnum;
    }


}