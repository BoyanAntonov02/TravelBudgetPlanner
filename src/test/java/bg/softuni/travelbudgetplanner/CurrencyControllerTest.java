package bg.softuni.travelbudgetplanner;

import bg.softuni.travelbudgetplanner.controller.CurrencyController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CurrencyControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CurrencyController currencyController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(currencyController).build();
    }

    @Test
    void getExchangeRates_Success() throws Exception {
        Map<String, Object> mockResponse = new HashMap<>();
        Map<String, Object> mockRates = new HashMap<>();
        mockRates.put("USD", 1.0);
        mockRates.put("EUR", 0.85);
        mockResponse.put("rates", mockRates);

        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(mockResponse);

        mockMvc.perform(get("/api/currencies"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.USD").value(1.0))
                .andExpect(jsonPath("$.EUR").value(0.85));
    }

    @Test
    void getExchangeRates_EmptyResponse() throws Exception {
        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(new HashMap<>());

        mockMvc.perform(get("/api/currencies"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getExchangeRates_NullResponse() throws Exception {
        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(null);

        mockMvc.perform(get("/api/currencies"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isEmpty());
    }
}