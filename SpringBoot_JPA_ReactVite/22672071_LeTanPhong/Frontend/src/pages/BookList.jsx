import React, { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { api, resolveImagePath } from "../api/books";

export default function BookList() {
  const [books, setBooks] = useState([]);
  const [q, setQ] = useState("");
  const [loading, setLoading] = useState(false);
  const nav = useNavigate();

  const load = async (keyword="") => {
    setLoading(true);
    try {
      const data = keyword ? await api.search(keyword) : await api.list();
      setBooks(data);
    } catch (e) {
      alert("Lỗi khi tải dữ liệu: " + e.message);
    } finally { setLoading(false); }
  };

  useEffect(() => { load(); }, []);

  const onDelete = async (id) => {
    if (!confirm("Bạn có chắc muốn xóa sách này?")) return;
    try {
      await api.remove(id);
      setBooks((s) => s.filter(b => b.id !== id));
      alert("Xóa thành công");
    } catch (e) {
      alert("Xóa thất bại: " + e.message);
    }
  };

  return (
    <div>
      <div className="mb-3 d-flex">
        <input className="form-control me-2" placeholder="Tìm kiếm theo tên, tác giả, thể loại"
               value={q} onChange={e=>setQ(e.target.value)} />
        <button className="btn btn-outline-primary me-2" onClick={()=>load(q)}>Tìm</button>
        <button className="btn btn-secondary" onClick={()=>{setQ(""); load("");}}>Làm mới</button>
      </div>

      {loading ? <div>Đang tải...</div> : (
        books.length === 0 ? (
          <div className="text-muted">Không có sách.</div>
        ) : (
          <div className="row">
            {books.map(b => {
              const img = b.imageUrl || b.image_url || null;
              const year = b.publishedYear || b.published_year || "";
              const desc = b.description || "";
              return (
                <div className="col-sm-6 col-md-4 mb-3" key={b.id}>
                  <div className="card h-100">
                    {img ? (
                      <div className="card-img-top d-flex align-items-center justify-content-center bg-light" style={{height:220, overflow:'hidden'}}>
                        <img src={resolveImagePath(img)} alt={b.title} style={{maxHeight:'100%', maxWidth:'100%', objectFit:'contain'}} />
                      </div>
                    ) : (
                      <div className="card-img-top bg-light d-flex align-items-center justify-content-center" style={{height:220}}>
                        <span className="text-muted">No image</span>
                      </div>
                    )}
                    <div className="card-body d-flex flex-column">
                      <h5 className="card-title"><Link to={`/books/${b.id}`}>{b.title}</Link></h5>
                      <p className="card-text mb-1"><strong>Tác giả:</strong> {b.author}</p>
                      <p className="card-text mb-1"><strong>Thể loại:</strong> {b.category}</p>
                      <p className="card-text mb-1"><strong>NXB:</strong> {b.publisher} {year ? `, ${year}` : ''}</p>
                      <p className="card-text text-muted small mt-auto">{desc.length > 120 ? desc.slice(0,120) + '...' : desc}</p>
                      <div className="mt-3 d-flex justify-content-between align-items-center">
                        <div className="text-muted">Số lượng: {b.quantity}</div>
                        <div>
                          <button className="btn btn-sm btn-warning me-1" onClick={()=>nav(`/edit/${b.id}`)}>Sửa</button>
                          <button className="btn btn-sm btn-danger" onClick={()=>onDelete(b.id)}>Xóa</button>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              );
            })}
          </div>
        )
      )}
    </div>
  );
}
