package asad.computation.endpoints;


import asad.computation.models.Command;
import asad.computation.models.Field;
import asad.computation.models.FieldCommand;
import asad.computation.services.ComputeService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/compute", produces = "application/json", consumes = "application/json")
public class ComputeEndpoints {

    private final ComputeService computeService;

    @PutMapping()
    public ResponseEntity<?> updateStart(
            @RequestBody FieldCommand command
    ) {
        System.out.println("Updating field");
        Field field = computeService.computePaths(command.getData(), command.getPositions());
        if (field != null) {
            return ResponseEntity.ok(field);
        }

        return ResponseEntity.notFound().build();
    }
}
