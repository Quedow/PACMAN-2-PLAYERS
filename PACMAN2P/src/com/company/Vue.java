/*
PACMAN2P - MAMMA Quentin & LUQUET Steven - A21
 */
package com.company;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Vue {

    private Presentation presentation;

    private final int longueur;
    private final int hauteur;
    private final int taillePixel;
    private boolean statBerseker;
    private int[][] memoryStats;

    private ArrayList<ImageView> imagePlayers;
    private ArrayList<ImageView> imageGhosts;
    private ArrayList<Label> labels;
    private List<ColorAdjust> colorAdjusts;

    private Stage stage = new Stage();

    // Elements of the 1st view (menu)
    private Scene sceneMenu;
    private Pane paneMenu;
    private Button onePlayer;
    private Button twoPlayer;
    private Button exit;
    private TextField numberE;

    // Elements of the 2nd view (game)
    private Scene sceneGame;
    private BorderPane borderPaneGame;
    private Pane pane;
    private Pane right;
    private Pane left;

    public Vue(Presentation presentation)
    {
        this.presentation = presentation;
        memoryStats = new int[2][2];
        statBerseker = false;

        Screen screen = Screen.getPrimary();
        longueur = (int) screen.getBounds().getWidth(); //Recupère la longueur de l'écran du PC
        hauteur = (int) screen.getBounds().getHeight(); //Recupère la hauteur de l'écran du PC
        taillePixel = presentation.setTaillePixel(hauteur);

        // Elements of the 1st view (menu)
        initializeMenu();

        // Elements of the 2nd view (game)
        initializeGame();

        // Initialize scenes
        sceneGame = new Scene(borderPaneGame, longueur, hauteur);
        sceneMenu = new Scene(paneMenu, longueur, hauteur);

        // Initialize window
        stage.setTitle("PACMAN FOR 2 PLAYERS");
        stage.setMaximized(true);
        stage.setResizable(false);
        stage.setScene(sceneMenu);
        stage.show();

        sceneGame.setOnKeyPressed(presentation);
        onePlayer.setOnAction(event -> initializeEntities(1));
        twoPlayer.setOnAction(event -> initializeEntities(2));
        exit.setOnAction(event -> System.exit(1));

        numberE.setOnAction(event -> {
            TextField textField = (TextField) event.getSource();
            try {
                int nE = Integer.parseInt(textField.getText());
                if (nE >= 0 && nE <= 200){
                    presentation.setNumberE(nE);
                    textField.setText("Correct entry");
                }else{
                    textField.setText("Incorrect entry");
                }
            }catch (Exception exception){
                textField.setText("Incorrect entry");
            }
        });
    }

    // Initialize all Views

    private void initializeMenu(){
        paneMenu = new Pane();
        paneMenu.setStyle("-fx-background-color: BLACK");

        Text title = new Text("PAC-MAN");
        title.setStyle("-fx-font: 120 Verdana; -fx-text-fill: YELLOW; -fx-font-weight: bold; -fx-stroke: firebrick; -fx-stroke-width: 4px;");
        title.setFill(Color.YELLOW);
        title.setX(longueur/3.0);
        title.setY(200);

        onePlayer = new Button("1 PLAYER");
        onePlayer.setStyle("-fx-font: 42 arial; -fx-base: BLACK; -fx-font-weight: bold;");
        onePlayer.setPrefSize(280,50);
        onePlayer.setLayoutX(longueur/2.0-onePlayer.getPrefWidth()/2.0);
        onePlayer.setLayoutY(hauteur/2.0-100);

        twoPlayer = new Button("2 PLAYERS");
        twoPlayer.setStyle("-fx-font: 42 arial; -fx-base: BLACK; -fx-font-weight: bold;");
        twoPlayer.setPrefSize(300,50);
        twoPlayer.setLayoutX(longueur/2.0-twoPlayer.getPrefWidth()/2.0);
        twoPlayer.setLayoutY(hauteur/2.0);

        exit = new Button("EXIT");
        exit.setStyle("-fx-font: 42 arial; -fx-base: BLACK; -fx-font-weight: bold;");
        exit.setPrefSize(180,50);
        exit.setLayoutX(longueur/2.0-exit.getPrefWidth()/2.0);
        exit.setLayoutY(hauteur/2.0+100);

        numberE = new TextField("Number of ghost ? (0-200)");
        numberE.setPrefSize(200,25);
        numberE.setLayoutX(longueur/2.0-exit.getPrefWidth()/2.0);
        numberE.setLayoutY(hauteur/2.0-160);

        paneMenu.getChildren().addAll(title, onePlayer, twoPlayer,exit, numberE);
    }

    private void initializeGame(){
        borderPaneGame = new BorderPane();

        pane = new Pane();
        pane.setMaxWidth(taillePixel*presentation.getColumns());
        pane.setStyle("-fx-background-color: DARKBLUE");
        borderPaneGame.setCenter(pane);

        left = new Pane();
        left.setStyle("-fx-background-color: BLACK");
        left.setMinWidth((longueur-taillePixel*presentation.getColumns())/2.0);
        borderPaneGame.setLeft(left);

        right = new Pane();
        right.setStyle("-fx-background-color: BLACK");
        right.setMinWidth((longueur-(taillePixel*presentation.getColumns())-left.getMinWidth()));
        borderPaneGame.setRight(right);

        drawMap();
        initializeColors();
    }

    private void drawMap() {
        for (int L = 0; L < getLines(); L++) {
            for (int C = 0; C < getColumns(); C++) {

                if (matrixMap(L,C)==1) {
                    noWalkableZone(L,C);
                }
                else if (matrixMap(L,C)==2) {
                    walkableZone(L,C);
                    scoreZone(L,C);
                }
                else if (matrixMap(L,C)==3){
                    walkableZone(L,C);
                }
                else if (matrixMap(L,C)==0){
                    ghostZone(L,C);
                }
                else if (matrixMap(L,C)==4){
                    walkableZone(L,C);
                    bonusZone(L,C);
                }
            }
        }
    }

    private void initializeColors() {
        ColorAdjust yellow = new ColorAdjust(); //Yellow car c'est la couleur de base de l'image
        ColorAdjust green = new ColorAdjust();
        green.setHue(0.3);

        colorAdjusts = Arrays.asList(yellow, green);
    }

    private void initializeEntities(int nJ){
        stage.setScene(sceneGame);
        presentation.setNumberJ(nJ);
        drawPlayer();
        drawEnemies();
        drawStatsLabel();
    }

    private void drawPlayer() {
        imagePlayers = new ArrayList<ImageView>();

        for (int i = 0; i < getNumberJ(); i++) {
            ImageView imageView = new ImageView(new Image(new File("pacman.gif").toURI().toString()));
            imageView.setFitWidth(taillePixel); imageView.setFitHeight(taillePixel);
            imageView.setX(getCPlayer(i)*taillePixel); imageView.setY(getLPlayer(i)*taillePixel);
            imagePlayers.add(imageView);

            try {
                imagePlayers.get(i).setEffect(colorAdjusts.get(i));
            }catch (Exception ignored){}
            pane.getChildren().add(imagePlayers.get(i));
        }

        for (int i = 0; i < getNumberJ(); i++) {
            removeScoreBalls(i);
        }
    }

    private void drawEnemies() {
        imageGhosts = new ArrayList<ImageView>();

        for (int i = 0; i < getNumberE(); i++) {
            ImageView imageView = new ImageView(new Image(new File("ghost.png").toURI().toString()));
            imageView.setFitWidth(taillePixel); imageView.setFitHeight(taillePixel);
            imageView.setX(getCEnemy(i)*taillePixel); imageView.setY(getLEnemy(i)*taillePixel);
            imageGhosts.add(imageView);
            pane.getChildren().add(imageGhosts.get(i));
        }
    }

    private void drawStatsLabel() {
        labels = new ArrayList<Label>();

        for (int nJ = 0; nJ < presentation.getNumberJ(); nJ++) {
            labels.add(new Label("Player score "+(nJ+1)+" : "+getScore(nJ)+"\n"+"Player life "+(nJ+1)+" : "+getLife(nJ)));
            labels.get(nJ).setFont(new Font("Arial", 25));
            labels.get(nJ).setTextFill(Color.WHITE);
            labels.get(nJ).setLayoutX(20);
            labels.get(nJ).setLayoutY(nJ*35*2+15);
            right.getChildren().add(labels.get(nJ));

            memoryStats[nJ][0]=getScore(nJ);
            memoryStats[nJ][1]=getLife(nJ);
        }
    }

    // Move effects

    public void updatePlayerPosition(int nJ, int[] position, int[] xy){
        imagePlayers.get(nJ).setX(position[0]*taillePixel);
        imagePlayers.get(nJ).setY(position[1]*taillePixel);
        if (xy[0]!=0){
            imagePlayers.get(nJ).setRotate(Math.acos(xy[0])*180/Math.PI);
        }else {
            imagePlayers.get(nJ).setRotate(Math.asin(xy[1])*180/Math.PI);
        }
        enemiesAreHoverAll();
        removeScoreBalls(nJ);
    }

    public void updateViewElements(int nJ, boolean collisionPlayer, boolean collisionEnemy, int numberBerseker){
        if (!left.getStyle().equals("-fx-background-color: BLACK")){
            left.setStyle("-fx-background-color: BLACK");
            right.setStyle("-fx-background-color: BLACK");
            System.out.println("0");
        }
        else if (collisionPlayer || collisionEnemy){
            left.setStyle("-fx-background-color: RED");
            right.setStyle("-fx-background-color: RED");
        }
        else if (memoryStats[nJ][1] < getLife(nJ)){
            left.setStyle("-fx-background-color: GREEN");
            right.setStyle("-fx-background-color: GREEN");
        }
        else if (getScore(nJ)- memoryStats[nJ][0]==300){
            left.setStyle("-fx-background-color: YELLOW");
            right.setStyle("-fx-background-color: YELLOW");
        }

        if (numberBerseker == 0 && statBerseker){
            for (int nE = 0; nE < getNumberE(); nE++) {
                imageGhosts.get(nE).setImage(new Image(new File("ghost.png").toURI().toString()));
            }
            statBerseker = false;
        }
        else if (numberBerseker > 0 && !statBerseker){
            for (int nE = 0; nE < getNumberE(); nE++) {
                imageGhosts.get(nE).setImage(new Image(new File("ghost_2.png").toURI().toString()));
            }
            statBerseker = true;
        }
        labels.get(nJ).setText("Player score "+(nJ+1)+" : "+getScore(nJ)+"\n"+"Player life "+(nJ+1)+" : "+getLife(nJ));

        memoryStats[nJ][0]=getScore(nJ);
        memoryStats[nJ][1]=getLife(nJ);
    }

    public void updatePlayerSkin(int nJ, boolean playerBerseker) {
        if (!playerBerseker){
            imagePlayers.get(nJ).setImage(new Image(new File("pacman.gif").toURI().toString()));
        }else{
            imagePlayers.get(nJ).setImage(new Image(new File("pacman_berseker.png").toURI().toString()));
        }
    }

    public void updateEnemyPosition(int nE, int[] position){
        imageGhosts.get(nE).setX(position[0]*taillePixel);
        imageGhosts.get(nE).setY(position[1]*taillePixel);
    }

    public void removeScoreBalls(int nJ){
        if (matrixMap(getLPlayer(nJ), getCPlayer(nJ))==3){
            walkableZone(getLPlayer(nJ), getCPlayer(nJ));
            playersAreHoverAll();
        }
    }

    private void playersAreHoverAll(){
        for (int i = 0; i < getNumberJ(); i++) {
            pane.getChildren().remove(imagePlayers.get(i));
            pane.getChildren().add(imagePlayers.get(i));
        }
    }

    private void enemiesAreHoverAll(){
        for (int i = 0; i < getNumberE(); i++) {
            pane.getChildren().remove(imageGhosts.get(i));
            pane.getChildren().add(imageGhosts.get(i));
        }
    }

    // Map Zone Drawer

    private void noWalkableZone(int L, int C){
        Rectangle rect = new Rectangle(taillePixel,taillePixel,Color.DARKBLUE);
        rect.setX(C*taillePixel);
        rect.setY(L*taillePixel);
        rect.setSmooth(true);
        pane.getChildren().add(rect);
    }

    private void walkableZone(int L, int C){
        Rectangle rect = new Rectangle(taillePixel,taillePixel,Color.BLACK);
        rect.setX(C*taillePixel);
        rect.setY(L*taillePixel);
        rect.setSmooth(true);
        pane.getChildren().add(rect);
    }

    private void ghostZone(int L, int C){
        Rectangle rect = new Rectangle(taillePixel,taillePixel,Color.WHITE);
        rect.setX(C*taillePixel);
        rect.setY(L*taillePixel);
        rect.setSmooth(true);
        pane.getChildren().add(rect);
    }

    private void scoreZone(int L, int C){
        Circle score = new Circle(setCenter(C),setCenter(L),taillePixel/6.0);
        score.setFill(Color.PINK);
        score.setSmooth(true);
        pane.getChildren().add(score);
    }

    private void bonusZone(int L, int C){
        Circle score = new Circle(setCenter(C),setCenter(L),taillePixel/3.5);
        score.setFill(Color.ORANGE);
        score.setSmooth(true);
        pane.getChildren().add(score);
    }

    // Autres méthodes

    private int setCenter(int index){ return index*taillePixel+(taillePixel/2); }

    public void showWinner(int nWinner) {
        Label winner;
        if(nWinner!=-1){
            winner = new Label("Winner : Player "+(nWinner+1));
        }else {
            winner = new Label("Winner : Ghosts");
        }
        winner.setFont(new Font("Arial", 42));
        winner.setTextFill(Color.WHITE);
        winner.setLayoutX(10);
        winner.setLayoutY(5);
        left.getChildren().add(winner);
    }

    public void stageSize() { stage.setMaximized(true); }

    //Get data of "PLayer" and "Enemy" through "Presentation" and "Game"

    private int getLPlayer(int id){ return presentation.getLPlayer(id); }

    private int getCPlayer(int id){ return presentation.getCPlayer(id); }

    private int getLEnemy(int id){ return presentation.getLEnemy(id); }

    private int getCEnemy(int id){ return presentation.getCEnemy(id); }

    private int getScore(int nJ){ return presentation.getScore(nJ); }

    private int getLife(int nJ){ return presentation.getLife(nJ); }

    private int getColumns(){ return presentation.getColumns(); }

    private int getLines(){ return presentation.getLines(); }

    //Get data of "Game" "Presentation"

    private int matrixMap(int L, int C){ return presentation.statZone(L,C); }

    private int getNumberJ(){ return presentation.getNumberJ(); }

    private int getNumberE() { return presentation.getNumberE(); }
}