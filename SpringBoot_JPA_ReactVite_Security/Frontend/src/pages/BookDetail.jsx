import React, { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import api from "../api/api"; // Use the configured axios instance

// Helper to resolve image paths
const BACKEND_BASE = "http://localhost:8080";
export function resolveImagePath(p) {
    if (!p) return null;
    if (p.startsWith('http://') || p.startsWith('https://')) return p;
    if (p.startsWith('/')) return BACKEND_BASE + p;
    return BACKEND_BASE + '/upload/' + p;
}

export default function BookDetail(){
  const { id } = useParams();
  const [b, setB] = useState(null);

  useEffect(() => {
    api.get(`/books/${id}`)
      .then(response => setB(response.data))
      .catch(e => alert(e.response?.data?.message || e.message));
  }, [id]);

  if(!b) return <div>Đang tải...</div>;

  return (
    <div>
      <Link to="/" className="btn btn-link">&larr; Quay lại</Link>
      <h4>{b.title} <small className="text-muted">#{b.id}</small></h4>
      <div className="row">
        <div className="col-md-3">
          { (b.imageUrl || b.image_url) ? (
            <img src={resolveImagePath(b.imageUrl || b.image_url)} alt="" className="img-fluid"/>
          ) : (
            <div className="text-muted">Không có ảnh</div>
          )}
        </div>
        <div className="col-md-9">
          <p><strong>Tác giả:</strong> {b.author}</p>
          <p><strong>Thể loại:</strong> {b.category}</p>
          <p><strong>Nhà xuất bản:</strong> {b.publisher}</p>
          <p><strong>Năm xuất bản:</strong> {b.publishedYear}</p>
          <p><strong>Số lượng:</strong> {b.quantity}</p>
          <p><strong>Mô tả:</strong></p>
          <div>{b.description}</div>
        </div>
      </div>
    </div>
  );
}
