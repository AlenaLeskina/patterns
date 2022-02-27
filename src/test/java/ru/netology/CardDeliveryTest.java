package ru.netology;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.data.CardDelivery;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.data.DataGenerator.Generate.*;

public class CardDeliveryTest {

    private final SelenideElement city = $("[data-test-id=city] input");
    private final SelenideElement cityInvalid = $("[data-test-id=city] .input__sub");
    private final SelenideElement date = $("[data-test-id=date] input");
    private final SelenideElement dateInvalid = $("[data-test-id=date] .input__sub");
    private final SelenideElement name = $("[data-test-id=name] input");
    private final SelenideElement nameInvalid = $("[data-test-id=name] .input__sub");
    private final SelenideElement phone = $("[data-test-id=phone] input");
    private final SelenideElement phoneInvalid = $("[data-test-id=phone] .input__sub");
    private final SelenideElement agreementCheckbox = $("[data-test-id=agreement]");
    private final SelenideElement book = $(withText("Запланировать"));
    private final SelenideElement notification = $("[data-test-id=success-notification]");
    private final SelenideElement notificationReplan = $(withText("У вас уже запланирована встреча на " +
            "другую дату. Перепланировать?"));
    private final SelenideElement replan = $(withText("Перепланировать"));
    private CardDelivery user;
    
    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
        user = generateUserData("ru");
        clearBrowserCookies();
    }

    @Test
    void shouldTestOrderSuccess() {
        city.setValue(user.getCity());
        date.doubleClick().sendKeys(Keys.BACK_SPACE);
        date.setValue(generateDeliveryDate(5));
        name.setValue(user.getName());
        phone.setValue(user.getPhone());
        agreementCheckbox.click();
        book.click();
        notification.shouldBe(visible, Duration.ofMillis(15000))
                .shouldHave(exactText("Успешно! Встреча успешно запланирована на " + generateDeliveryDate(5)));
    }

    @Test
    void shouldTestReschedulingPopup() {
        city.setValue(user.getCity());
        date.doubleClick().sendKeys(Keys.BACK_SPACE);
        date.setValue(generateDeliveryDate(5));
        name.setValue(user.getName());
        phone.setValue(user.getPhone());
        agreementCheckbox.click();
        book.click();
        notification.shouldBe(visible, Duration.ofMillis(15000))
                .shouldHave(exactText("Успешно! Встреча успешно запланирована на " + generateDeliveryDate(5)));
        date.doubleClick().sendKeys(Keys.BACK_SPACE);
        date.setValue(generateDeliveryDate(6));
        book.click();
        notificationReplan.shouldBe(visible, Duration.ofMillis(15000));
        replan.click();
        notification.shouldBe(visible, Duration.ofMillis(15000))
                .shouldHave(exactText("Успешно! Встреча успешно запланирована на " + generateDeliveryDate(6)));

    }

    @Test
    void shouldTestInvalidCity() {
        city.setValue(generateInvalidCity());
        date.doubleClick().sendKeys(Keys.BACK_SPACE);
        date.setValue(generateDeliveryDate(5));
        name.setValue(user.getName());
        phone.setValue(user.getPhone());
        agreementCheckbox.click();
        book.click();
        cityInvalid.shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldTestInvalidDate() {
        city.setValue(user.getCity());
        date.doubleClick().sendKeys(Keys.BACK_SPACE);
        date.setValue(generateDeliveryDate(1));
        name.setValue(user.getName());
        phone.setValue(user.getPhone());
        agreementCheckbox.click();
        book.click();
        dateInvalid.shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }


    @Test
    void shouldTestInvalidDatePast() {
        city.setValue(user.getCity());
        date.doubleClick().sendKeys(Keys.BACK_SPACE);
        date.setValue(generateDeliveryDate(-3));
        name.setValue(user.getName());
        phone.setValue(user.getPhone());
        agreementCheckbox.click();
        book.click();
        dateInvalid.shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldTestInvalidName() {
        city.setValue(user.getCity());
        date.doubleClick().sendKeys(Keys.BACK_SPACE);
        date.setValue(generateDeliveryDate(3));
        name.setValue(generateUserData("en-US").getName());
        phone.setValue(user.getPhone());
        agreementCheckbox.click();
        book.click();
        nameInvalid.shouldHave(exactText("Имя и Фамилия указаные неверно. " +
                "Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldTestInvalidPhone() {
        city.setValue(user.getCity());
        date.doubleClick().sendKeys(Keys.BACK_SPACE);
        date.setValue(generateDeliveryDate(3));
        name.setValue(user.getName());
        phone.setValue(generateInvalidPhoneNumber());
        agreementCheckbox.click();
        book.click();
        phoneInvalid.shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldTestCalendarIcon() {
        city.setValue(generateCity());
        $(".input__icon").click();
        $("[role=grid]").sendKeys(Keys.ARROW_DOWN);
        $("[role=grid]").sendKeys(Keys.ARROW_LEFT, Keys.ARROW_LEFT, Keys.ARROW_LEFT);
        $("[role=grid]").sendKeys(Keys.ENTER);
        name.setValue(user.getName());
        phone.setValue(user.getPhone());
        agreementCheckbox.click();
        book.click();
        notification.shouldBe(visible, Duration.ofMillis(15000))
                .shouldHave(exactText("Успешно! Встреча успешно запланирована на " + date.getValue()));
    }
}