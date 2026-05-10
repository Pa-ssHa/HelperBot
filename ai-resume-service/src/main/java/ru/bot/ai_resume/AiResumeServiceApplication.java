package ru.bot.ai_resume;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.logging.Logger;

@SpringBootApplication
public class AiResumeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiResumeServiceApplication.class, args);
	}

	Logger log = Logger.getLogger(AiResumeServiceApplication.class.getName());

	@EventListener(ApplicationReadyEvent.class)
	public void onStartup() {
		log.info("========================================");
		log.info("🚀 AI Resume Service started successfully!");
		log.info("========================================");
		log.info("Available endpoints:");
		log.info("  POST   /api/resume/analyze            - Анализ PDF резюме");
		log.info("  POST   /api/resume/generate-cover-letter - Генерация сопроводительного письма");
		log.info("  POST   /api/resume/analyze-text       - Анализ текста резюме");
		log.info("  GET    /api/resume/health             - Проверка здоровья сервиса");
		log.info("  GET    /api/resume/test               - Тест AI подключения");
		log.info("  POST   /api/ai-test/generate          - Тестовый генератор текста");
		log.info("  POST   /api/ai-test/chat              - Тестовый чат");
		log.info("  GET    /api/ai-test/health            - Проверка здоровья AI");
		log.info("========================================");
	}
}
