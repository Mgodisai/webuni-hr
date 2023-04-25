package hu.webuni.hr.alagi.repository;

import hu.webuni.hr.alagi.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {
    List<Position> getPositionByName(String name);
}
