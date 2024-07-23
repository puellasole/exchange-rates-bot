package ru.dasha.bot.client;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import ru.dasha.bot.exception.ServiceException;

@Component
public class CbrClient {
	
	@Autowired
	private OkHttpClient client;
	
	@Value("${cbr.currency.rates.xml}")
	private String url;
	
	public String getCurrencyRatesXml() throws ServiceException{
		var request = new Request.Builder()
				.url(url)
				.build();
		try (var response = client.newCall(request).execute();){
			var body = response.body();
			return body == null ? null : body.string();
		} catch (IOException e) {
			throw new ServiceException("Failed getting currency rates", e);
		}
	}
}
