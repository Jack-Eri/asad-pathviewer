package asad.computation.endpoints;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
@RequestMapping(path = "/health")
public class HealthEndpoint {

    private final Status status;

    public HealthEndpoint() {
        String hostname = "unknown";
        String address = "unknown";

        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            hostname = inetAddress.getHostName();
            address = inetAddress.getHostAddress();

        } catch (UnknownHostException e) {}

        status = new Status("UP", hostname, address);
    }

    @Data
    @AllArgsConstructor
    private static class Status {
        private String status;
        private String hostname;
        private String address;
    }

    @GetMapping()
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok(status);
    }
}
