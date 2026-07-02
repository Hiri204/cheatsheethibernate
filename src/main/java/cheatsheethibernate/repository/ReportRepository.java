package cheatsheethibernate.repository;

import cheatsheethibernate.entity.Report;
import java.util.List;

public interface ReportRepository {
	void save(Report report);

	List<Report> findAll();

	Report findById(Integer id);

	void update(Report report);

	void delete(Integer id);
}