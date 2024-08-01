package bg.softuni.travelbudgetplanner;

import bg.softuni.travelbudgetplanner.model.entity.Trip;
import bg.softuni.travelbudgetplanner.model.entity.UserEntity;
import bg.softuni.travelbudgetplanner.repository.TripRepository;
import bg.softuni.travelbudgetplanner.service.TripService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TripServiceTest {

    @Mock
    private TripRepository tripRepository;

    @InjectMocks
    private TripService tripService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveTrip_Success() {
        Trip trip = new Trip();
        when(tripRepository.save(any(Trip.class))).thenReturn(trip);

        Trip savedTrip = tripService.saveTrip(trip);

        assertNotNull(savedTrip);
        verify(tripRepository).save(trip);
    }

    @Test
    void findById_Success() {
        Long id = 1L;
        Trip trip = new Trip();
        when(tripRepository.findById(id)).thenReturn(Optional.of(trip));

        Trip foundTrip = tripService.findById(id);

        assertNotNull(foundTrip);
        assertEquals(trip, foundTrip);
    }

    @Test
    void findById_NotFound() {
        Long id = 1L;
        when(tripRepository.findById(id)).thenReturn(Optional.empty());

        Trip foundTrip = tripService.findById(id);

        assertNull(foundTrip);
    }

    @Test
    void getTripById_Success() {
        Long id = 1L;
        Trip trip = new Trip();
        when(tripRepository.findById(id)).thenReturn(Optional.of(trip));

        Trip foundTrip = tripService.getTripById(id);

        assertNotNull(foundTrip);
        assertEquals(trip, foundTrip);
    }

    @Test
    void getTripById_NotFound() {
        Long id = 1L;
        when(tripRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> tripService.getTripById(id));
    }

    @Test
    void findTripsByUser_Success() {
        UserEntity user = new UserEntity();
        List<Trip> trips = Arrays.asList(new Trip(), new Trip());
        when(tripRepository.findByUser(user)).thenReturn(trips);

        List<Trip> foundTrips = tripService.findTripsByUser(user);

        assertNotNull(foundTrips);
        assertEquals(2, foundTrips.size());
    }

    @Test
    void deleteTrip_Success() {
        Long id = 1L;
        doNothing().when(tripRepository).deleteById(id);

        tripService.deleteTrip(id);

        verify(tripRepository).deleteById(id);
    }
}
