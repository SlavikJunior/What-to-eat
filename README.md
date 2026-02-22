# 🍔 What to eat (Что поесть)


> Приложение, которое помогает раз и навсегда решить ежедневную проблему выбора еды, предлагая рецепты на основе выших пожеланий или того, чтое сть у вас в холодильнике.

## 🌟 Особенности

* **Удобные фильтры:** Настраивайте поиск по ингредиентам, типу кухни или времени приготовления.
* **Сохранение в избранное:** Добавляйте понравившиеся рецепты в личный список.
* **Сохранение своих рецептов:** Добавляйте свои рецепты в приложение.

## 🛠 Стек технологий

* **UI:** Jetpack Compose, Coil, Compose-shimmer, Material Design 3
* **Async:** Coroutines + Flow
* **Net:** Retrofit + Custom OkHttp, Kotlinx Serialization
* **База данных:** SQLite с Room
* **Архитектура:** SOLID'ный Монолит с Clean Arch, гибрид MVVM и MVI
* **API:** Spoonacular, Yandex Cloud AI Translate

## 🚀 Установка и локальный запуск

Чтобы запустить проект на своем компьютере, выполните следующие шаги:

1. **Клонируйте репозиторий:**
   ```bash
   git clone [https://github.com/SlavikJunior/What-to-eat.git](https://github.com/SlavikJunior/What-to-eat.git)
   ```
2. **Перейдите в директорию проекта:**
   ```bash
   cd What-to-eat
   ```
3. **Отредактируйте файлик local.properties:**
   ```bash
   SPOONACULAR_API_KEY=YOUR_SPOONACULAR_API_KEY
   YANDEX_TRANSLATE_API_KEY=YOUR_YANDEX_TRANSLATE_API_KEY
   FOLDER_ID=YOUR_FOLDER_ID
   ```
4. **Обновите зависиммости:**
   ```bash
   bash ./gradlew build --refresh-dependencies
   ```
5. **Соберите проект:**
   ```bash
   bash ./gradlew build
   ```

## 🚀 Скриншоты (скоро появятся)