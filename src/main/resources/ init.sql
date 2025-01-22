INSERT INTO command (id, command, response) VALUES
                                            (1,'/start', 'Добро пожаловать в бота'),
                                            (2,'/help', 'Список доступных команд: /start, /help, /info'),
                                            (3,'/info', 'Эта команда предоставляет информацию о боте'),
                                            (4,'/settings', 'Настройки бота: вы можете изменить параметры уведомлений');

INSERT INTO inline_button (id, text, url, callback_data, switch_inline_query) VALUES
    (1, 'google', 'https://google.com','',''),
    (2, 'reddit', 'https://www.reddit.com/','',''),
    (3, 'github', 'https://github.com/','','');

INSERT INTO inline_keyboard (id, inline_keyboard_name) VALUES
                                                           (1, 'links');

INSERT INTO inline_keyboard_button (inline_keyboard_id, inline_button_id) VALUES
    (1, 1),
    (1, 2),
    (1, 3);

INSERT INTO command_inline_keyboard(command_id, inline_keyboard_id) VALUES
    (1, 1);

UPDATE command
SET inline_keyboard_id = 1
WHERE id = 1;

SELECT * FROM command WHERE command = '/start';