#  Online Bookstore Management System

An end-to-end **Java + SQL** application that allows users to browse, search, and purchase books online, while enabling administrators to manage inventory, customers, and orders efficiently.

---

## Features

###  User Module
- Register and log in securely  
- Browse books by category, author, or title  
- Search books using keywords  
- Add books to the cart and place orders  
- View order history and invoices  

###  Admin Module
- Add, edit, or delete books  
- Manage stock and pricing  
- View customers and order history  
- Generate sales reports  

---

##  Tech Stack

| Layer | Technology |
|-------|-------------|
| **Frontend** | Java Swing / JavaFX (CLI optional) |
| **Backend** | Core Java (OOP, JDBC) |
| **Database** | MySQL |
| **Connectivity** | JDBC (MySQL Connector/J) |
| **Build Tool** | Maven / Manual Compilation |
| **IDE** | IntelliJ IDEA / Eclipse / VS Code |

---

##  Database Design

**Tables:**
- `users(user_id, name, email, password, address)`  
- `books(book_id, title, author, category, price, quantity)`  
- `orders(order_id, user_id, order_date, total_amount)`  
- `order_items(order_item_id, order_id, book_id, quantity, price)`  

---

##   Concepts Used

- Object-Oriented Programming (Encapsulation, Inheritance, Polymorphism)

- CRUD operations with JDBC

- MVC design pattern

- Exception handling & input validation

- SQL joins and relationships

- Basic authentication & session handling
