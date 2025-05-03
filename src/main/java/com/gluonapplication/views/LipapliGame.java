package com.gluonapplication.views;

import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.geometry.Insets;
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

public class LipapliGame extends View {

    private static final int LEVELS_PER_CATEGORY = 3;
    private static final int TOTAL_CATEGORIES = 3;
    private final boolean[] levelResults = new boolean[LEVELS_PER_CATEGORY];
    private MediaPlayer videoPlayer;
    private MediaPlayer audioPlayer;
    private MediaView mediaView;
    private int currentLevel = 0;
    private int currentCategory = 0;

    public LipapliGame()
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
        String[][][] questions = {
                // Category 0
                {
                        {"Khomo ea lebese ha e itsoale?", "Ha ho motho a iketsetsang lintho", "Hase ha ngata ngoana \n aka futsang Motsoali ka matla ", "Batho ba thusana", "Motho a phelang ka litsietse", "1"},
                        {"Khomo Lija Tika Motse?", "Batho ba sebetsa ntse ba orohela hae", "Batho baja", "Batho ba bitsoa moketeng", "Masholu ka hara motse", "0"},
                        {"Khomo li ne li tseba Monoang?", "Thimola", "Motho a phelang ka litsietse ", "Motho a phelang ka ho Hlorisoa", "Mokhubu", "1"}
                },
                // Category 1
                {
                        {"Lefura la monga khomo le psheisa mongalona? ", "Moholu", "Letlotlo", "Bolo", "chai", "0"},
                        {"Khomo e thibela lerumo? ", "Ho hlaba khomo nakong ea mokete","Khomo e thusana li nthong tse ngata", "Bophelo ba motho bo bohlokoa ho feta leruo", "Motho o etsa sehlabelo ka ena ho thusa ba bang", "2"},
                        {"Nama e ka mpeng ho khome?", "Ho se bui litaba ha ho hlokala", "Ho pata litaba", "Ke lekunutu kapa pinyane", "Ho iphapanya", "2"}
                },
                // Category 2
                {
                        {"Moketa Khomo o nonela tlhakong?", "Monna o nyala ngaoana ena ale moholo", "Monna aka na nyala moqekoa a holileng ho mo thusa", "Ngoana o holela mosebetsing", "Motho o holisoa ke ho sebetsa", "1"},
                        {"Ke u tsoela Khomo?", "Motho a senyang nako, a etsa seo se sa motsoeleng molemo", "Moholu", "Moraha ka sakeng", "Mohloa", "3"},
                        {"Ho tlola Khomo?", "Moraha ka sakeng", "Ho senyeheloa", "Jwala bo qhalaneng", "Bana ba hae", "1"}
                }
        };

        if (currentCategory < TOTAL_CATEGORIES && levelIndex < LEVELS_PER_CATEGORY) {
            String[] questionData = questions[currentCategory][levelIndex];
            String questionText = "Khetha tlhaloso ea Leele le latelang: \n" + questionData[0];
            String[] options = {questionData[1], questionData[2], questionData[3], questionData[4]};
            int correctIndex = Integer.parseInt(questionData[5]);

            return createQuestion(questionText, options, correctIndex);
        } else {
            return createResultsView();
        }
    }

    private VBox createQuestion(String questionText, String[] options, int correctIndex) {
        Label questionLabel = new Label(questionText);
        questionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        questionLabel.setWrapText(true);

        VBox optionsBox = new VBox(10);
        optionsBox.setAlignment(Pos.CENTER);

        for (int i = 0; i < options.length; i++) {
            Button optionButton = createOptionButton(options[i], i == correctIndex);
            optionsBox.getChildren().add(optionButton);
        }

        VBox questionBox = new VBox(20, questionLabel, optionsBox);
        questionBox.setAlignment(Pos.CENTER);

        return questionBox;
    }

    private Button createOptionButton(String text, boolean isCorrect) {
        Button button = new Button(text);
        button.setOnAction(e -> handleAnswer(isCorrect));
        return button;
    }

    private void handleAnswer(boolean isCorrect) {
        levelResults[currentLevel] = isCorrect;
        currentLevel++;

        if (currentLevel < LEVELS_PER_CATEGORY) {
            showLevel(currentLevel);
        } else {
            showResultsView();
        }
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
                    LevelsView.setL1Icon("/win1.png");
                    break;
                case "2":
                    LevelsView.setL2Icon("/win2.png");
                    break;
                case "3":
                    LevelsView.setL3Icon("/win3.png");
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
            currentLevel = 0;
            showLevel(currentLevel);
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

    @Override
    protected void updateAppBar(AppBar appBar) {
        appBar.setNavIcon(MaterialDesignIcon.ARROW_BACK.button(e -> {
            cleanupMediaPlayers();
            onHidden();
            getAppManager().goHome();
        }));
        appBar.setTitleText("PAPALI KA LILOTHO - KAROLO EA: " + (currentCategory));
    }

    protected void onHidden() {
        cleanupMediaPlayers();
    }

    public void seeLevels()
    {
        LevelsView levelsView = new LevelsView();
        setCenter(levelsView);
    }
}