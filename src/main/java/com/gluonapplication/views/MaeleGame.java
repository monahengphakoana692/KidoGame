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

    public class MaeleGame extends View {

        private static final int LEVELS_PER_CATEGORY = 3;
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
            currentCategory = Math.max(3, Math.min(5, Integer.parseInt(PrimaryView.getLevelnum())));
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
            // Define all questions for categories 3-5 (displayed as 1-3)
            String[][][] questions = {
                    // Category 3 (displayed as Level 1)
                    {
                            {"/likhomo.jpg","Khomo ea lebese ha e itsoale?",
                                    "Ha ho motho a iketsetsang lintho",
                                    "Hase ha ngata ngoana \naka futsang Motsoali \nka matla ",
                                    "Batho ba thusana",
                                    "Motho a phelang \n ka litsietse", "1"},
                            {"/home.jpg","Khomo Lija Tika Motse?",
                                    "Batho ba sebetsa ntse \nba orohela hae",
                                    "Batho baja",
                                    "Batho ba bitsoa moketeng",
                                    "Masholu ka hara motse", "0"},
                            {"/litsietsi.jpg","Khomo li ne li tseba Monoang?",
                                    "ho hlaha makhopho",
                                    "Motho a phelang ka litsietse ",
                                    "Motho a phelang ka thata",
                                    "lekoko la khomo le \ntlohileng boya", "1"}
                    },
                    // Category 4 (displayed as Level 2)
                    {
                            {"/khomo.jpg","khetha maele a amanang le sets'oants'o seka holimo",
                                    "Khomo ea senone le tlhako? ",
                                    "ho tlola khomo",
                                    "Bosiu ba naka tsa khomo",
                                    "ke u tsoela khomo", "0"},
                            {"/hoseng.jpg","khetha maele a amanang le sets'oants'o seka holimo",
                                    "Khomo e thibela lerumo? ",
                                    "Khomo ha lina motlooa pele",
                                    "Bosiu ba naka tsa khomo?",
                                    "Khomo ea senona le tlhako",
                                     "2"},
                            {"/meat.jpg","khetha maele a amanang le sets'oants'o seka holimo",
                                    "khomo li hlabana \nmaqhubu sakeng?",
                                    "Lebitla la khomoka molomo?",
                                    "Ho opa khomo lenaka?",
                                    "Khomo tsabo moshemane \nhali jooe?", "1"}
                    },
                    // Category 5 (displayed as Level 3)
                    {
                            {"/lipuotsahae.jpg","khetha maele a amanang le sets'oants'o seka holimo",
                                    "Moketa Khomo o nonela tlhakong?",
                                    "Morena ke khomo ea \nsehangoa ke bohle",
                                    "Khomo e ts'oaroa ka linaka?",
                                    "Ho batla ka \nkhomo mmele?", "2"},
                            {"/haeno.jpg", "khetha maele a amanang le sets'oants'o seka holimo",
                                    "Ke u tsoela Khomo?",
                                    "Khomo e thibela lerumo?",
                                    "Khomo halina \nmotlowa pele",
                                    "Khomo boela haeno\n o holile?", "3"},
                            {"/honepa.jpg", "khetha maele a amanang le sets'oants'o seka holimo",
                                    "Ho tlola Khomo?",
                                    "Ho opa khomo lenaka?",
                                    "Khomo li jeoa ka\n ngoana a le mong?",
                                    "Khomo ha lina motlooa \npele?", "1"}
                    }
            };

            // Calculate which category to show (3-5)
            int categoryIndex = currentCategory - 3;

            if (categoryIndex >= 0 && categoryIndex < questions.length &&
                    levelIndex < questions[categoryIndex].length) {

                String[] questionData = questions[categoryIndex][levelIndex];
                if (questionData != null && questionData.length >= 7)
                {
                    String questionText = "";
                    if("khetha maele a amanang le sets'oants'o seka holimo".equals(questionData[1]))
                    {
                        questionText = questionData[1];
                    }else
                    {
                        questionText = "Khetha tlhaloso ea Maele a latelang: \n" + questionData[1];
                    }

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
            return createResultsView();
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

        private VBox createQuestion(String url, String questionText, String[] options, int correctIndex)
        {
            Label questionLabel = new Label(questionText);
            questionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 11));
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
                PrimaryView.setTotalQuestionsAnswered(Integer.toString(LEVELS_PER_CATEGORY + Integer.parseInt(PrimaryView.getTotalQuestionsAnswered())));

                // Update icons and unlock next category
                switch(currentCategory) {
                    case 3:
                        levelsView.setL1Icon("/win1.png");
                        if (Integer.parseInt(PrimaryView.getLevelnum()) == 3) {
                            PrimaryView.setLevelnum("4"); // Unlock category 4
                        }
                        break;
                    case 4:
                        levelsView.setL2Icon("/win2.png");
                        if (Integer.parseInt(PrimaryView.getLevelnum()) == 4) {
                            PrimaryView.setLevelnum("5"); // Unlock category 5
                        }
                        break;
                    case 5:
                        levelsView.setL3Icon("/win3.png");
                        if (Integer.parseInt(PrimaryView.getLevelnum()) == 5) {
                            PrimaryView.setLevelnum("6");
                        }
                        break;
                }

                getAppManager().switchView("LevelsView");
                levelsView.refreshLevelBoxes();
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