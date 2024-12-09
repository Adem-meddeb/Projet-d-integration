import React from "react";
import { useAuth } from "../context/AuthContext";

const Profile = () => {
  const { user, logout } = useAuth();

  return user ? (
    <div>
      <h1>Welcome, {user.firstName}</h1>
      <button onClick={logout} className="btn">Logout</button>
    </div>
  ) : (
    <h1>Please login</h1>
  );
};

export default Profile;
