package bg.softuni.travelbudgetplanner.service;

import bg.softuni.travelbudgetplanner.model.entity.Expense;
import bg.softuni.travelbudgetplanner.model.entity.Report;
import bg.softuni.travelbudgetplanner.model.entity.Trip;
import bg.softuni.travelbudgetplanner.model.entity.UserEntity;
import bg.softuni.travelbudgetplanner.repository.ReportRepository;
import bg.softuni.travelbudgetplanner.repository.TripRepository;
import bg.softuni.travelbudgetplanner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final TripRepository tripRepository;
    private final UserRepository userRepository;

    @Autowired
    public ReportService(ReportRepository reportRepository, TripRepository tripRepository, UserRepository userRepository) {
        this.reportRepository = reportRepository;
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
    }

    public Report createReport(Long userId, Long tripId, LocalDate startDate, LocalDate endDate, double totalExpenses) {
        UserEntity user = userRepository.findById(userId).orElseThrow();
        Trip trip = tripRepository.findById(tripId).orElseThrow();
        Report report = new Report();
        report.setUser(user);
        report.setTrip(trip);
        report.setStartDate(startDate);
        report.setEndDate(endDate);
        report.setTotalExpenses(totalExpenses);
        return reportRepository.save(report);
    }

    public List<Report> getReportsByUserId(Long userId) {
        return reportRepository.findByUserId(userId);
    }

    public double calculateTotalExpenses(Long tripId, LocalDate startDate, LocalDate endDate) {
        Trip trip = tripRepository.findById(tripId).orElseThrow();
        return trip.getExpenses().stream()
                .filter(expense -> !expense.getDate().isBefore(startDate) && !expense.getDate().isAfter(endDate))
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    public Long getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            Optional<UserEntity> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                return userOptional.get().getId();
            } else {
                return null; // or throw an exception if user not found
            }
        } else {
            return null;
        }
    }

    public Report findById(Long reportId) {
        return reportRepository.findById(reportId).orElseThrow();
    }

    public void deleteAllReports() {
        reportRepository.deleteAll();
    }

    public void deleteAllReportsByUser(String username) {
        Optional<UserEntity> user = userRepository.findByUsername(username);
        if (user != null) {
            reportRepository.deleteAllByUserId(user.get().getId());
        }
    }
}
