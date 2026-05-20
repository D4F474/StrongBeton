package com.strongBeton.strongBeton.enums;

public enum NotificationType {
//ENUM('CLAN_INVITE', 'CLAN_INVITE_ACCEPTED', 'CLAN_INVITE_DECLINED', 'CLAN_KICKED',
// 'CLAN_PROMOTED', 'CLAN_DEMOTED', 'CLAN_LEADERSHIP_TRANSFERRED', 'FRIEND_REQUEST', 'FRIEND_ACCEPTED')
    CLAN_INVITE(1, "Clan Invited"),
    CLAN_INVITE_ACCEPT(2, "Accepted To Clan"),
    CLAN_INVITE_DECLINED(3, "Declined From Clan"),
    CLAN_KICKED(4, "Kicked From Clan"),
    CLAN_PROMOTED(5, "Promoted In Clan"),
    CLAN_DEMOTED(8, "Demoted In Clan"),
    CLAN_LEADERSHIP_TRANSFER(6, "Leadership Transfered"),
    FRIEND_REQUEST(7, "Friend Request"),
    FRIEND_ACCEPTED(4, "Friend Accepted");

    private int value =0;
    private final String Text;

    private NotificationType(int value, String Text){
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
