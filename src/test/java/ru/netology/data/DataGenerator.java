package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@Data
@RequiredArgsConstructor
public class DataGenerator {

    public static class Generate {
        private Generate() {
        }

        public static CardDelivery generateUserData(String locale) {
            Faker faker = new Faker(new Locale(locale));
            return new CardDelivery(generateCity(), faker.name().lastName() + " " + faker.name().firstName(), faker.phoneNumber().phoneNumber());
        }

        public static String generateDeliveryDate(int days) {
            return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        }

        public static String generateCity() {
            Random random = new Random();
            List<String> cityValid = Arrays.asList("Москва", "Санкт-Петербург", "Казань", "Екатеринбург", "Тамбов",
                    "Архангельск", "Нижний Новгород", "Пенза", "Краснодар", "Анадырь", "Барнаул", "Калининград");
            return cityValid.get(random.nextInt(cityValid.size()));
        }

        public static String generateInvalidPhoneNumber() {
            return "89999999999";
        }

        public static String generateInvalidCity() {
            return "TestCity";
        }
    }
}

