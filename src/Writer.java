public class Writer implements Runnable {

    private TicketPool ticketPool;
    private String writerName;
    private boolean IsRuning = true;

    public Writer(TicketPool ticketPool, String writerName) {
        this.ticketPool = ticketPool;
        this.writerName = writerName;
    }

    public void stop() {
        IsRuning = false;
    }

    @Override
    public void run() {
        int i = 1;
        while (IsRuning) {
            Ticket ticket = new Ticket("Special : " + i++, writerName, "Special Event");
            ticketPool.addTicket(ticket);
            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getName() + " Stopping!");
    }
}
