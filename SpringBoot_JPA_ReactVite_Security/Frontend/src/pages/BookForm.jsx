import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import api from "../api/api"; // Use the configured axios instance

const empty = {
  title: "", author: "", category: "", publisher: "", publishedYear: 2020, quantity: 0, description: "", imageUrl: ""
};

// Helper to resolve image paths
const BACKEND_BASE = "http://localhost:8080";
export function resolveImagePath(p) {
    if (!p) return null;
    if (p.startsWith('http://') || p.startsWith('https://')) return p;
    if (p.startsWith('/')) return BACKEND_BASE + p;
    return BACKEND_BASE + '/upload/' + p;
}

export default function BookForm(){
  const { id } = useParams();
  const editMode = !!id;
  const [book, setBook] = useState(empty);
  const nav = useNavigate();

  useEffect(() => {
    if (editMode) {
      api.get(`/books/${id}`)
        .then(response => setBook({ ...response.data, publishedYear: response.data.publishedYear || 2020 }))
        .catch(e => alert(e.message));
    }
  }, [id, editMode]);

  const onChange = (e) => {
    const { name, value } = e.target;
    setBook(b => ({...b, [name]: name === "quantity" || name === "publishedYear" ? Number(value) : value}));
  };

  const onFileChange = async (e) => {
    const file = e.target.files && e.target.files[0];
    if (!file) return;
    
    const formData = new FormData();
    formData.append('file', file);

    try {
      // POST /api/books/upload
      const response = await api.post('/books/upload', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      });
      
      if (response.data && (response.data.url || response.data.path)) {
        const url = response.data.url || response.data.path;
        setBook(b => ({...b, imageUrl: url}));
        alert('Upload ảnh thành công');
      } else {
        alert('Không nhận được đường dẫn ảnh từ server');
      }
    } catch (err) {
      alert('Upload thất bại: ' + (err.response?.data?.message || err.message));
    }
  };

  const onSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editMode) {
        // PUT /api/books/{id}
        await api.put(`/books/${id}`, book);
      } else {
        // POST /api/books
        await api.post('/books', book);
      }
      alert(editMode ? "Cập nhật thành công" : "Thêm thành công");
      nav("/");
    } catch (err) {
      alert("Lỗi: " + (err.response?.data?.message || err.message));
    }
  };

  return (
    <form onSubmit={onSubmit}>
      <div className="mb-3">
        <label className="form-label">Tiêu đề</label>
        <input name="title" className="form-control" required maxLength={255} value={book.title} onChange={onChange}/>
      </div>
      <div className="mb-3">
        <label className="form-label">Tác giả</label>
        <input name="author" className="form-control" required maxLength={150} value={book.author} onChange={onChange}/>
      </div>
      <div className="row">
        <div className="col-md-4 mb-3">
          <label className="form-label">Thể loại</label>
          <input name="category" className="form-control" maxLength={100} value={book.category} onChange={onChange}/>
        </div>
        <div className="col-md-4 mb-3">
          <label className="form-label">Nhà xuất bản</label>
          <input name="publisher" className="form-control" maxLength={150} value={book.publisher} onChange={onChange}/>
        </div>
        <div className="col-md-2 mb-3">
          <label className="form-label">Năm</label>
          <input name="publishedYear" type="number" className="form-control" value={book.publishedYear} onChange={onChange}/>
        </div>
        <div className="col-md-2 mb-3">
          <label className="form-label">Số lượng</label>
          <input name="quantity" type="number" className="form-control" min="0" value={book.quantity} onChange={onChange}/>
        </div>
      </div>
      <div className="mb-3">
        <label className="form-label">Ảnh (URL)</label>
        <input name="imageUrl" className="form-control mb-2" maxLength={255} value={book.imageUrl} onChange={onChange}/>
        <input type="file" accept="image/*" className="form-control" onChange={onFileChange} />
        {book.imageUrl ? (
          <div className="mt-2">
            <img src={resolveImagePath(book.imageUrl)} alt="preview" style={{maxHeight:120}}/>
          </div>
        ) : null}
      </div>
      <div className="mb-3">
        <label className="form-label">Mô tả</label>
        <textarea name="description" className="form-control" rows="4" value={book.description} onChange={onChange}/>
      </div>
      <div className="d-flex gap-2">
        <button className="btn btn-primary" type="submit">{editMode ? "Cập nhật" : "Thêm"}</button>
        <button type="button" className="btn btn-secondary" onClick={()=>nav(-1)}>Hủy</button>
      </div>
    </form>
  );
}
