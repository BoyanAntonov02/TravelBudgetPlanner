package bg.softuni.travelbudgetplanner.controller;

import bg.softuni.travelbudgetplanner.model.entity.Report;
import bg.softuni.travelbudgetplanner.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/create/{tripId}")
    public String showReportForm(@PathVariable Long tripId, Model model) {
        Long userId = reportService.getCurrentUserId();
        model.addAttribute("tripId", tripId);
        model.addAttribute("userId", userId);
        List<Report> reports = reportService.getReportsByUserId(userId);
        model.addAttribute("reports", reports);
        return "reports";
    }

    @PostMapping("/create")
    public String generateReport(@RequestParam Long userId, @RequestParam Long tripId, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate, Model model) {
        double totalExpenses = reportService.calculateTotalExpenses(tripId, startDate, endDate);
        Report report = reportService.createReport(userId, tripId, startDate, endDate, totalExpenses);
        return "redirect:/reports/view/" + report.getId();
    }

    @GetMapping("/view/{reportId}")
    public String viewReport(@PathVariable Long reportId, Model model) {
        Report report = reportService.findById(reportId);
        model.addAttribute("report", report);
        return "report-view";
    }

    @PostMapping("/reports/deleteAll")
    public String deleteAllReports(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        reportService.deleteAllReportsByUser(userDetails.getUsername());

        return "redirect:/reports";
    }
}
