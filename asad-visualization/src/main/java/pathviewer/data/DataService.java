package pathviewer.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import pathviewer.models.Field;
import pathviewer.models.Position;
import pathviewer.models.Command;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

public class DataService {

    private static final String BASE_PATH = "http://localhost:8080/data";
    private static final Duration TIMEOUT = Duration.ofSeconds(2);

    private final HttpClient client;
    private final ObjectMapper objectMapper;

    public DataService() {
        client = HttpClient.newHttpClient();
        objectMapper = new ObjectMapper();
    }

    public Field fetchField(String identifier) {
        HttpRequest request = get(String.format("/fields/%s", identifier));

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), Field.class);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Field createField(String identifier, int width, int height, String algorithm) {
        String str = "body";
        HttpRequest request = get(
                String.format("/fields?identifier=%s&width=%d&height=%d&algorithm=%s", identifier, width, height, algorithm)
        );

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), Field.class);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean deleteField(String identifier) {
        HttpRequest request = delete(String.format("/fields/%s", identifier));

        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
            return true;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Field updateCost(String identifier, List<Position> positions, int cost, String algorithm) {
        return update(String.format("/fields/%s/costs?algorithm=%s", identifier, algorithm), new Command<>(positions, cost));
    }

    public Field updateStart(String identifier, List<Position> positions, Position start, String algorithm) {
        return update(String.format("/fields/%s/starts?algorithm=%s", identifier, algorithm), new Command<>(positions, start));
    }

    public Field updateEnd(String identifier, List<Position> positions, Position end, String algorithm) {
        return update(String.format("/fields/%s/ends?algorithm=%s", identifier, algorithm), new Command<>(positions, end));
    }

    public Field changeCellToField(String identifier, List<Position> positions, int width, int height, String algorithm) {
        return update(
                String.format("/fields/%s/cellToField?algorithm=%s", identifier, algorithm),
                new Command<>(positions, new Field(width, height))
        );
    }

    public Field changeFieldToCell(String identifier, List<Position> positions, String algorithm) {
        return update(
                String.format("/fields/%s/fieldToCell?algorithm=%s", identifier, algorithm),
                new Command<>(positions, null)
        );
    }

    private Field update(String path, Command<?> command) {
        try {
            String body = objectMapper.writeValueAsString(command);
            HttpRequest request = put(path, HttpRequest.BodyPublishers.ofString(body));

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), Field.class);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    private HttpRequest get(String path) {
        return request(path)
                .GET()
                .build();
    }

    private HttpRequest post(String path, HttpRequest.BodyPublisher bodyPublisher) {
        return request(path)
                .POST(bodyPublisher)
                .build();
    }

    private HttpRequest put(String path, HttpRequest.BodyPublisher bodyPublisher) {
        return request(path)
                .PUT(bodyPublisher)
                .build();
    }

    private HttpRequest delete(String path) {
        return request(path)
                .DELETE()
                .build();
    }

    private HttpRequest.Builder request(String path) {
        return HttpRequest.newBuilder()
                .timeout(TIMEOUT)
                .uri(URI.create(BASE_PATH + path))
                .header("Content-Type", "application/json");
    }
}
