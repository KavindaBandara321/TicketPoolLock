public interface TicketPool {
    void addTicket(Ticket ticket);
    Ticket purchaseTicket();
    void printTicketPoolStatus();
}
