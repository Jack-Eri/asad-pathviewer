package asad.computation.endpoints;


import asad.computation.models.Command;
import asad.computation.models.Field;
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
            @RequestBody Command<Field> command
    ) {
        Field field = computeService.computePaths(command.getData(), command.getPositions());
        if (field != null) {
            return ResponseEntity.ok(field);
        }

        return ResponseEntity.notFound().build();
    }
}
