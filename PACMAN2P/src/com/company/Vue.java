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

    private int longueur;
    private int hauteur;
    private int taillePixel;
    private boolean statBerseker;

    private ArrayList<ImageView> imagePlayers;
    private ArrayList<ImageView> imageGhosts;
    private ArrayList<Label> labels;
    private List<ColorAdjust> colorAdjusts;

    private Stage fenetre = new Stage();

    // Elements of the 1st view (menu)
    private Scene sceneMenu;
    private Pane canvasMenu;
    private Button onePlayer;
    private Button twoPlayer;
    private Button exit;
    private TextField numberE;

    // Elements of the 2nd view (game)
    private Scene sceneGame;
    private BorderPane borderPane;
    private Pane pane;
    private Pane right;
    private Pane left;

    public Vue(Presentation presentation)
    {
        this.presentation = presentation;
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
        sceneGame = new Scene(borderPane, longueur, hauteur);
        sceneMenu = new Scene(canvasMenu, longueur, hauteur);

        // Initialize window
        fenetre.setTitle("PACMAN FOR 2 PLAYERS");
        fenetre.setMaximized(true);
        fenetre.setResizable(false);
        fenetre.setScene(sceneMenu);
        fenetre.show();

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

    public void initializeMenu(){
        canvasMenu = new Pane();
        canvasMenu.setStyle("-fx-background-color: BLACK");

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

        canvasMenu.getChildren().addAll(title, onePlayer, twoPlayer,exit, numberE);
    }

    public void initializeGame(){
        borderPane = new BorderPane();

        pane = new Pane();
        pane.setMaxWidth(taillePixel*presentation.getColonnes());
        pane.setStyle("-fx-background-color: DARKBLUE");
        borderPane.setCenter(pane);

        left = new Pane();
        left.setStyle("-fx-background-color: BLACK");
        left.setMinWidth((longueur-taillePixel*presentation.getColonnes())/2.0);
        borderPane.setLeft(left);

        right = new Pane();
        right.setStyle("-fx-background-color: BLACK");
        right.setMinWidth((longueur-(taillePixel*presentation.getColonnes())-left.getMinWidth()));
        borderPane.setRight(right);

        drawMap();
        initializeColors();
    }

    public void drawMap() {
        for (int L = 0; L < getLignes(); L++) {
            for (int C = 0; C < getColonnes(); C++) {

                if (matriceMap(L,C)==1) {
                    unwalkableZone(L,C);
                }
                else if (matriceMap(L,C)==2) {
                    walkableZone(L,C);
                    scoreZone(L,C);
                }
                else if (matriceMap(L,C)==3){
                    walkableZone(L,C);
                }
                else if (matriceMap(L,C)==0){
                    ghostZone(L,C);
                }
                else if (matriceMap(L,C)==4){
                    walkableZone(L,C);
                    bonusZone(L,C);
                }
            }
        }
    }

    public void initializeColors() {
        ColorAdjust yellow = new ColorAdjust(); //Yellow car c'est la couleur de base de l'image
        ColorAdjust green = new ColorAdjust();
        green.setHue(0.3);

        colorAdjusts = Arrays.asList(yellow, green);
    }

    public void initializeEntities(int nJ){
        fenetre.setScene(sceneGame);
        presentation.setNumberJ(nJ);
        drawPlayer();
        drawEnnemies();
        drawScoreLabel();
    }

    public void drawPlayer() {
        imagePlayers = new ArrayList<ImageView>();

        for (int i = 0; i < getNumberJ(); i++) {
            ImageView imageView = new ImageView(new Image(new File("pacman.gif").toURI().toString()));
            imageView.setFitWidth(taillePixel); imageView.setFitHeight(taillePixel);
            imageView.setX(getCplayer(i)*taillePixel); imageView.setY(getLplayer(i)*taillePixel);
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

    private void drawEnnemies() {
        imageGhosts = new ArrayList<ImageView>();

        for (int i = 0; i < getNumberE(); i++) {
            ImageView imageView = new ImageView(new Image(new File("ghost.png").toURI().toString()));
            imageView.setFitWidth(taillePixel); imageView.setFitHeight(taillePixel);
            imageView.setX(getCennemy(i)*taillePixel); imageView.setY(getLennemy(i)*taillePixel);
            imageGhosts.add(imageView);
            pane.getChildren().add(imageGhosts.get(i));
        }
    }

    public void drawScoreLabel() {
        labels = new ArrayList<Label>();

        for (int i = 0; i < presentation.getNumberJ(); i++) {
            labels.add(new Label("Score Joueur "+i+" : "+getScore(i)+"\n"+"Vie Joueur "+i+" : "+getLife(i)));
            labels.get(i).setFont(new Font("Arial", 25));
            labels.get(i).setTextFill(Color.WHITE);
            labels.get(i).setLayoutX(20);
            labels.get(i).setLayoutY(i*35*2+15);
            right.getChildren().add(labels.get(i));
        }
    }

    // Move effects

    public void updatePlayerVue(int nJ, int[] position, int[] xy){
        imagePlayers.get(nJ).setX(position[0]*taillePixel);
        imagePlayers.get(nJ).setY(position[1]*taillePixel);
        if (xy[0]!=0){
            imagePlayers.get(nJ).setRotate(Math.acos(xy[0])*180/Math.PI);
        }else {
            imagePlayers.get(nJ).setRotate(Math.asin(xy[1])*180/Math.PI);
        }
        removeScoreBalls(nJ);
        ennemiesAreHoverAll();
        //showPositionPlayer(nJ);
    }

    public void updatePlayerStat(){
        for (int nJ = 0; nJ < getNumberJ(); nJ++) {
            labels.get(nJ).setText("Score Joueur "+nJ+" : "+getScore(nJ)+"\n"+"Vie Joueur "+nJ+" : "+getLife(nJ));
        }
    }

    public void updateEnnemyVue(int nE, int[] position){
        imageGhosts.get(nE).setX(position[0]*taillePixel);
        imageGhosts.get(nE).setY(position[1]*taillePixel);
    }

    public void removeScoreBalls(int nJ){
        if (matriceMap(getLplayer(nJ), getCplayer(nJ))==3){
            walkableZone(getLplayer(nJ), getCplayer(nJ));
            playersAreHoverAll();
        }
    }

    public void playersAreHoverAll(){
        for (int i = 0; i < getNumberJ(); i++) {
            pane.getChildren().remove(imagePlayers.get(i));
            pane.getChildren().add(imagePlayers.get(i));
        }
    }

    public void ennemiesAreHoverAll(){
        for (int i = 0; i < getNumberE(); i++) {
            pane.getChildren().remove(imageGhosts.get(i));
            pane.getChildren().add(imageGhosts.get(i));
        }
    }

    public void bersekerAmbiance(int nJ, boolean playerBerseker, int numberBerseker) {
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

        if (!playerBerseker){
            imagePlayers.get(nJ).setImage(new Image(new File("pacman.gif").toURI().toString()));
        }else{
            imagePlayers.get(nJ).setImage(new Image(new File("pacman_berseker.png").toURI().toString()));
        }
    }

    // Map Zone Drawer

    public void unwalkableZone(int L, int C){
        Rectangle rect = new Rectangle(taillePixel,taillePixel,Color.DARKBLUE);
        rect.setX(C*taillePixel);
        rect.setY(L*taillePixel);
        rect.setSmooth(true);
        pane.getChildren().add(rect);
    }

    public void walkableZone(int L, int C){
        Rectangle rect = new Rectangle(taillePixel,taillePixel,Color.BLACK);
        rect.setX(C*taillePixel);
        rect.setY(L*taillePixel);
        rect.setSmooth(true);
        pane.getChildren().add(rect);
    }

    public void ghostZone(int L, int C){
        Rectangle rect = new Rectangle(taillePixel,taillePixel,Color.WHITE);
        rect.setX(C*taillePixel);
        rect.setY(L*taillePixel);
        rect.setSmooth(true);
        pane.getChildren().add(rect);
    }

    public void scoreZone(int L, int C){
        Circle score = new Circle(setCenter(C),setCenter(L),taillePixel/6.0);
        score.setFill(Color.PINK);
        score.setSmooth(true);
        pane.getChildren().add(score);
    }

    public void bonusZone(int L, int C){
        Circle score = new Circle(setCenter(C),setCenter(L),taillePixel/3.5);
        score.setFill(Color.ORANGE);
        score.setSmooth(true);
        pane.getChildren().add(score);
    }

    // Autres méthodes

    public int setCenter(int index){
        return index*taillePixel+(taillePixel/2);
    }

    public void showPositionPlayer(int nJ){
        System.out.println("Position du joueur "+nJ+" : Colonne "+ getCplayer(nJ)+", Ligne "+ getLplayer(nJ));
    }

    public void showWinner(int nWinner) {
        Label winner;
        if(nWinner!=-1){
            winner = new Label("Gagant : Joueur "+nWinner);
        }else {
            winner = new Label("Gagant : Fantômes");
        }
        winner.setFont(new Font("Arial", 44));
        winner.setTextFill(Color.RED);
        winner.setLayoutX(10);
        winner.setLayoutY(5);
        left.getChildren().add(winner);
    }

    public void fenetreSize() { fenetre.setMaximized(true); }

    //Get data of "PLayer" and "Ennemy" through "Presentation" and "Game"

    public int getLplayer(int id){ return presentation.getLplayer(id); }

    public int getCplayer(int id){ return presentation.getCplayer(id); }

    public int getLennemy(int id){ return presentation.getLennemy(id); }

    public int getCennemy(int id){ return presentation.getCennemy(id); }

    public int getScore(int nJ){ return presentation.getScore(nJ); }

    public int getLife(int nJ){ return presentation.getLife(nJ); }

    public int getColonnes(){ return presentation.getColonnes(); }

    public int getLignes(){ return presentation.getLignes(); }

    //Get data of "Game" "Presentation"

    public int matriceMap(int L, int C){ return presentation.statZone(L,C); }

    public int getNumberJ(){ return presentation.getNumberJ(); }

    public int getNumberE() { return presentation.getNumberE(); }
}