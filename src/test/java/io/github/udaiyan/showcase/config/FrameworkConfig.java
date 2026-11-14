package io.github.udaiyan.showcase.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads framework.properties from the classpath, with system-property overrides
 * so CI and local runs can reconfigure without code changes (-Dbrowser=firefox).
 */
public class FrameworkConfig {

    private final Properties properties = new Properties();

    public FrameworkConfig() {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("framework.properties")) {
            if (in == null) {
                throw new IllegalStateException("framework.properties not found on test classpath");
            }
            properties.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load framework.properties", e);
        }
    }

    private String get(String key) {
        return System.getProperty(key, properties.getProperty(key));
    }

    public String baseUrl()          { return get("base.url"); }
    public String browser()          { return get("browser"); }
    public boolean headless()        { return Boolean.parseBoolean(get("headless")); }
    public double defaultTimeoutMs() { return Double.parseDouble(get("default.timeout.ms")); }
}
