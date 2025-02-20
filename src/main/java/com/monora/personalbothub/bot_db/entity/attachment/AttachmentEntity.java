package com.monora.personalbothub.bot_db.entity.attachment;

import com.monora.personalbothub.bot_db.entity.CommandEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "attachment")
@DiscriminatorColumn(name = "type")
@Getter
@Setter
public abstract class AttachmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", insertable = false, updatable = false)
    private String type;

    @ManyToOne
    @JoinColumn(name = "command_id", nullable = false)
    private CommandEntity command;  // Добавляем связь с командой
}
