package com.monora.personalbothub.bot_db.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "inline_button")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class InlineButtonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "url")
    private String url;

    @Column(name = "callback_data")
    private String callbackData;

    @Column(name = "switch_inline_query")
    private String switchInlineQuery;

    @ManyToMany(mappedBy = "buttons")
    private List<InlineKeyboardEntity> inlineKeyboards;
}
