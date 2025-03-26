package me.hippy.prdsz;

import me.hippy.prdsz.config.BotConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
@EnableConfigurationProperties
public class Main {

    public static void main(String[] args) throws TelegramApiException {
        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        BotConfiguration botConfiguration = context.getBean("botConfigurationPrimary", BotConfiguration.class);
        botsApi.registerBot(botConfiguration);
    }
}