package me.hippy.prdsz.handler;

import lombok.RequiredArgsConstructor;
import me.hippy.prdsz.model.Wish;
import me.hippy.prdsz.service.WishService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.cached.InlineQueryResultCachedPhoto;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InlineQueryHandler implements TelegramUpdateHandler {

    private final WishService wishService;

    @Override
    public AnswerInlineQuery handle(Update update) {
        return AnswerInlineQuery.builder()
                .inlineQueryId(update.getInlineQuery().getId())
                .cacheTime(0)
                .results(getQueryResults(update.getInlineQuery().getFrom().getUserName()))
                .isPersonal(true)
                .build();
    }

    private List<InlineQueryResult> getQueryResults(String username) {
        List<InlineQueryResult> results = new ArrayList<>();
        Wish wish = wishService.findRandom();
        String content = wish.getContent();
        String linkToTelegramPhoto = wish.getLinkToTelegramPhoto();

        if (linkToTelegramPhoto != null) {
            InlineQueryResultCachedPhoto cachedPhoto = InlineQueryResultCachedPhoto.builder()
                    .id(String.valueOf(1))
                    .photoFileId(wish.getLinkToTelegramPhoto())
                    .title("Предсказание")
                    .description("Получить предсказание")
                    .caption("Предсказание для @" + username)
                    .build();

            results.add(cachedPhoto);
            return results;
        } else {
            InlineQueryResultArticle article = new InlineQueryResultArticle(
                    Integer.toString(1),
                    "Предсказание",

                    InputTextMessageContent
                            .builder()
                            .disableWebPagePreview(true)
                            .parseMode(ParseMode.MARKDOWN)
                            .messageText("**Предсказание для **@" + username + "\n\n" + content)
                            .build()
            );

            article.setDescription("Предсказание");
            results.add(article);
        }

        return results;
    }

}
