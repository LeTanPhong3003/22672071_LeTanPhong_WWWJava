import React, { createContext, useState, useContext } from 'react';
import api from '../api/api';
import { Buffer } from 'buffer';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);

  const login = async (username, password) => {
    try {
      // Create Basic Auth header
      const token = Buffer.from(`${username}:${password}`, 'utf8').toString('base64');
      
      // Set header for all future requests
      api.defaults.headers.common['Authorization'] = `Basic ${token}`;
      
      // Make a test request to validate credentials
      await api.get('/books'); 

      const role = username === 'bookkeeper' ? 'BOOKKEEPER' : 'STUDENT';
      const authUser = { username, token, role };
      setUser(authUser);
      
      // Store user info in local storage to persist login
      localStorage.setItem('user', JSON.stringify(authUser));

      return authUser;
    } catch (error) {
      console.error('Login failed:', error);
      // Clear auth header on failure
      delete api.defaults.headers.common['Authorization'];
      throw new Error('Invalid username or password');
    }
  };

  const logout = () => {
    setUser(null);
    localStorage.removeItem('user');
    delete api.defaults.headers.common['Authorization'];
  };

  // Check local storage on initial load
  useState(() => {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      const parsedUser = JSON.parse(storedUser);
      setUser(parsedUser);
      api.defaults.headers.common['Authorization'] = `Basic ${parsedUser.token}`;
    }
  }, []);


  return (
    <AuthContext.Provider value={{ user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  return useContext(AuthContext);
};
