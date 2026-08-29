# 🚀 Руководство по работе с репозиторием GitHub

## 📌 Репозиторий проекта

* **GitHub URL**: `https://github.com/MarryBye/Larper-Than-Wolves`
* **Основная ветка**: `main`
* **Актуальный тег релиза**: `v1.8.0`

---

## 🛠️ Команды для сборки и отправки изменений

### 1. Сборка мода
```bash
./gradlew build
```
Готовый JAR файл: `build/libs/larperthanwolves-1.8.0.jar`.

### 2. Фиксация изменений и отправка в репозиторий
```bash
# Добавить измененные файлы
git add .

# Сделать коммит
git commit -m "Ваше описание изменений"

# Отправить на GitHub
git push origin main
```

### 3. Обновление тега релиза
```bash
# Создать тег релиза на текущем коммите
git tag -a v1.8.0 -m "Release v1.8.0"

# Отправить тег на GitHub
git push origin v1.8.0
```
