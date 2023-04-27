package hu.webuni.hr.alagi.controller.rest;

import hu.webuni.hr.alagi.model.Position;
import hu.webuni.hr.alagi.repository.PositionRepository;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class PositionMapper {
    protected PositionRepository positionRepository;

    Position stringToPosition(String positionName) {
        List<Position> positions = positionRepository.getPositionByName(positionName);
        if (positions.isEmpty()) {
            return new Position(positionName, null);
        }
        return positions.get(0);
    }

    String positionToString(Position position) {
        return position.getName();
    }
}
