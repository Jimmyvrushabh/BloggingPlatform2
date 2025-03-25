import { useState } from "react";
import { registerUser } from "../Api";
import { useNavigate } from "react-router-dom";
import GoogleLoginButton from "./GoogleLoginButton";



const Register = () => {
  const [user, setUser] = useState({ username: "", email: "", password: "" });
  const [error, setError] = useState(""); 
  const navigate = useNavigate();

  const handleChange = (e) => {
    setUser({ ...user, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(""); 

    try {
      const response = await registerUser(user);
      if (response.error) {
        alert(response.error);
      } else {
        alert("Registration successful! Now login.");
        navigate("/");
      }
    } catch (error) {
      alert("Registration failed! Please try again.");
    }
  };

  return (
    <div className="flex items-center justify-center min-h-screen bg-gray-100">
      <div className="bg-white p-8 rounded-2xl shadow-lg max-w-md w-full">
        <h2 className="text-2xl font-semibold text-center text-gray-800 mb-6">
          Create an Account
        </h2>

        {error && <p className="text-red-500 text-center">{error}</p>}

        <form onSubmit={handleSubmit} className="space-y-4">
          <input
            type="text"
            name="username"
            placeholder="Username"
            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring focus:ring-indigo-200 focus:outline-none"
            onChange={handleChange}
            required
          />

          <input
            type="email"
            name="email"
            placeholder="Email"
            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring focus:ring-indigo-200 focus:outline-none"
            onChange={handleChange}
            required
          />

          <input
            type="password"
            name="password"
            placeholder="Password"
            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring focus:ring-indigo-200 focus:outline-none"
            onChange={handleChange}
            required
          />
         
          <button
            type="submit"
            className="w-full bg-indigo-600 text-white py-2 rounded-lg font-medium hover:bg-indigo-700 transition duration-300"
          >
            Sign Up
          </button>
        </form>

        <div className="my-4 flex items-center">
          <hr className="flex-grow border-gray-300" />
          <span className="px-2 text-gray-500">OR</span>
          <hr className="flex-grow border-gray-300" />
        </div>

   
        <p className="text-center text-gray-600 mt-4">
          Already have an account?{" "}

        

          <a href="/" className="text-indigo-600 hover:underline">
            Login
          </a>
        </p>
        <GoogleLoginButton/>
      </div>
    </div>
  );
};

export default Register;
