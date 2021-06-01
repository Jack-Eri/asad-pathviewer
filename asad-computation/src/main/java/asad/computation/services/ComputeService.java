package asad.computation.services;

import asad.computation.algorithms.Astar;
import asad.computation.algorithms.PathFinding;
import asad.computation.models.Cell;
import asad.computation.models.Field;
import asad.computation.models.Position;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Stack;

@Service
@AllArgsConstructor
public class ComputeService {

    private final Astar astar;

    public Field computePaths(Field field, List<Position> positions) {
        return computePaths(field, positions, astar);
    }

    private Field computePaths(Field root, List<Position> positions, PathFinding pathFinding) {
        Stack<Field> fieldStack =  fieldsToUpdate(root, positions);
        if (fieldStack == null) return null;

        while (!fieldStack.isEmpty()) {
            Field field = fieldStack.pop();

            field.setPath(pathFinding.GetPath(field.getCells(), field.getStart(), field.getEnd()));
        }

        return root;
    }

    private Stack<Field> fieldsToUpdate(Field root, List<Position> positions) {
        Stack<Field> stack = new Stack<>();
        stack.add(root);

        Field field = root;
        for (Position position : positions) {
            if (!field.isPositionInField(position)) return null;
            Cell cell = field.getCell(position);

            if (!cell.isField()) return null;
            field = cell.getField();

            stack.add(field);
        }

        return stack;
    }
}
