package airport.simulation;
import airport.exception.*; import airport.model.*; import airport.persistence.JdbcRepository; import java.util.*;
public class Airport { private final Map<String,Passenger> passengers=new LinkedHashMap<>(); private final Map<String,Flight> flights=new LinkedHashMap<>(); private final Map<String,Baggage> bags=new LinkedHashMap<>(); private final JdbcRepository repo;
 public Airport(JdbcRepository r){repo=r;} public void addPassenger(Passenger p){passengers.put(p.getId(),p);repo.savePassenger(p);} public void addFlight(Flight f){flights.put(f.getFlightNumber(),f);repo.saveFlight(f);}
 public void addBaggage(Baggage b)throws AirportException{if(bags.containsKey(b.getBagId()))throw new DuplicateBaggageException("Baggage ID already exists: "+b.getBagId());if(!flights.containsKey(b.getFlight().getFlightNumber()))throw new FlightNotFoundException("Flight does not exist: "+b.getFlight().getFlightNumber());bags.put(b.getBagId(),b);}
 public Collection<Flight> getFlights(){return flights.values();} public Collection<Baggage> getBaggage(){return bags.values();} public Flight getFlight(String n){return flights.get(n);}
 public void showFlights(){System.out.println("\n========== FLIGHTS ==========");if(flights.isEmpty())System.out.println("No flights.");else flights.values().forEach(System.out::println);}
 public void showBaggage(){System.out.println("\n========== BAGGAGE ==========");if(bags.isEmpty())System.out.println("No baggage.");else bags.values().forEach(b->System.out.println(b+" | "+b.getHandlingInstruction()));}
}
