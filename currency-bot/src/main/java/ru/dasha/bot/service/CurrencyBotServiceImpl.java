package ru.dasha.bot.service;

import java.io.StringReader;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import ru.dasha.bot.client.CbrClient;
import ru.dasha.bot.exception.ServiceException;

@Service
public class CurrencyBotServiceImpl implements CurrencyBotService{
	
	private static final Logger log = LoggerFactory.getLogger(CurrencyBotServiceImpl.class);
	
	private static final String USD_XPATH = "/ValCurs//Valute[@ID='R01235']/Value";
    private static final String EUR_XPATH = "/ValCurs//Valute[@ID='R01239']/Value";
    private static final String CNY_XPATH = "/ValCurs//Valute[@ID='R01375']/Value";
	
	@Autowired
	private CbrClient client;

	@Cacheable(value="usd", unless="#result == null or #result.isEmpty()")
	@Override
	public String getUSDExchangeRate() throws ServiceException {
		log.info("Getting USD rates");
		var xml = client.getCurrencyRatesXml();
		return extractCurrencyValueFromXml(xml, USD_XPATH);
	}

	@Cacheable(value="eur", unless="#result == null or #result.isEmpty()")
	@Override
	public String getEURExchangeRate() throws ServiceException {
		log.info("Getting EUR rates");
		var xml = client.getCurrencyRatesXml();
		return extractCurrencyValueFromXml(xml, EUR_XPATH);
	}

	@Cacheable(value="cny", unless="#result == null or #result.isEmpty()")
	@Override
	public String getCNYExchangeRate() throws ServiceException {
		log.info("Getting CNY rates");
		var xml = client.getCurrencyRatesXml();
		return extractCurrencyValueFromXml(xml, CNY_XPATH);
	}
	
	@CacheEvict(value="usd")
	@Override
	public void clearUSDCache() {
		log.info("USD Cache is cleared");
	}
	
	@CacheEvict(value="eur")
	@Override
	public void clearEURCache() {
		log.info("EUR Cache is cleared");
	}
	
	@CacheEvict(value="cny")
	@Override
	public void clearCNYCache() {
		log.info("CNY Cache is cleared");
	}
	
	private static String extractCurrencyValueFromXml(String xml, String xpathExpression) throws ServiceException {
		var source = new InputSource(new StringReader(xml));
		try {
			var xpath = XPathFactory.newInstance().newXPath();
			var document = (Document) xpath.evaluate("/", source, XPathConstants.NODE);
			return xpath.evaluate(xpathExpression, document);
		} catch (XPathExpressionException e) {
			throw new ServiceException("Failed parsing XML", e);
		}
	}
}
