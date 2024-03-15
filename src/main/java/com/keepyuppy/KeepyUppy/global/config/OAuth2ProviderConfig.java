package com.keepyuppy.KeepyUppy.global.config;

import com.keepyuppy.KeepyUppy.security.oauth.OAuth2Provider;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "oauth")
public class OAuth2ProviderConfig {
    private Map<String, OAuth2Provider> providers;

    public Map<String, OAuth2Provider> getProviders() {
        return providers;
    }

    public void setProviders(Map<String, OAuth2Provider> providers) {
        this.providers = providers;
    }
}
