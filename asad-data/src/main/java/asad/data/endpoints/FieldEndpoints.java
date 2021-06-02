package asad.data.endpoints;

import asad.data.models.Position;
import asad.data.models.Command;
import asad.data.models.Field;
import asad.data.services.FieldService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(produces = "application/json", consumes = "application/json")
public class FieldEndpoints {

    private final FieldService fieldService;

    public FieldEndpoints(FieldService fieldService) {
        this.fieldService = fieldService;
    }

    @GetMapping("/fields")
    public ResponseEntity<?> createField(
            @RequestParam String identifier,
            @RequestParam int width,
            @RequestParam int height,
            @RequestParam(defaultValue = "ASTAR") String algorithm
    ) {
        if (identifier == null || identifier.isEmpty() || width <= 0 || height <= 0) {
            return ResponseEntity.badRequest().build();
        }

        Field field = fieldService.createField(identifier, width, height, algorithm);
        if (field == null) return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(field);
    }

    @GetMapping("/fields/{identifier}")
    public ResponseEntity<?> getField(
            @PathVariable String identifier
    ) {
        Field field = fieldService.getField(identifier);
        if (field != null) {
            return ResponseEntity.ok(field);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/fields/{identifier}")
    public ResponseEntity<?> deleteField(
            @PathVariable String identifier
    ) {
        fieldService.removeField(identifier);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/fields/{identifier}/costs")
    public ResponseEntity<?> updateCost(
            @PathVariable String identifier,
            @RequestBody Command<Integer> command,
            @RequestParam(defaultValue = "ASTAR") String algorithm
    ) {
        int cost = command.getData() != null ? command.getData() : 1;
        Field field = fieldService.updateCost(identifier, command.getPositions(), cost, algorithm);
        if (field != null) {
            return ResponseEntity.ok(field);
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/fields/{identifier}/starts")
    public ResponseEntity<?> updateStart(
            @PathVariable String identifier,
            @RequestBody Command<Position> command,
            @RequestParam(defaultValue = "ASTAR") String algorithm
    ) {
        Field field = fieldService.updateStart(identifier, command.getPositions(), command.getData(), algorithm);
        if (field != null) {
            return ResponseEntity.ok(field);
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/fields/{identifier}/ends")
    public ResponseEntity<?> updateEnd(
            @PathVariable String identifier,
            @RequestBody Command<Position> command,
            @RequestParam(defaultValue = "ASTAR") String algorithm
    ) {
        Field field = fieldService.updateEnd(identifier, command.getPositions(), command.getData(), algorithm);
        if (field != null) {
            return ResponseEntity.ok(field);
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/fields/{identifier}/cellToField")
    public ResponseEntity<?> changeCellToField(
            @PathVariable String identifier,
            @RequestBody Command<Field> command,
            @RequestParam(defaultValue = "ASTAR") String algorithm
    ) {
        Field field = fieldService.changeCellToField(identifier, command.getPositions(), command.getData(), algorithm);
        if (field != null) {
            return ResponseEntity.ok(field);
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/fields/{identifier}/fieldToCell")
    public ResponseEntity<?> changeFieldToCell(
            @PathVariable String identifier,
            @RequestBody Command<Integer> command,
            @RequestParam(defaultValue = "ASTAR") String algorithm
    ) {
        int cost = command.getData() != null ? command.getData() : 1;
        Field field = fieldService.changeFieldToCell(identifier, command.getPositions(), cost, algorithm);
        if (field != null) {
            return ResponseEntity.ok(field);
        }

        return ResponseEntity.notFound().build();
    }
}
