package asad.data.endpoints;

import asad.data.models.Position;
import asad.data.models.Command;
import asad.data.models.Field;
import asad.data.services.FieldService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/fields", produces = "application/json", consumes = "application/json")
public class FieldEndpoints {

    private final FieldService fieldService;

    public FieldEndpoints(FieldService fieldService) {
        this.fieldService = fieldService;
    }

    @PostMapping()
    public ResponseEntity<?> createField(
            @RequestParam String identifier, @RequestParam int width, @RequestParam int height
    ) {
        if (identifier == null || identifier.isEmpty() || width <= 0 || height <= 0) {
            return ResponseEntity.badRequest().build();
        }

        Field field = fieldService.createField(identifier, width, height);
        if (field == null) return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(field);
    }

    @GetMapping("/{identifier}")
    public ResponseEntity<?> getField(
            @PathVariable String identifier
    ) {
        Field field = fieldService.getField(identifier);
        if (field != null) {
            return ResponseEntity.ok(field);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{identifier}")
    public ResponseEntity<?> updateField(
            @PathVariable String identifier, @RequestBody Field field
    ) {
        Field updatedField = fieldService.updateField(identifier, field);
        if (updatedField != null) {
            return ResponseEntity.ok(updatedField);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{identifier}")
    public ResponseEntity<?> deleteField(
            @PathVariable String identifier
    ) {
        fieldService.removeField(identifier);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{identifier}/costs")
    public ResponseEntity<?> updateCost(
            @PathVariable String identifier,
            @RequestBody Command<Integer> command
    ) {
        int cost = command.getData() != null ? command.getData() : 1;
        Field field = fieldService.updateCost(identifier, command.getPositions(), cost);
        if (field != null) {
            return ResponseEntity.ok(field);
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{identifier}/starts")
    public ResponseEntity<?> updateStart(
            @PathVariable String identifier,
            @RequestBody Command<Position> command
    ) {
        Field field = fieldService.updateStart(identifier, command.getPositions(), command.getData());
        if (field != null) {
            return ResponseEntity.ok(field);
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{identifier}/ends")
    public ResponseEntity<?> updateEnd(
            @PathVariable String identifier,
            @RequestBody Command<Position> command
    ) {
        Field field = fieldService.updateEnd(identifier, command.getPositions(), command.getData());
        if (field != null) {
            return ResponseEntity.ok(field);
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{identifier}/cellToField")
    public ResponseEntity<?> changeCellToField(
            @PathVariable String identifier,
            @RequestBody Command<Field> command
    ) {
        Field field = fieldService.changeCellToField(identifier, command.getPositions(), command.getData());
        if (field != null) {
            return ResponseEntity.ok(field);
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{identifier}/fieldToCell")
    public ResponseEntity<?> changeFieldToCell(
            @PathVariable String identifier,
            @RequestBody Command<Integer> command
    ) {
        int cost = command.getData() != null ? command.getData() : 1;
        Field field = fieldService.changeFieldToCell(identifier, command.getPositions(), cost);
        if (field != null) {
            return ResponseEntity.ok(field);
        }

        return ResponseEntity.notFound().build();
    }
}