package com.monora.personalbothub.bot_db.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inline_button")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
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

    @Column(name = "login_url")
    private String loginUrl;


}
