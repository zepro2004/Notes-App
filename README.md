# 📝 Notes + To-Do Application

[![Build Status](https://img.shields.io/badge/build-passing-green)](https://github.com/zepro2004/Notes-and-ToDo-Application)
[![License](https://img.shields.io/badge/license-MIT-blue)](LICENSE)

*A simple yet effective desktop application for managing your tasks and notes, built with Java Swing and SQLite.*

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
    * ✅ Create, Edit, Delete tasks efficiently.
    
    * 📊 Track completion status (Completed / Pending).

* **Reliable Data Persistence:**
    * 💾 Stores all tasks and notes locally using **SQLite**.
    * 🔒 Your data remains saved even after closing the application.

* **Intuitive Graphical Interface:**
    * 🖱️ Built using **Java Swing** for a familiar desktop experience.
    * ✨ Clean and user-friendly design.

* **Quality Assured:**
    * 🧪 Core logic tested using **JUnit** to ensure reliability.

---

## 🚀 Technologies & Tools

* **Core Language:** Java (JDK 8+)
* **GUI Framework:** Java Swing
* **Database:** SQLite
* **DB Connectivity:** JDBC (SQLite JDBC Driver)
* **Testing:** JUnit
* **IDE:** Developed using Eclipse / IntelliJ IDEA

---

## 💻 Getting Started

### Prerequisites

* **Java Development Kit (JDK):** Version 8 or higher installed.
* **SQLite JDBC Driver:** Download the `.jar` file. You can find it [here (e.g., link to Maven Central)](https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc) or search "SQLite JDBC Driver download".

### Installation & Running

1.  **Clone the Repository:**
    ```bash
    git clone [https://github.com/zepro2004/Notes-and-ToDo-Application.git](https://github.com/zepro2004/Notes-and-ToDo-Application.git)
    cd Notes-and-ToDo-Application
    ```
2.  **Configure IDE:**
    * Open the project folder in your preferred Java IDE (Eclipse, IntelliJ IDEA, etc.).
    * Add the downloaded **SQLite JDBC driver `.jar` file** to your project's build path/classpath. (Consult your IDE's documentation if unsure).
3.  **Compile & Run:**
    * Locate the main application file (e.g., `App.java` or similar).
    * Compile and run this file through your IDE.

---

## 📈 Future Enhancements

      
* **Integrated Notes:**
    * ✍️ Link detailed notes directly to specific tasks.
    * 📑 Create and edit notes easily within the task context.
* 🔢 Set task priorities (High, Medium, Low).
* **Search & Filter:** Implement robust search and filtering options (by title, date, status).
* **Recurring Tasks:** Add support for setting up recurring tasks.
* **Reminders:** Integrate task reminders and notifications.
* **Data Export/Import:** Allow users to export/import their data (e.g., CSV).
* **Cloud Sync (Advanced):** Explore options for syncing data across devices.

---

## 🛠️ Contributing

Contributions are welcome! If you'd like to help improve the application:

1.  **Fork** the repository.
2.  Create a **new branch** (`git checkout -b feature/YourFeatureName`).
3.  Make your changes and **commit** them (`git commit -m 'Add some feature'`).
4.  **Push** to the branch (`git push origin feature/YourFeatureName`).
5.  Open a **Pull Request**.

---

## 📜 License

This project is licensed under the **MIT License**. See the [LICENSE](LICENSE) file for details.
