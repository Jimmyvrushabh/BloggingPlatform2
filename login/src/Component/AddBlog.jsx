import { useState, useEffect } from "react";
import axios from "axios";

const AddBlog = () => {
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [categories, setCategories] = useState([]);
  const [categoryId, setCategoryId] = useState("");
  const [newCategory, setNewCategory] = useState(""); // New category input
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchCategories();
  }, []);

  const fetchCategories = async () => {
    try {
      const response = await axios.get("http://localhost:8080/categories");
      setCategories(response.data);
    } catch (err) {
      console.error("Error fetching categories:", err);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const token = localStorage.getItem("token");
    const username = localStorage.getItem("username");

    const blogData = {
      title,
      content,
      author:{ username : username},
      category: { id: categoryId },  // Ensure the category object has an id field
    };

    try {
      const response = await axios.post("http://localhost:8080/blogs/add", blogData, {
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });

      console.log("Blog created:", response.data);
      alert("Blog post created successfully!");
      setTitle("");
      setContent("");
      setCategoryId("");
      
    } catch (error) {
      console.error("Error creating blog post:", error);
      setError(error.response?.data?.message || "Error creating blog.");
    }
};

  const handleAddCategory = async () => {
    if (!newCategory.trim()) return;

    try {
      const response = await axios.post("http://localhost:8080/categories", {
        name: newCategory,
      });

      setCategories([...categories, response.data]); // Update dropdown dynamically
      setNewCategory(""); // Clear input
    } catch (error) {
      console.error("Error adding category:", error);
      alert("Failed to add category.");
    }
  };

  return (
    <div className="max-w-2xl p-6 mx-auto mt-10 bg-white border border-gray-200 shadow-md rounded-2xl">
      <h2 className="mb-6 text-3xl font-bold text-gray-900">📝 Create a Blog Post</h2>

      {error && <p className="p-3 mb-4 text-sm text-red-600 bg-red-100 border border-red-300 rounded-md">{error}</p>}

      <form onSubmit={handleSubmit} className="space-y-5">
        <div>
          <label className="block mb-2 text-sm font-medium text-gray-700">Title</label>
          <input
            type="text"
            placeholder="Enter your blog title..."
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            className="w-full px-4 py-2 text-gray-800 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none"
            required
          />
        </div>

        <div>
          <label className="block mb-2 text-sm font-medium text-gray-700">Content</label>
          <textarea
            placeholder="Write your blog content here..."
            value={content}
            onChange={(e) => setContent(e.target.value)}
            className="w-full px-4 py-3 text-gray-800 border border-gray-300 rounded-lg resize-none focus:ring-2 focus:ring-blue-500 focus:outline-none"
            rows="6"
            required
          ></textarea>
        </div>

        {/* Dropdown for Category Selection */}
        <div>
          <label className="block mb-2 text-sm font-medium text-gray-700">Category</label>
          <select
            value={categoryId}
            onChange={(e) => setCategoryId(e.target.value)}
            className="w-full px-4 py-2 text-gray-800 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none"
            required
          >
            <option value="">Select Category</option>
            {categories.map((cat) => (
              <option key={cat.id} value={cat.id}>
                {cat.name}
              </option>
            ))}
          </select>
        </div>

        {/* Add New Category Section */}
        <div className="flex items-center gap-2">
          <input
            type="text"
            placeholder="New category name..."
            value={newCategory}
            onChange={(e) => setNewCategory(e.target.value)}
            className="flex-1 px-4 py-2 text-gray-800 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none"
          />
          <button
            type="button"
            onClick={handleAddCategory}
            className="px-4 py-2 text-white transition-all bg-green-500 rounded-lg hover:bg-green-600"
          >
            ➕ Add
          </button>
        </div>

        <button type="submit" className="w-full py-3 text-lg font-medium text-white transition-all bg-blue-600 rounded-lg hover:bg-blue-700">
          🚀 Publish Blog
        </button>
      </form>
    </div>
  );
};

export default AddBlog;
