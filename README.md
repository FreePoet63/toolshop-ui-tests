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

---

## ✨ What is covered

| Area | Scenarios |
|------|-----------|
| **Search** | existing product, partial name, unknown product (empty state), all results match the query, open a product from results |
| **Filters** | single filter, multiple filters, reset, every card matches the filter, filters across different categories (data-driven) |
| **Product** | open from catalog, price and description, quantity control, add to cart |
| **Cart** | product in cart, quantity change recalculates totals, remove product, cart total equals sum of lines |
| **Registration** | _planned_ |

---

## 🧱 Architecture

Page Object pattern with **component composition**: a page owns reusable components instead of
duplicating locators.

```
src
├── main/java
│   ├── pages        # CatalogPage, ProductPage, CartPage, HomePage ...
│   ├── components   # Header, FilterPanel, ProductList, CartTable, QuantityControl, CartToast
│   ├── config       # TestConfig  (system properties)
│   ├── driver       # DriverConfig (browser setup)
│   └── utils        # AllureUtils, Money
└── test
    ├── java
    │   ├── base       # BaseTest (setup / teardown, Allure artifacts on failure)
    │   ├── listener   # Retry, RetryTransformer, TestLifecycleListener
    │   └── test       # search, catalog, product, cart
    └── resources      # allure.properties, META-INF/services
```

Example of a test that reads like a scenario:

```java
var catalog = homePage.openCategory("Hand Tools");
catalog.filters().select("Screwdriver");
catalog.products().shouldAllContain("Screwdriver");
```

---

## 🎯 Design decisions

- **No `sleep`, no flaky checks.** Waiting is built into Selenide conditions
  (`shouldHaveCount`, `shouldHaveFewerThan`, `shouldShowMessage` ...), so a test never reads a list
  that is still being re-rendered.
- **Clear assertion rule.** Element state is checked with Selenide conditions inside page/component
  methods; values that were already read (numbers, strings) are checked with AssertJ, wrapped in an
  Allure step so the report shows the real data (`96 = 96`).
- **Controlled retries.** A TestNG `IRetryAnalyzer` (up to 3 attempts per test and per data-provider
  row) is registered once through an `IAnnotationTransformer`, so no annotation is needed in tests.
  Retries are visible in the Allure **Retries** tab and every retried test is analysed, not ignored.
- **Failure diagnostics.** On failure the report gets a screenshot, page source, browser logs and the
  failure reason.
- **Readable report.** Allure steps, stories and descriptions are written in English.
- **Configurable from the command line**, no code changes needed.

---

## 🚀 Getting started

**Requirements:** JDK 17, Maven 3.9+, Chrome or Firefox.

```bash
git clone https://github.com/FreePoet63/toolshop-ui-tests.git
cd toolshop-ui-tests

# run all tests
mvn clean test

# one class / one method
mvn test -Dtest=FilterTest
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
- [ ] Registration and login
- [ ] Checkout

---

## 🧰 Tech stack

Java 17 · Selenide · TestNG · AssertJ · Allure · Maven · GitHub Actions · GitHub Pages

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
| **Корзина** | товар в корзине, пересчёт суммы при смене количества, удаление, итог равен сумме позиций |
| **Регистрация** | _в планах_ |

### Архитектура

Паттерн Page Object с **композицией компонентов**: страница собирается из переиспользуемых
компонентов (шапка, панель фильтров, список товаров, таблица корзины), а локаторы не дублируются.
Структура пакетов приведена в английской части выше.

### Принципы

- **Без `sleep` и хрупких проверок.** Ожидания встроены в условия Selenide
  (`shouldHaveCount`, `shouldHaveFewerThan`, `shouldShowMessage` ...), поэтому тест не читает список,
  который ещё перерисовывается.
- **Чёткое правило проверок.** Состояние элементов проверяется условиями Selenide внутри методов
  страниц и компонентов; уже считанные значения (числа, строки) проверяются через AssertJ в шаге
  Allure, поэтому в отчёте видны реальные данные (`96 = 96`).
- **Контролируемые повторы.** `IRetryAnalyzer` TestNG (до 3 попыток на тест и на набор данных)
  подключается один раз через `IAnnotationTransformer`, в тестах аннотации не нужны. Повторы видны во
  вкладке **Retries** в Allure, и каждый повторённый тест разбирается, а не игнорируется.
- **Диагностика падений.** При падении в отчёт попадают скриншот, исходный код страницы, логи
  браузера и причина.
- **Читаемый отчёт.** Шаги, истории и описания в Allure написаны на английском.
- **Настройка из командной строки**, без правок кода.

### Запуск

**Требования:** JDK 17, Maven 3.9+, Chrome или Firefox.

```bash
git clone https://github.com/FreePoet63/toolshop-ui-tests.git
cd toolshop-ui-tests

mvn clean test                                   # все тесты
mvn test -Dtest=FilterTest                       # один класс
mvn test "-Dtest=FilterTest#shouldResetFilters"  # один метод
mvn allure:serve                                 # открыть отчёт Allure
```

| Параметр | По умолчанию | Описание |
|----------|--------------|----------|
| `browser` | `chrome` | `chrome` или `firefox` |
| `headless` | `false` | запуск без окна браузера |
| `timeout` | `10000` | таймаут ожиданий Selenide, мс |
| `base.url` | `https://practicesoftwaretesting.com` | тестируемое приложение |

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
- [ ] Регистрация и вход
- [ ] Оформление заказа

</details>