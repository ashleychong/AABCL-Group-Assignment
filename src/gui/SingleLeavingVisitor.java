package gui;

import controller.MuseumSceneController;

public class SingleLeavingVisitor {
    public static String ticketID;
    public String exit;
    public String turnstileNum;

    public SingleLeavingVisitor(String ticketID, String exit, String turnstileNum) {
        this.exit = exit;
        this.turnstileNum = turnstileNum;

        MuseumSceneController.currentLeavingVisitor = ticketID;
        MuseumSceneController.currentLeavingVisitor_exit = exit;
        MuseumSceneController.currentLeavingVisitor_turnstilenum = turnstileNum;
    }

    public static String getTicketID() {
        return ticketID;
    }

    public String getExit() {
        return exit;
    }

    public String getTurnstileNum() {
        return turnstileNum;
    }
}
