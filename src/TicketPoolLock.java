import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.*;

public class TicketPoolLock implements TicketPool {

    private Queue<Ticket> ticketQueue = new LinkedList<Ticket>();
    private int maxSize;
    private int noOfTicketsOffered = 0;
    private int noOfTicketsBought = 0;

    private ReadWriteLock lock = new ReentrantReadWriteLock(true);
    private Lock readLock = lock.readLock();
    private Lock writeLock = lock.writeLock();

    private Condition queueFull = writeLock.newCondition();
    private Condition queueEmpty = writeLock.newCondition();

    public TicketPoolLock(int maxSize) {

        this.maxSize = maxSize;
    }

    @Override
    public void addTicket(Ticket ticket) {
        try {
            writeLock.lock();
            while (ticketQueue.size() == this.maxSize) {
                try {
                    queueFull.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            ticketQueue.offer(ticket);
            noOfTicketsOffered++;
            System.out.println(Thread.currentThread().getName() + " added TicketNumber: " + ticket.getTicketId() + ", Vendor: " + ticket.getVendor() + ", Event: " + ticket.getEvent());
            queueEmpty.signalAll();
        } finally {
            writeLock.unlock();
        }

    }

    @Override
    public Ticket purchaseTicket() {

        try {
            writeLock.lock();
            while (ticketQueue.isEmpty()) {
                try {
                    queueEmpty.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Ticket ticket = ticketQueue.poll();
            noOfTicketsBought++;
            System.out.println(Thread.currentThread().getName() + " purchased TicketNumber: " + ticket.getTicketId() + ", Vendor: " + ticket.getVendor() + ", Event: " + ticket.getEvent());
            queueFull.signalAll();
            return ticket;
        } finally {
            writeLock.unlock();
        }

    }

    @Override
    public void printTicketPoolStatus() {

        try {
            readLock.lock();
            System.out.println("No of tickets added to queue by Vendor: " + noOfTicketsOffered);
            System.out.println("No of tickets bought from queue by Customer: " + noOfTicketsBought);
            System.out.println("No of tickets currently available in the queue: " + ticketQueue.size());
        } finally {
            readLock.unlock();
        }
    }
}

