package com.gluonapplication.views;

import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.animation.*;
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

public class LiaparoGame extends View {

    private static final int LEVELS_PER_CATEGORY = 3;
    private static final int TOTAL_CATEGORIES = 3;
    private final boolean[] levelResults = new boolean[LEVELS_PER_CATEGORY];
    private MediaPlayer videoPlayer;
    private MediaPlayer audioPlayer;
    private MediaView mediaView;
    private int currentLevelIndex = 0;
    private int currentCategory;
    private Media video;
    private MediaPlayer mediaPlayer;
    private MediaView videoView;
    private LevelsView4 levelsView = new LevelsView4();

    // Timer related variables
    private Timeline questionTimer;
    private DoubleProperty timeRemaining = new SimpleDoubleProperty(10);
    private ProgressBar timerProgressBar;

    public LiaparoGame() {
        initializeUI();
        loadFirstLevel();
    }

    private void initializeUI() {
        getStylesheets().add(getClass().getResource("primary.css").toExternalForm());
        currentCategory = Integer.parseInt(PrimaryView.getLevelnum());
    }

    private void loadFirstLevel() {
        currentLevelIndex = 0;
        showLevel(currentLevelIndex);
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
        String[][][] questions = new String[12][3][7];
        questions[9] = new String[][] {
                {"/ts'ea.jpg","Ts'ea ke ?", "mose o tenoang ke baroetsana\n ha ba bina pina \ntsa mokopu", "ke kobo ea Basotho ", "seaparo se tenoang ke \nbahlanka ba tsoang lebollong", "kobo e aparoang ke makoti", "2"},
                {"/pokoma.jpg","pokoma ke ?", "mose o tenoang ke baroetsana \nha ba bina pina tsa mokopu", "ke kobo ea Basotho ", "kobo e aparoang ke makoti", "mose o tenoang ke baroetsana", "0"},
                {"/tjale.jpg","tjale ke ?", "mose o aparoang ke basali\n ba basotho", "kobo e aparoang ke makoti", "mose o tenoang ke baroetsana", "ke mose oa khomo", "1"}
        };

        questions[10] = new String[][] {
                {"/kholokoane.jpg","khetha seaparo se amanang\n le sets'oants'o se ka holimo ", "Thethana", "pokoma", "Kholokoane", "tjale", "2"},
                {"/mose-oa-khomo.jpg","khetha seaparo se amanang\n le sets'oants'o se ka holimo ", "ts'ea","seanamarena", "sebeto", "mose-oa-khomo", "3"},
                {"/letata.jpg","khetha seaparo se amanang\n le sets'oants'o se ka holimo ", "letata", "tjale", "kholokoane", "seanamarena", "0"}
        };

        questions[11] = new String[][] {
                {"/thethana.jpg","khetha seaparo se amanang \nle sets'oants'o se ka holimo ", "pokoma", "thethana", "kholokoane", "letata", "1"},
                {"/mokorotlo.jpg","khetha seaparo se amanang \nle sets'oants'o se ka holimo ", "sebeto", "ts'ea", "pokoma", "Mokorotlo", "3"},
                {"/sebeto.jpg","khetha seaparo se amanang le \nsets'oants'o se ka holimo ", "kholokoane", "thethana", "sebeto", "tjale", "2"}
        };

        if (currentCategory >= 9 && currentCategory <= 11 &&
                questions[currentCategory] != null &&
                levelIndex < questions[currentCategory].length) {

            String[] questionData = questions[currentCategory][levelIndex];
            if (questionData != null && questionData.length >= 7) {
                String questionText =  questionData[1];
                String[] options = {questionData[2], questionData[3], questionData[4], questionData[5]};
                int correctIndex = Integer.parseInt(questionData[6]);
                return createQuestion(questionData[0], questionText, options, correctIndex);
            }
        }
        return createResultsView();
    }

    private VBox createOptionImage(String url) {
        Image image = new Image(url);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(70);
        imageView.setFitWidth(150);

        VBox imageHolder = new VBox(imageView);
        imageHolder.setAlignment(Pos.TOP_CENTER);
        return imageHolder;
    }

    private VBox createQuestion(String url, String questionText, String[] options, int correctIndex) {
        Label questionLabel = new Label(questionText);
        questionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        questionLabel.setWrapText(true);

        // Timer progress bar
        timerProgressBar = new ProgressBar();
        timerProgressBar.setPrefWidth(300);
        timerProgressBar.setMaxWidth(Double.MAX_VALUE);
        timerProgressBar.progressProperty().bind(timeRemaining.divide(10));
        timerProgressBar.setStyle("-fx-accent: #4CAF50;");

        VBox optionsBox = new VBox(10);
        optionsBox.setAlignment(Pos.CENTER);

        for (int i = 0; i < options.length; i++) {
            Button optionButton = createOptionButton(options[i], i == correctIndex);
            optionsBox.getChildren().add(optionButton);
        }

        VBox questionBox = new VBox(20, createOptionImage(url), questionLabel, timerProgressBar, optionsBox);
        questionBox.setAlignment(Pos.CENTER);

        startQuestionTimer(correctIndex);
        return questionBox;
    }

    private Button createOptionButton(String text, boolean isCorrect) {
        Button button = new Button(text);
        button.setUserData(isCorrect);
        button.setStyle("-fx-font-size: 13px; " +
                "-fx-pref-width: 210px; " +
                "-fx-pref-height: 60px; " +
                "-fx-background-radius: 25; " +
                "-fx-border-radius: 25; ");
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

        levelResults[currentLevelIndex] = isCorrect;

        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(event -> {
            currentLevelIndex++;
            if (currentLevelIndex < LEVELS_PER_CATEGORY) {
                showLevel(currentLevelIndex);
            } else {
                showResultsView();
            }
        });
        delay.play();
    }

    private void startQuestionTimer(int correctIndex) {
        if (questionTimer != null) {
            questionTimer.stop();
        }

        timeRemaining.set(20);
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
                }));
        questionTimer.setCycleCount(Timeline.INDEFINITE);
        questionTimer.play();
    }

    private void timeUp(int correctIndex) {
        VBox questionBox = (VBox) timerProgressBar.getParent();
        VBox optionsBox = (VBox) questionBox.getChildren().get(3);

        for (var node : optionsBox.getChildren()) {
            Button button = (Button) node;
            button.setDisable(true);

            if ((boolean) button.getUserData()) {
                button.setStyle(button.getStyle() +
                        "-fx-background-color: #4CAF50; " +
                        "-fx-text-fill: white;");
            }
        }

        levelResults[currentLevelIndex] = false;

        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(event -> {
            currentLevelIndex++;
            if (currentLevelIndex < LEVELS_PER_CATEGORY) {
                showLevel(currentLevelIndex);
            } else {
                showResultsView();
            }
        });
        delay.play();
    }

    private void showResultsView() {
        int correctCount = countCorrectAnswers();
        VBox resultsView = createResultsView(correctCount);
        ScrollPane scrollPane = new ScrollPane(resultsView);
        scrollPane.setFitToWidth(true);
        setCenter(scrollPane);

        if (correctCount == LEVELS_PER_CATEGORY) {
            playSuccessAnimation();

            // Update icons based on current category progression
            switch(currentCategory) {
                case 9:
                    levelsView.setL1Icon("/win1.png");
                    // Unlock next category (7)
                    PrimaryView.setLevelnum("10");
                    break;
                case 10:
                    levelsView.setL2Icon("/win2.png");
                    // Unlock next category (8)
                    PrimaryView.setLevelnum("11");
                    break;
                case 11:
                    levelsView.setL3Icon("/win3.png");
                    // All categories completed
                    PrimaryView.setLevelnum("12");
                    break;
            }

            // Refresh the levels view
            getAppManager().switchView("LevelsView");

            StackPane mediaContainer = new StackPane(mediaView);
            mediaContainer.setPadding(new Insets(10));
            mediaContainer.setAlignment(Pos.CENTER);
            resultsView.getChildren().add(mediaContainer);
        }
    }

    private VBox createResultsView() {
        return createResultsView(countCorrectAnswers());
    }

    private VBox createResultsView(int correctCount) {
        Label resultLabel = new Label(String.format(
                "U fumane likarabo \n tse nepahetseng tse: %d/%d",
                correctCount, LEVELS_PER_CATEGORY
        ));
        resultLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        Button homeButton = new Button("Ea Lapeng");
        homeButton.setOnAction(e -> {
            onHidden();
            getAppManager().goHome();
        });

        Button retryButton = new Button("Bapala");
        retryButton.setOnAction(e -> {
            onHidden();
            loadFirstLevel();
        });

        Button nextCategoryButton = new Button("Lapeng");
        nextCategoryButton.setOnAction(e -> {
            // Move to next category if available

            getAppManager().goHome();

            HoldMediaPlayers();
        });

        VBox resultsBox = new VBox(20, resultLabel);
        resultsBox.setAlignment(Pos.CENTER);

        if (correctCount == LEVELS_PER_CATEGORY) {
            if (currentCategory < 8) { // If not the last category
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
            // Load media resources
            Media videoMedia = new Media(getClass().getResource("/applauseV.mp4").toString());
            Media audioMedia = new Media(getClass().getResource("/claps.mp3").toString());
            videoPlayer = new MediaPlayer(videoMedia);
            audioPlayer = new MediaPlayer(audioMedia);

            // Create MediaView with initial transparent and scaled down state
            mediaView = new MediaView(videoPlayer);
            mediaView.setFitWidth(300);
            mediaView.setFitHeight(200);
            mediaView.setPreserveRatio(true);
            mediaView.setOpacity(0);
            mediaView.setScaleX(0.5);
            mediaView.setScaleY(0.5);

            // Add the MediaView to your scene (replace 'rootContainer' with your actual container)
            // For example: rootContainer.getChildren().add(mediaView);
            // Make sure to position it properly in your layout

            // Animation for video appearance
            FadeTransition fadeIn = new FadeTransition(Duration.millis(500), mediaView);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);

            ScaleTransition scaleIn = new ScaleTransition(Duration.millis(500), mediaView);
            scaleIn.setFromX(0.5);
            scaleIn.setFromY(0.5);
            scaleIn.setToX(1.0);
            scaleIn.setToY(1.0);

            ParallelTransition appearAnimation = new ParallelTransition(fadeIn, scaleIn);

            // Animation for video disappearance
            FadeTransition fadeOut = new FadeTransition(Duration.millis(500), mediaView);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);

            ScaleTransition scaleOut = new ScaleTransition(Duration.millis(500), mediaView);
            scaleOut.setFromX(1.0);
            scaleOut.setFromY(1.0);
            scaleOut.setToX(0.5);
            scaleOut.setToY(0.5);

            ParallelTransition disappearAnimation = new ParallelTransition(fadeOut, scaleOut);

            // Set up the sequence of events
            videoPlayer.setOnReady(() -> {
                // Play the appear animation
                appearAnimation.play();

                // Start media playback after appear animation finishes
                appearAnimation.setOnFinished(e -> {
                    videoPlayer.play();
                    audioPlayer.play();
                });

                // Schedule cleanup after 10 seconds
                PauseTransition delay = new PauseTransition(Duration.seconds(10));
                delay.setOnFinished(e -> {
                    // Play the disappear animation
                    disappearAnimation.play();

                    // Clean up after disappear animation finishes
                    disappearAnimation.setOnFinished(ev -> {
                        cleanupMediaPlayers();
                        // Remove the MediaView from the scene
                        // For example: rootContainer.getChildren().remove(mediaView);
                    });
                });
                delay.play();
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
        if (videoPlayer != null) {
            videoPlayer.stop();
        }
        if (audioPlayer != null) {
            audioPlayer.stop();
        }
    }

    @Override
    protected void updateAppBar(AppBar appBar) {
        appBar.setNavIcon(MaterialDesignIcon.ARROW_BACK.button(e -> {
            cleanupMediaPlayers();
            onHidden();
            getAppManager().goHome();
        }));
        appBar.setTitleText("PAPALI KA LIAPARO - BOEMO KE: " + currentCategory);
    }

    protected void onHidden() {
        cleanupMediaPlayers();
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