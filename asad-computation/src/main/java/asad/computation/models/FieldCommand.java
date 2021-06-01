package asad.computation.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FieldCommand extends Command<Field> {
    @JsonCreator
    public FieldCommand(List<Position> positions, Field data) {
        super(positions, data);
    }
}
