import React from "react";
import { Routes, Route, Link, useNavigate } from "react-router-dom";
import BookList from "./pages/BookList";
import BookForm from "./pages/BookForm";
import BookDetail from "./pages/BookDetail";
import LoginPage from "./pages/LoginPage";
import ProtectedRoute from "./components/ProtectedRoute";
import { useAuth } from "./contexts/AuthContext";

export default function App() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <div className="container my-4">
      <header className="d-flex justify-content-between align-items-center mb-4">
        <h3>Library</h3>
        <nav>
          {user && <Link to="/" className="me-2">Danh sách</Link>}
          
          {user?.role === 'BOOKKEEPER' && (
            <Link to="/add" className="btn btn-primary btn-sm me-2">Thêm sách</Link>
          )}

          {user ? (
            <button onClick={handleLogout} className="btn btn-secondary btn-sm">Logout</button>
          ) : (
            <Link to="/login" className="btn btn-primary btn-sm">Login</Link>
          )}
        </nav>
      </header>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route 
          path="/" 
          element={
            <ProtectedRoute>
              <BookList />
            </ProtectedRoute>
          } 
        />
        <Route 
          path="/add" 
          element={
            <ProtectedRoute allowedRoles={['BOOKKEEPER']}>
              <BookForm />
            </ProtectedRoute>
          } 
        />
        <Route 
          path="/edit/:id" 
          element={
            <ProtectedRoute allowedRoles={['BOOKKEEPER']}>
              <BookForm />
            </ProtectedRoute>
          } 
        />
        <Route 
          path="/books/:id" 
          element={
            <ProtectedRoute>
              <BookDetail />
            </ProtectedRoute>
          } 
        />
      </Routes>
    </div>
  );
}
