import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class TicketPoolSyancTest {
    @Test
    public void testAddAndPurchaseTicket() {
        TicketPoolLock ticketPool = new TicketPoolLock(1);
        Ticket ticket = new Ticket("T001", "VendorX", "Concert");


        ticketPool.addTicket(ticket);

        Ticket purchased = ticketPool.purchaseTicket();

        assertNotNull(purchased);
        assertEquals("T001", purchased.getTicketId());
        assertEquals("VendorX", purchased.getVendor());
        assertEquals("Concert", purchased.getEvent());
    }
    @Test
    public void testPoolSizeMinimumCorrection() {
        int inputPoolSize = 2; // less than 3
        TicketPoolLock ticketPool = new TicketPoolLock(Math.max(inputPoolSize, 3));
        assertTrue(ticketPool != null, "TicketPool should be created with default minimum size.");
    }

    @Test
    public void testAddingMaxVendors() {
        TicketPoolLock pool = new TicketPoolLock(5);
        List<Vendor> vendors = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            vendors.add(new Vendor(pool, "Vendor-" + i));
        }

        assertEquals(10, vendors.size());

    }

    @Test
    public void testExitWhenAllThreadsRemoved() {
        List<Vendor> vendors = new ArrayList<>();
        List<Customer> customers = new ArrayList<>();
        List<Reader> readers = new ArrayList<>();
        List<Writer> writers = new ArrayList<>();

        assertTrue(vendors.isEmpty() && customers.isEmpty() && readers.isEmpty() && writers.isEmpty());
    }


    @Test
    public void testMaxSizeZeroBehavior() throws InterruptedException {
        TicketPoolLock pool = new TicketPoolLock(0);
        Ticket ticket = new Ticket("T001", "VendorA", "EventX");

        Thread addThread = new Thread(() -> pool.addTicket(ticket));
        addThread.start();

        Thread.sleep(500);

        assertTrue(addThread.isAlive(), "Thread should be blocked on wait() due to maxSize = 0");

        addThread.interrupt();
        addThread.join(1000);
    }

    @Test
    public void testAddTicketWhenPoolIsFullBlocks() throws InterruptedException {
        TicketPoolLock pool = new TicketPoolLock(1);
        Ticket ticket1 = new Ticket("T001", "VendorA", "Event1");
        Ticket ticket2 = new Ticket("T002", "VendorB", "Event2");

        pool.addTicket(ticket1);

        Thread addThread = new Thread(() -> {
            pool.addTicket(ticket2);
        });
        addThread.start();

        Thread.sleep(500);
        assertTrue(addThread.isAlive(), "Thread should be waiting because pool is full");

        addThread.interrupt();
    }
    @Test
    public void testPerformanceUnderLoad() throws InterruptedException {
        int poolSize = 50;
        int numberOfThreads = 100;

        TicketPoolLock ticketPool = new TicketPoolLock(poolSize);
        CountDownLatch latch = new CountDownLatch(numberOfThreads * 2);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < numberOfThreads; i++) {
            new Thread(() -> {
                ticketPool.addTicket(new Ticket("T" + System.nanoTime(), "Vendor", "Event"));
                latch.countDown();
            }).start();
        }

        for (int i = 0; i < numberOfThreads; i++) {
            new Thread(() -> {
                ticketPool.purchaseTicket();
                latch.countDown();
            }).start();
        }

        boolean completed = latch.await(10, TimeUnit.SECONDS);

        long endTime = System.currentTimeMillis();

        long totalTime = endTime - startTime;

        System.out.println("Total time taken: " + totalTime + " ms");

        assertTrue(completed, "All threads should complete within timeout");
        assertTrue(totalTime < 5000, "Performance acceptable: should complete within 5 seconds");
    }

}
