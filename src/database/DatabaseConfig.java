package database;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class DatabaseConfig {
    private static final Properties props = new Properties();

    static {
        try (InputStream in = Files.newInputStream(Paths.get("src/database/db.properties"))) {
            props.load(in);
        } catch (IOException e) {
            throw new ExceptionInInitializerError("Failed to load database properties: " + e.getMessage());
        }
    }

    public static String getUrl() {
        return props.getProperty("jdbc.url");
    }
    public static String getUsername() {
        return props.getProperty("jdbc.username");
    }
    public static String getPassword() {
        return props.getProperty("jdbc.password");
    }
}