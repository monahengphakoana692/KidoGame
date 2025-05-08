package com.gluonapplication.views;

import com.gluonhq.charm.glisten.animation.BounceInRightTransition;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.FloatingActionButton;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SecondaryView extends View {

    public SecondaryView() {
        
        getStylesheets().add(SecondaryView.class.getResource("secondary.css").toExternalForm());
        
        VBox Container = new VBox(10);
        Container.setAlignment(Pos.TOP_CENTER);

        HBox sectionCategories = new HBox(10);
        sectionCategories.setAlignment(Pos.CENTER);
        VBox Lilotho = new VBox(10);
        Lilotho.getChildren().add(new Label("Lilotho"));
        VBox Maele = new VBox(10);
        Maele.getChildren().add(new Label("Maele"));
        VBox Lipapali = new VBox(10);
        Lipapali.getChildren().add(new Label("Lapapali"));


        sectionCategories.getChildren().addAll(Lilotho,Maele,Lipapali);
        Container.getChildren().addAll(sectionCategories);


        VBox controls = new VBox(Container);
        controls.setAlignment(Pos.CENTER);
        
        setCenter(controls);
        
        setShowTransitionFactory(BounceInRightTransition::new);
        
        FloatingActionButton floatingActionButton = new FloatingActionButton(MaterialDesignIcon.INFO.text,
                e -> System.out.println("Info")  );
        floatingActionButton.showOn(this);
    }

    @Override
    protected void updateAppBar(AppBar appBar) {
        appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> getAppManager().getDrawer().open()));
        appBar.setTitleText("Secondary");
        appBar.getActionItems().add(MaterialDesignIcon.FAVORITE.button(e -> System.out.println("Favorite")));
    }
    
}
