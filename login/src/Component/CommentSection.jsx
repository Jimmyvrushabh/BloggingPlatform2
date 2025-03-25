import React, { useState, useEffect } from "react";
import { getComments, addComment, deleteComment } from "../Api";

const CommentSection = ({ blogPostId }) => {
  const [comments, setComments] = useState([]);
  const [newComment, setNewComment] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (blogPostId) {
      fetchComments();
    }
  }, [blogPostId]);

  const fetchComments = async () => {
    setLoading(true);
    try {
      const response = await getComments(blogPostId);
      console.log("Fetched comments:", response.data); // Debugging
      setComments(response.data || []);
    } catch (error) {
      console.error("Error fetching comments:", error);
    } finally {
      setLoading(false);
    }
  };
  
  const handleAddComment = async () => {
    if (!newComment.trim()) return;

    try {
      const response = await addComment(blogPostId, newComment);
      setNewComment("");

      if (response.data) {
        setComments((prev) => [response.data, ...prev]);
      } else {
        fetchComments();
      }
    } catch (error) {
      console.error("Error adding comment:", error);
    }
  };

  const handleDeleteComment = async (commentId) => {
    try {
      await deleteComment(commentId);
      setComments((prev) => prev.filter((comment) => comment.id !== commentId));
    } catch (error) {
      console.error("Error deleting comment:", error);
    }
  };

  return (
    <div className="p-6 mt-6 bg-white rounded-lg shadow-lg">
      <h2 className="mb-4 text-lg font-semibold text-gray-900">💬 Comments</h2>

      {/* Input Field */}
      <div className="flex items-center gap-3">
        <input
          type="text"
          value={newComment}
          onChange={(e) => setNewComment(e.target.value)}
          placeholder="Write a comment..."
          className="w-full px-4 py-2 text-sm border rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none"
        />
        <button
          onClick={handleAddComment}
          className="px-4 py-2 text-sm font-medium text-white transition bg-blue-600 rounded-lg hover:bg-blue-700"
        >
          Add
        </button>
      </div>

      {/* Comments List */}
      <div className="p-3 mt-4 overflow-y-auto rounded-lg shadow-inner bg-gray-50 max-h-60">
        {loading ? (
          <p className="text-sm text-center text-gray-500">Loading comments...</p>
        ) : comments.length === 0 ? (
          <p className="text-sm text-center text-gray-500">No comments yet. Be the first! 😊</p>
        ) : (
          <ul className="divide-y divide-gray-300">
            {comments.map((comment) => (
              <li key={comment.id} className="flex items-center justify-between py-3">
                <div>
                <p className="text-sm font-semibold text-gray-800">
  {typeof comment.commenter === "object" 
    ? comment.commenter.username // Assuming 'commenter' is an object with 'username' key
    : comment.commenter || "Anonymous"}
</p>
                  <p className="text-xs text-gray-600">{comment.content}</p>
                </div>
                <button
                  onClick={() => handleDeleteComment(comment.id)}
                  className="text-xs text-red-500 transition hover:underline hover:text-red-700"
                >
                  ❌ Delete
                </button>
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
};

export default CommentSection;
