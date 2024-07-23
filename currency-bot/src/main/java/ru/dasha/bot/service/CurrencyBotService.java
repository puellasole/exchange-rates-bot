package ru.dasha.bot.service;

import ru.dasha.bot.exception.ServiceException;

public interface CurrencyBotService {
	
	String getUSDExchangeRate() throws ServiceException;
	String getEURExchangeRate() throws ServiceException;
	String getCNYExchangeRate() throws ServiceException;
	
	void clearUSDCache();
	void clearEURCache();
	void clearCNYCache();
}
