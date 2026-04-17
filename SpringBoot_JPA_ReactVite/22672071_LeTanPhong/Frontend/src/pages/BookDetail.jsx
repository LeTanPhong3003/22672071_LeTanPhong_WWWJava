import React, { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import { api, resolveImagePath } from "../api/books";

export default function BookDetail(){
  const { id } = useParams();
  const [b, setB] = useState(null);

  useEffect(()=>{ api.get(id).then(setB).catch(e=>alert(e.message)); }, [id]);

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
