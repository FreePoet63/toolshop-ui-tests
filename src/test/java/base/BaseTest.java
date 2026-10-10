package base;

import com.codeborne.selenide.logevents.SelenideLogger;
import driver.DriverConfig;
import io.qameta.allure.Step;
import io.qameta.allure.selenide.AllureSelenide;
import listener.TestLifecycleListener;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import pages.HomePage;
import utils.AllureUtils;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;

@Listeners(TestLifecycleListener.class)
public abstract class BaseTest {

    protected HomePage homePage;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        initDriver();
        homePage = openHomePage();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if(result.getStatus() == ITestResult.FAILURE) {
            AllureUtils.attachFailureArtifacts(result.getName(), result.getThrowable());
        }
        closeBrowser();
    }

    @Step("Настраиваем драйвер и Allure-листенер")
    private void initDriver() {
        DriverConfig.setUp();
        SelenideLogger.addListener("AllureSelenide",
                new AllureSelenide().screenshots(true).savePageSource(true).includeSelenideSteps(false));
    }

    @Step("Открываем главную страницу practicesoftwaretesting.com")
    private HomePage openHomePage() {
        return open("/", HomePage.class);
    }

    @Step("Закрываем браузер")
    private void closeBrowser() {
        closeWebDriver();
    }
}