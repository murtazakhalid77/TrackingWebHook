package com.example.trackingbot.service.ServiceImpl;

import com.example.trackingbot.service.WebhookService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class WebhookServiceImpl implements WebhookService {
    @Override
    public String getTrackingDate(){
        String formattedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return formattedDate;

    }

}
