package com.monora.personalbothub.bot_impl.service;

import com.monora.personalbothub.bot_api.dto.request.CommandRequestDTO;
import com.monora.personalbothub.bot_api.dto.response.CommandResponseDTO;

import java.util.List;

public interface CommandService {

    CommandResponseDTO findByCommand(String command);

    List<CommandResponseDTO> findAll();

    CommandResponseDTO findById(Long id);

    void create(CommandRequestDTO commandRequestDTO);

    void update(Long id, CommandRequestDTO commandRequestDTO);

    void delete(Long id);


}
