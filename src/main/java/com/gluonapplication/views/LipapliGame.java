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
    private int currentLevelIndex = 0; // Index within current category (0-2)
    private int currentCategory;      // Category index (6-8)
    Media video ;
    MediaPlayer mediaPlayer;
    MediaView videoView;

    public LipapliGame() {
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
        String[][][] questions = new String[9][3][7];
        questions[6] = new String[][] {
                {"/applauseV.mp4","Khomo ea lebese ha e itsoale?", "Ha ho motho a iketsetsang lintho", "Hase ha ngata ngoana \n aka futsang Motsoali ka matla ", "Batho ba thusana", "Motho a phelang ka litsietse", "1"},
                {"/applauseV.mp4","Khomo Lija Tika Motse?", "Batho ba sebetsa ntse ba orohela hae", "Batho baja", "Batho ba bitsoa moketeng", "Masholu ka hara motse", "0"},
                {"/applauseV.mp4","Khomo li ne li tseba Monoang?", "Thimola", "Motho a phelang ka litsietse ", "Motho a phelang ka ho Hlorisoa", "Mokhubu", "1"}
        };

        questions[7] = new String[][] {
                {"/applauseV.mp4","Lefura la monga khomo le psheisa mongalona? ", "Moholu", "Letlotlo", "Bolo", "chai", "0"},
                {"/applauseV.mp4","Khomo e thibela lerumo? ", "Ho hlaba khomo nakong ea mokete","Khomo e thusana li nthong tse ngata", "Bophelo ba motho bo bohlokoa ho feta leruo", "Motho o etsa sehlabelo ka ena ho thusa ba bang", "2"},
                {"/applauseV.mp4","Nama e ka mpeng ho khome?", "Ho se bui litaba ha ho hlokala", "Ho pata litaba", "Ke lekunutu kapa pinyane", "Ho iphapanya", "2"}
        };

        questions[8] = new String[][] {
                {"/applauseV.mp4","Moketa Khomo o nonela tlhakong?", "Monna o nyala ngaoana ena ale moholo", "Monna aka na nyala moqekoa a holileng ho mo thusa", "Ngoana o holela mosebetsing", "Motho o holisoa ke ho sebetsa", "1"},
                {"/applauseV.mp4","Ke u tsoela Khomo?", "Motho a senyang nako, a etsa seo se sa motsoeleng molemo", "Moholu", "Moraha ka sakeng", "Mohloa", "3"},
                {"/applauseV.mp4","Ho tlola Khomo?", "Moraha ka sakeng", "Ho senyeheloa", "Jwala bo qhalaneng", "Bana ba hae", "3"}
        };

        // Check if the category exists and has questions
        if (currentCategory >= 6 && currentCategory <= 8 &&
                questions[currentCategory] != null &&
                levelIndex < questions[currentCategory].length) {

            String[] questionData = questions[currentCategory][levelIndex];

            // Additional safety check for question data
            if (questionData != null && questionData.length >= 7) {
                String questionText = "Khetha tlhaloso ea Leele le latelang: \n" + questionData[1];
                String[] options = {
                        questionData[2],
                        questionData[3],
                        questionData[4],
                        questionData[5]
                };
                int correctIndex = Integer.parseInt(questionData[6]);

                return createQuestion(questionData[0], questionText, options, correctIndex);
            }
        }

        // If any check fails, return results view
        return createResultsView();
    }

    private VBox createOptionVideo(String url)
    {


        video = new Media(getClass().getResource(url).toString());
        mediaPlayer = new MediaPlayer(video);
        videoView = new MediaView(mediaPlayer);
        videoView.setFitHeight(70);
        videoView.setFitWidth(70);
        mediaPlayer.setCycleCount(10);
        mediaPlayer.play();

        VBox videoHolder = new VBox(videoView);
        videoHolder.setAlignment(Pos.TOP_CENTER);

        return videoHolder;
    }

    private VBox createQuestion(String url, String questionText, String[] options, int correctIndex) {
        Label questionLabel = new Label(questionText);
        questionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        questionLabel.setWrapText(true);

        VBox optionsBox = new VBox(10);
        optionsBox.setAlignment(Pos.CENTER);

        for (int i = 0; i < options.length; i++) {
            Button optionButton = createOptionButton(options[i], i == correctIndex);
            optionsBox.getChildren().add(optionButton);
        }

        VBox questionBox = new VBox(20, createOptionVideo(url), questionLabel, optionsBox);
        questionBox.setAlignment(Pos.CENTER);

        return questionBox;
    }

    private Button createOptionButton(String text, boolean isCorrect) {
        Button button = new Button(text);
        button.setOnAction(e -> handleAnswer(isCorrect));
        return button;
    }

    private void handleAnswer(boolean isCorrect) {
        levelResults[currentLevelIndex] = isCorrect;
        currentLevelIndex++;

        if (currentLevelIndex < LEVELS_PER_CATEGORY) {
            showLevel(currentLevelIndex);
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

            // Update icons based on current category progression
            switch(currentCategory) {
                case 6:
                    LevelsView.setL1Icon("/win1.png");
                    // Unlock next category (7)
                    PrimaryView.setLevelnum("7");
                    break;
                case 7:
                    LevelsView.setL2Icon("/win2.png");
                    // Unlock next category (8)
                    PrimaryView.setLevelnum("8");
                    break;
                case 8:
                    LevelsView.setL3Icon("/win3.png");
                    // All categories completed
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
        appBar.setTitleText("PAPALI KA LILOTHO - KAROLO EA: " + currentCategory);
    }

    protected void onHidden() {
        cleanupMediaPlayers();
    }

}