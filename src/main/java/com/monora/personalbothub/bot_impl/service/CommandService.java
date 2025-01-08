package com.monora.personalbothub.bot_impl.service;

import com.monora.personalbothub.bot_db.entity.CommandEntity;

public interface CommandService {

    CommandEntity  findCommand(String command);

    void addCommand(CommandEntity command);


}
