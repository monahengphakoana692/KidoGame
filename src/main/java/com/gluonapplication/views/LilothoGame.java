package com.gluonapplication.views;

import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
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
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LilothoGame extends View {

    private static final int LEVELS_PER_CATEGORY = 3;
    private static final int TOTAL_CATEGORIES = 5;
    private final boolean[] levelResults = new boolean[LEVELS_PER_CATEGORY];
    private MediaPlayer videoPlayer;
    private MediaPlayer audioPlayer;
    private MediaView mediaView;
    private int currentLevel = 0;
    private int currentCategory = 0;
    private LevelsView levelsView = new LevelsView();

    private Timeline questionTimer;
    private DoubleProperty timeRemaining = new SimpleDoubleProperty(10);
    private ProgressBar timerProgressBar;

    public LilothoGame() {
        initializeUI();
        loadFirstLevel();
    }

    private void initializeUI() {
        getStylesheets().add(getClass().getResource("primary.css").toExternalForm());
        currentCategory = Integer.parseInt(PrimaryView.getLevelnum());
    }

    private void loadFirstLevel() {
        currentLevel = 0;
        showLevel(currentLevel);
    }

    public void showLevel(int levelIndex) {
        VBox questionView = createQuestionView(levelIndex);
        StackPane levelContainer = createLevelContainer(questionView);
        setCenter(levelContainer);
    }

    private StackPane createLevelContainer(VBox content) {
        Image background = new Image("/background.jpg");
        ImageView bgView = new ImageView(background);
        bgView.setOpacity(0.2);
        bgView.setPreserveRatio(false);
        bgView.setFitWidth(315);
        bgView.setFitHeight(600);

        StackPane container = new StackPane();
        container.setMaxSize(200, 600);
        container.setStyle("-fx-background-color: transparent;");
        container.getChildren().addAll(bgView, content);
        container.setPadding(new Insets(10));

        return container;
    }

    private VBox createQuestionView(int levelIndex) {
        String[][][] questions = {
                // Category 0
                {
                        {"/nko.jpg","'Me Nts'oare ke nye", "Leihlo", "Tsebe", "Ngoana", "Nko", "3"},
                        {"/litsebe.jpg","Ts'oene tse peli \n Lihloa thaba \n lisa e qete?", "Matsoele", "Moriri", "Litsebe", "Li Ts'oene", "2"},
                        {"/Leleme.jpg","Mohlankana ea lulang \n lehaheng?", "Thimola", "Leleme", "Khoeli", "Mokhubu", "1"}
                },
                // Category 1
                {
                        {"/moholu.jpg","khetha selotho sa amanang le sets'oants'o se amanang le se ka holimo","Ka qhala Phoofo \n ka ja mokotla? ", "nthethe a bina moholo a lutse", "mala nku mararang", "mokopu ha u nama", "0"},
                        {"/likhobe.jpg","khetha selotho sa amanang le sets'oants'o se \n amanang le se ka holimo", "O monate fela oa hlaba? ", "Ka qhala Phoofo \n ka ja mokotla?","Thankha-Thankha ketla tsoalla kae","Maqheku a qabana ka lehaheng", "3"},
                        {"/mokopu.jpg","khetha selotho sa amanang le sets'oants'o se \n amanang le se ka holimo","Thankha-Thankha ketla tsoalla kae?", "Setoto se ts'ela tsela?", "Mohaisane o tlola jarete?", "tsa anehoa tsa tsoha li ile?", "0"}
                },
                // Category 2
                {
                        {"/Leleme.jpg","Phate li ea lekana?", "Likobo", "Lefats'e", "Leholimo le Lefats'e", "Leholimo", "2"},
                        {"/Leleme.jpg","Mala a nku marang-rang?", "Boea", "Moholu", "Moraha ka sakeng", "Mohloa", "3"},
                        {"/Leleme.jpg","Setoto se tlala ntlo?", "Moraha ka sakeng", "Metsi", "Jwala bo qhalaneng", "Bana ba hae", "0"}
                }
        };

        if (currentCategory < TOTAL_CATEGORIES && levelIndex < LEVELS_PER_CATEGORY) {
            String[] questionData = questions[currentCategory][levelIndex];
            String url = questionData[0];
            String questionText = questionData[1];
            String[] options = {questionData[2], questionData[3], questionData[4], questionData[5]};
            int correctIndex = Integer.parseInt(questionData[6]);

            return createQuestion(url, questionText, options, correctIndex);
        } else {
            return createResultsView();
        }
    }

    private VBox createQuestion(String url, String questionText, String[] options, int correctIndex) {
        Label questionLabel = new Label(questionText);
        questionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        questionLabel.setWrapText(true);

        timerProgressBar = new ProgressBar();
        timerProgressBar.setPrefWidth(300);
        timerProgressBar.setMaxWidth(Double.MAX_VALUE);
        timerProgressBar.progressProperty().bind(timeRemaining.divide(10));
        timerProgressBar.setStyle("-fx-accent: #4CAF50;");

        VBox optionsBox = new VBox(10);
        optionsBox.setAlignment(Pos.CENTER);

        List<Integer> indexList = new ArrayList<>();
        for (int i = 0; i < options.length; i++) {
            indexList.add(i);
        }
        Collections.shuffle(indexList); // Randomizing the order

        for (int i : indexList) {
            boolean isCorrect = (i == correctIndex); // preserve correct flag
            Button optionButton = createOptionButton(options[i], isCorrect);
            optionsBox.getChildren().add(optionButton);
        }

        VBox questionBox = new VBox(20, createOptionImage(url), questionLabel, timerProgressBar, optionsBox);
        questionBox.setAlignment(Pos.CENTER);

        startQuestionTimer(correctIndex);
        return questionBox;
    }

    private VBox createOptionImage(String url) {
        Image image = new Image(url);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(100);
        imageView.setFitWidth(150);

        VBox imageHolder = new VBox(imageView);
        imageHolder.setAlignment(Pos.TOP_CENTER);
        return imageHolder;
    }

    private Button createOptionButton(String text, boolean isCorrect) {
        Button button = new Button(text);
        button.setUserData(isCorrect);
        button.setStyle("-fx-font-size: 13px; -fx-pref-width: 210px; -fx-pref-height: 30px; -fx-background-radius: 25; -fx-border-radius: 25;");
        button.setOnAction(e -> handleAnswer(button, isCorrect));
        return button;
    }

    private void handleAnswer(Button selectedButton, boolean isCorrect) {
        if (questionTimer != null) {
            questionTimer.stop();
        }

        VBox optionsBox = (VBox) selectedButton.getParent();
        for (var node : optionsBox.getChildren()) {
            Button button = (Button) node;
            button.setDisable(true);

            if ((boolean) button.getUserData()) {
                button.setStyle(button.getStyle() + "-fx-background-color: #4CAF50; -fx-text-fill: white;");
                if (button == selectedButton) {
                    CorrectSound();
                }
            } else if (button == selectedButton) {
                button.setStyle(button.getStyle() + (isCorrect ? "-fx-background-color: #4CAF50;" : "-fx-background-color: #F44336;") + " -fx-text-fill: white;");
                IncorrectSound();
            }
        }

        levelResults[currentLevel] = isCorrect;

        PauseTransition delay = new PauseTransition(Duration.seconds(1.5));
        delay.setOnFinished(event -> {
            currentLevel++;
            if (currentLevel < LEVELS_PER_CATEGORY) {
                showLevel(currentLevel);
            } else {
                showResultsView();
            }
        });
        delay.play();
    }

    private void showResultsView() {
        int correctCount = countCorrectAnswers();
        PrimaryView.setCorrectAnswers(Integer.toString(correctCount + Integer.parseInt(PrimaryView.getCorrectAnswers())));
        PrimaryView.setTotalQuestionsAnswered(Integer.toString(LEVELS_PER_CATEGORY));
        PrimaryView.setWrongAnswers(Integer.toString(Integer.parseInt(PrimaryView.getWrongAnswers())));

        VBox resultsView = createResultsView(correctCount);
        ScrollPane scrollPane = new ScrollPane(resultsView);
        scrollPane.setFitToWidth(true);
        setCenter(scrollPane);

        if (correctCount == LEVELS_PER_CATEGORY) {
            playSuccessAnimation();
            switch (PrimaryView.getLevelnum()) {
                case "1": levelsView.setL1Icon("/win1.png"); break;
                case "2": levelsView.setL2Icon("/win2.png"); break;
                case "3": levelsView.setL3Icon("/win3.png"); break;
            }
            getAppManager().switchView("LevelsView");

            StackPane mediaContainer = new StackPane(mediaView);
            mediaContainer.setPadding(new Insets(10));
            mediaContainer.setAlignment(Pos.CENTER);
            resultsView.getChildren().add(mediaContainer);

            if (currentCategory < TOTAL_CATEGORIES - 1) {
                currentCategory++;
                PrimaryView.setLevelnum(String.valueOf(currentCategory));
            }
        }
    }

    private VBox createResultsView() {
        return createResultsView(countCorrectAnswers());
    }

    private VBox createResultsView(int correctCount) {
        Label resultLabel = new Label(String.format("U fumane likarabo \n tse nepahetseng tse: %d/%d", correctCount, LEVELS_PER_CATEGORY));
        resultLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        Button homeButton = new Button("Ea Lapeng");
        homeButton.setOnAction(e -> {
            onHidden();
            getAppManager().goHome();
        });

        Button retryButton = new Button("Leka ho Lekha");
        retryButton.setOnAction(e -> {
            onHidden();
            loadFirstLevel();
        });

        Button nextCategoryButton = new Button("Karolo e 'ngoe");
        nextCategoryButton.setOnAction(e -> {
            getAppManager().goHome();
            HoldMediaPlayers();
        });

        VBox resultsBox = new VBox(20, resultLabel);
        resultsBox.setAlignment(Pos.CENTER);

        if (correctCount == LEVELS_PER_CATEGORY) {
            if (currentCategory < TOTAL_CATEGORIES - 1) {
                resultsBox.getChildren().add(nextCategoryButton);
            } else {
                resultsBox.getChildren().add(homeButton);
            }
            if (mediaView != null) {
                resultsBox.getChildren().add(mediaView);
            }
        } else {
            resultsBox.getChildren().addAll(retryButton, homeButton);
        }

        return resultsBox;
    }

    private int countCorrectAnswers() {
        int count = 0;
        for (boolean result : levelResults) {
            if (result) count++;
        }
        return count;
    }

    private void playSuccessAnimation() {
        cleanupMediaPlayers();
        try {
            Media videoMedia = new Media(getClass().getResource("/applauseV.mp4").toString());
            Media audioMedia = new Media(getClass().getResource("/claps.mp3").toString());
            videoPlayer = new MediaPlayer(videoMedia);
            audioPlayer = new MediaPlayer(audioMedia);
            mediaView = new MediaView(videoPlayer);
            mediaView.setFitWidth(300);
            mediaView.setFitHeight(200);
            mediaView.setPreserveRatio(true);
            videoPlayer.setOnReady(() -> {
                videoPlayer.play();
                audioPlayer.play();
            });
            videoPlayer.setCycleCount(1);
            audioPlayer.setCycleCount(2);
        } catch (Exception e) {
            System.err.println("Error loading media: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void cleanupMediaPlayers() {
        if (videoPlayer != null) {
            videoPlayer.stop();
            videoPlayer.dispose();
            videoPlayer = null;
        }
        if (audioPlayer != null) {
            audioPlayer.stop();
            audioPlayer.dispose();
            audioPlayer = null;
        }
    }

    private void HoldMediaPlayers() {
        if (videoPlayer != null) videoPlayer.stop();
        if (audioPlayer != null) audioPlayer.stop();
    }

    private void timeUp(int correctIndex) {
        VBox questionBox = (VBox) timerProgressBar.getParent();
        VBox optionsBox = (VBox) questionBox.getChildren().get(3);
        for (var node : optionsBox.getChildren()) {
            Button button = (Button) node;
            button.setDisable(true);
            if ((boolean) button.getUserData()) {
                button.getStyleClass().add("correct-answer");
            }
        }
        levelResults[currentLevel] = false;
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(event -> {
            currentLevel++;
            if (currentLevel < LEVELS_PER_CATEGORY) {
                showLevel(currentLevel);
            } else {
                showResultsView();
            }
        });
        delay.play();
    }

    @Override
    protected void updateAppBar(AppBar appBar) {
        appBar.setNavIcon(MaterialDesignIcon.ARROW_BACK.button(e -> {
            cleanupMediaPlayers();
            onHidden();
            getAppManager().goHome();
        }));
        appBar.setTitleText("PAPALI KA LILOTHO - BOEMO KE: " + (currentCategory));
    }

    protected void onHidden() {
        cleanupMediaPlayers();
    }

    private void startQuestionTimer(int correctIndex) {
        if (questionTimer != null) {
            questionTimer.stop();
        }
        timeRemaining.set(10);
        questionTimer = new Timeline(
                new KeyFrame(Duration.seconds(0.1), event -> {
                    timeRemaining.set(timeRemaining.get() - 0.1);
                    if (timeRemaining.get() < 3) {
                        timerProgressBar.setStyle("-fx-accent: #F44336;");
                    } else if (timeRemaining.get() < 7) {
                        timerProgressBar.setStyle("-fx-accent: #FFC107;");
                    }
                    if (timeRemaining.get() <= 0) {
                        questionTimer.stop();
                        timeUp(correctIndex);
                    }
                })
        );
        questionTimer.setCycleCount(Timeline.INDEFINITE);
        questionTimer.play();
    }

    public void CorrectSound()
    {
        Media audioMedia = new Media(getClass().getResource("/correct.mp3").toString());
        audioPlayer = new MediaPlayer(audioMedia);
        audioPlayer.play();
        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(event -> {
           audioPlayer.stop();
        });
        delay.play();

    }
    public void IncorrectSound()
    {
        Media audioMedia = new Media(getClass().getResource("/wrong.mp3").toString());
        audioPlayer = new MediaPlayer(audioMedia);
        audioPlayer.play();
        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(event -> {
            audioPlayer.stop();
        });
        delay.play();

    }
}
