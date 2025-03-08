package com.monora.personalbothub.bot_api.controller;

import com.monora.personalbothub.bot_impl.configuration.TelegramBotConfig;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

@RestController
@RequestMapping("/auth/telegram")
@AllArgsConstructor

public class TelegramAuthController {
    private final TelegramBotConfig telegramBotConfig;

    @GetMapping
    public ResponseEntity<Resource> getAuthScript() {
        Resource resource = (Resource) new ClassPathResource("static/tgAuth.html");
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=tgAuth.html");
        return ResponseEntity.ok().headers(headers).body(resource);
    }

    @PostMapping("/token")
    public String authenticate(@RequestBody Map<String, Object> telegramData) {
        return telegramDataIsValid(telegramData) ? "pretend-that-it-is-your-token" : "error";
    }

    /**
     * checks data received from telegrams
     */
    private boolean telegramDataIsValid(Map<String, Object> telegramData) {
        //get a hash, which we will later compare with the rest of the data
        String hash = (String) telegramData.get("hash");
        telegramData.remove("hash");

        //create a verification line - sort all the parameters and combine them into a line like:
        //auth_date=<auth_date>\nfirst_name=<first_name>\nid=<id>\nusername=<username>
        StringBuilder sb = new StringBuilder();
        telegramData.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> sb.append(entry.getKey()).append("=").append(entry.getValue()).append("\n"));
        sb.deleteCharAt(sb.length() - 1);
        String dataCheckString = sb.toString();

        try {
            //generate a SHA-256 hash from the bot's token
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] key = digest.digest(telegramBotConfig.getToken().getBytes(UTF_8));

            //create an HMAC with the generated hash
            Mac hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "HmacSHA256");
            hmac.init(secretKeySpec);

            //add a verification line to HMAC and convert it to hexadecimal format
            byte[] hmacBytes = hmac.doFinal(dataCheckString.getBytes(UTF_8));
            StringBuilder validateHash = new StringBuilder();
            for (byte b : hmacBytes) {
                validateHash.append(String.format("%02x", b));
            }

            //compare the hash received from telegram and the generated hash
            return hash.contentEquals(validateHash);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
