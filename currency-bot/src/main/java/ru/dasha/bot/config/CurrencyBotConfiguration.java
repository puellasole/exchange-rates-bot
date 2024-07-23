package ru.dasha.bot.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import ru.dasha.bot.bot.CurrencyBot;

@EnableCaching
@EnableScheduling
@Configuration
public class CurrencyBotConfiguration {
	
	HttpLoggingInterceptor log = new HttpLoggingInterceptor().setLevel(Level.BASIC);
	
	@Bean
	public OkHttpClient okHttpClient() {
		return new OkHttpClient.Builder()
				.addInterceptor(log)
				.build();
	}
	
	@Bean
	public TelegramBotsApi telegramBotsApi(CurrencyBot currencyBot) throws TelegramApiException{
		var api = new TelegramBotsApi(DefaultBotSession.class);
		api.registerBot(currencyBot);
		return api;
	}
}
