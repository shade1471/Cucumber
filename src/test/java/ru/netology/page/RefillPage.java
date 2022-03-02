package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.$;

public class RefillPage {
    private SelenideElement amountField = $("[data-test-id=amount] input");
    private SelenideElement fromField = $("[data-test-id=from] input");
    private SelenideElement refillButton = $("[data-test-id=action-transfer]");
    private SelenideElement cancelButton = $("[data-test-id=action-cancel]");
    private SelenideElement errorNotification = $("[data-test-id=error-notification]");

    private void clearField() {
        amountField.sendKeys(Keys.CONTROL + "A");
        amountField.sendKeys(Keys.BACK_SPACE);
        fromField.sendKeys(Keys.CONTROL + "A");
        fromField.sendKeys(Keys.BACK_SPACE);
    }

    public DashboardPage refillCard(String amount, String from) {
        new RefillPage().clearField();
        amountField.setValue(amount);
        fromField.setValue(from);
        refillButton.click();
        return new DashboardPage();
    }

    public void wrongRefillCard(String amount, String from){
        new RefillPage().clearField();
        amountField.setValue(amount);
        fromField.setValue(from);
        refillButton.click();
        errorNotification.isDisplayed();
    }

}
