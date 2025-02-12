package com.monora.personalbothub.bot_impl.mapper;

import com.monora.personalbothub.bot_api.controller.InlineButtonController;
import com.monora.personalbothub.bot_api.dto.request.CommandRequestDTO;
import com.monora.personalbothub.bot_api.dto.response.CommandResponseDTO;
import com.monora.personalbothub.bot_db.entity.CommandEntity;
import com.monora.personalbothub.bot_db.entity.attachment.AttachmentEntity;
import com.monora.personalbothub.bot_db.repository.AttachmentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = AttachmentMapper.class)
public interface CommandMapper {

    AttachmentRepository attachmentRepository = null;

    @Mapping(target = "attachments", source = "attachmentIds")
    CommandEntity toEntity(CommandRequestDTO commandRequestDTO);

    @Mapping(target = "attachmentIds", source = "attachments")
    CommandResponseDTO toResponseDTO(CommandEntity commandEntity);

    List<CommandResponseDTO> toResponseDTOList(List<CommandEntity> commandEntityList);

    // Преобразуем Set<Long> attachmentIds в Set<AttachmentEntity>
    default Set<AttachmentEntity> mapAttachmentIds(Set<Long> attachmentIds) {

        return attachmentIds.stream()
                .map(id -> attachmentRepository.findById(id) // Получаем AttachmentEntity по ID
                        .orElseThrow(() -> new EntityNotFoundException("Attachment not found for ID: " + id)))
                .collect(Collectors.toSet());
    }

    // Преобразуем Set<AttachmentEntity> в Set<Long>
    default Set<Long> mapAttachmentsToIds(Set<AttachmentEntity> attachments) {
        return attachments.stream()
                .map(AttachmentEntity::getId)
                .collect(Collectors.toSet());
    }
}
