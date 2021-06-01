package asad.computation.algorithms;

import asad.computation.models.Cell;
import asad.computation.models.Position;

public interface PathFinding {
    Position[] GetPath(Cell[][] field, Position start, Position end);
}
