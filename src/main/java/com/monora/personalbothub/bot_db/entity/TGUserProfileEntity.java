package com.monora.personalbothub.bot_db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tg_user_profile")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TGUserProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private Long telegramId; // ID пользователя Telegram
    private String firstName; // Имя пользователя
    private String lastName; // Фамилия пользователя
    private String userName; // Имя пользователя в Telegram
    private String languageCode; // Языковой код
}
