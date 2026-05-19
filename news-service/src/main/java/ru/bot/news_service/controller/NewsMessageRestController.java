package ru.bot.news_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bot.news_service.dto.BotResponse;
import ru.bot.news_service.dto.NewsMessageRequest;
import ru.bot.news_service.service.NewsSearchMessageService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/v1/news")
public class NewsMessageRestController {

    private final NewsSearchMessageService newsSearchMessageService;

    @PostMapping("/message")
    public ResponseEntity<BotResponse> handleNewsMessage(@RequestBody NewsMessageRequest request) {
        return ResponseEntity.ok(newsSearchMessageService.handle(request));
    }
}
