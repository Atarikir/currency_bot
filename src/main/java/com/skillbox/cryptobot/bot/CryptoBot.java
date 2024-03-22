package com.skillbox.cryptobot.bot;

import com.skillbox.cryptobot.data.model.Subscriber;
import com.skillbox.cryptobot.service.SubscriberService;
import com.skillbox.cryptobot.utils.TextUtil;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@Slf4j
public class CryptoBot extends TelegramLongPollingCommandBot {

  private final String botUsername;

  @Value("${telegram.bot.notify.delay.value}")
  private Long notifyDelay;

  private final SubscriberService subscriberService;

  public CryptoBot(
      @Value("${telegram.bot.token}") String botToken,
      @Value("${telegram.bot.username}") String botUsername,
      List<IBotCommand> commandList,
      SubscriberService subscriberService) {
    super(botToken);
    this.botUsername = botUsername;
    this.subscriberService = subscriberService;
    commandList.forEach(this::register);
  }

  @Override
  public String getBotUsername() {
    return botUsername;
  }

  @Override
  public void processNonCommandUpdate(Update update) {}

  public void sendMessageToSubscribers(double bitcoinPrice) {
    List<Subscriber> subscribers = subscriberService.allSubscribers();
    for (Subscriber subscriber : subscribers) {
      if (this.checkSendMessage(subscriber, bitcoinPrice)) {
        this.prepareAndSendMessage(subscriber, bitcoinPrice);
      }
    }
  }

  private boolean checkSendMessage(Subscriber subscriber, double bitcoinPrice) {
    return subscriber.getCoinPrice() != null
        && subscriber.getCoinPrice() > bitcoinPrice
        && subscriber.getLastMessageTime().isBefore(LocalDateTime.now().minusMinutes(notifyDelay));
  }

  private void prepareAndSendMessage(Subscriber subscriber, double bitcoinPrice) {
    String textToSend =
        String.format("Пора покупать, стоимость биткоина %s USD", TextUtil.toString(bitcoinPrice));
    SendMessage message = new SendMessage();
    message.setChatId(String.valueOf(subscriber.getChatId()));
    message.setText(textToSend);
    this.executeMessage(message);
    subscriber.setLastMessageTime(LocalDateTime.now());
    subscriberService.save(subscriber);
  }

  private void executeMessage(SendMessage message) {
    try {
      execute(message);
    } catch (TelegramApiException e) {
      log.error(e.getMessage());
    }
  }
}
