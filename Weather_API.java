package weather;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.time.Duration;

public final class WeatherClient {

    private final HttpClient http;
    private final String apiKey;
    private final URI endpoint;

    public WeatherClient(String apiKey, String city) {
        this.apiKey = apiKey;
        this.http = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .version(HttpClient.Version.HTTP_2)
                .build();

        this.endpoint = URI.create(
                "https://api.openweathermap.org/data/2.5/weather?q="
                + sanitize(city)
                + "&units=metric&appid="
                + sanitize(apiKey)
        );
    }

    public WeatherResult getCurrentWeather() throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .GET()
                .uri(endpoint)
                .timeout(Duration.ofSeconds(5))
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> resp = http.send(req, HttpResponse.BodyHandlers.ofString());

        if (resp.statusCode() == 401) {
            throw new SecurityException("API key inválida o no autorizada");
        }

        if (resp.statusCode() >= 500) {
            throw new IOException("Error del servidor externo: " + resp.statusCode());
        }

        if (resp.statusCode() != 200) {
            throw new IOException("Respuesta inesperada: " + resp.statusCode());
        }

        return WeatherParser.parse(resp.body());
    }

    private static String sanitize(String input) {
        return input.replaceAll("[^a-zA-Z0-9_\\-]", "");
    }

    public static record WeatherResult(
            double temperature,
            int humidity,
            String description
    ) {}

    public static final class WeatherParser {
        public static WeatherResult parse(String json) {
            try {
                // Uso mínimo para evitar dependencias; asumir formato predecible
                String temp = extract(json, "\"temp\":", ",");
                String hum = extract(json, "\"humidity\":", "}");
                String desc = extract(json, "\"description\":\"", "\"");

                return new WeatherResult(
                        Double.parseDouble(temp),
                        Integer.parseInt(hum),
                        desc
                );
            } catch (Exception ex) {
                throw new IllegalArgumentException("JSON inválido", ex);
            }
        }

        private static String extract(String json, String start, String end) {
            int s = json.indexOf(start);
            if (s < 0) throw new IllegalArgumentException("Campo no encontrado");
            s += start.length();
            int e = json.indexOf(end, s);
            if (e < 0) throw new IllegalArgumentException("Final no encontrado");
            return json.substring(s, e).trim();
        }
    }

    public static void main(String[] args) {
        try {
            WeatherClient client = new WeatherClient("TU_API_KEY", "Bogota");
            WeatherResult r = client.getCurrentWeather();
            System.out.println("Temp: " + r.temperature());
            System.out.println("Hum: " + r.humidity());
            System.out.println("Desc: " + r.description());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
