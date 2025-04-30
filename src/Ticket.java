public class Ticket {
    private String ticketId;
    private String vendor;
    private String event;

    public Ticket(String ticketId, String vendor, String event) {
        this.ticketId = ticketId;
        this.vendor = vendor;
        this.event = event;
    }

    public String getTicketId() {

        return ticketId;
    }

    public String getVendor() {

        return vendor;
    }

    public String getEvent() {
        return event;
    }
}
