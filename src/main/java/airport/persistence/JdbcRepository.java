package airport.persistence;

import airport.model.Baggage;
import airport.model.Flight;
import airport.model.Passenger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class JdbcRepository {

    public void savePassenger(Passenger passenger) {
        String sql = "INSERT OR REPLACE INTO passengers(id, name) VALUES(?, ?)";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, passenger.getId());
            statement.setString(2, passenger.getName());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Could not save passenger", e);
        }
    }

    public void saveFlight(Flight flight) {
        String sql = "INSERT OR REPLACE INTO flights "
                + "(flight_number, destination, baggage_capacity, loaded_bags) "
                + "VALUES(?, ?, ?, ?)";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, flight.getFlightNumber());
            statement.setString(2, flight.getDestination());
            statement.setInt(3, flight.getBaggageCapacity());
            statement.setInt(4, flight.getLoadedBags());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Could not save flight", e);
        }
    }

    public void saveBaggage(Baggage bag) {
        String sql = "INSERT OR REPLACE INTO baggage "
                + "(bag_id, passenger_id, flight_number, type, weight, status) "
                + "VALUES(?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, bag.getBagId());
            statement.setString(2, bag.getPassenger().getId());
            statement.setString(3, bag.getFlight().getFlightNumber());
            statement.setString(4, bag.getClass().getSimpleName());
            statement.setDouble(5, bag.getWeight());
            statement.setString(6, bag.getStatus().name());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Could not save baggage", e);
        }
    }

    public void updateBaggageStatus(Baggage bag) {
        String sql = "UPDATE baggage SET status = ? WHERE bag_id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, bag.getStatus().name());
            statement.setString(2, bag.getBagId());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Could not update baggage status", e);
        }
    }

    public void saveHistory(String bagId, String stage, String result, String message) {
        String sql = "INSERT INTO processing_history "
                + "(bag_id, stage, result, message, timestamp) "
                + "VALUES(?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, bagId);
            statement.setString(2, stage);
            statement.setString(3, result);
            statement.setString(4, message);
            statement.setString(5, LocalDateTime.now().toString());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Could not save processing history", e);
        }
    }

    public void clearHistory(String bagId) {
        String sql = "DELETE FROM processing_history WHERE bag_id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, bagId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Could not clear history", e);
        }
    }

    public void printHistory(String bagId) {
        String sql = "SELECT stage, result, message, timestamp "
                + "FROM processing_history "
                + "WHERE bag_id = ? ORDER BY id";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, bagId);

            try (ResultSet result = statement.executeQuery()) {
                System.out.println("\n--- HISTORY FOR " + bagId + " ---");

                while (result.next()) {
                    System.out.printf(
                            "%s | %s | %s | %s%n",
                            result.getString("timestamp"),
                            result.getString("stage"),
                            result.getString("result"),
                            result.getString("message")
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Could not read baggage history", e);
        }
    }
}
