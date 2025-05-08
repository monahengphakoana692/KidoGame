package com.gluonapplication.views;

import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class PrimaryView extends View
{
    private static String Levelnum = "11";
    private Label levelLabel;
    public static final String[] basicLevel = {"/L1.png","/L2.png","/L3.png"};
    public static final String[] AdvancedLevel = {"/win1.png","/win2.png","/win3.png"};
    public static String secondaryView = "secView";
    private static String TotalQuestionsAnswered = "0";
    private static String CorrectAnswers = "0";
    private static String WrongAnswers = "0";
    private static String SuccessPercentage = Double.toString(Integer.parseInt((getLevelnum()))/15*100);

    public PrimaryView()
    {

        getStylesheets().add(PrimaryView.class.getResource("primary.css").toExternalForm());

        Image backgroundImage = new Image("/background.jpg");
        HomeView homeView = new HomeView();
        ImageView imageView = new ImageView(backgroundImage);
        imageView.setPreserveRatio(false);
        StackPane background1 = new StackPane();
        background1.setMaxWidth(200);
        background1.setMaxHeight(700);
        background1.setStyle("-fx-background-color:yellow;");

        imageView.setFitWidth(315);
        imageView.setFitHeight(900);

        background1.getChildren().addAll(imageView,homeView.getBackgroundLayout());
        background1.setPadding(new javafx.geometry.Insets(10, 10, 0, 10));

        VBox controls = new VBox(background1);
        controls.setAlignment(Pos.CENTER);

        setCenter(controls);

        ScrollPane scrollPane = new ScrollPane(controls);
        scrollPane.setFitToWidth(true); // Makes content fit viewport width
        scrollPane.setFitToHeight(true); // Makes content fit viewport height
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPannable(true); // Allows panning (touch scrolling)


        // Set the ScrollPane as the center of the View
        setCenter(scrollPane);
        if(getLevelnum().equals(15))
        {
            getAppManager().addViewFactory(secondaryView, () -> new SecondaryView());
        }

    }

    @Override
    protected void updateAppBar(AppBar appBar) {
        appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> getAppManager().getDrawer().open()));
        appBar.setTitleText("Lapeng");

        levelLabel = new Label("BOEMO: " + Levelnum);
        appBar.getActionItems().add(levelLabel);
    }


    public static void setLevelnum(String levelnum)

    {
        Levelnum = levelnum;

    }

    public static String getLevelnum()

    {
        return Levelnum;
    }

    public static String getTotalQuestionsAnswered() {
        return TotalQuestionsAnswered;
    }

    public static String getCorrectAnswers() {
        return CorrectAnswers;
    }

    public static String getWrongAnswers() {
        return WrongAnswers;
    }

    public static String getSuccessPercentage() {
        return SuccessPercentage;
    }

    public static void setTotalQuestionsAnswered(String TotalQuestions)
    {
        TotalQuestionsAnswered =TotalQuestions;
    }
    public static void setCorrectAnswers(String CorrectAnswer)
    {
        CorrectAnswers = CorrectAnswer;
    }

    public static void setWrongAnswers(String WrongAnswer)
    {
        WrongAnswers  = WrongAnswer;
    }

    public static void setSuccessPercentage(String SuccessPercent)
    {
        SuccessPercentage = SuccessPercent;
    }

    public static class CategoryStats {
        public int totalQuestions = 9;
        public int correctAnswers = Integer.parseInt(PrimaryView.getCorrectAnswers());
        public int wrongAnswers   = Integer.parseInt(PrimaryView.getWrongAnswers());
        public LevelStat[] levelStats = new LevelStat[3];

        public CategoryStats() {
            // Initialize level stats
            for (int i = 0; i < levelStats.length; i++) {
                levelStats[i] = new LevelStat();
                levelStats[i].correctAnswers = correctAnswers;
            }
        }

        public double successPercentage() {
            return totalQuestions > 0 ? (correctAnswers * 100.0 / totalQuestions) : 0;
        }
    }

    public static class LevelStat {
        public int questionsAttempted;
        public int correctAnswers;

        public double completionPercentage() {
            return questionsAttempted > 0 ? (correctAnswers * 100.0 / questionsAttempted) : 0;
        }
    }

    public static class OverallStats {
        public int totalQuestions = 45;
        public int totalCorrect;
        public int totalWrong;

        public double overallPercentage() {
            return totalQuestions > 0 ? (totalCorrect * 100.0 / totalQuestions) : 0;
        }
    }

    // Add these methods to track statistics for each category
    public static CategoryStats getLilothoStats() {
        CategoryStats stats = new CategoryStats();
        // Implement actual statistics tracking
        return stats;
    }

    public static CategoryStats getMaeleStats() {
        CategoryStats stats = new CategoryStats();
        // Implement actual statistics tracking
        return stats;
    }

    public static CategoryStats getLipapaliStats() {
        CategoryStats stats = new CategoryStats();
        // Implement actual statistics tracking
        return stats;
    }

    public static OverallStats getOverallStats() {
        OverallStats stats = new OverallStats();
        // Implement actual statistics tracking
        return stats;
    }


}