package com.strongBeton.strongBeton.enums;

public enum ClanRoleType {
    LEADER(0, "leader"),
    OFFICER(1, "officer"),
    STRENGTHSPECIALIST(2, "strength_specialist"),
    CONSISTENCYSPECIALIST(3, "consistency_specialist"),
    MEMBER(4, "member"),
    PENDING(5, "pending");

    private int value =0 ;
    private String Text;

    ClanRoleType(int value, String text) {
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
