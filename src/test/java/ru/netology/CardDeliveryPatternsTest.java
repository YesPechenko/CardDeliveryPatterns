package ru.netology;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;


public class CardDeliveryPatternsTest {
    DataGenerator dataGenerator = new DataGenerator();
    String city = dataGenerator.getCity();
    String name = dataGenerator.getName();
    String phone = dataGenerator.getPhone();


    @BeforeEach
    void setUp () {
        open ("http://localhost:9999");
    }


    @Test
    void shouldTestCorrectForm () {
        $("[data-test-id=city] input").setValue(city);

        $("[data-test-id='date'] input").doubleClick().sendKeys(dataGenerator.formateDate(3));

        $("[data-test-id=name] input").setValue(name);
        $("[data-test-id=phone] input").setValue(phone);
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();
        $(byText("Успешно!")).should(exist);
        $("[data-test-id=success-notification] .notification__content")
                .shouldHave(exactText("Встреча успешно запланирована на "+dataGenerator.formateDate(3)));
        $("[data-test-id='date'] input").doubleClick().sendKeys(dataGenerator.formateDate(5));
        $(".button").click();

        $("[data-test-id=replan-notification]").shouldBe(visible, Duration.ofMillis(12000));

        $(byText("Необходимо подтверждение")).should(exist);
        $(byText("У вас уже запланирована встреча на другую дату. Перепланировать?")).should(exist);
        $(byText("Перепланировать")).click();
        $(".notification_status_ok").should(exist);
        $(".notification__content")
                .shouldHave(exactText("Встреча успешно запланирована на "+dataGenerator.formateDate(5)));
    }


    @Test
    void shouldTestFormCityNotCorrect () {
        $("[data-test-id=city] input").setValue(DataGenerator.getNotCorrectCity());

        $("[data-test-id='date'] input").doubleClick().sendKeys(dataGenerator.formateDate(3));

        $("[data-test-id=name] input").setValue(name);
        $("[data-test-id=phone] input").setValue(phone);
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();

        $("[data-test-id=city].input_invalid .input__sub")
                .shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldTestFormNameHyphen () {
        $("[data-test-id=city] input").setValue(city);

        $("[data-test-id='date'] input").doubleClick().sendKeys(dataGenerator.formateDate(3));

        $("[data-test-id=name] input").setValue("Иванов-Александров Василий");
        $("[data-test-id=phone] input").setValue(phone);
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();
        $(byText("Успешно!")).shouldBe(visible, Duration.ofMillis(12000));
        $("[data-test-id=success-notification] .notification__content")
                .shouldHave(exactText("Встреча успешно запланирована на "+dataGenerator.formateDate(3)));
        $("[data-test-id='date'] input").doubleClick().sendKeys(dataGenerator.formateDate(5));
        $(".button").click();

        $("[data-test-id=replan-notification]").shouldBe(visible, Duration.ofMillis(12000));

        $(byText("Необходимо подтверждение")).should(exist);
        $(byText("У вас уже запланирована встреча на другую дату. Перепланировать?")).should(exist);
        $(byText("Перепланировать")).click();
        $(".notification_status_ok").should(exist);
        $(".notification__content")
                .shouldHave(exactText("Встреча успешно запланирована на "+dataGenerator.formateDate(5)));

    }

    @Test
    void shouldTestFormNameSpace () {
        $("[data-test-id=city] input").setValue(city);

        $("[data-test-id='date'] input").doubleClick().sendKeys(dataGenerator.formateDate(3));

        $("[data-test-id=name] input").setValue("   Иванов Василий ");
        $("[data-test-id=phone] input").setValue(phone);
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();
        $(byText("Успешно!")).shouldBe(visible, Duration.ofMillis(12000));
        $("[data-test-id=success-notification] .notification__content")
                .shouldHave(exactText("Встреча успешно запланирована на "+dataGenerator.formateDate(3)));
        $("[data-test-id='date'] input").doubleClick().sendKeys(dataGenerator.formateDate(5));
        $(".button").click();

        $("[data-test-id=replan-notification]").shouldBe(visible, Duration.ofMillis(12000));

        $(byText("Необходимо подтверждение")).should(exist);
        $(byText("У вас уже запланирована встреча на другую дату. Перепланировать?")).should(exist);
        $(byText("Перепланировать")).click();
        $(".notification_status_ok").should(exist);
        $(".notification__content")
                .shouldHave(exactText("Встреча успешно запланирована на "+dataGenerator.formateDate(5)));
    }

    @Test
    void shouldTestFormNameNotCorrect () {
        $("[data-test-id=city] input").setValue(city);

        $("[data-test-id='date'] input").doubleClick().sendKeys(dataGenerator.formateDate(3));

        $("[data-test-id=name] input").setValue(DataGenerator.getNotCorrectName());
        $("[data-test-id=phone] input").setValue(phone);
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();

        $("[data-test-id=name].input_invalid .input__sub")
                .shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldTestFormNameEmpty () {
        $("[data-test-id=city] input").setValue(city);

        $("[data-test-id='date'] input").doubleClick().sendKeys(dataGenerator.formateDate(3));

        $("[data-test-id=name] input").setValue(DataGenerator.getNotCorrectNameEmptyField());
        $("[data-test-id=phone] input").setValue(phone);
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();

        $("[data-test-id=name].input_invalid .input__sub")
                .shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldTestFormPhoneIncorrectly () {
        $("[data-test-id=city] input").setValue(city);

        $("[data-test-id='date'] input").doubleClick().sendKeys(dataGenerator.formateDate(3));

        $("[data-test-id=name] input").setValue(name);
        $("[data-test-id=phone] input").setValue(DataGenerator.getNotCorrectPhone());
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();

        $("[data-test-id=phone].input_invalid .input__sub")
                .shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));

    }

    @Test
    void shouldTestFormAgreementEmpty () {
        $("[data-test-id=city] input").setValue(city);

        $("[data-test-id='date'] input").doubleClick().sendKeys(dataGenerator.formateDate(3));
        $("[data-test-id=name] input").setValue(name);
        $("[data-test-id=phone] input").setValue(phone);

        $$("button").find(exactText("Запланировать")).click();

        $("[data-test-id=agreement].input_invalid .checkbox__text")
                .shouldHave(exactText("Я соглашаюсь с условиями обработки и использования моих персональных данных"));

    }

    @Test
    void shouldTestFormCityAndNameAndPhoneEmpty () {

        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();

        $("[data-test-id=city].input_invalid .input__sub")
                .shouldHave(exactText("Поле обязательно для заполнения"));
    }
}
