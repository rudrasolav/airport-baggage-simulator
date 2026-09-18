package airport.model;
import airport.enums.BaggageStatus; import jakarta.persistence.*;
@Entity @Table(name="baggage") @Inheritance(strategy=InheritanceType.SINGLE_TABLE) @DiscriminatorColumn(name="baggage_type")
public abstract class Baggage {
 @Id private String bagId; @ManyToOne(optional=false) private Passenger passenger; @ManyToOne(optional=false) private Flight flight; private double weight; @Enumerated(EnumType.STRING) private BaggageStatus status;
 protected Baggage(){} protected Baggage(String id,Passenger p,Flight f,double w){bagId=id;passenger=p;flight=f;weight=w;status=BaggageStatus.CHECKED_IN;}
 public String getBagId(){return bagId;} public Passenger getPassenger(){return passenger;} public Flight getFlight(){return flight;} public double getWeight(){return weight;}
 public synchronized BaggageStatus getStatus(){return status;} public synchronized void setStatus(BaggageStatus s){status=s;}
 public abstract String getHandlingInstruction();
 public String toString(){return bagId+" | "+passenger.getName()+" | Flight: "+flight.getFlightNumber()+" | "+weight+" kg | "+status;}
}
