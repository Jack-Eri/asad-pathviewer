package asad.data.models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cell {
    @JsonProperty
    @Getter
    private Position position;

    @JsonProperty
    private Field field = null;

    private int cost;

    public Cell(Position position) {
        this.position = position;
        this.cost = 1;
    }

    public Cell(Position position, Field field) {
        this.position = position;
        this.field = field;
        this.cost = -1;
    }

    public Cell(Position position, int cost) {
        this.position = position;
        this.cost = cost;
    }

    @JsonIgnore
    public boolean isField() {
        return field != null;
    }

    @JsonIgnore
    public Field getField() {
        return field;
    }

    @JsonIgnore
    public void setField(Field field) {
        this.field = field;
    }

    @JsonGetter
    public int getCost() {
        if (field == null) {
            return cost;
        }

        int pathCost = 0;

        for (Position position : field.getPath()) {
            pathCost += field.getCell(position).getCost();
        }

        return pathCost;
    }

    @JsonSetter
    public void setCost(int cost) {
        this.cost = Math.max(1, Math.min(cost, 99));
    }
}
