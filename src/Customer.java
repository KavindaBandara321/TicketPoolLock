public class Customer implements Runnable {

    private TicketPool ticketPool;
    private boolean IsRuning = true;

    public Customer(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
    }

    public void stop() {
        IsRuning = false;
    }

    @Override
    public void run() {
        while (IsRuning) {
            ticketPool.purchaseTicket();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getName() + " Stopping!");
    }
}
