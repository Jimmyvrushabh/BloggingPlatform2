import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";

function Navbar() {
  const [searchTerm, setSearchTerm] = useState("");
  const [menuOpen, setMenuOpen] = useState(false);
  const navigate = useNavigate();
  const isAuthenticated = localStorage.getItem("token");

  const handleSearch = (e) => {
    e.preventDefault();
    if (searchTerm.trim()) {
      navigate(`/search?keyword=${encodeURIComponent(searchTerm)}`);
    }
  };

  const handleLogout = () => {
    localStorage.removeItem("user");
    localStorage.removeItem("token");
    alert("Session expired. Please log in again.");
    navigate("/");
  };
  return (
    <nav className="fixed top-0 left-0 z-50 w-full shadow-lg bg-gradient-to-r from-blue-700 to-indigo-800">
      <div className="flex items-center justify-between px-6 py-4 mx-auto max-w-7xl">
        {/* Logo */}
        <Link to="/home" className="flex items-center text-2xl font-bold text-white">
          ✍️ Blog<span className="ml-1 text-yellow-400">App</span>
        </Link>

        {/* Search Bar */}
        <form
          onSubmit={handleSearch}
          className="hidden overflow-hidden bg-white rounded-full shadow-md md:flex"
        >
          <input
            type="text"
            placeholder="Search blogs..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="w-56 p-2 text-gray-700 transition-all outline-none focus:w-72"
          />
          <button
            type="submit"
            className="px-4 py-2 text-white transition-all bg-blue-600 hover:bg-blue-700"
          >
            🔍
          </button>
        </form>

        {/* Desktop Links */}
        <div className="items-center hidden space-x-6 md:flex">
          <Link to="/home" className="text-lg text-white hover:text-yellow-300">Home</Link>
          <Link to="/add" className="text-lg text-white hover:text-yellow-300">Add Blog</Link>
          <Link to="/user-dashboard2" className="text-lg text-white hover:text-yellow-300">Dashboard</Link>
          {isAuthenticated && (
            <button
              onClick={handleLogout}
              className="px-4 py-2 text-white transition bg-red-500 rounded-full hover:bg-red-600"
            >
              🚪 Logout
            </button>
          )}
        </div>

        {/* Mobile Menu Button */}
        <button className="text-3xl text-white md:hidden" onClick={() => setMenuOpen(!menuOpen)}>
          ☰
        </button>
      </div>

      {/* Mobile Menu */}
      {menuOpen && (
        <div className="p-4 space-y-3 bg-blue-900 md:hidden">
          <Link to="/home" className="block text-lg text-white hover:text-yellow-300">Home</Link>
          <Link to="/add" className="block text-lg text-white hover:text-yellow-300">Add Blog</Link>
          <Link to="/user-dashboard2" className="block text-lg text-white hover:text-yellow-300">Dashboard</Link>
          {isAuthenticated && (
            <button
              onClick={handleLogout}
              className="w-full px-4 py-2 text-white transition bg-red-500 rounded-full hover:bg-red-600"
            >
              🚪 Logout
            </button>
          )}
        </div>
      )}
    </nav>
  );
}

export default Navbar;
