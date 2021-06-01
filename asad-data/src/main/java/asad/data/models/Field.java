package asad.data.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Field {

    @JsonProperty
    @Getter
    private Cell[][] cells;

    @JsonProperty
    @Getter
    private Position[] path;

    @JsonProperty
    @Getter @Setter
    private Position start;

    @JsonProperty
    @Getter @Setter
    private Position end;

    @JsonProperty
    @Getter
    private int width;

    @JsonProperty
    @Getter
    private int height;

    public Field(int width, int height) {
        this.width = width;
        this.height = height;

        this.cells = new Cell[width][height];
        this.path = new Position[0];
        this.start = new Position(0, 0);
        this.end = new Position(width - 1, height - 1);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                cells[x][y] = new Cell(new Position(x, y));
            }
        }
    }

	@JsonIgnore
    public Cell getCell(Position position) {
        return cells[position.getX()][position.getY()];
    }

    @JsonIgnore
    public boolean isPositionInField(Position position) {
        return position.getX() >= 0 && position.getX() < width
                && position.getY() >= 0 && position.getY() < height;
    }
}
