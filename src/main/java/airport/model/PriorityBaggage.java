package airport.model;
import jakarta.persistence.*;
@Entity @DiscriminatorValue("PRIORITY")
public class PriorityBaggage extends Baggage {
 protected PriorityBaggage(){} public PriorityBaggage(String id,Passenger p,Flight f,double w){super(id,p,f,w);}
 public String getHandlingInstruction(){return "Priority handling";}
}
