package ru.netology.steps;

import com.codeborne.selenide.Selenide;
import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Пусть;
import io.cucumber.java.ru.Тогда;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;
import ru.netology.page.RefillPage;
import ru.netology.page.VerificationPage;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TemplateSteps {
    private static RefillPage refillPage;
    private static LoginPage loginPage;
    private static DashboardPage dashboardPage;
    private static VerificationPage verificationPage;

    private DataHelper.AuthInfo authInfo = DataHelper.getAuthInfo();
    private DataHelper.AuthInfo authInfoInvalid = DataHelper.getInvalidAuthInfo(authInfo);
    private DataHelper.VerificationCode verificationCode = DataHelper.getVerificationCodeFor(authInfo);
    private String cardOneId = "92df3f1c-a033-48e6-8390-206f6b1f56c0";
    private String cardTwoId = "0f3f5c2a-249e-4c3d-8287-09f7a039391d";

    @Пусть("открыта страница с формой авторизации {string}")
    public void openAuthPage(String url) {
        loginPage = Selenide.open(url, LoginPage.class);
    }

    @Когда("пользователь пытается авторизоваться с именем {string} и паролем {string}")
    public void loginWithNameAndPassword(String login, String password) {
        verificationPage = loginPage.validLogin(new DataHelper.AuthInfo(login, password));
    }

    @Когда("пользователь пытается авторизоваться с несуществующим логином и паролем")
    public void loginInvalidUser() {
        loginPage.invalidLogin(authInfoInvalid);
    }

    @Тогда("получает сообщение об ошибке авторизации")
    public void verifyError() {
        loginPage.verifyErrorLogin();
    }

    @И("пользователь вводит проверочный код 'из смс' {string}")
    public void setValidCode(String code) {
        dashboardPage = verificationPage.validVerify(new DataHelper.VerificationCode(code));
    }

    @Тогда("получает сообщение об ошибке в наборе кода")
    public void verifyCodeError() {
        verificationPage.verifyErrorCode();
    }

    @Тогда("происходит успешная авторизация и пользователь попадает на страницу 'Личный кабинет'")
    public void verifyDashboardPage() {
        dashboardPage.verifyIsDashboardPage();
    }

    @Пусть("пользователь залогинен с именем {string} и паролем {string}")
    public void authValidUser(String login, String password) {
        openAuthPage("http://localhost:9999");
        loginWithNameAndPassword(login, password);
        setValidCode(verificationCode.getCode());
    }

    @И("пользователь переводит {string} рублей с карты с номером {string} на свою 1 карту с главной страницы")
    public void increaseBalanceFirstCard(String amount, String from) {
        dashboardPage.refresh(cardOneId, cardTwoId, authInfo);
        refillPage = dashboardPage.increaseBalance(cardOneId);
        refillPage.refillCard(amount, from);
    }

    @Тогда("баланс его 1 карты из списка на главной странице должен стать {int} рублей")
    public void getBalance(int expectedBalance) {
        int actualBalance = dashboardPage.getCardBalance(cardOneId);
        assertEquals(expectedBalance, actualBalance);
    }

}

