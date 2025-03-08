package com.monora.personalbothub.bot_db.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "telegram_users")
public class TelegramUser {
    @Id
    private Long id; // Unique identifier for the user

    private String firstName; // User's first name
    private String lastName;  // User's last name
    private String username;  // Telegram username
    private String photoUrl;  // URL of the user's profile photo
    private Instant authDate; // Date of authentication
    private String hash;      // Hash for data validation
}