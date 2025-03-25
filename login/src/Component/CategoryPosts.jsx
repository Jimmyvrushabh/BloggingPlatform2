import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import ArticleCard from "../Component/ArticleCard";

const CategoryPosts = () => {
  const { id } = useParams();
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [category, setCategory] = useState("");

  useEffect(() => {
    fetchCategoryPosts();
  }, [id]);

  const fetchCategoryPosts = async () => {
    try {
      const response = await axios.get(`http://localhost:8080/blogs/category/${id}`);
      setPosts(response.data);
      setLoading(false);

      // Fetch category name (Optional)
      const categoryResponse = await axios.get(`http://localhost:8080/categories/${id}`);
      setCategory(categoryResponse.data.name);
    } catch (error) {
      console.error("Error fetching category posts:", error);
      setLoading(false);
    }
  };

  return (
    <div className="flex flex-col items-center min-h-screen py-12 bg-gray-100">
      <div className="w-full max-w-5xl px-6">
        <h2 className="mb-6 text-3xl font-bold text-gray-900">{category} Blogs</h2>

        {loading ? (
          <p className="text-center text-gray-600">Loading posts...</p>
        ) : posts.length > 0 ? (
          <div className="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-3">
            {posts.map((post) => (
              <ArticleCard key={post.id} article={post} />
            ))}
          </div>
        ) : (
          <p className="text-center text-gray-600">No blog posts found in this category.</p>
        )}
      </div>
    </div>
  );
};

export default CategoryPosts;
