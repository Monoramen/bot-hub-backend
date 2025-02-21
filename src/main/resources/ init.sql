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

INSERT INTO keyboard (id, keyboard_name,one_time_keyboard, resize_keyboard,selective ) VALUES
    (1, 'keys', true, true, false);

INSERT INTO button(id, text, keyboard_id, position, request_contact,request_location,row) VALUES
    (1 ,'text', 1, 1, false, false, 1),
(2 ,'text2', 1, 2, false, false, 1);

INSERT INTO attachment (type, command_id, inline_keyboard_id, keyboard_id) VALUES
    ('KEYBOARD', 2, null, 1);

SELECT setval('button_id_seq', (SELECT MAX(id) FROM button));