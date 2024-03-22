package com.skillbox.cryptobot.service;

import static java.util.concurrent.TimeUnit.MINUTES;

import com.skillbox.cryptobot.bot.CryptoBot;
import com.skillbox.cryptobot.utils.TextUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class Scheduler {

  private final CryptoBot cryptoBot;
  private final CryptoCurrencyService cryptoCurrencyService;

  @Scheduled(fixedDelayString = "${telegram.bot.check.actual.currency.value}", timeUnit = MINUTES)
  public void bitcoinPrice() {
    try {
      double bitcoinPrice = cryptoCurrencyService.getBitcoinPrice();
      log.info(String.format("Текущая цена биткоина %s USD", TextUtil.toString(bitcoinPrice)));
      cryptoBot.sendMessageToSubscribers(bitcoinPrice);
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }
}
