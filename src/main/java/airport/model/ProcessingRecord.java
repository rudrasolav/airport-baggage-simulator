package airport.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "processing_history")
public class ProcessingRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bagId;
    private String stage;
    private String result;
    private String message;
    private LocalDateTime timestamp;

    protected ProcessingRecord() {
    }

    public ProcessingRecord(String bagId, String stage,
                            String result, String message) {

        this.bagId = bagId;
        this.stage = stage;
        this.result = result;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getBagId() {
        return bagId;
    }

    public String getStage() {
        return stage;
    }

    public String getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}