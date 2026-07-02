package cheatsheethibernate.service;

import cheatsheethibernate.entity.CheatSheet;
import cheatsheethibernate.entity.Report;
import cheatsheethibernate.entity.User;
import cheatsheethibernate.repository.CheatSheetRepository;
import cheatsheethibernate.repository.ReportRepository;
import cheatsheethibernate.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

	@Autowired
	private ReportRepository reportRepository;

	@Autowired
	private CheatSheetRepository cheatSheetRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	@Transactional
	public void submitReport(Long userId, Long cheatSheetId, String reason) {
		// Get user and cheat sheet
		User user = userRepository.getById(userId.intValue());
		CheatSheet cheatSheet = cheatSheetRepository.findById(cheatSheetId);

		if (user == null || cheatSheet == null) {
			throw new RuntimeException("User or CheatSheet not found");
		}

		Report report = new Report();
		report.setCheatSheet(cheatSheet);
		report.setReportedBy(user);
		report.setReason(reason);
		report.setStatus("pending");
		report.setCreatedAt(LocalDateTime.now());
		report.setUpdatedAt(LocalDateTime.now());

		reportRepository.save(report);
	}

	@Override
	public List<Report> getAllReports() {
		return reportRepository.findAll();
	}

	@Override
	public Report getReportById(Integer id) {
		return reportRepository.findById(id);
	}

	@Override
	@Transactional
	public void actionReport(Integer reportId, String status) {
		Report report = reportRepository.findById(reportId);
		if (report != null) {
			report.setStatus(status);
			report.setUpdatedAt(LocalDateTime.now());
			reportRepository.update(report);
		}
	}
}