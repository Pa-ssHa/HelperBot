package ru.bot.ai_resume.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.bot.ai_resume.dto.AiRequest;
import ru.bot.ai_resume.dto.ResumeAnalysisResponse;
import ru.bot.ai_resume.facade.ResumeMapper;
import ru.bot.ai_resume.services.AiService;
import ru.bot.ai_resume.services.PdfService;
import ru.bot.ai_resume.services.ResumeService;

import java.io.DataInput;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;
    private final ObjectMapper objectMapper;
    private final PdfService pdfService;
    private final ResumeMapper resumeMapper;
    private final ResumeService resumeService;

    @PostMapping("/ask")
    public ResponseEntity<ResumeAnalysisResponse> askQuestion(@RequestBody AiRequest request) {
        try {
            String response = aiService.generateResponse(request.getQuery());
            return ResponseEntity.ok(objectMapper.readValue(response, ResumeAnalysisResponse.class));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResumeAnalysisResponse());
        }
    }

    @PostMapping("/resume/analyze")
    public ResponseEntity<ResumeAnalysisResponse> analyzeResume(
            @RequestParam("file") MultipartFile file,
            @RequestParam("chatId") Long chatId,
            @RequestParam("fileName") String fileName,
            @RequestParam("telegramFileId") String telegramFileId
    ) {
        if (!"application/pdf".equals(file.getContentType())) {
            return ResponseEntity.badRequest().body(new ResumeAnalysisResponse());
        }

        try {
            String text = pdfService.parsePdf(file);

            JsonNode responseJson = aiService.generateResumeAnalysis(text);

            String aiText = responseJson.get("response").asText();

            aiText = aiText
                    .replace("```json", "")
                    .replace("```", "")
                    .trim();

            ResumeAnalysisResponse response = objectMapper.readValue(
                    aiText,
                    ResumeAnalysisResponse.class
            );

            resumeService.save(
                    resumeMapper.toEntity(
                            response,
                            chatId,
                            fileName,
                            telegramFileId,
                            aiText
                    )
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ResumeAnalysisResponse());
        }
    }

}

