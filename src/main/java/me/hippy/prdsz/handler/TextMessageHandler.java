package me.hippy.prdsz.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hippy.prdsz.model.Wish;
import me.hippy.prdsz.service.WishService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
@Slf4j
public class TextMessageHandler implements TelegramUpdateHandler {

    private final WishService wishService;

    @Override
    public SendMessage handle(Update update) {
        var text = update.getMessage().getText();
        var authorId = update.getMessage().getFrom().getId();

        if (text.startsWith("/insert ")) {
            Long chatId = update.getMessage().getChatId();
            var content = text.split("/insert ")[1];
            var wish = new Wish();
            wish.setContent(content);
            wishService.create(wish);

            var sendMessage = SendMessage.builder()
                    .parseMode(ParseMode.MARKDOWN)
                    .text("Добавлено предсказание с содержанием: " + content)
                    .chatId(chatId)
                    .build();

            log.info("Added new wish by adm with ID: {}", authorId);
            return sendMessage;
        }

        return null;
    }
}
