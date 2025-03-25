// src/Api.jsx
import axios from "axios";
const BASE_URL = "http://localhost:8080/login";

export const registerUser = async (user) => {
    try {
      const response = await fetch(`${BASE_URL}/register`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(user),
      });
  
      const data = await response.json();
      
      if (!response.ok) {
        throw new Error(data.error || "Registration failed.");
      }
      
      return data;
    } catch (error) {
      return { error: error.message };
    }
  };
  
  
  export const loginUser = async (credentials) => {
    try {
        const response = await axios.post(`${BASE_URL}/log`, credentials);
    
        console.log("Login Response:", response.data); // ✅ Debug API response
    
        if (response.data.token && response.data.user) {
            localStorage.setItem("token", response.data.token);  
            localStorage.setItem("user", JSON.stringify(response.data.user));  // ✅ Store full user object
        }
    
        return response.data;
    } catch (error) {
        console.error("Login failed:", error);
        throw error;
    }
};

  
  

export const verifyToken = async (token) => {
  const response = await fetch(`${BASE_URL}/verify`, {
    method: "GET",
    headers: { Authorization: `Bearer ${token}` },
  });
  return response.json();
};

export const verifyTokenn = async (token) => {
  const response = await fetch(`${BASE_URL}/verifyy`, {
    method: "GET",
    headers: { Authorization: `Bearer ${token}` },
  });
  return response.json();
};


;






const API_BASE_URL ="http://localhost:8080";

export const getAllPosts = () => axios.get(`${API_BASE_URL}/blogs/all`);


export const createPost = async (postData) => {
    const token = localStorage.getItem("token"); // Get stored token
    return axios.post("http://localhost:8080/blogs", postData, {
        headers: {
            Authorization: `Bearer ${token}`,  // Attach JWT token
            "Content-Type": "application/json"
        }
    });
};
export const searchByKeyword = (keyword) => axios.get(`${API_BASE_URL}/blogs/search?keyword=${keyword}`);


// ✅ Delete Post
export const deletePost = async (id) => {
  const token = localStorage.getItem("token");

  if (!token) {
    alert("Authentication required! Please log in.");
    return;
  }

  try {
    await axios.delete(`http://localhost:8080/blogs/${id}`, {
      headers: { Authorization: `Bearer ${token}` },
    });

    alert("Blog deleted successfully!");
  } catch (error) {
    console.error("Error deleting blog:", error);
    alert("Delete failed! You may not have permission.");
  }
};

// ✅ Update Post
export const updatePost = async (id, post) => {
  const token = localStorage.getItem("token");

  if (!token) {
    alert("Authentication required! Please log in.");
    return;
  }

  try {
    const response = await axios.put(`${API_BASE_URL}/blogs/${id}`, post, {
      headers: { Authorization: `Bearer ${token}` },
    });

    return response.data; // Return updated post data
  } catch (error) {
    console.error("Error updating blog:", error);
    alert("Update failed! You may not have permission.");
  }
};




// Search blog posts by title
export const searchBlogPosts = (keyword) => {
    return axios.get(`${API_BASE_URL}/search`, { params: { keyword } });
  };

  export const getPaginatedPosts = async (page, size, sortBy = "createdAt", sortDir = "asc") => {
    try {
      const response = await axios.get(`http://localhost:8080/blogs/paginated`, {
        params: { page, size, sortBy, sortDir }, // ✅ Use params for cleaner URL
      });
      return response.data; // { content, totalPages }
    } catch (error) {
      console.error("Error fetching paginated posts:", error.response?.data || error.message);
      return { content: [], totalPages: 1 }; // Prevents crashes
    }
  };
  
  


// Fetch a single post by ID
export const getPostById = async (id) => {
  return axios.get(`${API_BASE_URL}/blogs/${id}`);
};



const ABASE_URL = "http://localhost:8080/comments"; // Adjust if needed

// Fetch comments for a post
export const getComments = async (blogPostId) => {
  return axios.get(`${ABASE_URL}/${blogPostId}`);
};

// Add a new comment







export const addComment = async (blogPostId, commentText) => {
  const token = localStorage.getItem("token");
  const username = localStorage.getItem("username");
  console.log(token);
  console.log(username);
  
  

  if (!token) {
    console.error("❌ Authentication Error: Token or username is missing!");
    throw new Error("User is not authenticated");
  }

  const commentData = { 
    commenter: username,  // ✅ Send username as the commenter
    content: commentText,
  };

  return axios.post(`http://localhost:8080/comments/${blogPostId}`, commentData, {
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`, // ✅ Attach token
    },
  });
};



// Delete a comment
export const deleteComment = async (commentId) => {
  return axios.delete(`${ABASE_URL}/${commentId}`);
};



