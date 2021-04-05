package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Game {

    private int colonnes;
    private int lignes;
    private int [][] matriceMap;
    private List<String> lines;

    private ArrayList<Player> players;
    private ArrayList<Ennemy> ennemies;
    private int numberJ;
    private int numberE;

    public Game(){
        numberJ = 2;
        numberE = 4;
        lines = new ArrayList<String>();
        players = new ArrayList<Player>();
        ennemies = new ArrayList<Ennemy>();

        readMapFile();
        createMatriceMap();
    }

    // Initialize Game

    public void readMapFile(){
        Path path = Paths.get("PACMAN_MAP.txt");
        System.out.println("Fichier trouvé : " + Files.exists(path));
        try {
            lines = Files.readAllLines(path);
        } catch (IOException ignored) {}

        colonnes = lines.get(0).length();
        lignes = lines.size();
    }

    public void createMatriceMap(){
        matriceMap = new int[lignes][colonnes];

        for(int L = 0; L < lignes; L++ ){
            for(int C = 0; C < colonnes; C++) {
                try {
                    matriceMap[L][C] = Integer.parseInt(lines.get(L).substring(C,C+1));
                }catch (Exception exception){
                    matriceMap[L][C] = 1;
                }
            }
        }
        showMatriceMap();
    }

    public void showMatriceMap(){
        System.out.println("Matrice Map générée :");
        for(int L = 0; L < lignes; L++ ){
            for(int C = 0; C < colonnes; C++) {
                System.out.print(matriceMap[L][C]+" ");
            }
            System.out.println();
        }
    }

    public void setNumberJ(int nJ) {
        numberJ=nJ;
        initializePlayer();
        initializeEnnemy();
    }

    public void setNumberE(int nE) { numberE = nE; }

    public void initializePlayer(){
        for (int nJ = 0; nJ < numberJ; nJ++) {
            players.add(new Player());
            int[] couple = findSpecificPosition(2);
            players.get(nJ).setPosition(couple[0], couple[1]);
        }
    }

    public void initializeEnnemy(){
        for (int nE = 0; nE < numberE; nE++) {
            ennemies.add(new Ennemy(numberJ));
            int[] couple = findSpecificPosition(0);
            ennemies.get(nE).setPosition(couple[0], couple[1]);
        }
    }

    public int[] findSpecificPosition(int statZone){
        int L,C;
        do{
            L = randomValue(0, lignes-1);
            C = randomValue(0, colonnes-1);
        }while (statZone(L,C) != statZone);
        return new int[]{C, L};
    }

    // Move and teleport player or ennemy

    public int[] movePlayer(int x, int y, int nJ){
        players.get(nJ).moveEntity(x,y);
        removeScoreBalls(nJ);
        return new int[]{ players.get(nJ).getCentity(), players.get(nJ).getLentity()};
    }

    public int[] moveEnnemy(int x, int y, int nE){
        ennemies.get(nE).moveEntity(x,y);
        return new int[]{ ennemies.get(nE).getCentity(), ennemies.get(nE).getLentity()};
    }

    public int[] findEnnemyDirection(int nE) {
        return ennemies.get(nE).findEnnemyDirection(matriceMap, players.get(ennemies.get(nE).getPrefTarget()).Centity, players.get(ennemies.get(nE).getPrefTarget()).Lentity);
    }

    public int[] tp(int Centity, int Lentity){
        HashMap<Integer, Integer> locateExits = new HashMap<Integer, Integer>();
        System.out.println("Les sorties possibles :");
        for (int L=0;L<lignes;L++){
            for (int C=0;C<colonnes;C++){
                if((C==0 || C==colonnes-1 || L==0 || L==lignes-1)
                        && (matriceMap[L][C]==2 || matriceMap[L][C]==3 || matriceMap[L][C]==4)
                        && (C!= Centity || L!= Lentity)){
                    locateExits.put(C,L);
                    System.out.println("Key : "+C+" valeur : "+L);
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

    public void tpEnnemy(int nE, int[] tpPosition){
        if (tpPosition.length != 0){
            ennemies.get(nE).setPosition(tpPosition[0],tpPosition[1]);
        }
    }

    public void removeScoreBalls(int nJ){
        if (matriceMap[players.get(nJ).getLentity()][players.get(nJ).getCentity()]==2){
            players.get(nJ).incrementScore(10);
            matriceMap[players.get(nJ).getLentity()][players.get(nJ).getCentity()]=3;
        }
        else if (matriceMap[players.get(nJ).getLentity()][players.get(nJ).getCentity()]==4){
            players.get(nJ).randomPowerUp();
            matriceMap[players.get(nJ).getLentity()][players.get(nJ).getCentity()]=3;
        }
    }

    // Gestion des collisions

    public void collisionPlayer(){ //À voir si on la garde
        for (int i = 0; i < numberJ; i++) {
            for (int j = 0; j < numberJ; j++) {
                if (getLplayer(i) == getLplayer(j) && getCplayer(i)==getCplayer(j) && i!=j){
                    players.get(i).looseLife();
                }
            }
        }
    }

    public void collisionEnnemy(){
        try {
            for (int nJ = 0; nJ < numberJ; nJ++) {
                for (int nE = 0; nE < numberE; nE++) {
                    if (getLplayer(nJ) == getLennemy(nE) && getCplayer(nJ)==getCennemy(nE)){
                        players.get(nJ).looseLife();
                        if (players.get(nJ).isBerseker()){
                            int[] couple = findSpecificPosition(0);
                            tpEnnemy(nE, new int[]{couple[0], couple[1]});
                            players.get(nJ).incrementScore(400);
                        }
                    }
                }
            }
        }catch (Exception ignored){}
    }

    // Gestion de fin de partie

    public boolean winCondition(){
        if (numberJ == 1){
            return counterScoreBalls() == 0 || nAlive() < 1;
        }
        return counterScoreBalls() == 0 || nAlive() <= 1;
    }

    public int counterScoreBalls(){
        int numberScoreBall = 0;
        for(int L = 0; L < lignes; L++ ){
            for(int C = 0; C < colonnes; C++) {
                if (matriceMap[L][C] == 2){
                    numberScoreBall++;
                }
            }
        }
        return numberScoreBall;
    }

    public int nAlive(){
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

    // Indicateur d'état ou de valeur d'un lieu précis de la map

    public int walkable(int x, int y, int nJ){
        try {
            if(matriceMap[getLplayer(nJ)+y][getCplayer(nJ)+x]==2
                    || matriceMap[getLplayer(nJ)+y][getCplayer(nJ)+x]==3
                        || matriceMap[getLplayer(nJ)+y][getCplayer(nJ)+x]==4){
                return 1;
            }
            else {
                return 0;
            }
        } catch (Exception e) {
            return 2;
        }
    }

    public int statZone(int L, int C){ return matriceMap[L][C]; }

    public int getNumberBerseker(){
        int nB = 0;
        for (int nJ = 0; nJ < getNumberJ(); nJ++) {
            if (getPlayerBerseker(nJ)){
                nB++;
            }
        }
        return nB;
    }

    // Autres méthodes

    public int randomValue(int borneInf, int borneSup){ return (int)(Math.random() * ((borneSup - borneInf) + 1)) + borneInf; }

    //Get and Update data to "Player"

    public int getScore(int nJ){ return players.get(nJ).getScore(); }

    public int getLife(int nJ){ return players.get(nJ).getLife(); }

    public int getCplayer(int nJ){ return players.get(nJ).getCentity(); }

    public int getLplayer(int nJ){ return players.get(nJ).getLentity(); }

    public void incrementBersekerTime(int nJ) { players.get(nJ).incrementBersekerTime(); }

    public void incrementInvincibleTime(int nJ) { players.get(nJ).incrementInvincibleTime(); }

    //Get and Update data to "Ennemy"

    public int getCennemy(int nJ){ return ennemies.get(nJ).getCentity(); }

    public int getLennemy(int nJ){ return ennemies.get(nJ).getLentity(); }

    //Send data

    public int getColonnes() { return colonnes; }

    public int getLignes() { return lignes; }

    public int setTaillePixel(int hauteur) { return hauteur/lignes-1; }

    public int getNumberJ() { return numberJ; }

    public int getNumberE() { return numberE; }

    public boolean getPlayerBerseker(int nJ){ return players.get(nJ).isBerseker(); }
}
