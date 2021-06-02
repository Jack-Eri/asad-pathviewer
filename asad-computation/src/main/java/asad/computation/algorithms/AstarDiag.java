package asad.computation.algorithms;

import asad.computation.models.Cell;
import asad.computation.models.Position;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

@Service
public class AstarDiag implements PathFinding {
    private static class CellInfo {
        int gScore = Integer.MAX_VALUE;
        int fScore = Integer.MAX_VALUE;
        Position from;
    }

    public Position[] GetPath(Cell[][] field, Position start, Position end) {
        if (field.length == 0 || field[0].length == 0) return new Position[0];

        int height = field[0].length;
        int width = field.length;

        // Setup field scores
        CellInfo[][] fieldInfo = new CellInfo[width][height];

        for (int y = 0; y < height; y += 1) {
            for (int x = 0; x < width; x += 1) {
                fieldInfo[x][y] = new CellInfo();
            }
        }

        getCellInfo(fieldInfo, start).gScore = 0;
        getCellInfo(fieldInfo, start).fScore = estimateScore(start, end);

        // Setup queue. Items are sorted by their current f-score;
        PriorityQueue<Position> openSet = new PriorityQueue<>((positionA, positionB) -> {
            CellInfo cellInfoA = getCellInfo(fieldInfo, positionA);
            CellInfo cellInfoB = getCellInfo(fieldInfo, positionB);

            return Integer.compare(cellInfoA.fScore, cellInfoB.fScore);
        });

        openSet.offer(start);

        while (!openSet.isEmpty()) {
            Position position = openSet.poll();
            Cell cell = getCell(field, position);
            CellInfo cellInfo = getCellInfo(fieldInfo, position);

            // Path found
            if (position.equals(end)) {
                return computePath(field, fieldInfo, position);
            }

            // loop through neighbours
            for (Position neighbourPosition : getNeighbours(position, width, height)) {
                Cell neighbourCell = getCell(field, neighbourPosition);
                CellInfo neighbourCellInfo = getCellInfo(fieldInfo, neighbourPosition);

                int gScore = cellInfo.gScore + neighbourCell.getCost();

                if (gScore < neighbourCellInfo.gScore) {
                    neighbourCellInfo.from = position;
                    neighbourCellInfo.gScore = gScore;
                    neighbourCellInfo.fScore = gScore + estimateScore(neighbourPosition, end);

                    if (!openSet.contains(neighbourPosition)) {
                        openSet.offer(neighbourPosition);
                    }
                }
            }
        }

        return new Position[0];
    }

    private Position[] computePath(Cell[][] field, CellInfo[][] fieldInfo, Position end) {
        Position position = end;
        CellInfo cellInfo = getCellInfo(fieldInfo, position);

        List<Position> path = new LinkedList<>();
        path.add(0, position);

        while (cellInfo.from != null) {
            position = cellInfo.from;
            cellInfo = getCellInfo(fieldInfo, position);

            path.add(0, position);
        }

        return path.toArray(new Position[0]);
    }

    private Cell getCell(Cell[][] field, Position position) {
        return field[position.getX()][position.getY()];
    }

    private CellInfo getCellInfo(CellInfo[][] fieldInfo, Position position) {
        return fieldInfo[position.getX()][position.getY()];
    }

    private Position[] getNeighbours(Position position, int width, int height) {
        List<Position> neighbours = new LinkedList<>();

        // Top
        Position current = new Position(position.getX() - 1, position.getY());
        if (checkPosition(current, width, height)) neighbours.add(current);

        // Bottom
        current = new Position(position.getX() + 1, position.getY());
        if (checkPosition(current, width, height)) neighbours.add(current);

        // Left
        current = new Position(position.getX(), position.getY() - 1);
        if (checkPosition(current, width, height)) neighbours.add(current);

        // Right
        current = new Position(position.getX(), position.getY() + 1);
        if (checkPosition(current, width, height)) neighbours.add(current);

        // Top Left
        current = new Position(position.getX() - 1, position.getY() - 1);
        if (checkPosition(current, width, height)) neighbours.add(current);

        // Top Right
        current = new Position(position.getX() - 1, position.getY() + 1);
        if (checkPosition(current, width, height)) neighbours.add(current);

        // Bottom Left
        current = new Position(position.getX() + 1, position.getY() - 1);
        if (checkPosition(current, width, height)) neighbours.add(current);

        // Bottom Right
        current = new Position(position.getX() + 1, position.getY() + 1);
        if (checkPosition(current, width, height)) neighbours.add(current);

        return neighbours.toArray(new Position[0]);
    }

    private boolean checkPosition(Position position, int width, int height) {
        return position.getX() >= 0 && position.getX() < width
                && position.getY() >= 0 && position.getY() < height;
    }

    private int estimateScore(Position current, Position end) {
        return Math.abs(end.getX() - current.getX()) + Math.abs(end.getY() - current.getY());
    }
}
