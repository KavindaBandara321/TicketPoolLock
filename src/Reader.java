public class Reader implements Runnable {

    private TicketPool ticketPool;
    private boolean IsRuning = true;

    public Reader(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
    }

    public void stop() {
        IsRuning = false;
    }

    @Override
    public void run() {
        while (IsRuning) {
            ticketPool.printTicketPoolStatus();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getName() + " Stopping!");
    }
}
