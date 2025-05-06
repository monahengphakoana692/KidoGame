package com.gluonapplication;

import com.gluonapplication.views.SecondaryView;
import com.gluonhq.attach.lifecycle.LifecycleService;
import com.gluonhq.attach.util.Platform;
import com.gluonhq.attach.util.Services;
import com.gluonhq.charm.glisten.application.AppManager;
import com.gluonhq.charm.glisten.control.Avatar;
import com.gluonhq.charm.glisten.control.NavigationDrawer;
import com.gluonhq.charm.glisten.control.NavigationDrawer.Item;
import com.gluonhq.charm.glisten.control.NavigationDrawer.ViewItem;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.scene.image.Image;

import static com.gluonapplication.GluonApplication.*;
import static com.gluonhq.charm.glisten.application.AppManager.HOME_VIEW;

  public class DrawerManager {

    public static void buildDrawer(AppManager app) {
        NavigationDrawer drawer = app.getDrawer();

        NavigationDrawer.Header header = new NavigationDrawer.Header("Sample",
                "profile",
                new Avatar(21, new Image(DrawerManager.class.getResourceAsStream("/game.jpg"))));
        drawer.setHeader(header);

        final Item homeItem = new ViewItem("PrimaryView", MaterialDesignIcon.HOME.graphic(), HOME_VIEW);
        final Item secondView = new ViewItem("secondView", MaterialDesignIcon.HOME.graphic(),SECONDARY_VIEW);
        drawer.getItems().addAll(homeItem,secondView);

        if (Platform.isDesktop()) {
            final Item quitItem = new Item("Quit", MaterialDesignIcon.EXIT_TO_APP.graphic());
            quitItem.selectedProperty().addListener((obs, ov, nv) -> {
                if (nv) {
                    Services.get(LifecycleService.class).ifPresent(LifecycleService::shutdown);
                }
            });
            drawer.getItems().add(quitItem);
        }
    }
}