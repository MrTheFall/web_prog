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
            try {
		        String method = FCGIInterface.request.params.getProperty("REQUEST_METHOD");
		        if (method == null) {
		            System.out.println(errorResult("Unsupported HTTP method: null"));
		            continue;
		        }

		        if (method.equalsIgnoreCase("GET")) {
		            String queryString = FCGIInterface.request.params.getProperty("QUERY_STRING");
		            if (queryString != null) {
		                continue;
		            } else {
		                System.out.println(errorResult("No parameters provided"));
		            }
		            continue;
		        }
		        if (method.equals("POST")) {
					var contentType = FCGIInterface.request.params.getProperty("CONTENT_TYPE");
					if (contentType == null) {
						System.out.println(errorResult("Content-Type is null"));
						continue;
					}

					if (!contentType.equals("application/json")) {
						System.out.println(errorResult("Content-Type is not supported"));
						continue;
					}

					var requestBody = readRequestBody();
					JsonObject requestBodyJson = parseJson(requestBody);
					if (requestBodyJson == null) {
						System.out.println(errorResult("Request body could not be parsed as JSON"));
						continue;
					}
					
					if (!requestBodyJson.containsKey("method")) {
						System.out.println(errorResult("No method specified"));
						continue;
					}

					if (requestBodyJson.getString("method").equals("check-hit")) {
						if (!requestBodyJson.containsKey("x") || !requestBodyJson.containsKey("y") || !requestBodyJson.containsKey("R")) {
							System.out.println(errorResult("Missing x, y or R value"));
							continue;
						}

						double x = requestBodyJson.getJsonNumber("x").doubleValue();
						double y = requestBodyJson.getJsonNumber("y").doubleValue();
						double r = requestBodyJson.getJsonNumber("R").doubleValue();

						String xStr = requestBodyJson.getJsonNumber("x").toString();
						String yStr = requestBodyJson.getJsonNumber("y").toString();
						String rStr = requestBodyJson.getJsonNumber("R").toString();

						if (!xStr.equals(String.valueOf(x).replaceAll("\\.0$", ""))) {
							System.out.println(errorResult("Precision error in 'x' value" + String.valueOf(x) + xStr));
							continue;
						}
	
						if (!yStr.equals(String.valueOf(y).replaceAll("\\.0$", ""))) {
							System.out.println(errorResult("Precision error in 'y' value"));
							continue;
						}
	
						if (!rStr.equals(String.valueOf(r).replaceAll("\\.0$", ""))) {
							System.out.println(errorResult("Precision error in 'r' value"));
							continue;
						}

						List<Double> validXValues = Arrays.asList(-2.0, -1.5, -1.0, -0.5, 0.0, 0.5, 1.0, 1.5, 2.0);
						List<Double> validRValues = Arrays.asList(1.0, 1.5, 2.0, 2.5, 3.0);

						if (!validXValues.contains(x) || y < -3.0 || y > 5.0 || !validRValues.contains(r)) {
							System.out.println(errorResult("Invalid x, y or R value"));
							continue;
						}

						System.out.println(areaCheckPage(x, y, r));
						continue;
					}
					
					System.out.println(errorResult("Method not implemented"));
					continue;
				}

		        System.out.println(errorResult("Unsupported HTTP method: " + method));
		    }
		    catch (Exception e) {
		        System.out.println(errorResult("Error: " + e.toString()));
		    }
        }
    }

	private static JsonObject parseJson(String json) {
		try (JsonReader reader = Json.createReader(new StringReader(json))) {
		    return reader.readObject();
		} catch (Exception e) {
		    return null;
		}
	}

    private static Map<String, String> parseQueryString(String queryString) {
        return Arrays.stream(queryString.split("&"))
                .map(keyValue -> keyValue.split("="))
                .collect(Collectors.toMap(
                        pair -> pair[0],
                        pair -> pair.length > 1 ? pair[1] : ""));
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
                Content-Type: text/html
                Content-Length: %d
                
                %s
                """.formatted(error.getBytes(StandardCharsets.UTF_8).length, error);
    }

    private static String getHelloPage() {
        String content = """
                <html>
                    <head>
                        <title>Java FastCGI Hello World</title>
                    </head>
                    <body>
                        <h1>From Java FastCGI:</h1>
                        <p>Hello, World!</p>
                    </body>
                </html>
                """;
        return """
                HTTP/1.1 200 OK
                Content-Type: text/html
                Content-Length: %d
                
                %s
                """.formatted(content.getBytes(StandardCharsets.UTF_8).length, content);
    }

	private static String areaCheckPage(double x, double y, double r) {
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
