package pathviewer.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Command<T> {
    @JsonProperty
    @Getter
    private List<Position> positions;

    @JsonProperty
    @Getter
    private T data;
}
