package driver;

import com.codeborne.selenide.Configuration;

import static config.TestConfig.*;

public final class DriverConfig {
    private DriverConfig() {}

    public static void setUp() {
        Configuration.browser = BROWSER;
        Configuration.timeout = TIMEOUT_MS;
        Configuration.baseUrl = BASE_URL;
        Configuration.headless = HEADLESS;
        Configuration.browserSize = "1920x1080";
        Configuration.pageLoadStrategy = "eager";
    }
}