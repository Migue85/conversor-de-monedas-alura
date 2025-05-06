import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName; // Importa esta anotación si es necesario
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ConversorMonedas {

    private static final String API_KEY = "AQUÍ_DEBES_COLOCAR_TU_CLAVE_DE_API"; // ¡Reemplaza esto!
    private static final String API_URL_BASE = "AQUÍ_DEBES_COLOCAR_LA_URL_BASE_DE_TU_API"; // ¡Reemplaza esto!
    private static final Gson gson = new Gson();
    private static final HttpClient httpClient = HttpClient.newHttpClient();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            mostrarMenu();
            System.out.print("Elija una opción válida: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir la nueva línea

            switch (opcion) {
                case 1:
                    convertirMoneda("USD", "ARS", scanner);
                    break;
                case 2:
                    convertirMoneda("ARS", "USD", scanner);
                    break;
                case 3:
                    convertirMoneda("USD", "BRL", scanner);
                    break;
                case 4:
                    convertirMoneda("BRL", "USD", scanner);
                    break;
                case 5:
                    convertirMoneda("USD", "COP", scanner);
                    break;
                case 6:
                    convertirMoneda("COP", "USD", scanner);
                    break;
                case 7:
                    System.out.println("Saliendo del conversor de monedas. ¡Hasta luego!");
                    break;
                default:
                    System.out.println("Opción inválida. Por favor, elija una opción del menú.");
            }
            System.out.println(); // Línea en blanco para mejor visualización
        } while (opcion != 7);

        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println("--- Conversor de Monedas ---");
        System.out.println("1) Dólar => Peso Argentino");
        System.out.println("2) Peso Argentino => Dólar");
        System.out.println("3) Dólar => Real Brasilero");
        System.out.println("4) Real Brasilero => Dólar");
        System.out.println("5) Dólar => Peso Colombiano");
        System.out.println("6) Peso Colombiano => Dólar");
        System.out.println("7) Salir");
    }

    private static void convertirMoneda(String monedaOrigen, String monedaDestino, Scanner scanner) {
        System.out.print("Ingrese el valor que desea convertir: ");
        double valorAConvertir = scanner.nextDouble();
        scanner.nextLine(); // Consumir la nueva línea

        try {
            // *** Aquí construimos la URL de la API ***
            String apiUrl = String.format("%s/latest?access_key=%s&symbols=%s,%s&base=%s",
                    API_URL_BASE, API_KEY, monedaDestino, monedaOrigen, monedaOrigen);

            // *** Creamos la HttpRequest ***
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .build();

            // *** Enviamos la HttpRequest y obtenemos la HttpResponse ***
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // *** Procesamos la respuesta de la API ***
            if (response.statusCode() == 200) {
                String responseBody = response.body();
                JsonApiResponse jsonResponse = gson.fromJson(responseBody, JsonApiResponse.class);

                if (jsonResponse != null && jsonResponse.rates != null && jsonResponse.rates.containsKey(monedaDestino)) {
                    double tasaConversion = jsonResponse.rates.get(monedaDestino);
                    double resultado = valorAConvertir * tasaConversion;
                    System.out.printf("El valor de %.2f [%s] corresponde al valor final de => %.2f [%s]%n",
                            valorAConvertir, monedaOrigen, resultado, monedaDestino);
                } else {
                    System.out.println("No se pudo obtener la tasa de conversión para las monedas seleccionadas.");
                    System.out.println("Respuesta de la API: " + responseBody); // Para debugging
                }

            } else {
                System.out.println("Error al consumir la API. Código de respuesta: " + response.statusCode());
                System.out.println("Cuerpo de la respuesta: " + response.body()); // Para debugging
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("Ocurrió un error al realizar la petición a la API: " + e.getMessage());
        }
    }

    // *** Clase interna para mapear la respuesta JSON de la API (¡ADAPTAR SEGÚN TU API!) ***
    private static class JsonApiResponse {
        private boolean success;
        private long timestamp;
        private String base;
        private String date;
        private Rates rates;

        // Getters y setters
        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public String getBase() {
            return base;
        }

        public void setBase(String base) {
            this.base = base;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public Rates getRates() {
            return rates;
        }

        public void setRates(Rates rates) {
            this.rates = rates;
        }
    }

    private static class Rates {
        // Usamos un HashMap para almacenar las tasas de cambio
        @SerializedName("rates") // Si tu API envia un objeto "rates" dentro de otro "rates"
        private Map<String, Double> rates;

        public Map<String, Double> getRates() {
            return rates;
        }

        public void setRates(Map<String, Double> rates) {
            this.rates = rates;
        }

        // Método para verificar si una moneda existe en las tasas
        public boolean containsKey(String currencyCode) {
            return this.rates != null && this.rates.containsKey(currencyCode);
        }

        // Método para obtener la tasa de una moneda
        public Double get(String currencyCode) {
            if (this.rates != null) {
                return this.rates.get(currencyCode);
            }
            return null;
        }
    }
}}