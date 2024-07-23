package bg.softuni.travelbudgetplanner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.HashMap;

@Service
public class CurrencyService {

    private static final String API_URL = "https://openexchangerates.org/api/latest.json";
    private static final String APP_ID = "b5b24848aed64e42bf00875de87dbeaf";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;

    public Map<String, Double> getExchangeRates() {
        String url = API_URL + "?app_id=" + APP_ID;
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        if (response != null) {
            return (Map<String, Double>) response.get("rates");
        } else {
            return new HashMap<>();
        }
    }

    public Mono<Map<String, Double>> getExchangeRatesReactive() {
        return webClientBuilder.build()
                .get()
                .uri(API_URL + "?app_id=" + APP_ID)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    if (response != null && response.get("rates") != null) {
                        return (Map<String, Double>) response.get("rates");
                    } else {
                        return new HashMap<String, Double>();
                    }
                });
    }
}
