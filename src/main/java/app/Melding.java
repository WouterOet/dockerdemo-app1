package app;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * Test entity
 */
@Entity
public class Melding {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Version
    private Long version;

    private String result;

    private String hostprocessed;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    public Melding() {
    }

    public Melding(nl.marcenschede.dozerdemo.melding.v1_0.Melding melding) {
        this.result = melding.getResult().value();
        this.timestamp = new Date(System.currentTimeMillis());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getHostprocessed() {
        return hostprocessed;
    }

    public void setHostprocessed(final String hostprocessed) {
        this.hostprocessed = hostprocessed;
    }

    @Override
    public String toString() {
        return "Melding{" +
                "id=" + id +
                ", result='" + result + '\'' +
                ", host='" + hostprocessed + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
