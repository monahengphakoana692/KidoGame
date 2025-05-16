package com.gluonapplication.views;

import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.animation.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
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

public class LipapliGame extends View {

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
    private LevelsView3 levelsView = new LevelsView3();

    // Timer related variables
    private Timeline questionTimer;
    private DoubleProperty timeRemaining = new SimpleDoubleProperty(10);
    private ProgressBar timerProgressBar;

    public LipapliGame() {
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

    private VBox createQuestionView(int levelIndex)
    {   String[][][] questions = null;
        questions = new String[9][3][7];
        questions[6] = new String[][] {
                {"/mokhibo.mp4","Mokhibo ke papali e ", "bapaloang ke banna \n kapa bahlankana?", "bapaloang ka banana \nba motse,ba sebelisa\n majoana?", "bapaloang ke bana le baroetsana\nba sebelisa lechoba le thebe?", "bapaloa ke banna le \nbahlankana ba sebelisa \nmelangoana le machoba?", "2"},
                {"/Litolobonya.mp4","litolobonya ke papali e ", "bapaloang ke baroetsana,\nbanana kapa basali ba \ntenne lithethana", "bapaloa ke banna le \n bahlankana ba sebelisa\n melanoana le machoba?", "bapaloang ka banana ba motse,\nba sebelisa majoana?", "bapaloang ke banana kapa \nbaroetsana ba sebelisa\n lesokoana?", "0"},
                {"/lesokoana.mp4","lesokoana ke papali e bapaloang \nke?", "banana kapa baroetsana\nba sebelisa\nlesokoana?", "bapaloang ka banana ba \nmotse,ba sebelisa majoana?", "bapaloang ke baroetsana,\nbanana kapa basali ba \ntenne litolobonya?", "bapaloang ke banna \nkapa bahlankana?", "0"}
        };

        questions[7] = new String[][] {
                {"/morabaraba.mp4","khetha papali e hlang ts'oants'isong\n e ka holimo ","lesokoana?", "ho cheya litali?", "bolo?", "moraba-raba?", "3"},
                {"/mokhibo.mp4","khetha papali e hlang \nts'oants'isong e ka holimo ", "litolobonya?","mokhibo?", "selia-lia?", "liketoana", "1"},
                {"/mohobelo.mp4","khetha papali e hlang \nts'oants'isong e ka holimo ", "ndlamo?", "mokhibo?", "mohobelo?", "litolobonya?", "2"}
        };

        questions[8] = new String[][] {
                {"/khati.mp4","khetha papali e hlang \nts'oants'isong e ka holimo ", "mohobelo?", "khati?", "morabaraba?", "lesokoana?", "1"},
                {"/bolekeba-maipatile.mp4","khetha papali e hlang \nts'oants'isong e ka holimo ", "mohobelo?", "khati?", "lesokoana?", "bolekeba-maipatile", "3"},
                {"/mokallo.mp4","khetha papali e hlang \nts'oants'isong e ka holimo ", "mohobelo?", "khati?", "ho kalla?", "ntoa?", "2"}
        };

        if (currentCategory >= 6 && currentCategory <= 8 &&
                questions[currentCategory] != null &&
                levelIndex < questions[currentCategory].length) {

            String[] questionData = questions[currentCategory][levelIndex];
            if (questionData != null && questionData.length >= 7) {
                String questionText = questionData[1];
                String[] options = {questionData[2], questionData[3], questionData[4], questionData[5]};
                int correctIndex = Integer.parseInt(questionData[6]);
                return createQuestion(questionData[0], questionText, options, correctIndex);
            }
        }
        return createResultsView();
    }

    private VBox createOptionVideo(String url) {
        // Create media objects
        video = new Media(getClass().getResource(url).toString());
        mediaPlayer = new MediaPlayer(video);
        videoView = new MediaView(mediaPlayer);

        // Set video view properties
        videoView.setFitHeight(60);
        videoView.setFitWidth(70);

        // Set media player properties
        mediaPlayer.setCycleCount(10);
        mediaPlayer.setVolume(0); // Set volume to 0 as requested

        // Create progress slider
        Slider progressSlider = new Slider();
        progressSlider.setMinWidth(200);

        // Update slider as video plays
        mediaPlayer.currentTimeProperty().addListener((obs, oldVal, newVal) -> {
            if (!progressSlider.isValueChanging()) { // Don't update if user is dragging
                progressSlider.setValue(newVal.toSeconds() / mediaPlayer.getTotalDuration().toSeconds() * 1000);
            }
        });

        // Allow user to seek video
        progressSlider.valueChangingProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) { // When user finishes dragging
                mediaPlayer.seek(mediaPlayer.getMedia().getDuration().multiply(progressSlider.getValue() / 100));
            }
        });

        // Play the video
        mediaPlayer.play();

        // Create layout
        VBox videoHolder = new VBox(5, videoView, progressSlider);
        videoHolder.setAlignment(Pos.TOP_CENTER);

        return videoHolder;
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
        retryButton.setOnAction(e ->
        {
            onHidden();
            loadFirstLevel();
        });

        Button nextCategoryButton = new Button("Lapeng");
        nextCategoryButton.setOnAction(e -> {

            getAppManager().goHome();
            HoldMediaPlayers();
        });

        VBox resultsBox = new VBox(20, resultLabel);
        resultsBox.setAlignment(Pos.CENTER);

        if (correctCount == LEVELS_PER_CATEGORY) {
            if (currentCategory < 5) { // If not the last category
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

    private VBox createQuestion(String url, String questionText, String[] options, int correctIndex) {
        Label questionLabel = new Label(questionText);
        questionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
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

        VBox questionBox = new VBox(20, createOptionVideo(url), questionLabel, timerProgressBar, optionsBox);
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
    private void showResultsView() {
        int correctCount = countCorrectAnswers();
        VBox resultsView = createResultsView(correctCount);
        ScrollPane scrollPane = new ScrollPane(resultsView);
        scrollPane.setFitToWidth(true);
        setCenter(scrollPane);

        if (correctCount == LEVELS_PER_CATEGORY) {
            playSuccessAnimation();

            // Update icons based on current category progression
            switch(Integer.parseInt(PrimaryView.getLevelnum())) {
                case 6:
                    levelsView.setL1Icon("/win1.png");
                    // Unlock next category (4)
                    PrimaryView.setLevelnum("7");
                    break;
                case 7:
                    levelsView.setL2Icon("/win2.png");
                    // Unlock next category (5)
                    PrimaryView.setLevelnum("8");
                    break;
                case 8:
                    levelsView.setL3Icon("/win3.png");
                    // All categories completed
                    PrimaryView.setLevelnum("9");
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

    private void startQuestionTimer(int correctIndex) {
        if (questionTimer != null) {
            questionTimer.stop();
        }

        timeRemaining.set(20);
        questionTimer = new Timeline(
                new KeyFrame(Duration.seconds(0.1), event -> {
                    timeRemaining.set(timeRemaining.get() - 0.1);

                    // Change progress bar color as time runs out
                    if (timeRemaining.get() < 3) {
                        timerProgressBar.setStyle("-fx-accent: #F44336;"); // Red
                    } else if (timeRemaining.get() < 7) {
                        timerProgressBar.setStyle("-fx-accent: #FFC107;"); // Yellow
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
                        "-fx-background-color: #4CAF50; " + // Green
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

    // ... [rest of the existing methods remain unchanged] ...

    @Override
    protected void updateAppBar(AppBar appBar) {
        appBar.setNavIcon(MaterialDesignIcon.ARROW_BACK.button(e -> {
            cleanupMediaPlayers();
            onHidden();
            getAppManager().goHome();
        }));
        appBar.setTitleText("PAPALI KA LIPAPALI - BOEMO KE: " + currentCategory);
    }

    protected void onHidden() {
        cleanupMediaPlayers();
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

    private int countCorrectAnswers() {
        int count = 0;
        for (boolean result : levelResults) {
            if (result) count++;
        }
        return count;
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