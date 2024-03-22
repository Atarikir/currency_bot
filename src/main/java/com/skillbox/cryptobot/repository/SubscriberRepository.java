package com.skillbox.cryptobot.repository;

import com.skillbox.cryptobot.data.model.Subscriber;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriberRepository extends JpaRepository<Subscriber, UUID> {

  Optional<Subscriber> findByChatId(Long chatId);
}
