import React, { useState, useEffect } from "react";
import { getAllPosts, deletePost, updatePost } from "../Api";
import { useNavigate } from "react-router-dom";
import Navbar from "./Navbar";

function UserDashboard() {
  const [posts, setPosts] = useState([]);
  const [editId, setEditId] = useState(null);
  const [editTitle, setEditTitle] = useState("");
  const [editContent, setEditContent] = useState("");
  const navigate = useNavigate();

  // ✅ Fetch All Blog Posts
  useEffect(() => {
    fetchPosts();
  }, []);

  const fetchPosts = async () => {
    try {
      const response = await getAllPosts();
      setPosts(response.data);
    } catch (error) {
      console.error("Error fetching posts:", error);
    }
  };

  //  Handle Delete
  const handleDelete = async (id) => {
    try {
      await deletePost(id);
      setPosts(posts.filter((post) => post.id !== id));
    } catch (error) {
      console.error("Error deleting post:", error);
    }
  };

  // Handle Edit (Set Values)
  const handleEdit = (post) => {
    setEditId(post.id);
    setEditTitle(post.title);
    setEditContent(post.content);
  };

  //  Handle Update
  const handleUpdate = async () => {
    try {
      await updatePost(editId, { title: editTitle, content: editContent });
      setPosts(
        posts.map((post) =>
          post.id === editId ? { ...post, title: editTitle, content: editContent } : post
        )
      );
      setEditId(null);
    } catch (error) {
      console.error("Error updating post:", error);
    }
  };

  return (
    <>
    <Navbar/>
    <div className="container mx-auto p-6">
      <h2 className="text-2xl font-bold text-center mb-4 mt-20">📋 User Dashboard</h2>

      {/* Blog Posts Table */}
      <div className="overflow-x-auto">
        <table className="min-w-full border border-gray-300 bg-white shadow-md mt-20">
          <thead>
            <tr className="bg-gray-200 text-gray-700">
              <th className="py-2 px-4 border">Title</th>
              <th className="py-2 px-4 border">Content</th>
              <th className="py-2 px-4 border">Actions</th>
            </tr>
          </thead>
          <tbody>
            {posts.map((post) => (
              <tr key={post.id} className="border">
                {/* Edit Mode */}
                {editId === post.id ? (
                  <>
                    <td className="py-2 px-4 border">
                      <input
                        type="text"
                        value={editTitle}
                        onChange={(e) => setEditTitle(e.target.value)}
                        className="border p-1 w-full"
                      />
                    </td>
                    <td className="py-2 px-4 border">
                      <input
                        type="text"
                        value={editContent}
                        onChange={(e) => setEditContent(e.target.value)}
                        className="border p-1 w-full"
                      />
                    </td>
                    <td className="py-2 px-4 border flex gap-2">
                      <button
                        onClick={handleUpdate}
                        className="bg-green-500 text-white px-3 py-1 rounded"
                      >
                        Save
                      </button>
                      <button
                        onClick={() => setEditId(null)}
                        className="bg-gray-500 text-white px-3 py-1 rounded"
                      >
                        Cancel
                      </button>
                    </td>
                  </>
                ) : (
                  <>
                    <td className="py-2 px-4 border">{post.title}</td>
                    <td className="py-2 px-4 border">{post.content}</td>
                    <td className="py-2 px-4 border flex gap-2">
                      <button
                        onClick={() => handleEdit(post)}
                        className="bg-blue-500 text-white px-3 py-1 rounded"
                      >
                        ✏ Edit
                      </button>
                      <button
                        onClick={() => handleDelete(post.id)}
                        className="bg-red-500 text-white px-3 py-1 rounded"
                      >
                        🗑 Delete
                      </button>
                    </td>
                  </>
                )}
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
    <footer/>
    </>
  );
}

export default UserDashboard;
