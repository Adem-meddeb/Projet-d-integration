import React, { createContext, useState, useContext, useMemo, useEffect } from 'react';

const AuthContext = createContext(null);
//Ici, AuthContext sera utilisé pour partager les données d'authentification (user, token, etc.) avec les composants enfants sans avoir à passer ces données via des props.

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);//Contient les informations de l'utilisateur connecté (e
  const [token, setToken] = useState(() => localStorage.getItem('token'));

  useEffect(() => {
    //Si le token change (connexion ou déconnexion) 
    if (token) {
      // Optionally verify token validity here
      localStorage.setItem('token', token);
    } else {
      localStorage.removeItem('token');
    }
  }, [token]);

  const login = (userData, authToken) => {
    setUser(userData);//userData (données utilisateur) est stocké dans user.
    setToken(authToken);//authToken (jeton d'authentification) est stocké dans token.
  };

  const logout = () => {
    setUser(null);
    setToken(null);
  };
  //ela déclenche aussi la suppression du token de localStorage via useEffect.

  const value = useMemo(
    () => ({
      user,
      token,
      login,
      logout,
    }),
    [user, token]
  );
  //Ce value est recalculé uniquement lorsque user ou token change, optimisant les performances.

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
  //AuthContext.Provider rend les données (value) accessibles à tous les composants enfants via le contexte.
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === null) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};
