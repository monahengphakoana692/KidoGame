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

public class MaeleGame extends View {

    private static final int LEVELS_PER_CATEGORY = 3;
    private static final int TOTAL_CATEGORIES = 3;
    private final boolean[] levelResults = new boolean[LEVELS_PER_CATEGORY];
    private MediaPlayer videoPlayer;
    private MediaPlayer audioPlayer;
    private MediaView mediaView;
    private int currentLevelIndex = 0; // Index within current category (0-2)
    private int currentCategory;      // Category index (3-5)
    private LevelsView2 levelsView = new LevelsView2();

    private Timeline questionTimer;
    private DoubleProperty timeRemaining = new SimpleDoubleProperty(10);
    private ProgressBar timerProgressBar;

    public MaeleGame() {
        initializeUI();
        loadFirstLevel();
    }

    private void initializeUI() {
        getStylesheets().add(getClass().getResource("primary.css").toExternalForm());
        // Initialize current category from saved state
        currentCategory = Integer.parseInt(PrimaryView.getLevelnum());
    }

    private void loadFirstLevel() {
        currentLevelIndex = 0; // Always start at first question of category
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
        // Define all questions organized by category and level
        String[][][] questions = new String[6][3][5];
        questions[3] = new String[][] {
                {"/Leleme.jpg","Khomo ea lebese ha e itsoale?", "Ha ho motho a iketsetsang lintho", "Hase ha ngata ngoana \n aka futsang Motsoali ka matla ", "Batho ba thusana", "Motho a phelang ka litsietse", "1"},
                {"/Leleme.jpg","Khomo Lija Tika Motse?", "Batho ba sebetsa \n ntse ba orohela hae", "Batho baja", "Batho ba bitsoa moketeng", "Masholu ka hara motse", "0"},
                {"/Leleme.jpg","Khomo li ne li tseba Monoang?", "Thimola", "Motho a phelang\n ka litsietse ", "Motho a phelang ka ho Hlorisoa", "Mokhubu", "1"}
        };

        questions[4] = new String[][] {
                {"/Leleme.jpg","Lefura la monga khomo le psheisa mongalona? ", "Moholu", "Letlotlo", "Bolo", "chai", "0"},
                {"/Leleme.jpg","Khomo e thibela lerumo? ", "Ho hlaba khomo nakong \n ea mokete","Khomo e thusana li nthong \n tse ngata", "Bophelo ba motho bo \n bohlokoa ho feta leruo", "Motho o etsa sehlabelo ka \n ena ho thusa ba bang", "2"},
                {"/Leleme.jpg","Nama e ka mpeng ho khome?", "Ho se bui litaba \n ha ho hlokala", "Ho pata litaba", "Ke lekunutu kapa \npinyane", "Ho iphapanya", "2"}
        };

        questions[5] = new String[][] {
                {"/Leleme.jpg","Moketa Khomo o nonela tlhakong?", "Monna o nyala ngaoana \n ena ale moholo", "Monna aka na nyala moqekoa \n a holileng ho mo thusa", "Ngoana o holela \n mosebetsing", "Motho o holisoa \n ke ho sebetsa", "1"},
                {"/Leleme.jpg","Ke u tsoela Khomo?", "Motho a senyang nako, \n a etsa seo se sa\n motsoeleng molemo", "Moholu", "Moraha ka sakeng", "Mohloa", "3"},
                {"/Leleme.jpg","Ho tlola Khomo?", "Moraha ka sakeng", "Ho senyeheloa", "Jwala bo qhalaneng", "Bana ba hae", "3"}
        };

        // Check if the category exists and has questions
        if (currentCategory >= 3 && currentCategory <= 5 &&
                questions[currentCategory] != null &&
                levelIndex < questions[currentCategory].length) {

            String[] questionData = questions[currentCategory][levelIndex];

            // Additional safety check for question data
            if (questionData != null && questionData.length >= 6) {
                String questionText = "Khetha tlhaloso ea Leele le latelang: \n" + questionData[1];
                String[] options = {
                        questionData[2],
                        questionData[3],
                        questionData[4],
                        questionData[5]
                };
                int correctIndex = Integer.parseInt(questionData[6]);

                return createQuestion(questionData[0],questionText, options, correctIndex);
            }
        }

        // If any check fails, return results view
        return createResultsView();
    }

    private VBox createOptionImage(String url)
    {
        Image image = new Image(url);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(40);
        imageView.setFitWidth(70);

        VBox imageHolder = new VBox(imageView);
        imageHolder.setAlignment(Pos.TOP_CENTER);

        return imageHolder;

    }

    private VBox createQuestion(String url, String questionText, String[] options, int correctIndex)
    {
        Label questionLabel = new Label(questionText);
        questionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
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

    private Button createOptionButton(String text, boolean isCorrect) {
        Button button = new Button(text);
        button.setUserData(isCorrect); // Store whether this is the correct answer
        // Apply inline styling
        button.setStyle("-fx-font-size: 13px; " +
                "-fx-pref-width: 210px; " +
                "-fx-pref-height: 60px; " +
                "-fx-background-radius: 25; " +
                "-fx-border-radius: 25; ");
        button.setOnAction(e -> handleAnswer(button, isCorrect));
        return button;
    }

    private void handleAnswer(Button selectedButton, boolean isCorrect) {
        // Stopping the timer
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

        // Add delay before next question
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
            PrimaryView.setCorrectAnswers(Integer.toString(correctCount + Integer.parseInt(PrimaryView.getCorrectAnswers())));
            PrimaryView.setTotalQuestionsAnswered(Integer.toString(LEVELS_PER_CATEGORY));
            PrimaryView.setWrongAnswers(Integer.toString(Integer.parseInt(PrimaryView.getWrongAnswers())));
            // Update icons based on current category progression
            switch(currentCategory) {
                case 3:
                    levelsView.setL1Icon("/win1.png");
                    // Unlock next category (4)
                    PrimaryView.setLevelnum("4");
                    break;
                case 4:
                    levelsView.setL2Icon("/win2.png");
                    // Unlock next category (5)
                    PrimaryView.setLevelnum("5");
                    break;
                case 5:
                    levelsView.setL3Icon("/win3.png");
                    // All categories completed
                    PrimaryView.setLevelnum("6");
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

    @Override
    protected void updateAppBar(AppBar appBar) {
        appBar.setNavIcon(MaterialDesignIcon.ARROW_BACK.button(e -> {
            cleanupMediaPlayers();
            onHidden();
            getAppManager().goHome();
        }));
        appBar.setTitleText("PAPALI KA MAELE - BOEMONG BA: " + currentCategory);
    }

    protected void onHidden() {
        cleanupMediaPlayers();
    }
    private void timeUp(int correctIndex) {
        // Disable all buttons
        VBox questionBox = (VBox) timerProgressBar.getParent();
        VBox optionsBox = (VBox) questionBox.getChildren().get(3); // Options box is the 4th child

        for (var node : optionsBox.getChildren()) {
            Button button = (Button) node;
            button.setDisable(true);

            // Highlight the correct answer in green
            if ((boolean) button.getUserData()) {
                button.setStyle(button.getStyle() +
                        "-fx-background-color: #4CAF50; " + // Green
                        "-fx-text-fill: white;");
            }
        }

        // Mark this question as failed
        levelResults[currentLevelIndex] = false;

        // Delay before moving to next question
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
        // Stop any existing timer
        if (questionTimer != null) {
            questionTimer.stop();
        }

        // Reset time remaining
        timeRemaining.set(15);

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