package com.aurelios.server.game.user.chat;

public enum Title {

    TEST("Test");

    private String display;

    Title(String display){this.display = display;}

    public String getDisplay(){
        return "[" + display + "] ";
    }

}
