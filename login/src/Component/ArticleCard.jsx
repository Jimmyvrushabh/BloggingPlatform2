import React from "react";
import { Link } from "react-router-dom";

const ArticleCard = ({ article }) => {
  return (
    <div className="p-5 transition-all bg-white border border-gray-200 shadow-md rounded-2xl hover:shadow-lg">
      {/* Clickable Blog Title */}
      <Link to={`/post/${article.id}`} className="block mb-2 text-xl font-semibold text-gray-800 transition hover:text-blue-600">
        {article.title}
      </Link>

      {/* Blog Excerpt */}
      <p className="text-sm leading-relaxed text-gray-600">{article.content?.slice(0, 120)}...</p>

      {/* Author & Date Info */}
      <div className="flex items-center justify-between mt-4 text-sm text-gray-500">
        <span className="font-medium">{article.author?.username || "Unknown Author"}</span>
        <span>{article.createdAt ? new Date(article.createdAt).toLocaleDateString() : "Unknown Date"}</span>
      </div>
    </div>
  );
};

export default ArticleCard;
