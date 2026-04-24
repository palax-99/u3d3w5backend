package antoninopalazzolo.u3d3w5backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "bookings")
@NoArgsConstructor
@ToString
@Getter
@Setter
public class Booking {

    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    public Booking(User user, Event event) {
        this.user = user;
        this.event = event;
    }
}
