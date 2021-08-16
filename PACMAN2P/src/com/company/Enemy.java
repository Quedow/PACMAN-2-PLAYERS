package com.company;

import java.util.ArrayList;
import java.util.Arrays;

public class Enemy extends Entity {

    private ArrayList<int[]> direction;
    private int currentX;
    private int currentY;
    private boolean intelligent;
    private int prefTarget;

    public Enemy(int numberJ){
        CEntity = 0;
        LEntity = 0;
        currentX = 0;
        currentY = 0;
        prefTarget = 0;
        intelligent = false;
        setIntelligent(numberJ, randomValue(1, 2));
        initializeDirection();
    }

    private void setIntelligent(int numberJ, int randomDestiny) {
        if (randomDestiny == 1){
            intelligent = true;
            prefTarget = randomValue(0, numberJ-1);
        }
    }

    private void initializeDirection(){
        direction = new ArrayList<int[]>();
        direction.add(new int[]{0, -1});
        direction.add(new int[]{0, 1});
        direction.add(new int[]{-1, 0});
        direction.add(new int[]{1, 0});
    }

    public int[] findEnemyDirection(int[][] matriceMap, int Cplayer, int Lplayer) {
        ArrayList<int[]> possibleDirection = new ArrayList<int[]>();
        int possibleWay = 0;
        for (int[] ints : direction) {
            int x = ints[0];
            int y = ints[1];
            try {
                if (matriceMap[LEntity + y][CEntity + x] != 1) {
                    possibleDirection.add(new int[]{x, y});
                    possibleWay++;
                }
            } catch (Exception ignored) {}
        }
        if (possibleWay >= 2){
            for (int i = 0; i < possibleDirection.size(); i++) {
                if (Arrays.equals(possibleDirection.get(i), new int[]{-currentX, -currentY})){
                    possibleDirection.remove(i);
                    break;
                }
            }
        }
        int theDirection = randomValue(0, possibleDirection.size()-1);
        if (isIntelligent()){
            theDirection = shorterToPlayer(possibleDirection, Cplayer, Lplayer);
        }
        setCurrentXY(possibleDirection.get(theDirection));

        return possibleDirection.get(theDirection);
    }

    private int shorterToPlayer(ArrayList<int[]> possibleDirection, int Cplayer, int Lplayer){
        double shorterDistance = 100;
        int index = 0;
        for (int i = 0; i < possibleDirection.size(); i++) {
            double C = (Cplayer-(CEntity +possibleDirection.get(i)[0]));
            double L = (Lplayer-(LEntity +possibleDirection.get(i)[1]));
            double distance = Math.sqrt(C*C+L*L);

            if (distance <= shorterDistance){
                shorterDistance = distance;
                index = i;
            }
        }
        return index;
    }

    private void setCurrentXY(int[] xy){
        currentX = xy[0];
        currentY = xy[1];
    }

    public int getPrefTarget() { return prefTarget; }

    public boolean isIntelligent() { return intelligent; }

    public int getCEntity(){ return CEntity; }

    public int getLEntity(){ return LEntity; }
}
