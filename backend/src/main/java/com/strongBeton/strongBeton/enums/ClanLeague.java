package com.strongBeton.strongBeton.enums;

public enum ClanLeague {

    UNRANKED(0, "unranked"),
    BRONZE(1, "bronze"),
    SILVER(2, "silver"),
    GOLD(3, "gold"),
    DIAMOND(4, "diamond");

    private int value =0 ;
    private String Text;

    ClanLeague(int value, String text) {
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
