/*
PACMAN2P - MAMMA Quentin & LUQUET Steven - A21
 */
package com.company;

public class Player extends Entity {

    private int score;
    private int life;
    private boolean berserker;
    private int berserkerTime;
    private boolean invincible;
    private int invincibleTime;

    public Player(){
        CEntity =0;
        LEntity =0;
        score=0;
        life=3;

        berserker = false;
        berserkerTime = 0;
        invincible = false;
        invincibleTime = 0;
    }

    public void randomPowerUp(){
        incrementScore(100);
        int value = randomValue(1, 5);
        switch (value){
            case 1: case 2: case 3:
                berserkerMode();
            break;
            case 4:
                incrementScore(200);
            break;
            case 5:
                life += 2;
            break;
        }
    }

    private void berserkerMode(){
        berserker = true;
        System.out.println("Berserker début.");
    }

    public void incrementBerserkerTime(){
        if (berserker){
            berserkerTime++;
            if (berserkerTime >= 10){ //10 car cette methode s'effectue 1 fois/s
                berserker = false;
                berserkerTime = 0;
                System.out.println("Berserker fin.");
            }
        }
    }

    public void incrementInvincibleTime(){
        if (invincible){
            invincibleTime++;
            if (invincibleTime >= 3){ //3 car cette methode s'effectue 3 fois/s
                invincible = false;
                invincibleTime = 0;
                System.out.println("Invincible fin.");
            }
        }
    }

    public void looseLife(){
        if (!berserker && !invincible){
            life--;
            invincible = true;
            System.out.println("Invincible début.");
        }
    }

    public void incrementScore(int scoreValue){
        score+=scoreValue;
    }

    public boolean isBerserker(){ return berserker; }

    public int getLife(){ return life; }

    public int getScore(){ return score; }
}
