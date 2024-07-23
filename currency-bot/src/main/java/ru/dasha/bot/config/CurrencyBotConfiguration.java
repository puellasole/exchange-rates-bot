package ru.dasha.bot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import okhttp3.OkHttpClient;
import ru.dasha.bot.CurrencyBot;

@Configuration
public class CurrencyBotConfiguration {
	
	@Bean
	public OkHttpClient okHttpClient() {
		return new OkHttpClient();
	}
	
	@Bean
	public TelegramBotsApi telegramBotsApi(CurrencyBot currencyBot) throws TelegramApiException{
		var api = new TelegramBotsApi(DefaultBotSession.class);
		api.registerBot(currencyBot);
		return api;
	}
}