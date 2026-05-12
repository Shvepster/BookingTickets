"use client";

import { useState, useEffect, useCallback } from "react";
import useSWR, { mutate } from "swr";
import { Header } from "@/components/header";
import { EventCard } from "@/components/event-card";

import { BuyTicketDialog } from "@/components/buy-ticket-dialog";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Search, X } from "lucide-react";
import { ticketsApi, fetcher } from "@/lib/api";
import type { EventResponseDto, VenueResponseDto, CategoryResponseDto, UserResponseDto, TicketRequestDto } from "@/lib/types";

// Demo data for when API is not available
const demoEvents: EventResponseDto[] = [
  {
    id: 1,
    title: "Концерт Scorpions",
    formattedPrice: "150.00 BYN",
    venueName: "Минск-Арена",
    eventDate: "2026-07-15T20:00:00",
    categories: ["Рок", "Шоу"],
  },
  {
    id: 2,
    title: "Балет Лебединое озеро",
    formattedPrice: "80.00 BYN",
    venueName: "Большой театр Беларуси",
    eventDate: "2026-06-20T19:00:00",
    categories: ["Балет", "Классика"],
  },
  {
    id: 3,
    title: "Комедия Ревизор",
    formattedPrice: "45.00 BYN",
    venueName: "Купаловский театр",
    eventDate: "2026-06-10T18:30:00",
    categories: ["Комедия", "Театр"],
  },
  {
    id: 4,
    title: "DJ Max Live",
    formattedPrice: "60.00 BYN",
    venueName: "RE:PUBLIC",
    eventDate: "2026-06-25T22:00:00",
    categories: ["Электроника", "18+"],
  },
  {
    id: 5,
    title: "Детский спектакль Золушка",
    formattedPrice: "25.00 BYN",
    venueName: "ТЮЗ",
    eventDate: "2026-06-15T12:00:00",
    categories: ["Детское", "Сказка"],
  },
  {
    id: 6,
    title: "Фестиваль джаза",
    formattedPrice: "90.00 BYN",
    venueName: "Дворец Республики",
    eventDate: "2026-08-01T19:00:00",
    categories: ["Джаз", "Фестиваль"],
  },
];

const demoVenues: VenueResponseDto[] = [
  { id: 1, name: "Минск-Арена", address: "пр. Победителей, 111" },
  { id: 2, name: "Большой театр Беларуси", address: "пл. Парижской Коммуны, 1" },
  { id: 3, name: "Купаловский театр", address: "ул. Энгельса, 7" },
  { id: 4, name: "RE:PUBLIC", address: "пр. Победителей, 9" },
  { id: 5, name: "ТЮЗ", address: "ул. Энгельса, 26" },
  { id: 6, name: "Дворец Республики", address: "пр. Независимости, 20" },
];

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

const demoUsers: UserResponseDto[] = [
  { id: 1, username: "ivan_petrov", email: "ivan@example.com" },
  { id: 2, username: "maria_sidorova", email: "maria@example.com" },
  { id: 3, username: "alex_kozlov", email: "alex@example.com" },
];

export default function EventsPage() {
  const [isBuyOpen, setIsBuyOpen] = useState(false);
  const [buyingEvent, setBuyingEvent] = useState<EventResponseDto | null>(null);
  const [searchQuery, setSearchQuery] = useState("");
  const [filterVenue, setFilterVenue] = useState<string>("all");
  const [filterCategory, setFilterCategory] = useState<string>("all");
  const [useDemo, setUseDemo] = useState(false);

  // Fetch data from API
  const { data: apiEvents, error: eventsError } = useSWR<EventResponseDto[]>(
    "/api/events/all",
    fetcher,
    { revalidateOnFocus: false }
  );
  const { data: apiVenues } = useSWR<VenueResponseDto[]>("/api/venues", fetcher, { revalidateOnFocus: false });
  const { data: apiCategories } = useSWR<CategoryResponseDto[]>("/api/categories", fetcher, { revalidateOnFocus: false });
  const { data: apiUsers } = useSWR<UserResponseDto[]>("/api/users", fetcher, { revalidateOnFocus: false });

  // Use demo data if API is not available
  useEffect(() => {
    if (eventsError) {
      setUseDemo(true);
    }
  }, [eventsError]);

  const events = useDemo ? demoEvents : (apiEvents || []);
  const venues = useDemo ? demoVenues : (apiVenues || []);
  const categories = useDemo ? demoCategories : (apiCategories || []);
  const users = useDemo ? demoUsers : (apiUsers || []);

  // Filter events
  const filteredEvents = events.filter((event) => {
    const matchesSearch = event.title.toLowerCase().includes(searchQuery.toLowerCase());
    const matchesVenue = filterVenue === "all" || event.venueName === filterVenue;
    const matchesCategory =
      filterCategory === "all" ||
      event.categories?.some((c) => c === filterCategory);
    return matchesSearch && matchesVenue && matchesCategory;
  });

  const handleBuyTicket = useCallback(async (data: TicketRequestDto) => {
    if (useDemo) {
      alert(`Демо-режим: билет на место ${data.seatNumber} забронирован! (симуляция)`);
      return;
    }
    try {
      await ticketsApi.create(data);
      alert("Билет успешно куплен!");
    } catch (error) {
      console.error("Failed to buy ticket:", error);
    }
  }, [useDemo]);

  const openBuyDialog = (event: EventResponseDto) => {
    setBuyingEvent(event);
    setIsBuyOpen(true);
  };

  const clearFilters = () => {
    setSearchQuery("");
    setFilterVenue("all");
    setFilterCategory("all");
  };

  const hasFilters = searchQuery || filterVenue !== "all" || filterCategory !== "all";

  return (
    <div className="min-h-screen bg-background">
      <Header />
      <main className="mx-auto max-w-7xl px-4 py-8 sm:px-6 lg:px-8">
        <div className="mb-8 flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
          <div>
            <h1 className="text-3xl font-bold text-foreground">Афиша мероприятий</h1>
            <p className="mt-1 text-muted-foreground">
              {useDemo && (
                <span className="mr-2 rounded bg-yellow-100 px-2 py-0.5 text-xs text-yellow-800">
                  Демо-режим
                </span>
              )}
              Найдено: {filteredEvents.length} мероприятий
            </p>
          </div>

        </div>

        {/* Filters */}
        <div className="mb-6 rounded-lg border border-border bg-card p-4">
          <div className="flex flex-col gap-4 sm:flex-row sm:items-center">
            <div className="relative flex-1">
              <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
              <Input
                placeholder="Поиск по названию..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="pl-10"
              />
            </div>
            <Select value={filterVenue} onValueChange={setFilterVenue}>
              <SelectTrigger className="w-full sm:w-48">
                <SelectValue placeholder="Площадка" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">Все площадки</SelectItem>
                {venues.map((venue) => (
                  <SelectItem key={venue.id} value={venue.name}>
                    {venue.name}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
            <Select value={filterCategory} onValueChange={setFilterCategory}>
              <SelectTrigger className="w-full sm:w-48">
                <SelectValue placeholder="Категория" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">Все категории</SelectItem>
                {categories.map((category) => (
                  <SelectItem key={category.id} value={category.name}>
                    {category.name}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
            {hasFilters && (
              <Button variant="ghost" size="sm" onClick={clearFilters}>
                <X className="mr-1 h-4 w-4" />
                Сбросить
              </Button>
            )}
          </div>
        </div>

        {/* Events Grid */}
        {filteredEvents.length === 0 ? (
          <div className="flex flex-col items-center justify-center rounded-lg border border-dashed border-border py-16">
            <p className="text-lg text-muted-foreground">Мероприятия не найдены</p>
            {hasFilters && (
              <Button variant="link" onClick={clearFilters}>
                Сбросить фильтры
              </Button>
            )}
          </div>
        ) : (
          <div className="grid gap-6 sm:grid-cols-2 lg:grid-cols-3">
            {filteredEvents.map((event) => (
              <EventCard
                key={event.id}
                event={event}
                onBuyTicket={openBuyDialog}
              />
            ))}
          </div>
        )}
      </main>

      <BuyTicketDialog
        open={isBuyOpen}
        onOpenChange={setIsBuyOpen}
        event={buyingEvent}
        users={users}
        onSubmit={handleBuyTicket}
      />
    </div>
  );
}
