package gui;

import controller.MuseumSceneController;

import java.util.Calendar;
import java.util.Date;

import static code.Clock.timeFormat;

public class SingleVisitor {
    public String ticketID;
    public int stayDuration;
    public String startTime;
    public String expireTime;
    public String entrance;
    public String exit;
    public String turnstileNum;

    public SingleVisitor(String ticketID, int stayDuration, String startTime, String expireTime, String entrance, String exit, String turnstileNum) {
        this.ticketID = ticketID;
        this.stayDuration = stayDuration;
        this.startTime = startTime;
        this.expireTime = expireTime;
        this.entrance = entrance;
        this.exit = exit;
        this.turnstileNum = turnstileNum;

        MuseumSceneController.currentEnteringVisitor = ticketID;
        MuseumSceneController.currentEnteringVisitor_stayDuration = String.valueOf(stayDuration);
        MuseumSceneController.currentEnteringVisitor_entrance = entrance;
        MuseumSceneController.currentEnteringVisitor_turnstilenum = turnstileNum;
    }

    public String getTicketID() {
        return ticketID;
    }

    public int getStayDuration() {
        return stayDuration;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public String getEntrance() {
        return entrance;
    }

    public String getExit() {
        return exit;
    }

    public String getTurnstileNum() {
        return turnstileNum;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SingleVisitor && ((SingleVisitor) obj).ticketID.equals(ticketID);
    }
}
