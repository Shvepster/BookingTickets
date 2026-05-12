"use client";

import { useState, useEffect } from "react";
import useSWR from "swr";
import { Header } from "@/components/header";
import { Input } from "@/components/ui/input";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Search, MapPin } from "lucide-react";
import { fetcher } from "@/lib/api";
import type { VenueResponseDto } from "@/lib/types";

const demoVenues: VenueResponseDto[] = [
  { id: 1, name: "Минск-Арена", address: "пр. Победителей, 111" },
  { id: 2, name: "Большой театр Беларуси", address: "пл. Парижской Коммуны, 1" },
  { id: 3, name: "Купаловский театр", address: "ул. Энгельса, 7" },
  { id: 4, name: "RE:PUBLIC", address: "пр. Победителей, 9" },
  { id: 5, name: "ТЮЗ", address: "ул. Энгельса, 26" },
  { id: 6, name: "Дворец Республики", address: "пр. Независимости, 20" },
];

export default function VenuesPage() {
  const [searchQuery, setSearchQuery] = useState("");
  const [useDemo, setUseDemo] = useState(false);

  const { data: apiVenues, error } = useSWR<VenueResponseDto[]>(
    "/api/venues",
    fetcher,
    { revalidateOnFocus: false }
  );

  useEffect(() => {
    if (error) setUseDemo(true);
  }, [error]);

  const venues = useDemo ? demoVenues : (apiVenues || []);

  const filteredVenues = venues.filter((venue) =>
    venue.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
    venue.address.toLowerCase().includes(searchQuery.toLowerCase())
  );

  return (
    <div className="min-h-screen bg-background">
      <Header />
      <main className="mx-auto max-w-7xl px-4 py-8 sm:px-6 lg:px-8">
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-foreground">Площадки</h1>
          <p className="mt-1 text-muted-foreground">
            {useDemo && (
              <span className="mr-2 rounded bg-yellow-100 px-2 py-0.5 text-xs text-yellow-800">
                Демо-режим
              </span>
            )}
            Список площадок проведения мероприятий
          </p>
        </div>

        <div className="mb-6">
          <div className="relative max-w-sm">
            <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
            <Input
              placeholder="Поиск по названию или адресу..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="pl-10"
            />
          </div>
        </div>

        <div className="rounded-lg border border-border bg-card">
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead className="w-16">ID</TableHead>
                <TableHead>Название</TableHead>
                <TableHead>Адрес</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {filteredVenues.length === 0 ? (
                <TableRow>
                  <TableCell colSpan={3} className="h-24 text-center text-muted-foreground">
                    Площадки не найдены
                  </TableCell>
                </TableRow>
              ) : (
                filteredVenues.map((venue) => (
                  <TableRow key={venue.id}>
                    <TableCell className="font-medium">{venue.id}</TableCell>
                    <TableCell className="font-medium">{venue.name}</TableCell>
                    <TableCell>
                      <div className="flex items-center gap-2 text-muted-foreground">
                        <MapPin className="h-4 w-4" />
                        {venue.address}
                      </div>
                    </TableCell>
                  </TableRow>
                ))
              )}
            </TableBody>
          </Table>
        </div>
      </main>
    </div>
  );
}
