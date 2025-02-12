package com.monora.personalbothub.bot_db.entity;

import com.monora.personalbothub.bot_db.entity.attachment.AttachmentEntity;
import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Table(name = "command")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CommandEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "command", nullable = false)
    private String command;

    @Column(name = "response")
    private String response;

    @OneToMany(mappedBy = "command", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AttachmentEntity> attachments;
}
