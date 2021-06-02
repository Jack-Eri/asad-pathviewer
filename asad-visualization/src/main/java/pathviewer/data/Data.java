package pathviewer.data;

import lombok.Getter;
import lombok.Setter;
import pathviewer.models.Cell;
import pathviewer.models.Field;
import pathviewer.models.Position;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Data {
    private final DataService dataService;

    @Getter @Setter
    private boolean loading;

    @Getter
    private String identifier = "jack";

    @Getter  @Setter
    private String algorithm = "ASTAR";

    @Getter
    private String error;

    private Field field;

    @Getter
    private Field currentField;

    private final List<Position> currentPosition = new ArrayList<>();

    public Data() {
        dataService = new DataService();
    }

    public boolean hasField() {
        return field != null;
    }

    public void fetchField(String identifier) {
        String oldIdentifier = this.identifier;
        if (setIdentifier(identifier)) {
            this.field = dataService.fetchField(identifier);
            if (field == null) {
                error = String.format("Failed loading field %s!", identifier);
                this.identifier = oldIdentifier;
                return;
            }
            currentPosition.clear();
            refreshCurrentField();
        }
    }

    public void createField(String identifier, int width, int height) {
        String oldIdentifier = this.identifier;
        if (setIdentifier(identifier)) {
            this.field = dataService.createField(identifier, width, height, algorithm);
            if (field == null) {
                error = String.format("Failed creating field %s!", identifier);
                this.identifier = oldIdentifier;
                return;
            }
            currentPosition.clear();
            refreshCurrentField();
        }
    }

    public void deleteField() {
        if (identifier != null && dataService.deleteField(identifier)) {
            field = null;
            currentField = null;
            identifier = null;
            currentPosition.clear();
        }
    }

    private boolean refreshField(Field field) {
        if (field == null) return false;

        this.field = field;
        refreshCurrentField();

        return true;
    }

    private void refreshCurrentField() {
        this.currentField = field;
        for (Position position : currentPosition) {
            this.currentField = currentField.getCell(position).getField();
        }
    }

    private boolean setIdentifier(String identifier) {
        if (identifier == null || identifier.isBlank() || identifier.contains(" ")) {
            error = "invalid identifier";
            return false;
        }

        this.identifier = identifier;
        error = null;
        return true;
    }

    public void zoomIn(Position position) {
        Cell cell = currentField.getCell(position);

        if (cell.isField()) {
            currentPosition.add(position);
            currentField = cell.getField();
        }
    }

    public void zoomOut() {
        if (currentPosition.size() > 0) {
            currentPosition.remove(currentPosition.size() - 1);
            refreshCurrentField();
        }
    }

    public void setCost(Position position, int cost) {
        List<Position> positions = new LinkedList<>(currentPosition);
        positions.add(position);

        if (refreshField(dataService.updateCost(identifier, positions, cost, algorithm))) {
            error = null;
        } else {
            error = "Failed updating cost!";
        }
    }

    public void setStart(Position position) {
        if (refreshField(dataService.updateStart(identifier, currentPosition, position, algorithm))) {
            error = null;
        } else {
            error = "Failed updating start!";
        }
    }

    public void setEnd(Position position) {
        if (refreshField(dataService.updateEnd(identifier, currentPosition, position, algorithm))) {
            error = null;
        } else {
            error = "Failed updating end!";
        }
    }

    public void AddField(Position position, int width, int height) {
        List<Position> positions = new LinkedList<>(currentPosition);
        positions.add(position);

        if (refreshField(dataService.changeCellToField(identifier, positions, width, height, algorithm))) {
            error = null;
        } else {
            error = "Failed changing to field!";
        }
    }

    public void setCell(Position position) {
        List<Position> positions = new LinkedList<>(currentPosition);
        positions.add(position);

        if (refreshField(dataService.changeFieldToCell(identifier, positions, algorithm))) {
            error = null;
        } else {
            error = "Failed cahnging to cell!";
        }
    }
}
