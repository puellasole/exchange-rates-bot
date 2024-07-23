package ru.dasha.bot.service;

import java.io.StringReader;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import ru.dasha.bot.client.CbrClient;
import ru.dasha.bot.exception.ServiceException;

@Service
public class CurrencyBotServiceImpl implements CurrencyBotService{
	
	private static final String USD_XPATH = "/ValCurs//Valute[@ID='R01235']/Value";
    private static final String EUR_XPATH = "/ValCurs//Valute[@ID='R01239']/Value";
    private static final String CNY_XPATH = "/ValCurs//Valute[@ID='R01375']/Value";
	
	@Autowired
	private CbrClient client;

	@Override
	public String getUSDExchangeRate() throws ServiceException {
		var xml = client.getCurrencyRatesXml();
		return extractCurrencyValueFromXml(xml, USD_XPATH);
	}

	@Override
	public String getEURExchangeRate() throws ServiceException {
		var xml = client.getCurrencyRatesXml();
		return extractCurrencyValueFromXml(xml, EUR_XPATH);
	}

	@Override
	public String getCNYExchangeRate() throws ServiceException {
		var xml = client.getCurrencyRatesXml();
		return extractCurrencyValueFromXml(xml, CNY_XPATH);
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
