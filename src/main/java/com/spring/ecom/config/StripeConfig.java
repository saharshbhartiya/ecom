package com.spring.ecom.config;

import com.stripe.StripeClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
public class StripeConfig {

    @Bean
    public StripeClient stripeClient(@Value("${stripe.secretKey:}") String rawSecretKey) {
        var secretKey = rawSecretKey == null ? "" : rawSecretKey.trim();

        if (!StringUtils.hasText(secretKey)) {
            throw new IllegalStateException(
                    "Stripe secret key is missing. Set STRIPE_SECRET_KEY in your environment or .env file."
            );
        }

        if (secretKey.startsWith("${") || "stripe.secretKey".equals(secretKey)) {
            throw new IllegalStateException(
                    "Stripe secret key was not resolved. Check STRIPE_SECRET_KEY and how your application loads .env."
            );
        }

        if (secretKey.startsWith("pk_")) {
            throw new IllegalStateException(
                    "Stripe secret key must be a server-side secret key, not a publishable key."
            );
        }

        return new StripeClient(secretKey);
    }
}
