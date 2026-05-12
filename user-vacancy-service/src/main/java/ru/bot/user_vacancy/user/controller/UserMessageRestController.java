package ru.bot.user_vacancy.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bot.user_vacancy.common.dto.BotResponse;
import ru.bot.user_vacancy.common.dto.UserMessageRequest;
import ru.bot.user_vacancy.user.service.UserMessageService;

@RestController
@RequiredArgsConstructor
@RequestMapping("internal/v1/user")
public class UserMessageRestController {

    private final UserMessageService userMessageService;

    @PostMapping("/message")
    public ResponseEntity<BotResponse> handleMessage(@RequestBody UserMessageRequest request) {
        return ResponseEntity.ok(userMessageService.handle(request));
    }
}
