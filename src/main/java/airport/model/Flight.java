package airport.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "flights")
public class Flight {

    @Id
    private String flightNumber;

    @Column(nullable = false)
    private String destination;

    private int baggageCapacity;
    private int loadedBags;

    protected Flight() {
    }

    public Flight(String flightNumber, String destination, int baggageCapacity) {
        this.flightNumber = flightNumber;
        this.destination = destination;
        this.baggageCapacity = baggageCapacity;
        this.loadedBags = 0;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getDestination() {
        return destination;
    }

    public int getBaggageCapacity() {
        return baggageCapacity;
    }

    public int getLoadedBags() {
        return loadedBags;
    }

    public boolean canLoadBag() {
        return loadedBags < baggageCapacity;
    }

    public boolean loadBag() {
        if (!canLoadBag()) {
            return false;
        }

        loadedBags++;
        return true;
    }

    public void resetLoadedBags() {
        loadedBags = 0;
    }

    @Override
    public String toString() {
        return flightNumber + " -> " + destination
                + " | Bags: " + loadedBags + "/" + baggageCapacity;
    }
}
