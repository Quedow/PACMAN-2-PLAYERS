package com.company;

public class Player extends Entity {

    private int score;
    private int life;
    private boolean berseker;
    private int bersekerTime;
    private boolean invincible;
    private int invincibleTime;

    public Player(){
        Centity=0;
        Lentity=0;
        score=0;
        life=3;
        berseker = false;
        bersekerTime = 0;
        invincible = false;
        invincibleTime = 0;
    }

    public void randomPowerUp(){
        incrementScore(100);
        int value = randomValue(1, 5);
        switch (value){
            case 1: case 2: case 3:
                bersekerMode();
            break;
            case 4:
                incrementScore(200);
                System.out.println("+200 de score");
            break;
            case 5:
                life += 1;
                System.out.println("+1 vie");
            break;
        }
    }

    public void bersekerMode(){
        berseker = true;
        System.out.println("Berseker commence.");
    }

    public void incrementBersekerTime(){
        if (berseker){
            bersekerTime++;
            if (bersekerTime >= 10){ //10 car cette methode s'effectue 1 fois/s
                berseker = false;
                bersekerTime = 0;
                System.out.println("Berseker termine.");
            }
        }
    }

    public void incrementInvincibleTime(){
        if (invincible){
            invincibleTime++;
            if (invincibleTime >= 3){ //3 car cette methode s'effectue 3 fois/s
                invincible = false;
                invincibleTime = 0;
                System.out.println("Invincible termine.");
            }
        }
    }

    public void looseLife(){
        if (!berseker && !invincible){
            life--;
            invincible = true;
            System.out.println("Invincible commence.");
        }
    }

    public void incrementScore(int scoreValue){
        score+=scoreValue;
    }

    public boolean isBerseker(){ return berseker; }

    public int getLife(){ return life; }

    public int getScore(){ return score; }
}
