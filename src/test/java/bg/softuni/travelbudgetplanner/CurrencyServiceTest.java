package bg.softuni.travelbudgetplanner;

import bg.softuni.travelbudgetplanner.service.CurrencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CurrencyServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private CurrencyService currencyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getExchangeRates_Success() {
        Map<String, Object> mockResponse = new HashMap<>();
        Map<String, Double> mockRates = new HashMap<>();
        mockRates.put("USD", 1.0);
        mockRates.put("EUR", 0.85);
        mockResponse.put("rates", mockRates);

        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(mockResponse);

        Map<String, Double> result = currencyService.getExchangeRates();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1.0, result.get("USD"));
        assertEquals(0.85, result.get("EUR"));
        verify(restTemplate).getForObject(anyString(), eq(Map.class));
    }

    @Test
    void getExchangeRates_Failure() {
        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(null);

        Map<String, Double> result = currencyService.getExchangeRates();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(restTemplate).getForObject(anyString(), eq(Map.class));
    }

    @Test
    void getExchangeRatesReactive_Success() {
        Map<String, Object> mockResponse = new HashMap<>();
        Map<String, Double> mockRates = new HashMap<>();
        mockRates.put("USD", 1.0);
        mockRates.put("EUR", 0.85);
        mockResponse.put("rates", mockRates);

        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(mockResponse));

        StepVerifier.create(currencyService.getExchangeRatesReactive())
                .expectNextMatches(result -> {
                    assertNotNull(result);
                    assertEquals(2, result.size());
                    assertEquals(1.0, result.get("USD"));
                    assertEquals(0.85, result.get("EUR"));
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void getExchangeRatesReactive_Failure() {
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(new HashMap<>()));

        StepVerifier.create(currencyService.getExchangeRatesReactive())
                .expectNextMatches(result -> {
                    assertNotNull(result);
                    assertTrue(result.isEmpty());
                    return true;
                })
                .verifyComplete();
    }
}