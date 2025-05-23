package com.gluonapplication;

import com.gluonapplication.views.*;
import com.gluonhq.charm.glisten.application.AppManager;
import com.gluonhq.charm.glisten.visual.Swatch;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import static com.gluonhq.charm.glisten.application.AppManager.HOME_VIEW;

public class GluonApplication extends Application {

    public static final String PRIMARY_VIEW = HOME_VIEW;
    public static final String SECONDARY_VIEW = "Second View";
    public static final String LESSSONS_VIEW = "Lessons";


    private final AppManager appManager = AppManager.initialize(this::postInit);

    @Override
    public void init()
    {
       System.setProperty("charm.glisten.license.hide", "true");  // Disables license checks
        System.setProperty("charm.glisten.tracking.hide", "true");
        System.setProperty("attach.storage.path", System.getProperty("user.home") + "/.KidoGame");
        appManager.addViewFactory(PRIMARY_VIEW, PrimaryView::new);
        //appManager.addViewFactory(SECONDARY_VIEW, SecondaryView::new);
        appManager.addViewFactory(LESSSONS_VIEW, Lessons::new);


        DrawerManager.buildDrawer(appManager);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        primaryStage.setResizable(false);
        appManager.start(primaryStage);
    }

    private void postInit(Scene scene) {
        Swatch.YELLOW.assignTo(scene); // Changed to blue for a travel app feel
        scene.getStylesheets().add(GluonApplication.class.getResource("style.css").toExternalForm());
        ((Stage) scene.getWindow()).getIcons().add(new Image(GluonApplication.class.getResourceAsStream("/game.jpg")));
    }

    public static void main(String[] args) {
        launch(args);
    }
}