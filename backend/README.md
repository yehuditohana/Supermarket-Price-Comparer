# Backend README for myStore Application

This document explains how to set up and run the backend part of the **myStore** project, which is a Spring Boot application designed for supermarket price comparison.

---

## Prerequisites

Before you start, make sure these programs are installed on your computer:

- **Java 21 (JDK 21)** — [Download JDK 21](https://download.oracle.com/java/21/latest/jdk-21_windows-x64_bin.exe)
- **Maven (latest version)** — [Download Maven](https://maven.apache.org/download.cgi)
- **PostgreSQL (13 or higher)** — [Download PostgreSQL](https://www.postgresql.org/download/)
- **Chrome browser (latest version)** — [Download Chrome](https://www.google.com/chrome/)

### Verify Installations

After installing, you can verify the installations by running the following commands in the terminal:

```bash
java -version
mvn -v
psql --version
```

If these commands show installed versions, you're ready to continue.

> **Note:** You don't need to install Spring Boot separately; it's handled by Maven.
>
> **Note:** WebDriverManager will usually handle downloading ChromeDriver automatically.

---

## Project Overview

The project uses:
- Spring Boot 3.4.4 with JPA (Hibernate)
- PostgreSQL for the database
- Jersey (JAX-RS) for APIs
- Selenium WebDriver (for downloading supermarket files)
- XML Parsing with SAX Parser
- (Optional) Python scripts for training the product classification model

---

## Choosing How to Run the System

There are **two ways** to set up and run the system:

### 1. Ready Database (Recommended)

- **Fastest way** to run the application.
- Use the provided database backup (`mystore_backup.sql`).
- No need to download supermarket files, train a model, or install Python.
- Simply restore the backup and run the backend.

> **Use this option if you want the system ready quickly and don't need to rebuild everything manually.**

### 2. Build and Initialize the Database from Scratch

- Download supermarket price files automatically.
- Insert the data into the database.
- **Train a new product classifier model once** using Python.
- **Important:**  
  You only need to **train** the model by running `ModelTraining.py`.  
  **You do not need to classify products manually** — the Java backend automatically handles classification during startup.

---
## Setting Up the Project

### 1. Open the Project

- Download and unzip the project files.
- Open the project folder (for example, `myStore`) in your preferred IDE like IntelliJ, Eclipse, or VSCode.

### 2. Set Up the Database

- Make sure PostgreSQL is running.
- Create a database called `myStoreDB` by running:

```sql
CREATE DATABASE myStoreDB;
```

- Database default settings (configured in `src/main/resources/application.properties`):
  - URL: `jdbc:postgresql://localhost:5432/myStoreDB`
  - Username: `postgres`
  - Password: `password`

If your settings are different, update them inside the `application.properties` file.

---

## Option 1: Load a Ready Database (Recommended)

If you want a quicker setup, you can load a pre-built database backup:

- Use the provided backup file (`mystore_backup.sql`).
- Restore the database by running:

```bash
psql -U postgres -d myStoreDB -f mystore_backup.sql
```

After restoring, you can skip all Python installations and model training.

---

## Option 2: Build Everything Automatically (Full Initialization)

If you want the system to automatically download files and fill the database:

- When the app runs, it will:
  - Download XML price files.
  - Process XML files.
  - Insert data into the database.
  - Classify products automatically in Java using a pre-trained model.

#### Additional Step: Train the Product Classifier

Before running the application for the first time, you need to **train the product classifier**.

#### Requirements:

- **Python 3.9 or higher** — [Download Python](https://www.python.org/downloads/)
- Required Python libraries:

```bash
pip install pandas scikit-learn joblib
```

- Make sure you have these files ready:
  - `python/ModelTraining.py`
  - `python/tagged_products.csv`

#### How to Train the Model:

1. Install the required libraries (if not already installed):

```bash
pip install pandas scikit-learn joblib
```

2. Make sure the necessary files are in place:
  - `python/ModelTraining.py`
  - `python/tagged_products.csv`

3. Run the model training script:

```bash
python cd python
python ModelTraining.py
```

This will create a file called `product_classifier.pkl` inside the `python/` directory.

You do **not** need to classify products manually — the Java backend will use this model automatically.

---

## Running the Application

In the root of the project, run:

```bash
mvn spring-boot:run
```

The server will start at:

```
http://localhost:8080/api
```

---

## Additional Information

- If you choose to build everything automatically, the process may take a few minutes due to file downloading and data processing.
- Most configurations (database paths, file paths, etc.) are managed inside `application.properties`.
- Logging has been minimized to make the console output easier to follow.
---
