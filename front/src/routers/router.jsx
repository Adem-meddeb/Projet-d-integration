  import React from 'react';
  import {
    createBrowserRouter,
    RouterProvider,
  } from "react-router-dom";
  import App from "../App";
  import Home from "../home/Home";
  import Login from "../components/login";
  import Register from "../components/Registration";
  import ProtectedRoute from "../components/ProtectedRoute";
  import { AuthProvider } from "../context/AuthContext"; 
  import Profile from '../components/Profile';
  import EventComponent from '../components/EventComponent';
  import EditEventComponent from '../components/EditEventComponent';
import EventList from '../components/EventList';

  const router = createBrowserRouter([
    {
      path: "/",
      element: <App />,
      children: [
        { 
          path: "/", 
          element: (
            <ProtectedRoute>
              <Home />
            </ProtectedRoute>
          ) 
        },
        { path: "/login", element: <Login /> },
        { path: "/register", element: <Register /> },
        {path: '/profile', element: <Profile />},
        { path: "/events", element: <EventList /> }, 
        { path: "/add-event", element: <EventComponent /> },
        { path: "/edit-event/:id", element: <EditEventComponent /> },
      

      ],
    },
  ]);

  export default router;