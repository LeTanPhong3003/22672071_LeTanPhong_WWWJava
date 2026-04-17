import React from "react";
import { Routes, Route, Link } from "react-router-dom";
import BookList from "./pages/BookList";
import BookForm from "./pages/BookForm";
import BookDetail from "./pages/BookDetail";

export default function App() {
  return (
    <div className="container my-4">
      <header className="d-flex justify-content-between align-items-center mb-4">
        <h3>Library</h3>
        <nav>
          <Link to="/" className="me-2">Danh sách</Link>
          <Link to="/add" className="btn btn-primary btn-sm">Thêm sách</Link>
        </nav>
      </header>
      <Routes>
        <Route path="/" element={<BookList />} />
        <Route path="/add" element={<BookForm />} />
        <Route path="/edit/:id" element={<BookForm />} />
        <Route path="/books/:id" element={<BookDetail />} />
      </Routes>
    </div>
  );
}
