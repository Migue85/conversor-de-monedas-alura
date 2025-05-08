import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Scanner;

public class ConversorMonedaschalenge {

    private static final String API_KEY = "644099eea221b53f0e7461d2";
    private static final String API_URL_BASE = "https://v6.exchangerate-api.com/v6/";
    private static final Gson gson = new Gson();
    private static final HttpClient httpClient = HttpClient.newHttpClient();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            mostrarMenu();
            System.out.print("Elija una opción válida: ");
            opcion = scanner.nextInt();
            scanner.nextLine();

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
            System.out.println();
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
        scanner.nextLine();

        try {
            // ***construccion de la URL de la API ***
            String apiUrl = String.format("%s%s/latest/%s",
                    API_URL_BASE, API_KEY, monedaOrigen);

            // *** Creacion de la HttpRequest ***
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .build();

            // *** Envio de la HttpRequest y obtencion de la HttpResponse ***
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // *** Proceso de la respuesta de la API ***
            if (response.statusCode() == 200) {
                String responseBody = response.body();
                ExchangeRateApiResponse jsonResponse = gson.fromJson(responseBody, ExchangeRateApiResponse.class);

                if (jsonResponse != null && jsonResponse.conversionRates != null && jsonResponse.conversionRates.containsKey(monedaDestino)) {
                    double tasaConversion = jsonResponse.conversionRates.get(monedaDestino);
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

    // *** Clase interna para el mapeo de la respuesta JSON de la API ***
    private static class ExchangeRateApiResponse {
        @SerializedName("result")
        private String result;
        @SerializedName("conversion_rates")
        private Map<String, Double> conversionRates;

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public Map<String, Double> getConversionRates() {
            return conversionRates;
        }

        public void setConversionRates(Map<String, Double> conversionRates) {
            this.conversionRates = conversionRates;
        }
    }
}
