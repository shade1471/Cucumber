package ru.netology.steps;

import com.codeborne.selenide.Selenide;
import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Пусть;
import io.cucumber.java.ru.Тогда;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;
import ru.netology.page.VerificationPage;

import static com.codeborne.selenide.Selenide.open;


public class TemplateSteps {
    private static LoginPage loginPage;
    private static DashboardPage dashboardPage;
    private static VerificationPage verificationPage;
    private DataHelper.AuthInfo authInfo = DataHelper.getAuthInfo();
    private DataHelper.AuthInfo authInfoInvalid = DataHelper.getInvalidAuthInfo(authInfo);
    private DataHelper.Card cardNumber = DataHelper.getNumberCard(authInfo);
    private DataHelper.VerificationCode verificationCode = DataHelper.getVerificationCodeFor(authInfo);

    @Пусть("открыта страница с формой авторизации {string}")
    public void openAuthPage(String url) {
        loginPage = Selenide.open(url, LoginPage.class);
    }

    @Когда("пользователь пытается авторизоваться с именем vasya и паролем qwerty123")
    public void loginWithNameAndPassword() {
        verificationPage = loginPage.validLogin(authInfo);
    }

    @И("пользователь вводит проверочный код 'из смс' 12345")
    public void setValidCode() {
        dashboardPage = verificationPage.validVerify(verificationCode);
    }

    @Тогда("происходит успешная авторизация и пользователь попадает на страницу 'Личный кабинет'")
    public void verifyDashboardPage() {
        dashboardPage.verifyIsDashboardPage();
    }


}