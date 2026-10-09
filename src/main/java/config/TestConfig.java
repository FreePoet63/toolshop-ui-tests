package config;

public final class TestConfig {
    private TestConfig() {}

    public static final String BASE_URL = System.getProperty("base.url", "https://practicesoftwaretesting.com");
    public static final String BROWSER = System.getProperty("browser", "chrome");
    public static final boolean HEADLESS = Boolean.parseBoolean(System.getProperty("headless", "false"));
    public static final long TIMEOUT_MS = Long.parseLong(System.getProperty("timeout", "10000"));
}