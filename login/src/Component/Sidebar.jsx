import React, { useEffect, useState } from "react";
import axios from "axios";
import { Link } from "react-router-dom";

const Sidebar = () => {
  const [categories, setCategories] = useState([]); // Ensure initial state is an array
  const [error, setError] = useState(null); // Track API errors

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const response = await axios.get("http://localhost:8080/categories");

        if (Array.isArray(response.data)) {
          setCategories(response.data);
        } else {
          throw new Error("Invalid response format: Expected an array");
        }
      } catch (error) {
        console.error("Error fetching categories:", error);
        setError("Failed to load categories");
      }
    };

    fetchCategories();
  }, []);

  return (
    <nav className="w-full p-4 mt-16 text-white bg-gray-800 shadow-md">
      <h2 className="mb-3 text-lg font-semibold">Categories</h2>
      
      {/* Handle API errors */}
      {error && <p className="text-red-400">{error}</p>}

      <div className="flex flex-wrap gap-3">
        {categories.length > 0 ? (
          categories.map((category) => (
            <Link
              key={category.id}
              to={`/category/${category.id}`}
              className="px-4 py-2 text-white transition bg-blue-600 rounded-md hover:bg-blue-700"
            >
              {category.name}
            </Link>
          ))
        ) : !error ? (
          <p className="text-gray-400">No categories found.</p>
        ) : null}
      </div>
    </nav>
  );
};

export default Sidebar;
