package test.login;

import api.UserApi;
import base.BaseTest;
import components.Field;
import io.qameta.allure.*;
import model.TestUser;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.LoginPage;

@Epic("practicesoftwaretesting.com")
@Feature("Login")
@Link(name = "Toolshop", url = "https://practicesoftwaretesting.com/")
public class LoginTest extends BaseTest {
    private static final String INVALID_CREDENTIALS = "Invalid email or password";

    private TestUser createUser() {
        return UserApi.registerRandom();
    }

    @Test
    @Story("Successful login")
    @Severity(SeverityLevel.BLOCKER)
    @Description("A registered user logs in and sees the account page with their name in the header")
    public void shouldLoginWithRegisteredUser() {
        var user = createUser();

        var account = LoginPage.open().loginAs(user);

        account.shouldBeOpened()
                .header().shouldShowUser(user.firstName() + " " + user.lastName());
    }

    @Test
    @Story("Wrong credentials")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Logging in with a wrong password is rejected")
    public void shouldRejectWrongPassword() {
        var user = createUser();

        LoginPage.open()
                .submit(user.email(), "WrongPassw0rd!")
                .shouldShowFormError(INVALID_CREDENTIALS);
    }

    @Test
    @Story("Wrong credentials")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Logging in with an email that is not registered is rejected")
    public void shouldRejectUnknownEmail() {
        LoginPage.open()
                .submit("nobody" + System.currentTimeMillis() + "@example.com","SomePassw0rd!")
                .shouldShowFormError(INVALID_CREDENTIALS);
    }

    @DataProvider(name = "requiredFields")
    public Object[][] requiredFields() {
        return new Object[][] {
                {Field.EMAIL},
                {Field.PASSWORD}
        };
    }

    @Test(dataProvider = "requiredFields")
    @Story("Required fields")
    @Severity(SeverityLevel.NORMAL)
    @Description("Submitting an empty login form shows a vslidation error under every field")
    public void shouldShowErrorForEmptyField(Field field) {
        var page = LoginPage.open().submitEmpty();

        page.form().field(field).shouldShowError();
    }

    @Test()
    @Story("Logout")
    @Severity(SeverityLevel.NORMAL)
    @Description("A logged in user signs out and returns to the login page")
    public void shouldLogout() {
        var user = createUser();
        var account = LoginPage.open().loginAs(user);

        account.shouldBeOpened()
                .header().logout()
                .shouldBeOpened();
    }
}