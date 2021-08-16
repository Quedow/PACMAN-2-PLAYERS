package com.company;

public class Main {

    public static void main(String[] strings) {

        Game game = new Game();
        Presentation presentation = new Presentation(game);
        Vew vew = new Vew(presentation);

        presentation.associateVue(vew);
    }
}
