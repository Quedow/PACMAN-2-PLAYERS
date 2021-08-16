package com.company;

public class Main {

    public static void main(String[] strings) {

        Game game = new Game();
        Presentation presentation = new Presentation(game);
        Vue vue = new Vue(presentation);

        presentation.associateVue(vue);
    }
}
