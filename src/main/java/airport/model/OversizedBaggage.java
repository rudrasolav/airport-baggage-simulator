package airport.model;
import jakarta.persistence.*;
@Entity @DiscriminatorValue("OVERSIZED")
public class OversizedBaggage extends Baggage {
 protected OversizedBaggage(){} public OversizedBaggage(String id,Passenger p,Flight f,double w){super(id,p,f,w);}
 public String getHandlingInstruction(){return "Oversized baggage route";}
}
