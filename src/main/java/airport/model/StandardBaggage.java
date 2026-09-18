package airport.model;
import jakarta.persistence.*;
@Entity @DiscriminatorValue("STANDARD")
public class StandardBaggage extends Baggage {
 protected StandardBaggage(){} public StandardBaggage(String id,Passenger p,Flight f,double w){super(id,p,f,w);}
 public String getHandlingInstruction(){return "Normal handling";}
}
