package com.strongBeton.strongBeton.enums;

public enum FriendStatus {
    PENDING(1, "Pending"),
    NOTHING(2, "Nothing"),
    ACCEPTED(3, "Accepted"),
    RESPONSE(4, "Response");

    private int value =0;
    private final String Text;

    private FriendStatus(int value, String Text){
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
