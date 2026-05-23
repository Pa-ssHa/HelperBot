package ru.bot.user_vacancy.vacancy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bot.user_vacancy.common.dto.BotResponse;
import ru.bot.user_vacancy.common.dto.SearchVacancyRequest;
import ru.bot.user_vacancy.vacancy.service.VacancySearchMessageService;

@RestController
@RequiredArgsConstructor
@RequestMapping("internal/v1/vacancy")
public class VacancySearchRestController {

    private final VacancySearchMessageService vacancySearchMessageService;

    @PostMapping("/search")
    public ResponseEntity<BotResponse> search(@RequestBody SearchVacancyRequest request) {
        return ResponseEntity.ok(vacancySearchMessageService.search(request));
    }

    @PostMapping("/favorites")
    public ResponseEntity<BotResponse> favorites(@RequestBody SearchVacancyRequest request) {
        return ResponseEntity.ok(vacancySearchMessageService.favorites(request));
    }
}
