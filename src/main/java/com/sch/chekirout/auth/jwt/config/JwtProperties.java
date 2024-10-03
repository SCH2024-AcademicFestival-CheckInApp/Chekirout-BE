package com.sch.chekirout.auth.jwt.config;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration

@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String issuer;
    private String secret_key;

    @PostConstruct
    public void init() {
        System.out.println("Issuer: " + issuer);
        System.out.println("Secret Key: " + secret_key);
    }


    // Getter & Setter
    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getSecret_key() {
        return secret_key;
    }

    public void setSecret_key(String secret_key) {
        this.secret_key = secret_key;
    }
}
