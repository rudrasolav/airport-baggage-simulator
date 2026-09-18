package airport.persistence;
import java.sql.*; import java.io.File;
public final class DatabaseManager { private static final String URL="jdbc:sqlite:database/airport.db"; private DatabaseManager(){}
 public static Connection getConnection() throws SQLException {new File("database").mkdirs(); return DriverManager.getConnection(URL);}
 public static void initialize(){String[] sql={"CREATE TABLE IF NOT EXISTS passengers(id TEXT PRIMARY KEY,name TEXT NOT NULL)","CREATE TABLE IF NOT EXISTS flights(flight_number TEXT PRIMARY KEY,destination TEXT NOT NULL,baggage_capacity INTEGER NOT NULL,loaded_bags INTEGER NOT NULL DEFAULT 0)","CREATE TABLE IF NOT EXISTS baggage(bag_id TEXT PRIMARY KEY,passenger_id TEXT NOT NULL,flight_number TEXT NOT NULL,type TEXT NOT NULL,weight REAL NOT NULL,status TEXT NOT NULL)","CREATE TABLE IF NOT EXISTS processing_history(id INTEGER PRIMARY KEY AUTOINCREMENT,bag_id TEXT NOT NULL,stage TEXT NOT NULL,result TEXT NOT NULL,message TEXT,timestamp TEXT NOT NULL)"};
 try(Connection c=getConnection();Statement s=c.createStatement()){for(String q:sql)s.execute(q);}catch(SQLException e){throw new RuntimeException("Database initialization failed: "+e.getMessage(),e);}}
}
