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

    public LilothoGame()
    {
        initializeUI();

        loadFirstLevel();
    }

    private void initializeUI() {
        getStylesheets().add(getClass().getResource("primary.css").toExternalForm());
        // Initialize current category from saved state
        currentCategory = Integer.parseInt(PrimaryView.getLevelnum());
    }

    private void loadFirstLevel() {
        currentLevel = 0;
        showLevel(currentLevel);
    }

    public void showLevel(int levelIndex)
    {

            VBox questionView = createQuestionView(levelIndex);
            StackPane levelContainer = createLevelContainer(questionView);
            setCenter(levelContainer);


    }

    private StackPane createLevelContainer(VBox content)
    {

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
        // Define all questions organized by category and level
        String[][][] questions = {
                // Category 0
                {
                        {"/Leleme.jpg","'Me Nts'oare ke nye", "Leihlo", "Tsebe", "Ngoana", "Nko", "3"},
                        {"/Leleme.jpg","Ts'oene tse peli \n Lihloa thaba \n lisa e qete?", "Matsoele", "Moriri", "Litsebe", "Li Ts'oene", "2"},
                        {"/Leleme.jpg","Mohlankana ea lulang \n lehaheng?", "Thimola", "Leleme", "Khoeli", "Mokhubu", "1"}
                },
                // Category 1
                {
                        {"/Leleme.jpg","Ka Qhala Phoofo \n ka ja mokotla? ", "Moholu", "Letlotlo", "Bolo", "chai", "0"},
                        {"/Leleme.jpg","Maqheku a qabana ka lehaheng?", "Likhobe","LIerekise", "Limathi", "Ntate mohole le Nkhono", "0"},
                        {"/Leleme.jpg","Thankha-Thankha ketla tsoalla kae?", "Lihaba ha li hola", "Khomo", "Mokopu ha u nama", "Lebese", "2"}
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

            return createQuestion(url,questionText, options, correctIndex);
        } else {
            return createResultsView();
        }
    }

    private VBox createQuestion(String url, String questionText, String[] options, int correctIndex)
    {
        Label questionLabel = new Label(questionText);
        questionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        questionLabel.setWrapText(true);

        // Create and configure the timer progress bar
        timerProgressBar = new ProgressBar();
        timerProgressBar.setPrefWidth(300);
        timerProgressBar.setMaxWidth(Double.MAX_VALUE);
        timerProgressBar.progressProperty().bind(timeRemaining.divide(10));

        // Style the progress bar to change color as time runs out
        timerProgressBar.setStyle("-fx-accent: #4CAF50;"); // Start with green

        VBox optionsBox = new VBox(10);
        optionsBox.setAlignment(Pos.CENTER);

        for (int i = 0; i < options.length; i++) {
            Button optionButton = createOptionButton(options[i], i == correctIndex);
            optionsBox.getChildren().add(optionButton);
        }

        VBox questionBox = new VBox(20, createOptionImage(url), questionLabel, timerProgressBar, optionsBox);
        questionBox.setAlignment(Pos.CENTER);

        // Start the timer for this question
        startQuestionTimer(correctIndex);

        return questionBox;
    }
    private VBox createOptionImage(String url)
    {
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
        // Apply inline CSS styling
        button.setStyle("-fx-font-size: 13px; " +
                "-fx-pref-width: 210px; " +
                "-fx-pref-height: 30px; " +
                "-fx-background-radius: 25; " +
                "-fx-border-radius: 25; ");
        button.setOnAction(e -> handleAnswer(button, isCorrect));
        return button;
    }

    private void handleAnswer(Button selectedButton, boolean isCorrect) {
        // Stop the timer
        if (questionTimer != null) {
            questionTimer.stop();
        }

        VBox optionsBox = (VBox) selectedButton.getParent();
        for (var node : optionsBox.getChildren()) {
            Button button = (Button) node;
            button.setDisable(true);

            // Highlight the correct answer in green
            if ((boolean) button.getUserData()) {
                button.setStyle(button.getStyle() +
                        "-fx-background-color: #4CAF50; " + // Green
                        "-fx-text-fill: white;");
            }
            // Highlight the selected answer (green if correct, red if wrong)
            else if (button == selectedButton) {
                if (isCorrect) {
                    button.setStyle(button.getStyle() +
                            "-fx-background-color: #4CAF50; " + // Green
                            "-fx-text-fill: white;");
                } else {
                    button.setStyle(button.getStyle() +
                            "-fx-background-color: #F44336; " + // Red
                            "-fx-text-fill: white;");
                }
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
        VBox resultsView = createResultsView(correctCount);
        ScrollPane scrollPane = new ScrollPane(resultsView);
        scrollPane.setFitToWidth(true);
        setCenter(scrollPane);

        if (correctCount == LEVELS_PER_CATEGORY) {
            playSuccessAnimation();

            // Update icons based on current level progression
            switch(PrimaryView.getLevelnum()) {
                case "1":
                    levelsView.setL1Icon("/win1.png");
                    break;
                case "2":
                    levelsView.setL2Icon("/win2.png");
                    break;
                case "3":
                    levelsView.setL3Icon("/win3.png");
                    break;
            }

            // Refresh the levels view
            getAppManager().switchView("LevelsView");

            StackPane mediaContainer = new StackPane(mediaView);
            mediaContainer.setPadding(new Insets(10));
            mediaContainer.setAlignment(Pos.CENTER);
            resultsView.getChildren().add(mediaContainer);

            // Progress to next category if available
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

        Button retryButton = new Button("Leka ho Lekha");
        retryButton.setOnAction(e -> {
            onHidden();
            loadFirstLevel();

        });

        Button nextCategoryButton = new Button("Karolo e 'ngoe");
        nextCategoryButton.setOnAction(e ->
        {
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
        if (videoPlayer != null) {
            videoPlayer.stop();


        }
        if (audioPlayer != null) {
            audioPlayer.stop();


        }
    }

    private void timeUp(int correctIndex) {
        // Disable all buttons
        VBox questionBox = (VBox) timerProgressBar.getParent();
        VBox optionsBox = (VBox) questionBox.getChildren().get(3); // Options box is the 4th child

        for (var node : optionsBox.getChildren()) {
            Button button = (Button) node;
            button.setDisable(true);

            // Highlight the correct answer
            if ((boolean) button.getUserData()) {
                button.getStyleClass().add("correct-answer");
            }
        }

        // Mark this question as failed
        levelResults[currentLevel] = false;

        // Delay before moving to next question
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
        // Stop any existing timer
        if (questionTimer != null) {
            questionTimer.stop();
        }

        // Reset time remaining
        timeRemaining.set(10);

        // Create new timer
        questionTimer = new Timeline(
                new KeyFrame(Duration.seconds(0.1), event -> {
                    timeRemaining.set(timeRemaining.get() - 0.1);

                    // Change color as time runs out
                    if (timeRemaining.get() < 3) {
                        timerProgressBar.setStyle("-fx-accent: #F44336;"); // Red when time is almost up
                    } else if (timeRemaining.get() < 7) {
                        timerProgressBar.setStyle("-fx-accent: #FFC107;"); // Yellow when time is halfway
                    }

                    if (timeRemaining.get() <= 0) {
                        questionTimer.stop();
                        timeUp(correctIndex);
                    }
                }
                ));
        questionTimer.setCycleCount(Timeline.INDEFINITE);
        questionTimer.play();
    }

}