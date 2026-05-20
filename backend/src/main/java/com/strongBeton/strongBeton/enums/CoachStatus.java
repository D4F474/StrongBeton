package com.strongBeton.strongBeton.enums;

public enum CoachStatus {

    PENDING(1, "Pending"),
    ACTIVE(0, "Active"),
    CANCELLED(3, "Canceled");

    private int value =0 ;
    private String Text;

    CoachStatus(int value, String text) {
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
        return "CoachStatus{" +
                "Text='" + Text + '\'' +
                '}';
    }
}
