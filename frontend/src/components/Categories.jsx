import { useEffect, useState } from "react";
import { Trash2, Pencil } from "lucide-react";
import Sidebar from "./layout/Sidebar";
import Header from "./layout/Header";
import CategorySlideOver from "./categories/CategorySlideOver";
import api from "../api/axios";

function CategoryRow({ name, categoryType, onEdit, onDelete }) {
  const isIncome = categoryType === "INCOME";
  return (
    <div className="flex items-center justify-between px-5 py-4 border-b border-neutral-800 last:border-0 hover:bg-neutral-900/50 transition-colors">
      <p className="mono text-white text-sm">{name}</p>
      <div className="flex items-center gap-4">
        <span
          className={`mono text-xs px-2 py-0.5 border ${
            isIncome
              ? "border-neutral-500 text-neutral-200"
              : "border-neutral-700 text-neutral-400"
          }`}
        >
          {isIncome ? "income" : "expense"}
        </span>
        <button
          onClick={onEdit}
          className="text-neutral-600 hover:text-neutral-300 transition-colors"
        >
          <Pencil size={14} />
        </button>
        <button
          onClick={onDelete}
          className="text-neutral-600 hover:text-neutral-300 transition-colors"
        >
          <Trash2 size={14} />
        </button>
      </div>
    </div>
  );
}

function Section({ title, categories, onEdit, onDelete }) {
  if (categories.length === 0) return null;
  return (
    <div>
      <p className="mono text-neutral-300 text-[13px] tracking-widest uppercase mb-3">
        {title}
      </p>
      <div className="border border-neutral-800">
        {categories.map((cat) => (
          <CategoryRow
            key={cat.id}
            name={cat.name}
            categoryType={cat.categoryType}
            onEdit={() => onEdit(cat)}
            onDelete={() => onDelete(cat.id)}
          />
        ))}
      </div>
    </div>
  );
}

export default function Categories() {
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [slideOverOpen, setSlideOverOpen] = useState(false);
  const [selectedCategory, setSelectedCategory] = useState(null);

  function fetchCategories() {
    setLoading(true);
    api
      .get("/api/v1/categories")
      .then((res) => setCategories(res.data))
      .catch(() => setError("Failed to load categories."))
      .finally(() => setLoading(false));
  }

  function handleDelete(id) {
    api
      .delete(`/api/v1/categories/${id}`)
      .then(fetchCategories)
      .catch(() => setError("Cannot delete category with associated transactions."));
  }

  useEffect(() => {
    fetchCategories();
  }, []);

  const income  = categories.filter((c) => c.categoryType === "INCOME");
  const expense = categories.filter((c) => c.categoryType === "EXPENSE");

  return (
    <>
      <div className="flex h-screen bg-neutral-950 overflow-hidden">
        <Sidebar />
        <div className="flex-1 flex flex-col overflow-hidden">
          <Header />
          <main className="flex-1 overflow-auto p-6">

            <div className="flex items-center justify-between mb-6">
              <div>
                <h1 className="font-display text-white text-xl">Categories</h1>
                <p className="mono text-neutral-500 text-xs mt-1">
                  {loading ? "Loading..." : `${categories.length} categories`}
                </p>
              </div>
              <button
                onClick={() => { setSelectedCategory(null); setSlideOverOpen(true); }}
                className="mono bg-neutral-100 hover:bg-neutral-300 text-neutral-950 text-xs px-4 py-2 transition-colors"
              >
                + New category
              </button>
            </div>

            {error && <p className="mono text-neutral-500 text-sm">{error}</p>}

            {!loading && !error && categories.length === 0 && (
              <p className="mono text-neutral-500 text-sm">No categories found.</p>
            )}

            {!loading && !error && categories.length > 0 && (
              <div className="flex flex-col gap-8">
                <Section
                  title="Income"
                  categories={income}
                  onEdit={(cat) => { setSelectedCategory(cat); setSlideOverOpen(true); }}
                  onDelete={handleDelete}
                />
                <Section
                  title="Expense"
                  categories={expense}
                  onEdit={(cat) => { setSelectedCategory(cat); setSlideOverOpen(true); }}
                  onDelete={handleDelete}
                />
              </div>
            )}

          </main>
        </div>
      </div>

      <CategorySlideOver
        isOpen={slideOverOpen}
        onClose={() => { setSlideOverOpen(false); setSelectedCategory(null); }}
        onSuccess={fetchCategories}
        category={selectedCategory}
      />
    </>
  );
}
