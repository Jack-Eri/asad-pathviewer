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

    private static final String BASE_PATH = "http://localhost/data";
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

    public Field createField(String identifier, int width, int height) {
        HttpRequest request = put(
                String.format("/fields?identifier=%s&width=%d&height=%d", identifier, width, height),
                HttpRequest.BodyPublishers.ofString("\"\"")
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

    public void updateField(String identifier, Field field) {
        try {
            String body = new ObjectMapper().writeValueAsString(field);

            HttpRequest request = post(
                    String.format("/fields/%s", identifier),
                    HttpRequest.BodyPublishers.ofString(body)
            );

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.printf("%s -> %s\n", response.statusCode(), response.body());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Field updateCost(String identifier, List<Position> positions, int cost) {
        return update(String.format("/fields/%s/costs", identifier), new Command<>(positions, cost));
    }

    public Field updateStart(String identifier, List<Position> positions, Position start) {
        return update(String.format("/fields/%s/starts", identifier), new Command<>(positions, start));
    }

    public Field updateEnd(String identifier, List<Position> positions, Position end) {
        return update(String.format("/fields/%s/ends", identifier), new Command<>(positions, end));
    }

    public Field changeCellToField(String identifier, List<Position> positions, int width, int height) {
        return update(
                String.format("/fields/%s/cellToField", identifier),
                new Command<>(positions, new Field(width, height))
        );
    }

    public Field changeFieldToCell(String identifier, List<Position> positions) {
        return update(
                String.format("/fields/%s/fieldToCell", identifier),
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
