package cheatsheethibernate.service;

public interface EmailService {

	void sendResetCode(String toEmail, String code);

}
