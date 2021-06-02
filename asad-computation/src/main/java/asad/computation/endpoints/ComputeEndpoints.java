package asad.computation.endpoints;


import asad.computation.models.Field;
import asad.computation.models.FieldCommand;
import asad.computation.services.ComputeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/compute", produces = "application/json", consumes = "application/json")
public class ComputeEndpoints {

    private final ComputeService computeService;

    @PutMapping()
    public ResponseEntity<?> updateStart(
            @RequestBody FieldCommand command,
            @RequestParam(defaultValue = "ASTAR") String algorithm
    ) {
        System.out.println(algorithm);

        Field field = computeService.computePaths(command.getData(), command.getPositions(), algorithm);
        if (field != null) {
            return ResponseEntity.ok(field);
        }

        return ResponseEntity.notFound().build();
    }
}
