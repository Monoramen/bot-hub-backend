INSERT INTO command (id, command, response) VALUES
                                            (1,'/start', 'Добро пожаловать в бота'),
                                            (2,'/help', 'Список доступных команд: /start, /help, /info'),
                                            (3,'/info', 'Эта команда предоставляет информацию о боте'),
                                            (4,'/settings', 'Настройки бота: вы можете изменить параметры уведомлений');

INSERT INTO inline_keyboard (id, inline_keyboard_name) VALUES
    (1, 'links');

INSERT INTO inline_button (id, text, url, callback_data, switch_inline_query, row, position,inline_keyboard_id) VALUES
    (1, 'google', 'https://google.com','','',1, 1, 1),
    (2, 'reddit', 'https://www.reddit.com/','','', 2, 1, 1),
    (3, 'github', 'https://github.com/','','',2,2,1);


INSERT INTO attachment (type, command_id, inline_keyboard_id, keyboard_id) VALUES
    ('INLINE_KEYBOARD', 1, 1, null);