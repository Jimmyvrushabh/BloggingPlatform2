import React, { useEffect, useState } from "react";
import axios from "axios";
import ArticleCard from "../Component/ArticleCard";
import Dashboard from "../Components/Dashboard";

const Bloglist = () => {
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(0);
  const [size] = useState(9); // Number of posts per page

  useEffect(() => {
    fetchPosts();
  }, [page]);

  const fetchPosts = async () => {
    setLoading(true); // ✅ Set loading before API call
    try {
      const response = await axios.get(`http://localhost:8080/blogs/paginated`, {
        params: { page, size, sortBy: "createdAt", sortDir: "desc" }, // ✅ Include sorting params
      });
      setPosts(response.data.content);
    } catch (error) {
      console.error("Error fetching posts:", error.response?.data || error.message);
    } finally {
      setLoading(false); // ✅ Ensure loading is set to false in all cases
    }
  };
  

  return (
    <div className="flex flex-col md:flex-row">
      {/* Sidebar - If you have one */}
      <div className="hidden w-full p-4 bg-gray-100 md:w-1/4 md:block">
        <Dashboard />
      </div>

      {/* Main Content */}
      <div className="w-full p-6 mx-auto md:w-3/4">
        <h1 className="mb-4 text-2xl font-bold text-gray-800">Latest Blog Posts</h1>

        {/* Blog Articles Grid */}
        {loading ? (
          <p className="text-center text-gray-500 animate-pulse">Loading posts...</p>
        ) : posts.length > 0 ? (
          <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
            {posts.map((post) => (
              <ArticleCard key={post.id} article={post} />
            ))}
          </div>
        ) : (
          <p className="text-center text-gray-500">No blog posts found.</p>
        )}

        {/* Pagination */}
        <div className="flex justify-between mt-6">
          <button
            disabled={page === 0}
            onClick={() => setPage(page - 1)}
            className="px-4 py-2 text-white bg-blue-600 rounded-lg hover:bg-blue-700 disabled:opacity-50"
          >
            ◀️ Previous
          </button>
          <button
            onClick={() => setPage(page + 1)}
            className="px-4 py-2 text-white bg-blue-600 rounded-lg hover:bg-blue-700"
          >
            Next ▶️
          </button>
        </div>
      </div>
    </div>
  );
};

export default Bloglist;
