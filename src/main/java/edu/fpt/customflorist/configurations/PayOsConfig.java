package edu.fpt.customflorist.configurations;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class PayOsConfig {
    @Value("${PayOS.client-id}")
    private String clientId;

    @Value("${PayOS.api-key}")
    private String apiKey;

    @Value("${PayOS.checksum-key}")
    private String checksumKey;

    @Value("${PayOS.url}")
    private String payOsUrl;

    @Value("${PayOS.return-url}")
    private String returnUrl;

    @Value("${PayOS.cancel-url}")
    private String cancelUrl;


}
