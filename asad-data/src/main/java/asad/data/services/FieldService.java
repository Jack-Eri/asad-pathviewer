package asad.data.services;

import asad.data.documents.FieldDocument;
import asad.data.models.Cell;
import asad.data.models.Command;
import asad.data.models.Field;
import asad.data.models.Position;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.FindAndReplaceOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
@AllArgsConstructor
public class FieldService {

    private final MongoTemplate mongoTemplate;
    private final ComputationService computationService;

    public Field getField(String identifier) {
        List<FieldDocument> documents = mongoTemplate.find(
                Query.query(Criteria.where("identifier").is(identifier)), FieldDocument.class);

        return documents.isEmpty() ? null : documents.get(0).getField();
    }

    public Field createField(String identifier, int width, int height) {
        Field field =  new Field(width, height);

        field = computationService.computePaths(new Command<>(new LinkedList<>(), field));
        if (field == null) return null;

        FieldDocument document = new FieldDocument(identifier, field);
        document = mongoTemplate.save(document);

        return document.getField();
    }

    public Field updateField(String identifier, Field field) {
        FieldDocument document = new FieldDocument(identifier, field);

        document = mongoTemplate.findAndReplace(
                Query.query(Criteria.where("identifier").is(identifier)),
                document,
                FindAndReplaceOptions.options().returnNew()
        );

        return document != null ? field : null;
    }

    public void removeField(String identifier) {
        mongoTemplate.remove(Query.query(Criteria.where("identifier").is(identifier)), FieldDocument.class);
    }

    public Field updateCost(String identifier, List<Position> positions, int cost) {
        if (positions.isEmpty()) return null;

        // Retrieve field from db
        Field root = getField(identifier);
        if (root == null) return null;

        // Find field containing the cell
        Field field = getFieldAt(positions, root, true);
        if (field == null) return null;

        // Get cell
        Position position = positions.get(positions.size() - 1);
        if (!field.isPositionInField(position)) return null;

        Cell cell = field.getCell(position);
        if (cell.isField()) return null;

        // Modify cost
        cell.setCost(Math.max(1, Math.min(cost, 99)));

        // Update paths
        root = updatePaths(root, positions, true);
        if (root == null) return null;

        // Update field in db
        return updateField(identifier, root);
    }

    public Field updateStart(String identifier, List<Position> positions, Position start) {
        // Retrieve field from db
        Field root = getField(identifier);
        if (root == null) return null;

        // Find field to update
        Field field = getFieldAt(positions, root, false);
        if (field == null) return null;

        // Update start
        field.setStart(start);

        // Update paths
        root = updatePaths(root, positions, false);
        if (root == null) return null;

        return updateField(identifier, root);
    }

    public Field updateEnd(String identifier, List<Position> positions, Position end) {
        // Retrieve field from db
        Field root = getField(identifier);
        if (root == null) return null;

        // Find field to update
        Field field = getFieldAt(positions, root, false);
        if (field == null) return null;

        // Update start
        field.setEnd(end);

        // Update paths
        root = updatePaths(root, positions, false);
        if (root == null) return null;

        return updateField(identifier, root);
    }

    public Field changeCellToField(String identifier, List<Position> positions, Field newField) {
        if (positions.isEmpty()) return null;

        // Retrieve field from db
        Field root = getField(identifier);
        if (root == null) return null;

        // Find field containing the cell
        Field field = getFieldAt(positions, root, true);
        if (field == null) return null;

        // Get cell
        Position position = positions.get(positions.size() - 1);
        if (!field.isPositionInField(position)) return null;

        Cell cell = field.getCell(position);
        if (cell.isField()) return null;

        // Change cell to field
        cell.setCost(-1);
        cell.setField(newField);

        // Update paths
        root = updatePaths(root, positions, false);
        if (root == null) return null;

        // Update field in db
        return updateField(identifier, root);
    }

    public Field changeFieldToCell(String identifier, List<Position> positions, int cost) {
        if (positions.isEmpty()) return null;

        // Retrieve field from db
        Field root = getField(identifier);
        if (root == null) return null;

        // Find field containing the cell
        Field field = getFieldAt(positions, root, true);
        if (field == null) return null;

        // Get cell
        Position position = positions.get(positions.size() - 1);
        if (!field.isPositionInField(position)) return null;

        Cell cell = field.getCell(position);
        if (!cell.isField()) return null;

        // Change cell to field
        cell.setField(null);
        cell.setCost(cost);

        // Update paths
        root = updatePaths(root, positions, true);
        if (root == null) return null;

        // Update field in db
        return updateField(identifier, root);
    }

    private Field updatePaths(Field field, List<Position> positions, boolean ignoreLast) {
        List<Position> fieldPosition = new LinkedList<>(positions);
        if (ignoreLast) {
            fieldPosition.remove(fieldPosition.size() - 1);
        }

        return computationService.computePaths(new Command<>(fieldPosition, field));
    }

    private Field getFieldAt(List<Position> positions, Field field, boolean ignoreLast) {
        for (int i = 0; i < positions.size() - (ignoreLast ? 1 : 0); i += 1) {
            Position position = positions.get(i);

            if (!field.isPositionInField(position)) return null;
            Cell cell = field.getCell(position);

            if (!cell.isField()) return null;
            field = cell.getField();
        }

        return field;
    }
}
