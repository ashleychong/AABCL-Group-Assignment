package gui;

public class TicketGUI {
    public String ticket_time;
    public String ticket_idSold;

    public TicketGUI(String ticket_time, String ticket_idSold) {
        this.ticket_time = ticket_time;
        this.ticket_idSold = ticket_idSold;
    }

    public String getTicket_time() {
        return ticket_time;
    }

    public String getTicket_idSold() {
        return ticket_idSold;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof TicketGUI && ((TicketGUI) obj).ticket_idSold.equals(ticket_idSold);
    }
}
