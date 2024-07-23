package ru.dasha.bot.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import ru.dasha.bot.service.CurrencyBotService;

@Component
public class CacheInvalidationInvoker {
	
	@Autowired
	private CurrencyBotService service;

	@Scheduled(cron = "* 0 0 * * ?")
	public void invalidateCache() {
		service.clearUSDCache();
		service.clearEURCache();
		service.clearCNYCache();
	}
}
