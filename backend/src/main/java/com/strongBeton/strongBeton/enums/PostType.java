package com.strongBeton.strongBeton.enums;

public enum PostType {

    WORKOUT(0,"workout"),
    ACHIEVEMENT(1,"achievement"),
    TEXT(2, "text");

    private int value = 0;
    private String Text;

    PostType(int value, String text) {
        this.value = value;
        Text = text;
    }

    public int getValue() {
        return value;
    }

    public String getText() {
        return Text;
    }

    @Override
    public String toString() {
        return Text;
    }
}
