import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getPostById } from "../Api";
import CommentSection from "./CommentSection";

function BlogDetail() {
  const { id } = useParams();
  const [post, setPost] = useState(null);

  useEffect(() => {
    getPostById(id)
      .then((response) => setPost(response.data))
      .catch((error) => console.error("Error fetching post:", error));
  }, [id]);

  if (!post) return <p className="mt-10 text-center text-gray-500">Loading...</p>;

  return (
    <div className="flex flex-col min-h-screen bg-gray-100">
      {/* Blog Content */}
      <div className="flex items-center justify-center flex-1 px-6 py-12">
        <div className="w-full max-w-4xl p-8 bg-white rounded-lg shadow-lg">
          <h1 className="text-4xl font-bold leading-tight text-gray-900">
            {post.title}
          </h1>
          <p className="mt-2 text-sm text-gray-500">Published on {post.createdAt}</p>
          <p className="mt-2 text-sm text-gray-500">  Publishby {post.author.username}</p>

          <div className="pt-6 mt-6 text-lg leading-relaxed text-gray-800 border-t">
            {post.content}
          </div>
        </div>
      </div>

      {/* Comment Section */}
      <div className="px-6 py-8 bg-white shadow-lg">
        <CommentSection blogPostId={id} />
      </div>
    </div>
  );
}

export default BlogDetail;
