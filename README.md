# 🛠️ Toolshop UI Tests

[![UI tests](https://github.com/FreePoet63/toolshop-ui-tests/actions/workflows/toolshop.yml/badge.svg)](https://github.com/FreePoet63/toolshop-ui-tests/actions/workflows/toolshop.yml)
![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk)
![Selenide](https://img.shields.io/badge/Selenide-7.18-green)
![TestNG](https://img.shields.io/badge/TestNG-7.10-red)
![Allure](https://img.shields.io/badge/Allure-2.29-purple)

🇬🇧 English · [🇷🇺 Русская версия ниже](#-русская-версия)

UI test automation framework for the demo online store
[practicesoftwaretesting.com](https://practicesoftwaretesting.com) (Toolshop).
Built with **Java, Selenide, TestNG and Allure**, runs in **GitHub Actions** and publishes an
**Allure report with run history** to GitHub Pages.

📊 **Live report:** https://freepoet63.github.io/toolshop-ui-tests/

<!-- Add a screenshot of the Allure Overview page here:
![Allure report](docs/allure-overview.png) -->

---

## ✨ What is covered

| Area | Scenarios |
|------|-----------|
| **Search** | existing product, partial name, unknown product (empty state), all results match the query, open a product from results |
| **Filters** | single filter, multiple filters, reset, every card matches the filter, filters across different categories (data-driven) |
| **Product** | open from catalog, price and description, quantity control, add to cart |
| **Cart** | product in cart, quantity change recalculates totals, remove product, cart total equals sum of lines, several units of one product |
| **Registration** | successful registration, validation of every required field (data-driven), invalid email, weak password, duplicate email |
| **Login** | successful login, wrong password, unknown email, empty fields, logout |
| **Checkout** | order with every payment method (data-driven), guest checkout, billing address autofill, order with a different postal code, invoice number, cart disappears after the order, order appears in the order history with the right total |
| **Payment validation** | invalid value in each payment field of every method (data-driven), empty payment form |

---

## 🧱 Architecture

Page Object pattern with **component composition**: a page owns reusable components instead of
duplicating locators.

```
src
├── main/java
│   ├── pages        # HomePage, CatalogPage, ProductPage, CartPage, RegisterPage, LoginPage, AccountPage,
│   │                # CheckoutPage, OrderConfirmationPage, InvoicesPage
│   ├── components   # Header, FilterPanel, ProductList, Table, CartTable, QuantityControl, CartToast,
│   │                # FormField, FormAlert, Field, RegistrationForm, LoginForm,
│   │                # BillingAddressForm, GuestForm, PaymentForm
│   ├── model        # TestUser, PaymentData, PaymentMethod (test data)
│   ├── api          # UserApi (test users created through the application API)
│   ├── config       # TestConfig  (system properties)
│   ├── driver       # DriverConfig (browser setup)
│   └── utils        # AllureUtils, Money
└── test
    ├── java
    │   ├── base       # BaseTest (setup / teardown, Allure artifacts on failure)
    │   ├── listener   # Retry, RetryTransformer, TestLifecycleListener
    │   └── test       # search, catalog, product, cart, registration, login, checkout
    └── resources      # allure.properties, META-INF/services
```

A whole checkout reads like a scenario:

```java
new Header().openCart()
        .proceedToCheckout()
        .continueAsSignedIn()
        .continueWithPrefilledAddress()
        .pay(PaymentData.creditCard())
        .shouldShowSuccess()
        .placeOrder()
        .shouldShowInvoiceNumber();
```

---

## 🎯 Design decisions

- **No `sleep`, no flaky checks.** Waiting is built into Selenide conditions
  (`shouldHaveCount`, `shouldHaveFewerThan`, `shouldShowMessage` ...), so a test never reads a list
  that is still being re-rendered.
- **Clear assertion rule.** Element state is checked with Selenide conditions inside page/component
  methods; values that were already read (numbers, strings) are checked with AssertJ, wrapped in an
  Allure step so the report shows the real data (`$29.43 = $29.43`).
- **No duplication.** One `Field` enum and one `FormField` component serve the registration, login and
  billing forms; `FormAlert` is the shared form message; `Table` is shared by the cart and the order
  history.
- **Data-driven tests.** Payment methods, required fields and invalid values are test data, not copies
  of the same test.
- **Test data through the API.** Users for login and checkout tests are created through the application
  API (RestAssured), so UI tests do not depend on the registration form. Registration itself is covered
  by dedicated UI tests.
- **No secrets in the report.** Passwords are masked in Allure steps and hidden in the `toString()` of
  the test data model. Cards, bank and gift card numbers are fake demo values.
- **Controlled retries.** A TestNG `IRetryAnalyzer` (up to 3 attempts per test and per data-provider
  row) is registered once through an `IAnnotationTransformer`, so no annotation is needed in tests.
  Retries are visible in the Allure **Retries** tab and every retried test is analysed, not ignored.
- **Test levels.** The same transformer puts every UI test into the `ui` TestNG group.
- **Failure diagnostics.** On failure the report gets a screenshot, page source, browser logs and the
  failure reason.
- **Readable report.** Allure steps, stories and descriptions are written in English.
- **Configurable from the command line**, no code changes needed.

---

## 🔎 Notes about the application under test

Behaviours found while writing the tests and covered as they are:

- The billing address is prefilled from the profile saved at registration.
- The billing address fields are not validated by the site, so there are no tests for that.
- A guest can place an order without signing in.
- An empty payment form shows a generic error; invalid values show a message per field.
- The cart disappears only after the final confirmation of the order.

---

## 🚀 Getting started

**Requirements:** JDK 17, Maven 3.9+, Chrome or Firefox.

```bash
git clone https://github.com/FreePoet63/toolshop-ui-tests.git
cd toolshop-ui-tests

# run all tests
mvn clean test

# run by level
mvn test -Dgroups=ui

# one class / one method
mvn test -Dtest=CheckoutTest
mvn test "-Dtest=FilterTest#shouldResetFilters"

# open the Allure report
mvn allure:serve
```

### Parameters

| Property | Default | Description |
|----------|---------|-------------|
| `browser` | `chrome` | `chrome` or `firefox` |
| `headless` | `false` | run without a browser window |
| `timeout` | `10000` | Selenide wait timeout, ms |
| `base.url` | `https://practicesoftwaretesting.com` | application under test |
| `api.url` | `https://api.practicesoftwaretesting.com` | API used to create test users |

```bash
mvn clean test -Dbrowser=firefox -Dheadless=true
```

---

## 🔄 CI / CD

GitHub Actions workflow (`.github/workflows/toolshop.yml`):

- runs on **pull requests** and on pushes to **`main`**; can also be started manually;
- runs tests headless in Firefox;
- builds an **Allure report with history** and publishes it to **GitHub Pages**
  (only from `main`, so a draft branch never overwrites the stable report);
- the job is **red if any test fails**, while the report is still published.

---

## 🗺️ Roadmap

- [x] Search
- [x] Filters
- [x] Product page
- [x] Cart
- [x] Registration and login
- [x] Checkout (registered user and guest)
- [ ] Order details: payment method and items
- [ ] API-level tests (`api` group)

---

## 🧰 Tech stack

Java 17 · Selenide · TestNG · RestAssured · AssertJ · Allure · Maven · GitHub Actions · GitHub Pages

---

## 🇷🇺 Русская версия

<details>
<summary>Развернуть описание на русском</summary>

### О проекте

Фреймворк UI-автотестов для демо-магазина
[practicesoftwaretesting.com](https://practicesoftwaretesting.com) (Toolshop).
Написан на **Java, Selenide, TestNG и Allure**, запускается в **GitHub Actions** и публикует
**отчёт Allure с историей запусков** на GitHub Pages.

📊 **Отчёт:** https://freepoet63.github.io/toolshop-ui-tests/

### Что покрыто

| Блок | Сценарии |
|------|----------|
| **Поиск** | существующий товар, часть названия, несуществующий товар (пустая выдача), все результаты соответствуют запросу, переход из выдачи в карточку |
| **Фильтры** | один фильтр, несколько фильтров, сброс, все карточки соответствуют фильтру, фильтры в разных категориях (параметризованный тест) |
| **Продукт** | открытие из каталога, цена и описание, изменение количества, добавление в корзину |
| **Корзина** | товар в корзине, пересчёт суммы при смене количества, удаление, итог равен сумме позиций, несколько единиц одного товара |
| **Регистрация** | успешная регистрация, проверка каждого обязательного поля (параметризованный тест), неверная почта, слабый пароль, занятая почта |
| **Авторизация** | успешный вход, неверный пароль, незарегистрированная почта, пустые поля, выход |
| **Оформление заказа** | заказ каждым способом оплаты (параметризованный тест), заказ гостем, автозаполнение адреса, заказ с другим индексом, номер заказа, корзина исчезает после заказа, заказ в истории с правильной суммой |
| **Валидация оплаты** | неверное значение в каждом поле оплаты каждого способа (параметризованный тест), пустая форма оплаты |

### Архитектура

Паттерн Page Object с **композицией компонентов**: страница собирается из переиспользуемых
компонентов (шапка, панель фильтров, список товаров, таблица корзины, поля и формы), а локаторы не
дублируются. Структура пакетов приведена в английской части выше.

### Принципы

- **Без `sleep` и хрупких проверок.** Ожидания встроены в условия Selenide
  (`shouldHaveCount`, `shouldHaveFewerThan`, `shouldShowMessage` ...), поэтому тест не читает список,
  который ещё перерисовывается.
- **Чёткое правило проверок.** Состояние элементов проверяется условиями Selenide внутри методов
  страниц и компонентов; уже считанные значения (числа, строки) проверяются через AssertJ в шаге
  Allure, поэтому в отчёте видны реальные данные (`$29.43 = $29.43`).
- **Без дублирования.** Один enum `Field` и один компонент `FormField` обслуживают формы регистрации,
  входа и адреса; `FormAlert` общий для сообщений форм; `Table` общий для корзины и истории заказов.
- **Параметризованные тесты.** Способы оплаты, обязательные поля и неверные значения заданы данными, а
  не копиями одного теста.
- **Тестовые данные через API.** Пользователи для тестов входа и оформления заказа создаются через API
  приложения (RestAssured), поэтому UI-тесты не зависят от формы регистрации. Сама регистрация
  проверяется отдельными UI-тестами.
- **Никаких секретов в отчёте.** Пароли маскируются в шагах Allure и скрыты в `toString()` модели
  тестовых данных. Номера карт, банковских счетов и подарочных карт вымышленные.
- **Контролируемые повторы.** `IRetryAnalyzer` TestNG (до 3 попыток на тест и на набор данных)
  подключается один раз через `IAnnotationTransformer`, в тестах аннотации не нужны. Повторы видны во
  вкладке **Retries** в Allure, и каждый повторённый тест разбирается, а не игнорируется.
- **Уровни тестов.** Этот же трансформер добавляет каждому UI-тесту группу `ui` в TestNG.
- **Диагностика падений.** При падении в отчёт попадают скриншот, исходный код страницы, логи
  браузера и причина.
- **Читаемый отчёт.** Шаги, истории и описания в Allure написаны на английском.
- **Настройка из командной строки**, без правок кода.

### Особенности тестируемого приложения

Поведение, найденное при написании тестов и покрытое как есть:

- Адрес оплаты подставляется из профиля, сохранённого при регистрации.
- Поля адреса сайт не валидирует, поэтому тестов на это нет.
- Гость может оформить заказ без входа.
- Пустая форма оплаты даёт общую ошибку, неверные значения дают сообщение по каждому полю.
- Корзина исчезает только после финального подтверждения заказа.

### Запуск

**Требования:** JDK 17, Maven 3.9+, Chrome или Firefox.

```bash
git clone https://github.com/FreePoet63/toolshop-ui-tests.git
cd toolshop-ui-tests

mvn clean test                                   # все тесты
mvn test -Dgroups=ui                             # по уровню
mvn test -Dtest=CheckoutTest                     # один класс
mvn test "-Dtest=FilterTest#shouldResetFilters"  # один метод
mvn allure:serve                                 # открыть отчёт Allure
```

| Параметр | По умолчанию | Описание |
|----------|--------------|----------|
| `browser` | `chrome` | `chrome` или `firefox` |
| `headless` | `false` | запуск без окна браузера |
| `timeout` | `10000` | таймаут ожиданий Selenide, мс |
| `base.url` | `https://practicesoftwaretesting.com` | тестируемое приложение |
| `api.url` | `https://api.practicesoftwaretesting.com` | API для создания тестовых пользователей |

### CI / CD

Workflow GitHub Actions (`.github/workflows/toolshop.yml`):

- запускается на **pull request** и при пуше в **`main`**, а также вручную;
- тесты идут в режиме headless в Firefox;
- строится **отчёт Allure с историей** и публикуется на **GitHub Pages** (только из `main`, чтобы
  черновая ветка не перезаписала стабильный отчёт);
- если тесты упали, **задача красная**, а отчёт всё равно публикуется.

### План

- [x] Поиск
- [x] Фильтры
- [x] Страница продукта
- [x] Корзина
- [x] Регистрация и вход
- [x] Оформление заказа (зарегистрированный пользователь и гость)
- [ ] Детали заказа: способ оплаты и состав
- [ ] Тесты уровня API (группа `api`)

</details>
