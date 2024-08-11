package bg.softuni.travelbudgetplanner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
public class CurrencyController {

    private final String API_KEY = "b5b24848aed64e42bf00875de87dbeaf";

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/api/currencies")
    public Map<String, Double> getExchangeRates() {
        String url = "https://openexchangerates.org/api/latest.json?app_id=" + API_KEY;
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        Map<String, Double> rates = new HashMap<>();
        if (response != null && response.containsKey("rates")) {
            Map<String, Object> rawRates = (Map<String, Object>) response.get("rates");
            for (Map.Entry<String, Object> entry : rawRates.entrySet()) {
                rates.put(entry.getKey(), ((Number) entry.getValue()).doubleValue());
            }
        }
        return rates;
    }
}
