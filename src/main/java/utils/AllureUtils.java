package utils;

import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.logging.LogEntry;

import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.screenshot;

public final class AllureUtils {
    private AllureUtils() {}

    @Attachment(value = "Screenshot", type = "image/png", fileExtension = ".png")
    public static byte[] attachScreenshot() {
        return screenshot(OutputType.BYTES);
    }

    @Attachment(value = "Page Source", type = "text/html", fileExtension = ".html")
    public static String attachPageSource() {
        return WebDriverRunner.getWebDriver().getPageSource();
    }

    @Attachment(value = "Browser Logs", type = "text/plain")
    public static String attachBrowserLogs() {
        try {
            return WebDriverRunner.getWebDriver()
                    .manage()
                    .logs()
                    .get("browser")
                    .getAll()
                    .stream()
                    .map(LogEntry::toString)
                    .collect(Collectors.joining("\n"));
        } catch (Exception e) {
            return "Browser logs not available: " + e.getMessage();
        }
    }

    @Attachment(value = "Pretty DOM", type = "text/html", fileExtension = ".html")
    public static String attachPrettyDom() {
        try {
            String html = WebDriverRunner.getWebDriver().getPageSource();
            return html.replace("><", ">\n<");
        } catch (Exception e) {
            return "Pretty DOM not available: " + e.getMessage();
        }
    }

    @Attachment(value = "Failure Reason", type = "text/plain", fileExtension = ".txt")
    public static String attachFailureReason(String reason) {
        return reason;
    }

    public static void attachFailureArtifacts(String testName, Throwable error) {
        attachScreenshot();
        attachPageSource();
        attachBrowserLogs();
        attachPrettyDom();
        attachFailureReason(error.getMessage());

        Allure.addAttachment("Test Name", "text/plain", testName, ".txt");
    }
}