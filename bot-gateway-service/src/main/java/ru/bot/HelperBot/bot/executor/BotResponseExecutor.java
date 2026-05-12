package ru.bot.HelperBot.bot.executor;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bot.HelperBot.bot.dto.BotCommand;
import ru.bot.HelperBot.bot.dto.BotResponse;

import java.util.List;

@Service
public class BotResponseExecutor {

    public void execute(BotResponse botResponse, TelegramLongPollingBot bot) {
        if (botResponse == null || botResponse.commands() == null) {
            return;
        }

        for (BotCommand command : botResponse.commands()) {
            executeCommand(command, bot);
        }
    }

    private void executeCommand(BotCommand command, TelegramLongPollingBot bot) {
        try {
            switch (command.type()) {
                case SEND_MESSAGE -> bot.execute(buildSendMessage(command));
                case DELETE_MESSAGE -> bot.execute(buildDeleteMessage(command));
                case ANSWER_CALLBACK -> bot.execute(buildAnswerCallback(command));
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private SendMessage buildSendMessage(BotCommand command) {
        SendMessage message = new SendMessage(command.chatId().toString(), command.text());
        if (command.parseMode() != null) {
            message.setParseMode(command.parseMode());
        }
        if (command.inlineKeyboard() != null && !command.inlineKeyboard().isEmpty()) {
            message.setReplyMarkup(toInlineKeyboard(command.inlineKeyboard()));
        }
        if (command.replyKeyboard() != null && !command.replyKeyboard().isEmpty()) {
            message.setReplyMarkup(toReplyKeyboard(command.replyKeyboard()));
        }
        return message;
    }

    private DeleteMessage buildDeleteMessage(BotCommand command) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(command.chatId());
        deleteMessage.setMessageId(command.messageId());
        return deleteMessage;
    }

    private AnswerCallbackQuery buildAnswerCallback(BotCommand command) {
        AnswerCallbackQuery answer = new AnswerCallbackQuery();
        answer.setCallbackQueryId(command.callbackQueryId());
        return answer;
    }

    private InlineKeyboardMarkup toInlineKeyboard(List<List<BotCommand.InlineButton>> rows) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(rows.stream()
                .map(row -> row.stream()
                        .map(button -> {
                            InlineKeyboardButton inlineButton = new InlineKeyboardButton();
                            inlineButton.setText(button.text());
                            inlineButton.setCallbackData(button.callbackData());
                            return inlineButton;
                        })
                        .toList())
                .toList());
        return markup;
    }

    private ReplyKeyboardMarkup toReplyKeyboard(List<List<String>> rows) {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        keyboard.setKeyboard(rows.stream()
                .map(row -> {
                    KeyboardRow keyboardRow = new KeyboardRow();
                    keyboardRow.addAll(row);
                    return keyboardRow;
                })
                .toList());
        keyboard.setResizeKeyboard(true);
        return keyboard;
    }
}
