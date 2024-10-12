import com.fastcgi.FCGIInterface;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class Server {
    public static void main(String[] args) throws IOException {
        FCGIInterface fcgiInterface = new FCGIInterface();
        while (fcgiInterface.FCGIaccept() >= 0) {
            processRequest();
        }
    }

    private static void processRequest() throws IOException {
        String method = FCGIInterface.request.params.getProperty("REQUEST_METHOD");

        if (method == null) {
            System.out.println(errorResult("Unsupported HTTP method: null"));
            return;
        }

        switch (method.toUpperCase()) {
            case "GET":
                handleGet();
                break;
            case "POST":
                handlePost();
                break;
            default:
                System.out.println(errorResult("Unsupported HTTP method: " + method));
        }
    }

    private static void handleGet() {
        String queryString = FCGIInterface.request.params.getProperty("QUERY_STRING");
        if (queryString != null) {
            // Currently no handlers for GET requests
        } else {
            System.out.println(errorResult("No parameters provided"));
        }
    }

    private static void handlePost() throws IOException {
        var contentType = FCGIInterface.request.params.getProperty("CONTENT_TYPE");
        if (contentType == null) {
            System.out.println(errorResult("Content-Type is null"));
            return;
        }

        if (!contentType.equals("application/json")) {
            System.out.println(errorResult("Content-Type is not supported"));
            return;
        }

        var requestBody = readRequestBody();
        JsonObject requestBodyJson = parseJson(requestBody);
        if (requestBodyJson == null || !requestBodyJson.containsKey("method")) {
            System.out.println(errorResult("Request body could not be parsed as JSON or no method specified"));
            return;
        }

        String method = requestBodyJson.getString("method");

        switch (method) {
            case "check-hit":
                handleCheckHit(requestBodyJson);
                break;
            default:
                System.out.println(errorResult("Method not implemented"));
                break;
        }

    }

    private static void handleCheckHit(JsonObject requestBodyJson) {
        if (!requestBodyJson.containsKey("x") || !requestBodyJson.containsKey("y") || !requestBodyJson.containsKey("R")) {
            System.out.println(errorResult("Missing x, y or R value"));
            return;
        }

        double x = requestBodyJson.getJsonNumber("x").doubleValue();
        double y = requestBodyJson.getJsonNumber("y").doubleValue();
        double r = requestBodyJson.getJsonNumber("R").doubleValue();

        String error = validatePrecision(requestBodyJson, x, y, r);
        if (error != null) {
            System.out.println(error);
            return;
        }

        if (!isValidValues(x, y, r)) {
            String errorMessage = "{\"error\":\"Invalid x, y or R value\"}";
            System.out.println(errorResultJson(errorMessage));
            return;
        }

        System.out.println(areaCheck(x, y, r));
    }

    private static String validatePrecision(JsonObject requestBodyJson, double x, double y, double r) {
        String xStr = requestBodyJson.getJsonNumber("x").toString();
        String yStr = requestBodyJson.getJsonNumber("y").toString();
        String rStr = requestBodyJson.getJsonNumber("R").toString();

        if (!xStr.equals(String.valueOf(x).replaceAll("\\.0$", ""))) {
            return errorResult("Precision error in 'x' value: " + xStr);
        }
        if (!yStr.equals(String.valueOf(y).replaceAll("\\.0$", ""))) {
            return errorResult("Precision error in 'y' value");
        }
        if (!rStr.equals(String.valueOf(r).replaceAll("\\.0$", ""))) {
            return errorResult("Precision error in 'r' value");
        }
        return null;
    }

    private static boolean isValidValues(double x, double y, double r) {
        List<Double> validXValues = Arrays.asList(-2.0, -1.5, -1.0, -0.5, 0.0, 0.5, 1.0, 1.5, 2.0);
        List<Double> validRValues = Arrays.asList(1.0, 1.5, 2.0, 2.5, 3.0);
        return validXValues.contains(x) && y >= -3.0 && y <= 5.0 && validRValues.contains(r);
    }

    private static JsonObject parseJson(String json) {
        try (JsonReader reader = Json.createReader(new StringReader(json))) {
            return reader.readObject();
        } catch (Exception e) {
            return null;
        }
    }

    private static String readRequestBody() throws IOException {
        FCGIInterface.request.inStream.fill();
        var contentLength = FCGIInterface.request.inStream.available();
        ByteBuffer buffer = ByteBuffer.allocate(contentLength);
        var readBytes = FCGIInterface.request.inStream.read(buffer.array(), 0, contentLength);

        var requestBodyRaw = new byte[readBytes];
        buffer.get(requestBodyRaw);
        buffer.clear();

        return new String(requestBodyRaw, StandardCharsets.UTF_8);
    }

    private static String errorResult(String error) {
        return """
                HTTP/1.1 400 Bad Request
                Status: 400
                Content-Type: text/html
                Content-Length: %d
                
                %s
                """.formatted(error.getBytes(StandardCharsets.UTF_8).length, error);
    }

    private static String errorResultJson(String error) {
        return """
                HTTP/1.1 400 Bad Request
                Status: 400
                Content-Type: application/json
                Content-Length: %d
                
                %s
                """.formatted(error.getBytes(StandardCharsets.UTF_8).length, error);
    }

    private static String areaCheck(double x, double y, double r) {
        long startTime = System.currentTimeMillis();
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        boolean isHit = isInArea(x, y, r);
        long executionTime = System.currentTimeMillis() - startTime;
        String content = "{\"hit\": " + isHit + ", \"currentDateTime\": \"" + now + "\", \"executionTime\": " + executionTime + "}";
        return """
                HTTP/1.1 200 OK
                Content-Type: application/json
                Content-Length: %d

                %s
                """.formatted(content.getBytes(StandardCharsets.UTF_8).length, content);
    }

    private static boolean isInArea(double x, double y, double r) {
        // Rectangle
        if (x >= -r && x <= 0 && y <= r / 2 && y >= 0) {
            return true;
        }

        // Triangle
        if (x >= 0 && y >= x / 2 - r / 2 && y <= 0) {
            return true;
        }

        // Quarter circle
        if (x >= 0 && y >= 0 && (x * x + y * y <= r * r)) {
            return true;
        }

        return false;
    }
}
