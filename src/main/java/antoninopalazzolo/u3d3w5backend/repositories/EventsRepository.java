package antoninopalazzolo.u3d3w5backend.repositories;

import antoninopalazzolo.u3d3w5backend.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventsRepository extends JpaRepository<Event, UUID> {
}
