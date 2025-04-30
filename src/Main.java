import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        final int maximumVendors = 10;
        final int maximumCustomer = 10;
        final int maximumReaders = 10;
        final int maximumWriters = 5;

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the Ticket Pool size: ");

        int poolSize=3;
        try {
            poolSize = Integer.parseInt(scanner.nextLine().trim());
            if (poolSize <= 3) {
                // min pool size should de grater than 3
                System.out.println("Value should be greater than 3! Defaulting to 3.");
                poolSize =3;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input\n");
        }

        TicketPoolLock ticketPool = new TicketPoolLock(poolSize);

        List<Vendor> vendors = new ArrayList<>();
        List<Customer> Customers = new ArrayList<>();
        List<Reader> readers = new ArrayList<>();
        List<Writer> writers = new ArrayList<>();

        int vendorCounter = 1;
        int CustomerCounter = 1;
        int readerCounter = 1;
        int writerCounter = 1;

        boolean IsRunning = true;

        while (IsRunning) {
            System.out.println("\n\n1 - Add Producer");
            System.out.println("2 - Add Customer");
            System.out.println("3 - Add Reader");
            System.out.println("4 - Add Writer");
            System.out.println("5 - Remove Producer");
            System.out.println("6 - Remove Customer");
            System.out.println("7 - Remove Reader");
            System.out.println("8 - Remove Writer");
            System.out.println("9 - Show Ticket Pool Status");
            System.out.println("0 - Exit");
            System.out.print("Enter command number below : \n\n");

            int command = -1;
            try {
                command = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Input error: Only numbers between 0 and 9 are accepted.");
                continue;
            }

            switch (command) {
                case 1:
                    if (vendors.size() < maximumVendors) {
                        Vendor newVendor = new Vendor(ticketPool, "Vendor-" + vendorCounter);
                        vendors.add(newVendor);
                        new Thread(newVendor, "Vendor: " + vendorCounter).start();
                        vendorCounter++;
                        System.out.println("Vendor successfully added.");
                    } else {
                        System.out.println("Operation denied. Maximum number of Vendors (" + maximumVendors + ") already added.\n");
                    }
                    break;
                case 2:
                    if (Customers.size() < maximumCustomer) {
                        Customer newCustomer = new Customer(ticketPool);
                        Customers.add(newCustomer);
                        new Thread(newCustomer, "Customer: " + CustomerCounter).start();
                        CustomerCounter++;
                        System.out.println("Customer successfully added.");
                    }
                    else {
                        System.out.println("Operation denied. Maximum number of Customers (" + maximumCustomer + ") already added.\n");

                    }
                    break;
                case 3:
                    if (readers.size() < maximumReaders) {
                        Reader newReader = new Reader(ticketPool);
                        readers.add(newReader);
                        new Thread(newReader, "Reader: " + readerCounter).start();
                        readerCounter++;
                        System.out.println("Reader successfully added.");
                    }else {
                        System.out.println("Operation denied. Maximum number of Readers (" + maximumReaders + ") already added.\n");
                    }
                    break;
                case 4:
                    if (writers.size() < maximumWriters) {
                        Writer newWriter = new Writer(ticketPool, "Writer-" + writerCounter);
                        writers.add(newWriter);
                        new Thread(newWriter, "Writer: " + writerCounter).start();
                        writerCounter++;
                        System.out.println("Writer successfully added.");
                    } else {
                        System.out.println("Operation denied. Maximum number of Readers (" + maximumReaders + ") already added.\n");
                    }
                    break;
                case 5:
                    if (!vendors.isEmpty()) {
                        System.out.println(vendors.size() + " Vendor stopped.");
                        vendors.remove(vendors.size() - 1).stop();
                    } else {
                        System.out.println("There are no Vendors to remove.");
                    }
                    break;
                case 6:
                    if (!Customers.isEmpty()) {
                        System.out.println(Customers.size() + " Customer stopped.");
                        Customers.remove(Customers.size() - 1).stop();
                    } else {
                        System.out.println("There are no Customers to remove.");
                    }
                    break;
                case 7:
                    if (!readers.isEmpty()) {
                        System.out.println(readers.size() + " Reader stopped.");
                        readers.remove(readers.size() - 1).stop();
                    } else {
                        System.out.println("There are no Readers to remove.");
                    }
                    break;
                case 8:
                    if (!writers.isEmpty()) {
                        System.out.println(writers.size() + " Writer stopped.");
                        writers.remove(writers.size() - 1).stop();
                    } else {
                        System.out.println("There are no Writers to remove.");
                    }
                    break;
                case 9:
                    ticketPool.printTicketPoolStatus();
                    break;
                case 0:
                    for (Vendor v : vendors) v.stop();
                    for (Customer c : Customers) c.stop();
                    for (Reader r : readers) r.stop();
                    for (Writer w : writers) w.stop();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Goodbye! Exiting now...");
                    IsRunning = false;
                    break;
                default:
                    System.out.println("Invalid command");
            }

            if (vendors.isEmpty() && Customers.isEmpty() && readers.isEmpty() && writers.isEmpty()) {
                System.out.println("\nNo active threads left. Wrapping things up...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Goodbye! Exiting now...");
                IsRunning = false;
            }
        }
    }
}

