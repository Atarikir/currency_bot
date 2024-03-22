CREATE TABLE IF NOT EXISTS subscribers
(
    id                uuid   PRIMARY KEY,
    chat_id           bigint NOT NULL UNIQUE,
    coin_price        bigint,
    last_message_time timestamp
);

COMMENT ON TABLE  subscribers                   IS 'подписчики телеграм-бота';
COMMENT ON COLUMN subscribers.id                IS 'идентификатор пользователя';
COMMENT ON COLUMN subscribers.chat_id           IS 'ID пользователя в Telegram';
COMMENT ON COLUMN subscribers.coin_price        IS 'цена криптовалюты, на которую подписан пользователь';
COMMENT ON COLUMN subscribers.last_message_time IS 'время отправки последнего сообщения пользователю о низкой цене';