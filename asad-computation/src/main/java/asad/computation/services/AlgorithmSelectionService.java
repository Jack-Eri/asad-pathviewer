package asad.computation.services;

import asad.computation.algorithms.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AlgorithmSelectionService {

    private final Astar astar;
    private final AstarDiag astarDiag;

    public PathFinding getAlgorithm(String algorithm) {
        switch (algorithm) {
            case "ASTAR":
                return astar;
            case "ASTAR_DIAG":
                return astarDiag;
        }

        return astar;
    }
}
