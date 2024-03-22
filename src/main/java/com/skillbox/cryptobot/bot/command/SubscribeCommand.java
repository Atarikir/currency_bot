package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.data.model.Subscriber;
import com.skillbox.cryptobot.service.CryptoCurrencyService;
import com.skillbox.cryptobot.service.SubscriberService;
import com.skillbox.cryptobot.utils.TextUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

/** Обработка команды подписки на курс валюты */
@Service
@Slf4j
@AllArgsConstructor
public class SubscribeCommand implements IBotCommand {

  private final CryptoCurrencyService service;
  private final SubscriberService subscriberService;

  @Override
  public String getCommandIdentifier() {
    return "subscribe";
  }

  @Override
  public String getDescription() {
    return "Подписывает пользователя на стоимость биткоина";
  }

  @Override
  public void processMessage(AbsSender absSender, Message message, String[] arguments) {
    SendMessage answer1 = new SendMessage();
    answer1.setChatId(message.getChatId());
    SendMessage answer2 = new SendMessage();
    answer2.setChatId(message.getChatId());
    try {
      Long telegramUserId = message.getFrom().getId();
      if(arguments.length == 1 && arguments[0].matches("\\d+")) {
        Long subscriptionPrice = Long.valueOf(arguments[0].trim());
        Subscriber subscriber = subscriberService.getSubscriberByChatId(telegramUserId);
        subscriber.setCoinPrice(subscriptionPrice);
        subscriberService.save(subscriber);
        answer1.setText(
                String.format(
                        "Текущая цена биткоина %s USD", TextUtil.toString(service.getBitcoinPrice())));
        answer2.setText(
                String.format("Новая подписка создана на стоимость %d USD", subscriptionPrice));
        absSender.execute(answer1);
      } else {
        answer2.setText("Некорректный ввод. Введите стоимость подписки цифрами без пробелов, букв и символов.");
      }
      absSender.execute(answer2);
    } catch (Exception e) {
      log.error("Ошибка возникла /subscribe методе", e);
    }
  }
}
