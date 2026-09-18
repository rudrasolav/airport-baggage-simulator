package airport.simulation;

import airport.enums.BaggageStatus;
import airport.model.Baggage;
import airport.model.Flight;
import airport.persistence.JdbcRepository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SimulationEngine {

    private final List<Flight> flights;
    private final List<Baggage> bags;
    private final JdbcRepository repo;

    private static final Baggage STOP = new StopBaggage();

    public SimulationEngine(List<Flight> flights,
                            List<Baggage> bags,
                            JdbcRepository repo) {
        this.flights = flights;
        this.bags = bags;
        this.repo = repo;
    }

    public void run() {
        System.out.println("\n========== CONCURRENT SIMULATION ==========\n");

        resetFlights();

        BlockingQueue<Baggage> securityQueue = new LinkedBlockingQueue<>();
        BlockingQueue<Baggage> sortingQueue = new LinkedBlockingQueue<>();
        BlockingQueue<Baggage> loadingQueue = new LinkedBlockingQueue<>();

        prepareBags(securityQueue);

        List<Thread> securityThreads = startWorkers(
                2,
                "SECURITY-",
                () -> securityWorker(securityQueue, sortingQueue)
        );

        List<Thread> sortingThreads = startWorkers(
                2,
                "SORTING-",
                () -> sortingWorker(sortingQueue, loadingQueue)
        );

        List<Thread> loadingThreads = startWorkers(
                2,
                "LOADING-",
                () -> loadingWorker(loadingQueue)
        );

        addStops(securityQueue, securityThreads.size());
        waitForThreads(securityThreads);

        addStops(sortingQueue, sortingThreads.size());
        waitForThreads(sortingThreads);

        addStops(loadingQueue, loadingThreads.size());
        waitForThreads(loadingThreads);

        writeReport();
        printReport();
    }

    private void resetFlights() {
        for (Flight flight : flights) {
            flight.resetLoadedBags();
            repo.saveFlight(flight);
        }
    }

    private void prepareBags(BlockingQueue<Baggage> securityQueue) {
        for (Baggage bag : bags) {
            repo.clearHistory(bag.getBagId());

            bag.setStatus(BaggageStatus.WAITING_FOR_SECURITY);
            repo.saveBaggage(bag);

            repo.saveHistory(
                    bag.getBagId(),
                    "CHECK_IN",
                    "PASSED",
                    "Bag entered airport processing"
            );

            securityQueue.offer(bag);
        }
    }

    private List<Thread> startWorkers(int count,
                                      String namePrefix,
                                      Runnable work) {
        List<Thread> threads = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            Thread thread = new Thread(work, namePrefix + i);
            threads.add(thread);
            thread.start();
        }

        return threads;
    }

    private void addStops(BlockingQueue<Baggage> queue, int count) {
        for (int i = 0; i < count; i++) {
            queue.offer(STOP);
        }
    }

    private void securityWorker(BlockingQueue<Baggage> input,
                                BlockingQueue<Baggage> output) {
        try {
            while (true) {
                Baggage bag = input.take();

                if (bag == STOP) {
                    return;
                }

                bag.setStatus(BaggageStatus.IN_SECURITY);
                log(
                        bag,
                        "SECURITY",
                        "STARTED",
                        Thread.currentThread().getName() + " screening"
                );

                Thread.sleep(250);

                if (bag.getWeight() > 35) {
                    bag.setStatus(BaggageStatus.SECURITY_REJECTED);
                    log(
                            bag,
                            "SECURITY",
                            "REJECTED",
                            "Weight exceeds simulated 35 kg limit"
                    );
                    continue;
                }

                bag.setStatus(BaggageStatus.SECURITY_CLEARED);
                log(bag, "SECURITY", "PASSED", "Security cleared");

                output.put(bag);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    private void sortingWorker(BlockingQueue<Baggage> input,
                               BlockingQueue<Baggage> output) {
        try {
            while (true) {
                Baggage bag = input.take();

                if (bag == STOP) {
                    return;
                }

                Thread.sleep(180);

                bag.setStatus(BaggageStatus.SORTED);
                log(
                        bag,
                        "SORTING",
                        "PASSED",
                        Thread.currentThread().getName()
                                + " routed to "
                                + bag.getFlight().getFlightNumber()
                );

                bag.setStatus(BaggageStatus.WAITING_FOR_LOADING);
                output.put(bag);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void loadingWorker(BlockingQueue<Baggage> input) {
        try {
            while (true) {
                Baggage bag = input.take();

                if (bag == STOP) {
                    return;
                }

                log(
                        bag,
                        "LOADING",
                        "STARTED",
                        Thread.currentThread().getName() + " loading bag"
                );

                Thread.sleep(250);

                Flight flight = bag.getFlight();

                synchronized (flight) {
                    if (flight.loadBag()) {
                        bag.setStatus(BaggageStatus.LOADED);
                        log(
                                bag,
                                "LOADING",
                                "PASSED",
                                "Bag loaded onto " + flight.getFlightNumber()
                        );
                        repo.saveFlight(flight);
                    } else {
                        bag.setStatus(BaggageStatus.DELAYED);
                        log(
                                bag,
                                "LOADING",
                                "FAILED",
                                "Flight baggage capacity reached"
                        );
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void waitForThreads(List<Thread> threads) {
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    private void log(Baggage bag,
                     String stage,
                     String result,
                     String message) {
        System.out.printf(
                "[%s] %s | %s | %s%n",
                Thread.currentThread().getName(),
                bag.getBagId(),
                result,
                message
        );

        repo.updateBaggageStatus(bag);
        repo.saveHistory(bag.getBagId(), stage, result, message);
    }

    private void writeReport() {
        File folder = new File("logs");
        folder.mkdirs();

        try (PrintWriter writer = new PrintWriter(
                new FileWriter("logs/simulation-report.txt"))) {

            writer.println("AIRPORT BAGGAGE SIMULATION REPORT");
            writer.println("Generated: " + LocalDateTime.now());
            writer.println();

            for (Baggage bag : bags) {
                writer.println(
                        bag.getBagId()
                                + " | "
                                + bag.getFlight().getFlightNumber()
                                + " | "
                                + bag.getStatus()
                );
            }

        } catch (IOException e) {
            System.out.println("Could not write report: " + e.getMessage());
        }
    }

    private void printReport() {
        int loaded = 0;
        int rejected = 0;
        int delayed = 0;

        for (Baggage bag : bags) {
            if (bag.getStatus() == BaggageStatus.LOADED) {
                loaded++;
            } else if (bag.getStatus() == BaggageStatus.SECURITY_REJECTED) {
                rejected++;
            } else if (bag.getStatus() == BaggageStatus.DELAYED) {
                delayed++;
            }
        }

        System.out.println("\n========== SIMULATION REPORT ==========");
        System.out.println("Total baggage       : " + bags.size());
        System.out.println("Loaded successfully : " + loaded);
        System.out.println("Security rejected   : " + rejected);
        System.out.println("Delayed             : " + delayed);

        for (Flight flight : flights) {
            System.out.println(flight);
        }

        System.out.println("\nLog: logs/simulation-report.txt");
        System.out.println("=======================================");
    }

    private static class StopBaggage extends Baggage {

        StopBaggage() {
            super("__STOP__", null, null, 0);
        }

        @Override
        public String getHandlingInstruction() {
            return "Internal stop signal";
        }
    }
}
