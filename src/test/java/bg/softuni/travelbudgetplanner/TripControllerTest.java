package bg.softuni.travelbudgetplanner;

import bg.softuni.travelbudgetplanner.controller.TripController;
import bg.softuni.travelbudgetplanner.model.dto.TripDTO;
import bg.softuni.travelbudgetplanner.model.entity.Trip;
import bg.softuni.travelbudgetplanner.model.entity.UserEntity;
import bg.softuni.travelbudgetplanner.service.TripService;
import bg.softuni.travelbudgetplanner.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.util.Optional;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TripControllerTest {

    @Mock
    private TripService tripService;

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private TripController tripController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void viewTrip_AuthenticatedUser() {
        Long tripId = 1L;
        Trip trip = new Trip();
        UserEntity user = new UserEntity();
        user.setUsername("testUser");
        trip.setUser(user);

        when(userDetails.getUsername()).thenReturn("testUser");
        when(tripService.getTripById(tripId)).thenReturn(trip);
        when(userService.isAdmin(userDetails)).thenReturn(false);

        String viewName = tripController.viewTrip(tripId, model, userDetails);

        assertEquals("view-trip", viewName);
        verify(model).addAttribute("trip", trip);
        verify(model).addAttribute("isAdmin", false);
    }

    @Test
    void viewTrip_UnauthenticatedUser() {
        Long tripId = 1L;

        String viewName = tripController.viewTrip(tripId, model, null);

        assertEquals("redirect:/login", viewName);
    }

    @Test
    void createTripForm_AuthenticatedUser() {
        when(userDetails.getUsername()).thenReturn("testUser");

        String viewName = tripController.createTripForm(model, userDetails);

        assertEquals("create-trip", viewName);
        verify(model).addAttribute(eq("tripData"), any(TripDTO.class));
    }

    @Test
    void createTripForm_UnauthenticatedUser() {
        String viewName = tripController.createTripForm(model, null);

        assertEquals("redirect:/login", viewName);
    }

    @Test
    void createTrip_Success() {
        TripDTO tripDTO = new TripDTO();
        tripDTO.setDestination("Test Destination");
        tripDTO.setStartDate("2023-05-01");
        tripDTO.setEndDate("2023-05-10");
        tripDTO.setBudget(1000.0);

        UserEntity user = new UserEntity();
        user.setUsername("testUser");

        when(userDetails.getUsername()).thenReturn("testUser");
        when(userService.findByUsername("testUser")).thenReturn(user);

        String viewName = tripController.createTrip(tripDTO, userDetails);

        assertEquals("redirect:/dashboard", viewName);
        verify(tripService).saveTrip(any(Trip.class));
    }

    @Test
    void createTrip_UnauthenticatedUser() {
        TripDTO tripDTO = new TripDTO();

        String viewName = tripController.createTrip(tripDTO, null);

        assertEquals("redirect:/login", viewName);
    }


    @Test
    void editTripForm_UnauthenticatedUser() {
        Long tripId = 1L;

        String viewName = tripController.editTripForm(tripId, model, null);

        assertEquals("redirect:/login", viewName);
    }

    @Test
    void editTrip_Success() {
        Long tripId = 1L;
        TripDTO tripDTO = new TripDTO();
        tripDTO.setDestination("Updated Destination");
        tripDTO.setStartDate("2023-05-01");
        tripDTO.setEndDate("2023-05-10");
        tripDTO.setBudget(1500.0);

        Trip trip = new Trip();
        UserEntity user = new UserEntity();
        user.setUsername("testUser");
        trip.setUser(user);

        when(userDetails.getUsername()).thenReturn("testUser");
        when(tripService.getTripById(tripId)).thenReturn(trip);
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = tripController.editTrip(tripId, tripDTO, bindingResult, userDetails, model);

        assertEquals("redirect:/dashboard", viewName);
        verify(tripService).saveTrip(trip);
    }

    @Test
    void editTrip_ValidationErrors() {
        Long tripId = 1L;
        TripDTO tripDTO = new TripDTO();

        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = tripController.editTrip(tripId, tripDTO, bindingResult, userDetails, model);

        assertEquals("edit-trip", viewName);
        verify(model).addAttribute("tripData", tripDTO);
    }

    @Test
    void deleteTrip_Success() {
        Long tripId = 1L;
        Trip trip = new Trip();
        UserEntity user = new UserEntity();
        user.setUsername("testUser");
        trip.setUser(user);

        when(userDetails.getUsername()).thenReturn("testUser");
        when(tripService.getTripById(tripId)).thenReturn(trip);

        String viewName = tripController.deleteTrip(tripId, userDetails);

        assertEquals("redirect:/dashboard", viewName);
        verify(tripService).deleteTrip(tripId);
    }

    @Test
    void deleteTrip_UnauthenticatedUser() {
        Long tripId = 1L;

        String viewName = tripController.deleteTrip(tripId, null);

        assertEquals("redirect:/login", viewName);
    }
}
