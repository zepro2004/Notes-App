# 📝 Notes + To-Do Application

[![Build Status](https://img.shields.io/badge/build-passing-green)](https://github.com/zepro2004/Notes-App)
[![License](https://img.shields.io/badge/license-MIT-blue)](LICENSE)

*A simple yet effective desktop application for managing your tasks and notes, built with Java Swing and MySQL/MariaDB.*

---

## 🎨 Screenshots

*A quick preview of the user interface:*

<div>
  <img src="https://github.com/user-attachments/assets/6d91bd18-1505-43e6-b780-210343825b3a" alt="Notes Page Interface" width="400"/>
  <img src="https://github.com/user-attachments/assets/91f5e5f3-f5e9-4e09-988f-06161c4ddee7" alt="ToDo List Interface" width="400"/>
</div>

---

## 📌 Core Features

* **Task Management:**

    * ✅ Create, edit, and delete tasks efficiently
    * 📊 Track completion status (Completed / Pending)

* **Reliable Data Persistence:**

    * 💾 Stores all tasks and notes locally using **MySQL/MariaDB**
    * 🔒 Data remains saved even after closing the application

* **Intuitive Graphical Interface:**

    * 🖥️ Built using **Java Swing** for a familiar desktop experience
    * ✨ Clean and user-friendly design

* **Quality Assured:**

    * 🧪 Core logic tested using **JUnit** for reliability

---

## 🚀 Technologies & Tools

* **Core Language:** Java (JDK 21)
* **GUI Framework:** Java Swing
* **Database:** MySQL or MariaDB
* **DB Connectivity:** JDBC (MySQL/MariaDB Connector)
* **Build Tool:** Maven
* **Testing:** JUnit 5
* **IDE:** Developed using Eclipse / IntelliJ IDEA

---

## 💻 Getting Started

### ✅ Prerequisites

1. **Java Development Kit (JDK):** Version 21 or higher installed
2. **MySQL or MariaDB:** Database server installed and running
3. **Maven:** For dependency management and building

### ✅ Database Setup

1. **Create a database** in your MySQL or MariaDB server
2. **Create the necessary tables** (detailed SQL setup will be provided in a separate file)

### ✅ Configuration Setup

#### For MySQL:

Create a properties file at `src/main/resources/db.properties`:

```properties
jdbc.url=jdbc:mysql://localhost:3306/YOUR_DATABASE_NAME
jdbc.username=your_username
jdbc.password=your_password
```

#### For MariaDB:

Create a properties file at `src/main/resources/db.properties`:

```properties
jdbc.url=jdbc:mariadb://localhost:3306/YOUR_DATABASE_NAME
jdbc.username=your_username
jdbc.password=your_password
```

> ⚠️ Note: The `db.properties` file is included in `.gitignore` to prevent sharing sensitive credentials.

---

### 📦 Installation & Running

1. **Clone the repository:**
    ```bash
    git clone https://github.com/zepro2004/Notes-App.git
    cd Notes-App
    ```

2. **Build with Maven:**
    ```bash
    mvn clean package
    ```

3. **Run the application:**
    ```bash
    mvn exec:java
    ```
   Or run the generated JAR file:
    ```bash
    java -jar target/notes-todo-app-1.0-SNAPSHOT.jar
    ```

4. **IDE Setup:**
    - Open the project in your IDE
    - Ensure `src/main/resources` is marked as a resources directory
    - Run `main.App` as the main class

---

## 📈 Future Enhancements

*   **Integrated Notes:**
    *   ✍️ Link detailed notes directly to specific tasks
    *   📁 Create and edit notes within the task context
*   🔢 **Priorities:** Set task priorities (High, Medium, Low).
*   🔍 **Search & Filter:** Filter by title, date, status, etc.
*   🔄 **Recurring Tasks:** Add support for recurring tasks.
*   🔔 **Reminders:** Task reminders and notifications.
*   📤📥 **Data Export/Import:** Support for exporting/importing data (e.g., CSV).
*   ☁️ **Cloud Sync (Advanced):** Explore syncing across devices.

---

## 🛠️ Contributing

Contributions are welcome! To help improve the application:

1. **Fork** the repository
2. Create a **new branch**:

   ```bash
   git checkout -b feature/YourFeatureName
   ```
3. Make your changes and **commit** them:

   ```bash
   git commit -m "Add some feature"
   ```
4. **Push** to the branch:

   ```bash
   git push origin feature/YourFeatureName
   ```
5. Open a **Pull Request**

---

## 📜 License

This project is licensed under the **MIT License**. See the [LICENSE](LICENSE) file for details.
