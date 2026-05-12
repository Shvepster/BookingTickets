"use client";

import { useState, useEffect } from "react";
import useSWR from "swr";
import { Header } from "@/components/header";
import { Input } from "@/components/ui/input";
import { Badge } from "@/components/ui/badge";
import { Search } from "lucide-react";
import { fetcher } from "@/lib/api";
import type { CategoryResponseDto } from "@/lib/types";

const demoCategories: CategoryResponseDto[] = [
  { id: 1, name: "Рок" },
  { id: 2, name: "Шоу" },
  { id: 3, name: "Балет" },
  { id: 4, name: "Классика" },
  { id: 5, name: "Комедия" },
  { id: 6, name: "Театр" },
  { id: 7, name: "Электроника" },
  { id: 8, name: "18+" },
  { id: 9, name: "Детское" },
  { id: 10, name: "Сказка" },
  { id: 11, name: "Джаз" },
  { id: 12, name: "Фестиваль" },
];

export default function CategoriesPage() {
  const [searchQuery, setSearchQuery] = useState("");
  const [useDemo, setUseDemo] = useState(false);

  const { data: apiCategories, error } = useSWR<CategoryResponseDto[]>(
    "/api/categories",
    fetcher,
    { revalidateOnFocus: false }
  );

  useEffect(() => {
    if (error) setUseDemo(true);
  }, [error]);

  const categories = useDemo ? demoCategories : (apiCategories || []);

  const filteredCategories = categories.filter((category) =>
    category.name.toLowerCase().includes(searchQuery.toLowerCase())
  );

  return (
    <div className="min-h-screen bg-background">
      <Header />
      <main className="mx-auto max-w-7xl px-4 py-8 sm:px-6 lg:px-8">
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-foreground">Категории</h1>
          <p className="mt-1 text-muted-foreground">
            {useDemo && (
              <span className="mr-2 rounded bg-yellow-100 px-2 py-0.5 text-xs text-yellow-800">
                Демо-режим
              </span>
            )}
            Категории мероприятий
          </p>
        </div>

        <div className="mb-6">
          <div className="relative max-w-sm">
            <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
            <Input
              placeholder="Поиск по названию..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="pl-10"
            />
          </div>
        </div>

        <div className="rounded-lg border border-border bg-card p-6">
          {filteredCategories.length === 0 ? (
            <p className="text-center text-muted-foreground">Категории не найдены</p>
          ) : (
            <div className="flex flex-wrap gap-3">
              {filteredCategories.map((category) => (
                <Badge
                  key={category.id}
                  variant="secondary"
                  className="px-4 py-2 text-sm"
                >
                  {category.name}
                </Badge>
              ))}
            </div>
          )}
        </div>
      </main>
    </div>
  );
}
