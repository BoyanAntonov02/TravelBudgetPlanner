package bg.softuni.travelbudgetplanner;

import bg.softuni.travelbudgetplanner.model.entity.Expense;
import bg.softuni.travelbudgetplanner.model.entity.Report;
import bg.softuni.travelbudgetplanner.model.entity.Trip;
import bg.softuni.travelbudgetplanner.model.entity.UserEntity;
import bg.softuni.travelbudgetplanner.repository.ReportRepository;
import bg.softuni.travelbudgetplanner.repository.TripRepository;
import bg.softuni.travelbudgetplanner.repository.UserRepository;
import bg.softuni.travelbudgetplanner.service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReportServiceTest {

    @Mock
    private ReportRepository reportRepository;

    @Mock
    private TripRepository tripRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReportService reportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createReport_ShouldSaveAndReturnReport() {
        Long userId = 1L;
        Long tripId = 2L;
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);
        double totalExpenses = 1000.0;

        UserEntity user = new UserEntity();
        user.setId(userId);
        Trip trip = new Trip();
        trip.setId(tripId);

        Report report = new Report();
        report.setUser(user);
        report.setTrip(trip);
        report.setStartDate(startDate);
        report.setEndDate(endDate);
        report.setTotalExpenses(totalExpenses);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(tripRepository.findById(tripId)).thenReturn(Optional.of(trip));
        when(reportRepository.save(ArgumentMatchers.any(Report.class))).thenReturn(report);

        Report createdReport = reportService.createReport(userId, tripId, startDate, endDate, totalExpenses);

        assertNotNull(createdReport);
        assertEquals(userId, createdReport.getUser().getId());
        assertEquals(tripId, createdReport.getTrip().getId());
        assertEquals(startDate, createdReport.getStartDate());
        assertEquals(endDate, createdReport.getEndDate());
        assertEquals(totalExpenses, createdReport.getTotalExpenses());
        verify(reportRepository, times(1)).save(ArgumentMatchers.any(Report.class));
    }

    @Test
    void getReportsByUserId_ShouldReturnReports() {
        Long userId = 1L;
        Report report = new Report();
        when(reportRepository.findByUserId(userId)).thenReturn(Collections.singletonList(report));

        List<Report> reports = reportService.getReportsByUserId(userId);

        assertNotNull(reports);
        assertEquals(1, reports.size());
        assertEquals(report, reports.get(0));
        verify(reportRepository, times(1)).findByUserId(userId);
    }

    @Test
    void calculateTotalExpenses_ShouldReturnSumOfExpenses() {
        Long tripId = 1L;
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);
        Trip trip = new Trip();
        Expense expense1 = new Expense();
        expense1.setDate(LocalDate.of(2023, 1, 15));
        expense1.setAmount(100.0);
        Expense expense2 = new Expense();
        expense2.setDate(LocalDate.of(2023, 6, 15));
        expense2.setAmount(200.0);
        trip.setExpenses(Set.of(expense1, expense2));

        when(tripRepository.findById(tripId)).thenReturn(Optional.of(trip));

        double totalExpenses = reportService.calculateTotalExpenses(tripId, startDate, endDate);

        assertEquals(300.0, totalExpenses);
        verify(tripRepository, times(1)).findById(tripId);
    }



    @Test
    void findById_ShouldReturnReport() {
        Long reportId = 1L;
        Report report = new Report();
        report.setId(reportId);

        when(reportRepository.findById(reportId)).thenReturn(Optional.of(report));

        Report foundReport = reportService.findById(reportId);

        assertNotNull(foundReport);
        assertEquals(reportId, foundReport.getId());
        verify(reportRepository, times(1)).findById(reportId);
    }

    @Test
    void deleteAllReports_ShouldCallDeleteAll() {
        reportService.deleteAllReports();
        verify(reportRepository, times(1)).deleteAll();
    }

    @Test
    void deleteAllReportsByUser_ShouldCallDeleteAllByUserId() {
        String username = "testuser";
        UserEntity user = new UserEntity();
        user.setId(1L);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        reportService.deleteAllReportsByUser(username);

        verify(reportRepository, times(1)).deleteAllByUserId(user.getId());
    }
}
