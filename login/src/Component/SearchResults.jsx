import React, { useEffect, useState } from "react";
import { useSearchParams } from "react-router-dom";
import axios from "axios";
import ArticleCard from "../Component/ArticleCard";

const SearchResults = () => {
  const [searchParams] = useSearchParams();
  const keyword = searchParams.get("keyword");
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (keyword) {
      fetchSearchResults();
    }
  }, [keyword]);

  const fetchSearchResults = async () => {
    try {
      const response = await axios.get(`http://localhost:8080/blogs/search?keyword=${encodeURIComponent(keyword)}`);
      setPosts(response.data);
      setLoading(false);
    } catch (error) {
      console.error("Error fetching search results:", error);
      setLoading(false);
    }
  };

  return (
    <div className="container mx-auto px-4 md:px-16 mt-16">
      <h2 className="text-xl font-semibold mb-4">Search Results for "{keyword}"</h2>
      {loading ? (
        <p>Loading...</p>
      ) : posts.length > 0 ? (
        posts.map((post) => <ArticleCard key={post.id} article={post} />)
      ) : (
        <p>No blog posts found.</p>
      )}
    </div>
  );
};

export default SearchResults;
