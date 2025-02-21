package com.monora.personalbothub.bot_db.entity.attachment.inlinekeyboard;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inline_button")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class InlineButtonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "url")
    private String url;

    @Column(name = "callback_data")
    private String callbackData;

    @Column(name = "switch_inline_query")
    private String switchInlineQuery;

    @Column(name = "row")
    private int row;

    @Column(name = "position")
    private int position;

    @ManyToOne
    @JoinColumn(name = "inline_keyboard_id", nullable = false)
    private InlineKeyboardEntity inlineKeyboard;
}