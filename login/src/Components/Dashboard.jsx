import { useEffect, useState } from "react";
import { verifyToken } from "../Api";
import { useNavigate } from "react-router-dom";

const Dashboard = () => {
  const [user, setUser] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchUser = async () => {
      const token = localStorage.getItem("token");
      if (!token) {
        alert("Not authenticated! Please login.");
        navigate("/");
        return;
      }

      try {
        const data = await verifyToken(token);
        if (data.username) {
          setUser(data);
        } else {
          alert("Invalid session! Please login again.");
          localStorage.removeItem("token");
          navigate("/");
        }
      } catch (error) {
        console.error("Token verification failed", error);
        localStorage.removeItem("token");
        navigate("/");
      }
    };

    fetchUser();
  }, [navigate]);

  return (
    <main className="flex items-center justify-center min-h-screen p-6">
      <div className="w-full max-w-md p-6 bg-white rounded-lg shadow-lg">
        {user ? (
          <div className="mt-4 text-center">
            <h2 className="text-xl font-bold text-gray-800">
              👋 Welcome to <span className="text-blue-600">BLOG APP</span>, {user.username}!
            </h2>
            <p className="mt-2 text-sm text-gray-600">📧 Email: {user.email}</p>
          </div>
        ) : (
          <div className="flex items-center justify-center">
            <div className="w-6 h-6 border-4 border-blue-500 border-dashed rounded-full animate-spin"></div>
            <p className="ml-2 text-gray-500">Loading...</p>
          </div>
        )}
      </div>
    </main>
  );
};

export default Dashboard;
