package ru.bot.ai_resume.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.bot.ai_resume.dto.AiRequest;
import ru.bot.ai_resume.dto.ResumeAnalysisResponse;
import ru.bot.ai_resume.services.AiService;
import ru.bot.ai_resume.services.PdfService;

import java.io.DataInput;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final AiService aiService;
    private ObjectMapper objectMapper;
    private final PdfService pdfService;

    @Autowired
    public AiController(AiService aiService, PdfService pdfService) {
        this.aiService = aiService;
        this.pdfService = pdfService;
    }

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
    public ResponseEntity<ResumeAnalysisResponse> analyzeResume(@RequestParam("file") MultipartFile file) {
        if (!file.getContentType().equals("application/pdf")) {
            return ResponseEntity.badRequest().body(new ResumeAnalysisResponse());
        }
        try {
            String text = pdfService.parsePdf(file);
            JsonNode responseJson = aiService.generateResumeAnalysis(text);
            ResumeAnalysisResponse response = objectMapper.readValue((DataInput) responseJson.get("response"), ResumeAnalysisResponse.class);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResumeAnalysisResponse());
        }
    }

}

