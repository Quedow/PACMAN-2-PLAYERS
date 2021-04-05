package com.company;

public class Bonus {

    private int pacmanSpeed;
    private int ghostSpeed;
    private int playerLife;
    private int scoreBonus;

    public Bonus(){

    }

    public int[] chooseBonus(){
        int value = randomValue(1,4);
        switch (value){
            case 1:
                return new int[]{1, randomValue(100,500)};
            case 2:
                return new int[]{2, randomValue(1,6)*10};
            case 3:
                return new int[]{3, 3};
            case 4:
                return new int[]{4, 250};
        }
        return new int[0];
    }

    public int randomValue(int borneInf, int borneSup){
        return (int)(Math.random() * ((borneSup - borneInf) + 1)) + borneInf;
    }

}
