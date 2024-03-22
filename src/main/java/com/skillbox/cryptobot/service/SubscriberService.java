package com.skillbox.cryptobot.service;

import com.skillbox.cryptobot.data.model.Subscriber;
import com.skillbox.cryptobot.repository.SubscriberRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class SubscriberService {

  private final SubscriberRepository repository;

  public Subscriber getSubscriberByChatId(Long chatId) {
    return repository.findByChatId(chatId).orElse(null);
  }

  @Transactional
  public void save(Subscriber subscriber) {
    repository.save(subscriber);
  }

  public List<Subscriber> allSubscribers() {
    return repository.findAll();
  }
}
