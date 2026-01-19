![Kotlin](https://img.shields.io/badge/Kotlin-1.9+-7F52FF?logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-UI-4285F4?logo=android&logoColor=white)
![Android](https://img.shields.io/badge/Android-8.0+-3DDC84?logo=android&logoColor=white)

![StateFlow](https://img.shields.io/badge/StateFlow-Architecture-blue)
![Clean Architecture](https://img.shields.io/badge/Clean%20Architecture-Yes-success)

![Room](https://img.shields.io/badge/Room-Database-6DB33F)
![Retrofit](https://img.shields.io/badge/Retrofit-Networking-orange)
![Hilt](https://img.shields.io/badge/Hilt-DI-FF9800)
![WorkManager](https://img.shields.io/badge/WorkManager-Background%20Sync-purple)
![Paging 3](https://img.shields.io/badge/Paging%203-Compose-lightblue)

![License](https://img.shields.io/badge/License-MIT-green)


---

# 📖 DailyQuote – Kotlin Jetpack Compose Tutorial

**DailyQuote** is a sample Android application built with **Jetpack Compose** that demonstrates **modern Android development practices** using clean code and real-world libraries.

This project is designed as a **learning-focused tutorial** for developers who want to understand how to build **offline-first + online-paginated apps** using **Jetpack Compose** and **StateFlow**.

---

## 🖼 Screenshots

<p align="center">
  <img src="screenshots/notification_permission.png" width="250" />  
  <img src="screenshots/daily_quote.png" width="250" />
  <img src="screenshots/all_quotes.png" width="250" />
</p>

---

## 🚀 What You’ll Learn From This Project

This project covers **end-to-end app development concepts**, including:

- Jetpack Compose UI
- State & Event handling using **StateFlow**
- Clean Architecture (UI → ViewModel → UseCase → Repository)
- Offline-first approach with **Room Database**
- Online pagination using **Compose Paging Library**
- Dependency Injection with **Hilt**
- Networking using **Retrofit**
- Background sync using **WorkManager**
- Local Push Notifications
- Asking & handling **Notification Permission** in Jetpack Compose
- Managing **manual + automatic data sync**

---

## 🧱 Tech Stack

- Kotlin
- Jetpack Compose
- StateFlow
- Room Database
- Retrofit
- Hilt (Dependency Injection)
- WorkManager
- Paging 3 (Compose Paging)
- Local Notifications

---

## 📱 App Overview

The app contains **two main screens**:

### 1️⃣ Daily Quote Screen

- Fetches one quote from server
- Displays the latest quote
- Saves quote to Room database
- Shows previously downloaded quotes
- Manual refresh
- Auto-sync using WorkManager

### 2️⃣ All Quotes Screen

- Live data from server
- Pagination using Paging Compose
- Proper loading & error handling

---

## 🔄 Architecture

UI → ViewModel (StateFlow) → Repository → Room / Retrofit

---

## 🔔 Notifications & Permissions

- Request notification permission (Android 13+)
- Handle permission in Jetpack Compose
- Show local notification after background sync

---

## 🛠 How to Run

1. Clone repository
2. Open in Android Studio
3. Sync Gradle
4. Run on device/emulator

---

## 📄 License

Free to use for learning purposes.
