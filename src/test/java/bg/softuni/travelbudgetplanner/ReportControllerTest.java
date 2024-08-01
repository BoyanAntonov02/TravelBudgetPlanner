package bg.softuni.travelbudgetplanner;

import bg.softuni.travelbudgetplanner.controller.ReportController;
import bg.softuni.travelbudgetplanner.model.entity.Report;
import bg.softuni.travelbudgetplanner.service.ReportService;
import bg.softuni.travelbudgetplanner.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ReportControllerTest {

    @Mock
    private ReportService reportService;

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @InjectMocks
    private ReportController reportController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void showReportForm_ShouldReturnReportsView() {
        Long tripId = 1L;
        Long userId = 1L;
        List<Report> reports = Collections.singletonList(new Report());

        when(reportService.getCurrentUserId()).thenReturn(userId);
        when(reportService.getReportsByUserId(userId)).thenReturn(reports);

        String viewName = reportController.showReportForm(tripId, model);

        verify(model).addAttribute("tripId", tripId);
        verify(model).addAttribute("userId", userId);
        verify(model).addAttribute("reports", reports);
        assertEquals("reports", viewName);
    }



    @Test
    void viewReport_ShouldReturnReportView() {
        Long reportId = 1L;
        Report report = new Report();
        report.setId(reportId);

        when(reportService.findById(reportId)).thenReturn(report);

        String viewName = reportController.viewReport(reportId, model);

        verify(model).addAttribute("report", report);
        assertEquals("report-view", viewName);
    }

    @Test
    void deleteAllReports_ShouldRedirectToReports() {
        UserDetails userDetails = mock(UserDetails.class);

        when(userDetails.getUsername()).thenReturn("admin");

        String viewName = reportController.deleteAllReports(userDetails);

        verify(reportService).deleteAllReportsByUser("admin");
        assertEquals("redirect:/reports", viewName);
    }
}
