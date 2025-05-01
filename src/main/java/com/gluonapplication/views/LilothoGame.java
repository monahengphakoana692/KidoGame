package com.gluonapplication.views;

import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class LilothoGame extends View {

    private static final String[] VIEW_NAMES = {"FirstLevel", "SecondLevel", "ThirdLevel"};
    private boolean[] choices = new boolean[3];
    private MediaPlayer mediaPlayer;
    private MediaPlayer mediaPlayerSound;
    private MediaView mediaView;
    private MediaView mediaViewSound;

    public LilothoGame() {
        showLevel(0);
    }

    private void showLevel(int levelIndex) {
        switch (levelIndex) {
            case 0:
                setCenter(createLevelView(createFirstQuestion(), levelIndex));
                break;
            case 1:
                setCenter(createLevelView(createSecondQuestion(), levelIndex));
                break;
            case 2:
                setCenter(createLevelView(createThirdQuestion(), levelIndex));
                break;
            default:
                showResults();
                break;
        }
    }

    private StackPane createLevelView(VBox questionBox, int levelIndex) {
        getStylesheets().add(PrimaryView.class.getResource("primary.css").toExternalForm());

        Image backgroundImage = new Image("/background.jpg");
        ImageView imageView = new ImageView(backgroundImage);
        imageView.setStyle("-fx-opacity:0.2;");
        imageView.setPreserveRatio(false);
        imageView.setFitWidth(315);
        imageView.setFitHeight(600);

        StackPane background = new StackPane();
        background.setMaxWidth(200);
        background.setMaxHeight(600);
        background.setStyle("-fx-background-color:transparent;");
        background.getChildren().addAll(imageView, questionBox);
        background.setPadding(new javafx.geometry.Insets(10, 10, 0, 10));

        return background;
    }

    private VBox createFirstQuestion() {
        Label question = new Label("'Me Nts'oare ke nye");
        question.setFont(Font.font("Arial", FontWeight.BOLD, 34));

        Button[] options = {
                createOption("Leihlo", false, 0),
                createOption("Tsebe", false, 0),
                createOption("Ngoana", false, 0),
                createOption("Nko", true, 0)
        };

        return createQuestionLayout(question, options);
    }

    private VBox createSecondQuestion() {
        Label question = new Label("Ts'oene tse peli \n Lihloa thaba \n lisa e qete?");
        question.setFont(Font.font("Arial", FontWeight.BOLD, 30));

        Button[] options = {
                createOption("Matsoele", false, 1),
                createOption("Moriri", false, 1),
                createOption("Litsebe", true, 1),
                createOption("Li Ts'oene", false, 1)
        };

        return createQuestionLayout(question, options);
    }

    private VBox createThirdQuestion() {
        Label question = new Label("Mohlankana ea lulang \n lehaheng?");
        question.setFont(Font.font("Arial", FontWeight.BOLD, 30));

        Button[] options = {
                createOption("Thimola", false, 2),
                createOption("Leleme", true, 2),
                createOption("Khoeli", false, 2),
                createOption("Mokhubu", false, 2)
        };

        return createQuestionLayout(question, options);
    }

    private Button createOption(String text, boolean isCorrect, int levelIndex) {
        Button button = new Button(text);
        button.setOnAction(e -> {
            choices[levelIndex] = isCorrect;
            if (levelIndex < VIEW_NAMES.length - 1) {
                showLevel(levelIndex + 1);
            } else {
                showResults();
            }
        });
        return button;
    }

    private VBox createQuestionLayout(Label question, Button... options) {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().add(question);
        layout.getChildren().addAll(options);
        return layout;
    }

    private void showResults() {
        int correctAnswers = 0;
        for (boolean choice : choices) {
            if (choice) correctAnswers++;
        }

        // Initialize media components
        Media media = new Media(getClass().getResource("/applauseV.mp4").toString());
        Media mediaSound = new Media(getClass().getResource("/claps.mp3").toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayerSound = new MediaPlayer(mediaSound);
        mediaView = new MediaView(mediaPlayer);
        mediaViewSound = new MediaView(mediaPlayerSound);
        mediaView.setFitWidth(300);  // Set appropriate size
        mediaView.setFitHeight(200);

        // Play the video
        mediaPlayer.setCycleCount(1); // Play only once
        mediaPlayerSound.setCycleCount(2);


        Label resultLabel = new Label("U fumane likarabo \n tse nepahetseng: " + correctAnswers + "/3");
        resultLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        Button homeButton = new Button("Ea Lapeng");
        homeButton.setOnAction(e -> {
            onHidden();// Stop video when leaving
            getAppManager().goHome();
        });

        VBox resultsView = new VBox(20, resultLabel, homeButton);
        resultsView.setAlignment(Pos.CENTER);



        if (correctAnswers == 3)
        {
            mediaPlayer.play();
            mediaPlayerSound.play();
            resultsView.getChildren().addAll(mediaView);
            PrimaryView.setLevelnum("1");

        }

        ScrollPane scrollPane = new ScrollPane(resultsView);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        setCenter(scrollPane);
    }

    @Override
    protected void updateAppBar(AppBar appBar)
    {
        Button navButton = MaterialDesignIcon.ARROW_BACK.button(e ->
                {
                    getAppManager().goHome();
                    onHidden();
                }
        );

        appBar.setNavIcon(navButton);
        appBar.setTitleText("PAPALI KA LILOTHO");
    }

    protected void onHidden() {
        // Clean up media player when view is hidden
        if ((mediaPlayer != null || mediaPlayerSound !=null) || (mediaPlayer != null && mediaPlayerSound !=null) )
        {
            mediaPlayerSound.stop();
            mediaPlayerSound.dispose();
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }
    }
}