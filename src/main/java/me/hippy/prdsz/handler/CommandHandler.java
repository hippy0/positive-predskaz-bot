package me.hippy.prdsz.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hippy.prdsz.model.Wish;
import me.hippy.prdsz.service.WishService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommandHandler implements TelegramUpdateHandler {

    @Value("${general.hello-message}")
    private String helloMessage;

    private final WishService wishService;

    @Override
    public SendMessage handle(Update update) {
        var message = update.getMessage();
        var command = message.getText();

        switch (command) {
            case "/start" -> {
                return SendMessage.builder()
                        .text(helloMessage)
                        .parseMode(ParseMode.MARKDOWN)
                        .chatId(message.getChatId())
                        .build();
            }
            case "/wish" -> {
                Wish wish = wishService.findRandom();
                String content = wish.getContent();

                return SendMessage.builder()
                        .parseMode(ParseMode.MARKDOWN)
                        .text("**Предсказание для **@" + message.getFrom().getUserName() + "\n\n" + content)
                        .chatId(message.getChatId())
                        .build();
            }
            default -> {
                return SendMessage.builder()
                        .parseMode(ParseMode.MARKDOWN)
                        .text("Это что-то неизвестное для меня.. попробуй использовать другую команду о_0")
                        .build();
            }
        }
    }
}
