package com.strongBeton.strongBeton.enums;

public enum status {
    Pending (1, "Pending"),
    Blocked(2, "Blocked"),
    Accepted(3, "Accepted"),
    Response(4, "Response");

    private int value =0;
    private final String Text;

    private status(int value, String Text){
        this.value = value;
        this.Text = Text;
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
