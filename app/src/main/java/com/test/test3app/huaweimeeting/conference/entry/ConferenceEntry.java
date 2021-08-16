package com.test.test3app.huaweimeeting.conference.entry;

/**
 * created by zhaoyuntao
 * on 2020-03-16
 * description:
 */
public class ConferenceEntry extends ConferenceBaseItem {
    private String organizer;
    private String team;
    private long timeStart;
    private int id;
    private int state = TYPE_ITEM_CLOSED;

    public ConferenceEntry(int state) {
        setState(state);
    }

    public int getType() {
        return state;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String origonizer) {
        this.organizer = origonizer;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public long getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(long timeStart) {
        this.timeStart = timeStart;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setState(int state) {
        this.state = state;
    }
}
