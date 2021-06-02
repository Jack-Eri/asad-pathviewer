package asad.data.services;

import asad.data.models.Command;
import asad.data.models.Field;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Service
public class ComputationService {
    private static final Duration TIMEOUT = Duration.ofMinutes(1);

    private final String BASE_PATH;
    private final HttpClient client;
    private final ObjectMapper objectMapper;

    public ComputationService(@Value("${asad.computation.url}") String url) {
        BASE_PATH = url;

        client = HttpClient.newHttpClient();
        objectMapper = new ObjectMapper();
    }

    public Field computePaths(Command<Field> command, String algorithm) {
        try {
            String body = objectMapper.writeValueAsString(command);
            HttpRequest request = put(
                    String.format("/compute?algorithm=%s", algorithm),
                    HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8)
            );

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

    private HttpRequest.Builder request(String path) {
        return HttpRequest.newBuilder()
                .timeout(TIMEOUT)
                .uri(URI.create(BASE_PATH + path))
                .header("Content-Type", "application/json; charset=utf-8")
                .timeout(TIMEOUT);
    }
}
