package me.hippy.prdsz.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.hippy.prdsz.dao.WishRepository;
import me.hippy.prdsz.dto.InputUserDto;
import me.hippy.prdsz.enums.UserRoleKey;
import me.hippy.prdsz.handler.CommandHandler;
import me.hippy.prdsz.handler.InlineQueryHandler;
import me.hippy.prdsz.handler.TextMessageHandler;
import me.hippy.prdsz.model.Wish;
import me.hippy.prdsz.service.PictureService;
import me.hippy.prdsz.service.UserService;
import me.hippy.prdsz.service.WishService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.List;

@Slf4j
@EnableScheduling
@Service("botConfigurationPrimary")
@RequiredArgsConstructor
public class BotConfiguration extends TelegramLongPollingBot {

    private final UserService userService;
    @Value("${telegram.secret.token}")
    private String token;

    @Value("${telegram.secret.username}")
    private String username;

    @Value("${telegram.first-init}")
    private boolean firstInit;

    @Value("${telegram.authorId}")
    private String authorId;

    private final InlineQueryHandler inlineQueryHandler;
    private final CommandHandler commandHandler;
    private final TextMessageHandler textMessageHandler;
    private final WishService wishService;
    private final PictureService pictureService;
    private final WishRepository wishRepository;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasInlineQuery()) {
            registerNewUser(update.getInlineQuery().getFrom());
            try {
                executeAsync(inlineQueryHandler.handle(update));
            } catch (TelegramApiException e) {
                log.error("Telegram integration error: {}", e.getMessage());
            }
        }

        if (update.getMessage() != null) {
            registerNewUser(update.getMessage().getFrom());
            var message = update.getMessage();

            if (message.isCommand()) {
                try {
                    executeAsync(commandHandler.handle(update));
                } catch (TelegramApiException e) {
                    log.error("Telegram integration error: {}", e.getMessage());
                }
            }

            if (message.getText() != null) {
                try {
                    var apiMethod = textMessageHandler.handle(update);
                    if (apiMethod != null) {
                        executeAsync(apiMethod);
                    }
                } catch (TelegramApiException e) {
                    log.error("Telegram integration error: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public String getBotUsername() {
        return this.username;
    }

    @Override
    public String getBotToken() {
        return this.token;
    }

    private void registerNewUser(User user) {
        if (userService.findByTelegramId(user.getId()) == null) {
            userService.create(InputUserDto.builder()
                    .roleKey(UserRoleKey.DEFAULT)
                    .telegramId(user.getId())
                    .build());
        }
    }

    @SneakyThrows
    @PostConstruct
    @Scheduled(cron = "${telegram.pictures.reset-file-id.cron}")
    public void generatePhotoUrl() {
        List<Wish> wishes;

        if (firstInit) {
            wishes = wishService.findAll();
        } else {
            wishes = wishService.linkToTelegram();
        }

        for (Wish wish : wishes) {
            File file = pictureService.generatePicture(wish);
            SendPhoto sendPhoto = SendPhoto.builder()
                    .photo(new InputFile(file))
                    .chatId(authorId)
                    .build();

            Message response = execute(sendPhoto);
            String fileId = response.getPhoto().getFirst().getFileId();
            wish.setLinkToTelegramPhoto(fileId);

            DeleteMessage deleteMessage = DeleteMessage.builder()
                    .messageId(response.getMessageId())
                    .chatId(authorId)
                    .build();
            execute(deleteMessage);

            file.delete();
        }

        wishRepository.saveAllAndFlush(wishes);
    }
}

