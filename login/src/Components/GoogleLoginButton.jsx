import React from "react";
import { GoogleLogin, GoogleOAuthProvider } from "@react-oauth/google";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const GoogleLoginButton = () => {
    const navigate = useNavigate();

    const handleSuccess = async (response) => {
        console.log("Google Login Success:", response);
    
        try {
            // Send Google token to backend
            const res = await axios.post("http://localhost:8080/auth/google", {
                token: response.credential,
            });
    
            console.log("Google Login Response:", res.data);
    
            // ✅ Debugging: Check the actual response
            if (!res.data) {
                console.error("Received empty response from server!");
                return;
            }
    
            if (!res.data.id || !res.data.token) {
                console.error("Missing required fields:", res.data);
                return;
            }
    
            // ✅ Ensure data is stored
            localStorage.setItem("token", res.data.token);
            localStorage.setItem("user", JSON.stringify({ 
                id: res.data.id,  
                username: res.data.username, 
                email: res.data.email 
            }));
    
            // ✅ Verify storage
            console.log("User stored in localStorage:", localStorage.getItem("user"));
    
            navigate("/home");
        } catch (error) {
            console.error("Login Error:", error);
        }
    };
    
    

    const handleFailure = (error) => {
        console.error("Google Login Failed:", error);
    };

    return (
        <>
        <GoogleOAuthProvider clientId="490647164435-3hn4egh6fkdg1natanm509fru9r10pmn.apps.googleusercontent.com">
        <div className="flex justify-center mt-10">
            <GoogleLogin onSuccess={handleSuccess} onError={handleFailure} />

            
        </div>
        </GoogleOAuthProvider>
        </>
    );
};

export default GoogleLoginButton;
