package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Game {

    private int columns;
    private int lines;
    private int [][] matrixMap;
    private List<String> textLines;

    private ArrayList<Player> players;
    private ArrayList<Enemy> enemies;
    private int numberJ;
    private int numberE;

    public Game(){
        numberJ = 2;
        numberE = 4;
        textLines = new ArrayList<String>();
        players = new ArrayList<Player>();
        enemies = new ArrayList<Enemy>();

        readMapFile();
        createMatrixMap();
    }

    // Initialize Game

    private void readMapFile(){
        Path path = Paths.get("PACMAN_MAP.txt");
        System.out.println("File find : " + Files.exists(path));
        try {
            textLines = Files.readAllLines(path);
        } catch (IOException ignored) {}

        columns = textLines.get(0).length();
        lines = textLines.size();
    }

    private void createMatrixMap(){
        matrixMap = new int[lines][columns];

        for(int L = 0; L < lines; L++ ){
            for(int C = 0; C < columns; C++) {
                try {
                    matrixMap[L][C] = Integer.parseInt(textLines.get(L).substring(C,C+1));
                }catch (Exception exception){
                    matrixMap[L][C] = 1;
                }
            }
        }
        showMatrixMap();
    }

    private void showMatrixMap(){
        System.out.println("Matrix Map generated :");
        for(int L = 0; L < lines; L++ ){
            for(int C = 0; C < columns; C++) {
                System.out.print(matrixMap[L][C]+" ");
            }
            System.out.println();
        }
    }

    public void setNumberJ(int nJ) {
        numberJ=nJ;
        initializePlayer();
        initializeEnemy();
    }

    public void setNumberE(int nE) { numberE = nE; }

    private void initializePlayer(){
        for (int nJ = 0; nJ < numberJ; nJ++) {
            players.add(new Player());
            int[] couple = findSpecificPosition(2);
            players.get(nJ).setPosition(couple[0], couple[1]);
            removeScoreBalls(nJ);
        }
    }

    private void initializeEnemy(){
        for (int nE = 0; nE < numberE; nE++) {
            enemies.add(new Enemy(numberJ));
            int[] couple = findSpecificPosition(0);
            enemies.get(nE).setPosition(couple[0], couple[1]);
        }
    }

    private int[] findSpecificPosition(int statZone){
        int L,C;
        do{
            L = randomValue(0, lines -1);
            C = randomValue(0, columns -1);
        }while (statZone(L,C) != statZone);
        return new int[]{C, L};
    }

    // Move and teleport player or enemy

    public int[] movePlayer(int x, int y, int nJ){
        players.get(nJ).moveEntity(x,y);
        removeScoreBalls(nJ);
        return new int[]{ players.get(nJ).getCEntity(), players.get(nJ).getLEntity()};
    }

    public int[] moveEnemy(int x, int y, int nE){
        enemies.get(nE).moveEntity(x,y);
        return new int[]{ enemies.get(nE).getCEntity(), enemies.get(nE).getLEntity()};
    }

    public int[] findEnemyDirection(int nE) {
        return enemies.get(nE).findEnemyDirection(matrixMap, players.get(enemies.get(nE).getPrefTarget()).CEntity, players.get(enemies.get(nE).getPrefTarget()).LEntity);
    }

    public int[] tp(int CEntity, int LEntity){
        HashMap<Integer, Integer> locateExits = new HashMap<Integer, Integer>();
        for (int L = 0; L< lines; L++){
            for (int C = 0; C< columns; C++){
                if((C==0 || C== columns -1 || L==0 || L== lines -1)
                        && (matrixMap[L][C]==2 || matrixMap[L][C]==3 || matrixMap[L][C]==4)
                        && (C!= CEntity || L!= LEntity)){
                    locateExits.put(C,L);
                }
            }
        }
        try {
            Object[] tabKeys = locateExits.keySet().toArray();
            int index = (int) (Math.random() * tabKeys.length);
            int randomKey = (int) tabKeys[index];

            return new int[]{ randomKey, locateExits.get(randomKey) };
        }catch (Exception ignored){}

        return new int[0];
    }

    public int[] tpPlayer(int nJ, int[] tpPosition){
        if (tpPosition.length != 0){
            players.get(nJ).setPosition(tpPosition[0],tpPosition[1]);
            removeScoreBalls(nJ);
        }
        return tpPosition;
    }

    private void tpEnemy(int nE, int[] tpPosition){
        if (tpPosition.length != 0){
            enemies.get(nE).setPosition(tpPosition[0],tpPosition[1]);
        }
    }

    public void removeScoreBalls(int nJ){
        if (matrixMap[players.get(nJ).getLEntity()][players.get(nJ).getCEntity()]==2){
            players.get(nJ).incrementScore(10);
            matrixMap[players.get(nJ).getLEntity()][players.get(nJ).getCEntity()]=3;
        }
        else if (matrixMap[players.get(nJ).getLEntity()][players.get(nJ).getCEntity()]==4){
            players.get(nJ).randomPowerUp();
            matrixMap[players.get(nJ).getLEntity()][players.get(nJ).getCEntity()]=3;
        }
    }

    // Collisions management

    public boolean collisionPlayer(){
        for (int i = 0; i < numberJ; i++) {
            for (int j = 0; j < numberJ; j++) {
                if (getLplayer(i) == getLplayer(j) && getCplayer(i)==getCplayer(j) && i!=j){
                    players.get(i).looseLife();
                    return true;
                }
            }
        }
        return false;
    }

    public boolean collisionEnemy(){
        //try {
        for (int nJ = 0; nJ < numberJ; nJ++) {
            for (int nE = 0; nE < numberE; nE++) {
                if (getLplayer(nJ) == getLEnemy(nE) && getCplayer(nJ) == getCEnemy(nE)){
                    players.get(nJ).looseLife();
                    if (players.get(nJ).isBerserker()){
                        int[] couple = findSpecificPosition(0);
                        tpEnemy(nE, new int[]{couple[0], couple[1]});
                        players.get(nJ).incrementScore(400);
                        return false;
                    }
                    return true;
                }
            }
        }
        //}catch (Exception ignored){}
        return false;
    }

    // End game management

    public boolean winCondition(){
        if (numberJ == 1){
            return counterScoreBalls() == 0 || nAlive() < 1;
        }
        return counterScoreBalls() == 0 || nAlive() <= 1;
    }

    private int counterScoreBalls(){
        int numberScoreBall = 0;
        for(int L = 0; L < lines; L++ ){
            for(int C = 0; C < columns; C++) {
                if (matrixMap[L][C] == 2){
                    numberScoreBall++;
                }
            }
        }
        return numberScoreBall;
    }

    private int nAlive(){
        int alive = 0;
        for (int i = 0; i < numberJ; i++) {
            if (players.get(i).getLife() > 0){
                alive++;
            }
        }
        return alive;
    }

    public int nWinner(){
        int bestScore = 0;
        int winner = -1;
        for (int i = 0; i < numberJ; i++) {
            if (players.get(i).getLife() > 0 && players.get(i).getScore() > bestScore){
                bestScore = players.get(i).getScore();
                winner = i;
            }
        }
        return winner;
    }

    // Stat map place indicator

    public int walkable(int x, int y, int nJ){
        try {
            if(matrixMap[getLplayer(nJ)+y][getCplayer(nJ)+x]==2
                    || matrixMap[getLplayer(nJ)+y][getCplayer(nJ)+x]==3
                        || matrixMap[getLplayer(nJ)+y][getCplayer(nJ)+x]==4){
                return 1;
            }
            else {
                return 0;
            }
        } catch (Exception e) {
            return 2;
        }
    }

    public int statZone(int L, int C){ return matrixMap[L][C]; }

    public int getNumberBerserker(){
        int nB = 0;
        for (int nJ = 0; nJ < getNumberJ(); nJ++) {
            if (getPlayerBerserker(nJ)){
                nB++;
            }
        }
        return nB;
    }

    // Other methods

    public int randomValue(int borneInf, int borneSup){ return (int)(Math.random() * ((borneSup - borneInf) + 1)) + borneInf; }

    //Get and Update data to "Player"

    public int getScore(int nJ){ return players.get(nJ).getScore(); }

    public int getLife(int nJ){ return players.get(nJ).getLife(); }

    public int getCplayer(int nJ){ return players.get(nJ).getCEntity(); }

    public int getLplayer(int nJ){ return players.get(nJ).getLEntity(); }

    public void incrementBerserkerTime(int nJ) { players.get(nJ).incrementBerserkerTime(); }

    public void incrementInvincibleTime(int nJ) { players.get(nJ).incrementInvincibleTime(); }

    //Get and Update data to "Enemy"

    public int getCEnemy(int nJ){ return enemies.get(nJ).getCEntity(); }

    public int getLEnemy(int nJ){ return enemies.get(nJ).getLEntity(); }

    //Send data

    public int getColumns() { return columns; }

    public int getLines() { return lines; }

    public int setPixelSize(int yScreen) { return yScreen/lines-1; }

    public int getNumberJ() { return numberJ; }

    public int getNumberE() { return numberE; }

    public boolean getPlayerBerserker(int nJ){ return players.get(nJ).isBerserker(); }
}
