package test.registration;

import base.BaseTest;
import components.RegistrationForm.Field;
import io.qameta.allure.*;
import model.TestUser;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.RegisterPage;

@Epic("practicesoftwaretesting.com")
@Feature("Registration")
@Link(name = "Toolshop", url = "https://practicesoftwaretesting.com/")
public class RegistrationTest extends BaseTest {
    @Test
    @Story("Successful registration")
    @Severity(SeverityLevel.BLOCKER)
    @Description("A new user registers with valid data and is redirected to the login page")
    public void shouldRegisterNewUser() {
        RegisterPage.open()
                .register(TestUser.random())
                .shouldBeOpened();
    }

    @DataProvider(name = "requiredFields")
    public Object[][] requiredFields() {
        return new Object[][] {
                {Field.FIRST_NAME},
                {Field.LAST_NAME},
                {Field.DATE_OF_BIRTH},
                {Field.POSTAL_CODE},
                {Field.HOUSE_NUMBER},
                {Field.STREET},
                {Field.CITY},
                {Field.STATE},
                {Field.PHONE},
                {Field.EMAIL},
                {Field.PASSWORD}
        };
    }

    @Test(dataProvider = "requiredFields")
    @Story("Required fields")
    @Severity(SeverityLevel.NORMAL)
    @Description("Submitting an empty form shows a validation error under every required field")
    public void shouldShowErrorForEmptyRequiredField(Field field) {
        var page = RegisterPage.open().submitEmpty();

        page.form().field(field).shouldShowError();
    }

    @Test()
    @Story("Required fields")
    @Severity(SeverityLevel.NORMAL)
    @Description("Only the fields left empty show errors, filled fields stay clean")
    public void shouldNotShowErrorForFilledFields() {
        var user = TestUser.random();
        var page = RegisterPage.open();
        page.form().field(Field.EMAIL).type(user.email());
        page.form().field(Field.PASSWORD).type(user.password());

        page.form().submit();

        page.form().field(Field.EMAIL).shouldHaveNoError();
        page.form().field(Field.PASSWORD).shouldHaveNoError();
        page.form().field(Field.FIRST_NAME).shouldShowError();
    }

    @Test
    @Story("Email validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("An email in the wrong format is rejected")
    public void shouldRejectInvalidEmail() {
        var user = TestUser.random().withEmail("not-an-email");

        var page = RegisterPage.open().submit(user);

        page.form().field(Field.EMAIL).shouldHaveError("Email format is invalid");
        page.form().field(Field.PASSWORD).shouldHaveNoError();
    }

    @Test
    @Story("Passport validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("A weak password is rejected")
    public void shouldRejectWeakPassword() {
        var user = TestUser.random().withPassword("123");

        var page = RegisterPage.open().submit(user);

        page.form().field(Field.PASSWORD).shouldHaveError(" Password must be minimal 6 characters long.\n" +
                "Password can not include invalid characters. ");
        page.form().field(Field.EMAIL).shouldHaveNoError();
    }

    @Test
    @Story("Duplicate email")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Registering a second user with an email that is already taken is rejected")
    public void shouldRejectDuplicateEmail() {
        var user = TestUser.random();
        RegisterPage.open().register(user).shouldBeOpened();

        RegisterPage.open()
                .submit(user)
                .shouldShowFormError("A customer with this email address already exists.");
    }
}