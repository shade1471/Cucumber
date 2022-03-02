package ru.netology.web.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.util.Locale;

public class DataHelper {
    private DataHelper() {
    }

    @Value
    public static class AuthInfo {
        private String login;
        private String password;
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static String generateLogin(String locale) {
        String login = new Faker(new Locale(locale)).name().username();
        return login;
    }

    public static String generatePassword(String locale) {
        String password = new Faker(new Locale(locale)).internet().password();
        return password;
    }

    public static AuthInfo getInvalidAuthInfo(AuthInfo original) {
        return new AuthInfo(DataHelper.generateLogin("en"), DataHelper.generatePassword("en"));
    }

    @Value
    public static class VerificationCode {
        private String code;
    }

    public static VerificationCode getVerificationCodeFor(AuthInfo authInfo) {
        return new VerificationCode("12345");
    }

    @Value
    public static class Card {
        private String numberOne;
        private String numberTwo;
        private String wrongCard;
    }

    public static Card getNumberCard(AuthInfo authInfo) {
        return new Card("5559 0000 0000 0001", "5559 0000 0000 0002", "5559 0000 0000 1111");
    }

}

