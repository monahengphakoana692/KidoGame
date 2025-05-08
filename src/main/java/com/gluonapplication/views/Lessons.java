package com.gluonapplication.views;

import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Lessons extends View {

    public Lessons() {
        VBox mainBox = new VBox(30);
        mainBox.setPadding(new Insets(20));
        mainBox.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("THUTO EA SESOTHO");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));

        mainBox.getChildren().add(title);
        mainBox.getChildren().add(createLessonSection("Lilotho", "/background.jpg", "Lipotso le likarabo tse bonolo ka litšoantšo tse hlalosang mantsoe.",
                "Lipotso tsena li thusa bana ho ithuta mantsoe a Sesotho ka mokhoa o monate."));

        mainBox.getChildren().add(createLessonSection("Maele", "/background.jpg", "Maele a bohlokoa bophelong. Bana ba ithuta ho bolela le ho utloisisa lithothokiso.",
                "Re hlalosa moelelo oa maele ka lipale tse khutšoanyane."));

        mainBox.getChildren().add(createLessonSection("Lipapali", "/background.jpg", "Lipapali tsa setso le litšoantšo li ruta bana ho bapala ka tsela ea Sesotho.",
                "E ruta litekanyo le lithuto ka papali."));

        mainBox.getChildren().add(createLessonSection("Lijo tsa Sesotho", "/background.jpg", "Tseba mefuta ea lijo tsa Sesotho tse kang motoho, papa, nama, le meroho.",
                "E kenyelletsa litšoantšo le mantsoe a hlalosang."));

        mainBox.getChildren().add(createLessonSection("Liaparo tsa Sesotho", "/background.jpg", "Bana ba ithuta ka liaparo tsa Sesotho tse kang seshoeshoe, likhiba, le likobo.",
                "Litšoantšo li bontša lebitso la seaparo le hore na se a aparoa neng."));

        ScrollPane scrollPane = new ScrollPane(mainBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        setCenter(scrollPane);
    }

    private VBox createLessonSection(String titleText, String imagePath, String introText, String explanation) {
        VBox section = new VBox(10);
        section.setAlignment(Pos.TOP_LEFT);
        section.setPadding(new Insets(10));
        section.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 10; -fx-padding: 15;");

        ImageView icon = new ImageView(new Image(imagePath));
        icon.setFitHeight(50);
        icon.setFitWidth(50);

        Label title = new Label(titleText);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setStyle("-fx-text-fill: black;");

        Label intro = new Label(introText);
        intro.setWrapText(true);
        intro.setFont(Font.font("Arial", 14));
        intro.setStyle("-fx-text-fill: black;");

        Label details = new Label(explanation);
        details.setWrapText(true);
        details.setFont(Font.font("Arial", 13));
        details.setStyle("-fx-text-fill: black;");

        section.getChildren().addAll(icon, title, intro, details);
        return section;
    }

    @Override
    protected void updateAppBar(AppBar appBar) {
        appBar.setNavIcon(MaterialDesignIcon.ARROW_BACK.button(e -> getAppManager().goHome()));
        appBar.setTitleText("THUTO");
    }
}
