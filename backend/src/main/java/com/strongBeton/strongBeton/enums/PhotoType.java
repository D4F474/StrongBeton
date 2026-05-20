package com.strongBeton.strongBeton.enums;

public enum PhotoType {
    PROFILE(0,"profile"),
    CERTIFICATE(1,"certificate"),
    OTHER(2, "other");

    private int value = 0;
    private String Text;

    PhotoType(int value, String text) {
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
