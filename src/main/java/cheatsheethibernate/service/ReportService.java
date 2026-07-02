package cheatsheethibernate.service;

import cheatsheethibernate.entity.Report;
import java.util.List;

public interface ReportService {
	void submitReport(Long userId, Long cheatSheetId, String reason);

	List<Report> getAllReports();

	Report getReportById(Integer id);

	void actionReport(Integer reportId, String status);
}