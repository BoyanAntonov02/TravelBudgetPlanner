//package bg.softuni.travelbudgetplanner;
//
//import bg.softuni.travelbudgetplanner.model.dto.TripDTO;
//import bg.softuni.travelbudgetplanner.model.entity.Trip;
//import bg.softuni.travelbudgetplanner.model.entity.UserEntity;
//import bg.softuni.travelbudgetplanner.repository.TripRepository;
//import bg.softuni.travelbudgetplanner.repository.UserRepository;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.time.LocalDate;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest
//@AutoConfigureMockMvc
//class TripControllerIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private TripRepository tripRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    private UserEntity user;
//
//    @BeforeEach
//    void setUp() {
//        userRepository.deleteAll();
//        tripRepository.deleteAll();
//        user = new UserEntity();
//        user.setUsername("test");
//        user.setPassword("test");
//        userRepository.save(user);
//    }
//
//    @Test
//    @WithMockUser(username = "test")
//    void testViewTrip() throws Exception {
//        Trip trip = new Trip();
//        trip.setDestination("Test Destination");
//        trip.setStartDate(LocalDate.of(2023, 5, 1));
//        trip.setEndDate(LocalDate.of(2023, 5, 10));
//        trip.setBudget(1000.0);
//        trip.setUser(user);
//        tripRepository.save(trip);
//
//        mockMvc.perform(get("/trips/view/" + trip.getId()))
//                .andExpect(status().isOk())
//                .andExpect(view().name("view-trip"))
//                .andExpect(model().attributeExists("trip"));
//    }
//
//    @Test
//    @WithMockUser(username = "test")
//    void testCreateTripForm() throws Exception {
//        mockMvc.perform(get("/trips/create"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("create-trip"))
//                .andExpect(model().attributeExists("tripData"));
//    }
//
//    @Test
//    @WithMockUser(username = "test")
//    void testCreateTrip() throws Exception {
//        TripDTO tripDTO = new TripDTO();
//        tripDTO.setDestination("Test Destination");
//        tripDTO.setStartDate("2023-05-01");
//        tripDTO.setEndDate("2023-05-10");
//        tripDTO.setBudget(1000.0);
//
//        mockMvc.perform(post("/trips")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(tripDTO)))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/dashboard"));
//
//        Trip trip = tripRepository.findAll().get(0);
//        assertEquals("Test Destination", trip.getDestination());
//    }
//
//    @Test
//    @WithMockUser(username = "test")
//    void testEditTripForm() throws Exception {
//        Trip trip = new Trip();
//        trip.setDestination("Test Destination");
//        trip.setStartDate(LocalDate.of(2023, 5, 1));
//        trip.setEndDate(LocalDate.of(2023, 5, 10));
//        trip.setBudget(1000.0);
//        trip.setUser(user);
//        tripRepository.save(trip);
//
//        mockMvc.perform(get("/trips/edit/" + trip.getId()))
//                .andExpect(status().isOk())
//                .andExpect(view().name("edit-trip"))
//                .andExpect(model().attributeExists("tripData"))
//                .andExpect(model().attributeExists("tripId"));
//    }
//
//    @Test
//    @WithMockUser(username = "test")
//    void testEditTrip() throws Exception {
//        Trip trip = new Trip();
//        trip.setDestination("Test Destination");
//        trip.setStartDate(LocalDate.of(2023, 5, 1));
//        trip.setEndDate(LocalDate.of(2023, 5, 10));
//        trip.setBudget(1000.0);
//        trip.setUser(user);
//        tripRepository.save(trip);
//
//        TripDTO tripDTO = new TripDTO();
//        tripDTO.setDestination("Updated Destination");
//        tripDTO.setStartDate("2023-05-01");
//        tripDTO.setEndDate("2023-05-10");
//        tripDTO.setBudget(1500.0);
//
//        mockMvc.perform(post("/trips/edit/" + trip.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(tripDTO)))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/dashboard"));
//
//        Trip updatedTrip = tripRepository.findById(trip.getId()).get();
//        assertEquals("Updated Destination", updatedTrip.getDestination());
//    }
//
//    @Test
//    @WithMockUser(username = "test")
//    void testDeleteTrip() throws Exception {
//        Trip trip = new Trip();
//        trip.setDestination("Test Destination");
//        trip.setStartDate(LocalDate.of(2023, 5, 1));
//        trip.setEndDate(LocalDate.of(2023, 5, 10));
//        trip.setBudget(1000.0);
//        trip.setUser(user);
//        tripRepository.save(trip);
//
//        mockMvc.perform(post("/trips/delete/" + trip.getId()))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/dashboard"));
//
//        assertFalse(tripRepository.findById(trip.getId()).isPresent());
//    }
//}
