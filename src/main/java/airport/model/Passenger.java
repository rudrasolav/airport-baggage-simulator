package airport.model;
import jakarta.persistence.*;
@Entity @Table(name="passengers")
public class Passenger {
 @Id private String id; @Column(nullable=false) private String name;
 protected Passenger(){} public Passenger(String id,String name){this.id=id;this.name=name;}
 public String getId(){return id;} public String getName(){return name;}
 public String toString(){return id+" - "+name;}
}
