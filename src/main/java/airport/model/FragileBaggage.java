package airport.model;
import jakarta.persistence.*;
@Entity @DiscriminatorValue("FRAGILE")
public class FragileBaggage extends Baggage {
 protected FragileBaggage(){} public FragileBaggage(String id,Passenger p,Flight f,double w){super(id,p,f,w);}
 public String getHandlingInstruction(){return "Handle with care";}
}
