package me.hippy.prdsz.handler;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Обработчик входящих событий Telegram API, единственный метод возвращает
 * объект наследуемый от org.telegram.telegrambots.meta.api.methods.BotApiMethod
 * для дальнейшей передачи этого метода в telegram API.
 *
 * @author hippy
 */
public interface TelegramUpdateHandler {

    /**
     * Принимает входящее событие и после обработки возвращает
     * объект для передачи его в telegram API.
     *
     * @param update    - входящее событие от Telegram API.
     * @return метод-ответ из перечня методов Telegram API для дальнейшего его вызова
     */
    BotApiMethod handle(Update update);

}
