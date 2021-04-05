package com.company;

import java.util.ArrayList;
import java.util.Arrays;

public class Ennemy extends Entity {

    private ArrayList<int[]> direction;
    private int currentX;
    private int currentY;
    private boolean intelligent;
    private int prefTarget;

    public Ennemy(int numberJ){
        Centity = 0;
        Lentity = 0;
        currentX = 0;
        currentY = 0;
        prefTarget = 0;
        intelligent = false;
        setIntelligent(numberJ, randomValue(1, 2));
        initializeDirection();
    }

    public void setIntelligent(int numberJ, int randomDestiny) {
        if (randomDestiny == 1){
            intelligent = true;
            prefTarget = randomValue(0, numberJ-1);
        }
    }

    public void initializeDirection(){
        direction = new ArrayList<int[]>();
        direction.add(new int[]{0, -1});
        direction.add(new int[]{0, 1});
        direction.add(new int[]{-1, 0});
        direction.add(new int[]{1, 0});
    }

    public int[] findEnnemyDirection(int[][] matriceMap, int Cplayer, int Lplayer) {
        ArrayList<int[]> possibleDirection = new ArrayList<int[]>();
        int possibleWay = 0;
        for (int[] ints : direction) {
            int x = ints[0];
            int y = ints[1];
            try {
                if (matriceMap[Lentity + y][Centity + x] != 1) {
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

    public int shorterToPlayer(ArrayList<int[]> possibleDirection, int Cplayer, int Lplayer){
        double shorterDistance = 100;
        int index = 0;
        for (int i = 0; i < possibleDirection.size(); i++) {
            double C = (Cplayer-(Centity+possibleDirection.get(i)[0]));
            double L = (Lplayer-(Lentity+possibleDirection.get(i)[1]));
            double distance = Math.sqrt(C*C+L*L);

            System.out.println("x = " + C);
            System.out.println("y = " + L);
            System.out.println("Distance : " + distance);

            if (distance <= shorterDistance){
                shorterDistance = distance;
                index = i;
            }
        }
        return index;
    }

    public void setCurrentXY(int[] xy){
        currentX = xy[0];
        currentY = xy[1];
    }

    public int getPrefTarget() { return prefTarget; }

    public boolean isIntelligent() { return intelligent; }

    public int getCentity(){ return Centity; }

    public int getLentity(){ return Lentity; }
}
