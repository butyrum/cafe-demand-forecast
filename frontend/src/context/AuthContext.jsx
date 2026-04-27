import { createContext, useState, useContext, useEffect } from 'react';
import api from '../services/api';

const AuthContext = createContext({});

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const storedUser = localStorage.getItem('user');
    const token = localStorage.getItem('token');

    if (storedUser && token) {
      setUser(JSON.parse(storedUser));
    }
    setLoading(false);
  }, []);

  async function signIn({ username, password }) {
    const response = await api.post('/auth/login', { username, password });
    
    const { token, username: userUsername, role } = response.data.data;
    
    localStorage.setItem('token', token);
    localStorage.setItem('user', JSON.stringify({ username: userUsername, role }));
    
    setUser({ username: userUsername, role });
    
    return response.data;
  }

  function signOut() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    setUser(null);
  }

  return (
    <AuthContext.Provider value={{ user, signIn, signOut, loading }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within AuthProvider');
  }
  return context;
}
