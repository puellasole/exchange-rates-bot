package ru.dasha.bot.bot;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import ru.dasha.bot.exception.ServiceException;
import ru.dasha.bot.service.CurrencyBotService;

@Component
public class CurrencyBot extends TelegramLongPollingBot{
	
	private static Logger log = LoggerFactory.getLogger(CurrencyBot.class);
	
	private static final String START = "/start";
	private static final String USD = "/usd";
	private static final String EUR = "/eur";
	private static final String CNY = "/cny";
	private static final String HELP = "/help";
	 
	@Autowired
	private CurrencyBotService currencyBotService;
	
	public CurrencyBot(@Value("${bot.token}") String botToken) {
		super(botToken);
	}

	@Override
	public void onUpdateReceived(Update update) {
		if(!update.hasMessage() || !update.getMessage().hasText()) {
			return;
		}
		var message = update.getMessage().getText();
		var chatId = update.getMessage().getChatId();
		switch(message) {
			case START -> {
				String userName = update.getMessage().getChat().getFirstName();
				startCommand(chatId, userName);
			}
			case USD -> usdCommand(chatId);
			case EUR -> eurCommand(chatId);
			case CNY -> cnyCommand(chatId);
			case HELP -> helpCommand(chatId);
			default -> unknownCommand(chatId);
		}
	}

	@Override
	public String getBotUsername() {
		return "byarias_bot";
	}
	
	private void startCommand(Long chatId, String userName) {
		var text = """
				Welcome, %s!
				
				Enter the following commands to get currency rates:
				/usd - usd exchange rate
				/eur - eur exchange rate
				/cny - cny exchange rate
				
				Additional commands:
				/help
				""";
		var formattedText = String.format(text, userName);
		sendMessage(chatId, formattedText);
	}
	
	private void usdCommand(Long chatId) {
        String formattedText;
        try {
            var usd = currencyBotService.getUSDExchangeRate();
            var text = "The dollar exchange rate for %s is %s rubles";
            formattedText = String.format(text, LocalDate.now(), usd);
        } catch (ServiceException e) {
            log.error("Failed getting dollar rate", e);
            formattedText = "Coudn't get the current dollar rate. Please try again later.";
        }
        sendMessage(chatId, formattedText);
    }

    private void eurCommand(Long chatId) {
        String formattedText;
        try {
            var eur = currencyBotService.getEURExchangeRate();
            var text = "The euro exchange rate for %s is %s rubles";
            formattedText = String.format(text, LocalDate.now(), eur);
        } catch (ServiceException e) {
            log.error("Failed getting euro rate", e);
            formattedText = "Coudn't get the current euro rate. Please try again later.";
        }
        sendMessage(chatId, formattedText);
    }
    
    private void cnyCommand(Long chatId) {
        String formattedText;
        try {
            var cny = currencyBotService.getCNYExchangeRate();
            var text = "The yuan exchange rate for %s is %s rubles";
            formattedText = String.format(text, LocalDate.now(), cny);
        } catch (ServiceException e) {
            log.error("Failed getting yuan rate", e);
            formattedText = "Coudn't get the current yuan rate. Please try again later.";
        }
        sendMessage(chatId, formattedText);
    }

    private void helpCommand(Long chatId) {
        var text = """
                Bot Information
                
                To get current exchange rates, use the commands:
                /usd - dollar exchange rate
                /eur - euro exchange rate
                /cny - yuan exchange rate
                """;
        sendMessage(chatId, text);
    }

    private void unknownCommand(Long chatId) {
        var text = "I don't know this command :(";
        sendMessage(chatId, text);
    }
	
	private void sendMessage(Long chatId, String text) {
		var chatIdStr = String.valueOf(chatId);
		var sendMessage = new SendMessage(chatIdStr, text);
		try {
			execute(sendMessage);
		} catch (TelegramApiException e) {
			log.error("Failed sending message");
		}
	}
}
