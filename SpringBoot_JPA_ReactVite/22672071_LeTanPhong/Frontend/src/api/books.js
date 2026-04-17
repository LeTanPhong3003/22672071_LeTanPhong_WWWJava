const BASE =
    import.meta.env.VITE_API_BASE || "http://localhost:8080/api/books";

// Derive backend base (strip /api/books)
const BACKEND_BASE = BASE.replace(/\/api\/books\/?$/, "");

export function resolveImagePath(p) {
    if (!p) return null;
    if (p.startsWith('http://') || p.startsWith('https://')) return p;
    if (p.startsWith('/')) return BACKEND_BASE + p;
    // treat as filename
    return BACKEND_BASE + '/upload/' + p;
}

async function request(path = "", options = {}) {
    const res = await fetch(`${BASE}${path}`, {
        headers: { "Content-Type": "application/json" },
        ...options
    });
    if (!res.ok) {
        const err = await res.json().catch(() => ({ message: res.statusText }));
        throw new Error(err.message || res.statusText);
    }
    return res.status === 204 ? null : res.json();
}

export const api = {
    list: () => request(""),
    get: (id) => request(`/${id}`),
    search: (q) => request(`/search?keyword=${encodeURIComponent(q || "")}`),
    create: (data) => request("", { method: "POST", body: JSON.stringify(data) }),
    update: (id, data) => request(`/${id}`, { method: "PUT", body: JSON.stringify(data) }),
    remove: (id) => request(`/${id}`, { method: "DELETE" }),
    // Upload image file to backend. Returns parsed JSON (expected { url: '/upload/...' })
    upload: async(file) => {
        const form = new FormData();
        form.append('file', file);
        const res = await fetch(`${BASE}/upload`, {
            method: 'POST',
            body: form
        });
        if (!res.ok) {
            const err = await res.json().catch(() => ({ message: res.statusText }));
            throw new Error(err.message || res.statusText);
        }
        return res.json();
    }
};