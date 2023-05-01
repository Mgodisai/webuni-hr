package hu.webuni.hr.alagi.controller.rest;

import hu.webuni.hr.alagi.model.Position;
import hu.webuni.hr.alagi.repository.PositionRepository;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class PositionMapper {

    @Autowired
    protected PositionRepository positionRepository;

    Position stringToPosition(String positionName) {
        List<Position> positions = positionRepository.getPositionsByName(positionName);
        if (positions.isEmpty()) {
            return new Position(positionName, null);
        }
        return positions.get(0);
    }

    String positionToString(Position position) {
        return position.getName();
    }
}
