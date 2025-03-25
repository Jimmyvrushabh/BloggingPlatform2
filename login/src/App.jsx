// src/App.jsx
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";

import Home from "./Home";
import BlogDetail from "./Component/BlogDetail";
import AddBlog from "./Component/AddBlog";
import UserDashboard from "./Component/UserDashboard";
import Navbar from "./Component/Navbar";
import CategoryPosts from "./Component/CategoryPosts";
import Sidebar from "./Component/Sidebar";
import SearchResults from "./Component/SearchResults";
import Login from "./Components/Login";
import Register from "./Components/Register";
import Dashboard from "./Components/Dashboard";
import UserDashboard1 from "./Component/UserDashboard1";
import { useState } from "react";


function App() {
 
    const [user, setUser] = useState(JSON.parse(localStorage.getItem("user")));
    return (
       
        <div className="flex flex-col min-h-screen">
        {/* Navbar */}
      <Navbar/>

        {/* Sidebar (Directly Below Navbar) */}
        <Sidebar />

        {/* Main Content */}
        <div className="flex-grow p-4">
             

                <Routes>
                  
                    <Route path="/register" element={<Register />} />
                    <Route path="/" element={<Login />} />

                 
                    <Route path="/dashboard" element={<Dashboard />} />
                    <Route path="/add" element={<AddBlog />} />
                    <Route path="/User" element={<UserDashboard />} />

                
                    <Route path="/home" element={<Home />} />
                    <Route path="/post/:id" element={<BlogDetail />} />
                    <Route path="/category/:id" element={<CategoryPosts />} />
                    <Route path="/search" element={<SearchResults />} />
                    <Route path="/user-dashboard2" element={<UserDashboard1 />} />
                </Routes>
            </div>
            </div>
    );
}

export default App;



