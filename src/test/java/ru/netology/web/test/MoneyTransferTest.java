package ru.netology.web.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest {
    private String cardOneId = "92df3f1c-a033-48e6-8390-206f6b1f56c0";
    private String cardTwoId = "0f3f5c2a-249e-4c3d-8287-09f7a039391d";

    private DataHelper.AuthInfo authInfo = DataHelper.getAuthInfo();
    private DataHelper.AuthInfo authInfoInvalid = DataHelper.getInvalidAuthInfo(authInfo);
    private DataHelper.Card cardNumber = DataHelper.getNumberCard(authInfo);
    private DataHelper.VerificationCode verificationCode = DataHelper.getVerificationCodeFor(authInfo);

    @Nested
    public class ShouldAuth {

        @BeforeEach
        void setup() {
            open("http://localhost:9999");
        }

        @Test
        void shouldAuthIfUserExist() {
            var loginPage = new LoginPage();
            var verificationPage = loginPage.validLogin(authInfo);
            verificationPage.validVerify(verificationCode);
        }

        @Test
        void shouldAuthIfUserNotExist() {
            var loginPage = new LoginPage();
            loginPage.invalidLogin(authInfoInvalid);
        }
    }

    @Nested
    public class TransferMoney {

        @BeforeEach
        void setup() {
            open("http://localhost:9999");
            var loginPage = new LoginPage();
            var verificationPage = loginPage.validLogin(authInfo);
            var dashBoard = verificationPage.validVerify(verificationCode);
            // Востановление к первоначальному состоянию системы, когда на картах по 10000 р.
            dashBoard.refresh(cardOneId, cardTwoId, authInfo);
        }

        @Test
        void shouldTransferMoneyBetweenOwnCardsFromSecondToFirst() {
            DashboardPage dashBoard = new DashboardPage();
            //сумма пополнения
            int amount = 5000;
            //Первичный баланс 1-ой и 2-ой карты до операции
            int primaryBalanceOne = dashBoard.getCardBalance(cardOneId);
            int primaryBalanceTwo = dashBoard.getCardBalance(cardOneId);
            // Выбор первой карты для пополнения
            var refillPage = dashBoard.increaseBalance(cardOneId);
            // Сколько и откуда пополнить
            refillPage.refillCard(Integer.toString(amount), cardNumber.getNumberTwo());
            // Баланс первой карты после операции
            int balanceActualOne = dashBoard.getCardBalance(cardOneId);
            // Баланс второй карты после операции
            int balanceActualTwo = dashBoard.getCardBalance(cardTwoId);
            assertEquals((primaryBalanceOne + amount), balanceActualOne);
            assertEquals((primaryBalanceTwo - amount), balanceActualTwo);
        }

        @Test
        void shouldTransferMoneyBetweenOwnCardsFromFirstToSecond() {
            DashboardPage dashBoard = new DashboardPage();
            int amount = 5000;
            int primaryBalanceOne = dashBoard.getCardBalance(cardOneId);
            int primaryBalanceTwo = dashBoard.getCardBalance(cardOneId);
            var refillPage = dashBoard.increaseBalance(cardTwoId);
            refillPage.refillCard(Integer.toString(amount), cardNumber.getNumberOne());
            int balanceActualOne = dashBoard.getCardBalance(cardOneId);
            int balanceActualTwo = dashBoard.getCardBalance(cardTwoId);
            assertEquals((primaryBalanceOne - amount), balanceActualOne);
            assertEquals((primaryBalanceTwo + amount), balanceActualTwo);
        }

        @Test
        void shouldTransferMoneyBetweenOwnCardsWhenNumberCardWrong() {
            DashboardPage dashBoard = new DashboardPage();
            int amount = 5000;
            var refillPage = dashBoard.increaseBalance(cardTwoId);
            refillPage.wrongRefillCard(Integer.toString(amount), cardNumber.getWrongCard());
        }

        @Test
        void shouldTransferMoneyBetweenOwnCardsIfNotEnoughBalanceAtFirstCard() {
            DashboardPage dashBoard = new DashboardPage();
            int amount = 15000;
            int primaryBalanceOne = dashBoard.getCardBalance(cardOneId);
            int primaryBalanceTwo = dashBoard.getCardBalance(cardOneId);
            var refillPage = dashBoard.increaseBalance(cardTwoId);
            refillPage.refillCard(Integer.toString(amount), cardNumber.getNumberOne());
            int balanceActualOne = dashBoard.getCardBalance(cardOneId);
            int balanceActualTwo = dashBoard.getCardBalance(cardTwoId);
            assertEquals(primaryBalanceOne, balanceActualOne);
            assertEquals(primaryBalanceTwo, balanceActualTwo);
        }
    }
}
