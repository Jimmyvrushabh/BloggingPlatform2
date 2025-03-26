# # 📖 Blogging Platform

A **full-stack blogging platform** built with **Spring Boot, MySQL, and React**. This platform enables users to create, edit, and manage blog posts with advanced features like **nested comments, categories & tags, search, and pagination**. It also supports **JWT and Google authentication** for secure access.

---

## 🚀 Features

✅ **User Authentication** (JWT & Google Login)  
✅ **Create, Read, Update, Delete (CRUD)** for blog posts  
✅ **Nested Comments** for engaging discussions  
✅ **Categories & Tags** for better organization  
✅ **Search & Pagination** for efficient navigation  
✅ **Role-based Access Control** (Admin, Author, Reader)  
✅ **Responsive UI** using React & Tailwind CSS  
✅ **RESTful API for backend**

---

## 🏗️ Tech Stack

### **Backend:**  
🔹 Spring Boot (REST API)  
🔹 Spring Security (Authentication & Authorization)  
🔹 JWT (JSON Web Token)  
🔹 MySQL (Database)  
🔹 Hibernate/JPA (ORM)  
🔹 Lombok (Boilerplate reduction)  
🔹 WebSockets (Real-time notifications)  

### **Frontend:**  
🎨 React.js (Frontend framework)  
🎨 Tailwind CSS (Styling)  
🎨 React Query (Data fetching & caching)  
🎨 Redux Toolkit (State management)  

---

## ⚡ Installation & Setup

### **Backend Setup**
```bash
# Clone the repository
git clone https://github.com/yourusername/blogging-platform.git
cd blogging-platform/backend

# Configure application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/blog_db
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update

# Build & run the backend
mvn clean install
mvn spring-boot:run
```

### **Frontend Setup**
```bash
cd ../frontend

# Install dependencies
npm install

# Start the React app
npm start
```

---

## 📺 Demo & Video

[![Watch the video](https://img.youtube.com/vi/your-video-id/maxresdefault.jpg)](https://www.youtube.com/watch?v=your-video-id)

🎬 Click **[here](https://www.youtube.com/watch?v=your-video-id)** to watch the demo!  

---

## 📜 API Documentation

📌 Explore API endpoints with **Swagger UI**:  
```
http://localhost:8080/swagger-ui/
```

---

## 🤝 Contributing

1. **Fork** the repository  
2. Create a **feature branch** (`git checkout -b feature-name`)  
3. **Commit** your changes (`git commit -m "Add feature"`)  
4. **Push** to the branch (`git push origin feature-name`)  
5. **Create a pull request** 🚀

---

## 📄 License

This project is **MIT Licensed**. Feel free to use and contribute! 🎉
