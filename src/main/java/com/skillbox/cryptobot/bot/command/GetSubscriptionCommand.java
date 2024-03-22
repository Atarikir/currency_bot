package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.data.model.Subscriber;
import com.skillbox.cryptobot.service.SubscriberService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Service
@Slf4j
@AllArgsConstructor
public class GetSubscriptionCommand implements IBotCommand {

  private final SubscriberService subscriberService;

  @Override
  public String getCommandIdentifier() {
    return "get_subscription";
  }

  @Override
  public String getDescription() {
    return "Возвращает текущую подписку";
  }

  @Override
  public void processMessage(AbsSender absSender, Message message, String[] arguments) {
    SendMessage answer = new SendMessage();
    answer.setChatId(message.getChatId());
    try {
      Long telegramUserId = message.getFrom().getId();
      Subscriber subscriber = subscriberService.getSubscriberByChatId(telegramUserId);
      if (subscriber.getCoinPrice() == null) {
        answer.setText("Активные подписки отсутствуют");
      } else {
        answer.setText(
            String.format("Вы подписаны на стоимость биткоина %d USD", subscriber.getCoinPrice()));
      }
      absSender.execute(answer);
    } catch (Exception e) {
      log.error("Ошибка возникла /get_subscription методе", e);
    }
  }
}
