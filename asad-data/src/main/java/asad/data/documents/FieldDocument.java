package asad.data.documents;

import asad.data.models.Field;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "fields")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FieldDocument {

    @Indexed(unique = true)
    @JsonProperty
    @Getter @Setter
    private String identifier;

    @JsonProperty
    @Getter @Setter
    private Field field;

    public FieldDocument(String identifier, Field field) {
        this.identifier = identifier;
        this.field = field;
    }
}
