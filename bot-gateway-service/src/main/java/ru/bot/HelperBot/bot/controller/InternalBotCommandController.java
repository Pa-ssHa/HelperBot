package ru.bot.HelperBot.bot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bot.HelperBot.bot.TelegramBot;
import ru.bot.HelperBot.bot.dto.BotResponse;
import ru.bot.HelperBot.bot.executor.BotResponseExecutor;

@RestController
@RequestMapping("/internal/v1/bot")
@RequiredArgsConstructor
public class InternalBotCommandController {

    private final BotResponseExecutor botResponseExecutor;
    private final TelegramBot telegramBot;

    @PostMapping("/commands")
    public ResponseEntity<Void> execute(@RequestBody BotResponse botResponse) {
        botResponseExecutor.execute(botResponse, telegramBot);
        return ResponseEntity.ok().build();
    }
}
