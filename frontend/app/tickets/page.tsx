"use client";

import { useState, useEffect, useCallback } from "react";
import useSWR, { mutate } from "swr";
import { Header } from "@/components/header";
import { DataTable } from "@/components/data-table";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Badge } from "@/components/ui/badge";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Plus, Search, Filter } from "lucide-react";
import { ticketsApi, usersApi, eventsApi, fetcher } from "@/lib/api";
import type { TicketResponseDto, TicketRequestDto, UserResponseDto, EventResponseDto } from "@/lib/types";

const demoTickets: TicketResponseDto[] = [
  { id: 1, seatNumber: "A-15", userName: "ivan_petrov", eventTitle: "Концерт Scorpions" },
  { id: 2, seatNumber: "B-22", userName: "maria_sidorova", eventTitle: "Балет Лебединое озеро" },
  { id: 3, seatNumber: "C-5", userName: "alex_kozlov", eventTitle: "Комедия Ревизор" },
  { id: 4, seatNumber: "VIP-1", userName: "ivan_petrov", eventTitle: "DJ Max Live" },
  { id: 5, seatNumber: "D-10", userName: "elena_novikova", eventTitle: "Детский спектакль Золушка" },
];

const demoUsers: UserResponseDto[] = [
  { id: 1, username: "ivan_petrov", email: "ivan@example.com" },
  { id: 2, username: "maria_sidorova", email: "maria@example.com" },
  { id: 3, username: "alex_kozlov", email: "alex@example.com" },
];

const demoEvents: EventResponseDto[] = [
  { id: 1, title: "Концерт Scorpions", formattedPrice: "150.00 BYN", venueName: "Минск-Арена", eventDate: "2026-07-15T20:00:00", categories: ["Рок"] },
  { id: 2, title: "Балет Лебединое озеро", formattedPrice: "80.00 BYN", venueName: "Большой театр", eventDate: "2026-06-20T19:00:00", categories: ["Балет"] },
  { id: 3, title: "Комедия Ревизор", formattedPrice: "45.00 BYN", venueName: "Купаловский театр", eventDate: "2026-06-10T18:30:00", categories: ["Театр"] },
];

export default function TicketsPage() {
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [searchQuery, setSearchQuery] = useState("");
  const [filterUser, setFilterUser] = useState<string>("all");
  const [seatNumber, setSeatNumber] = useState("");
  const [userId, setUserId] = useState("");
  const [eventId, setEventId] = useState("");
  const [useDemo, setUseDemo] = useState(false);

  const { data: apiTickets, error } = useSWR<TicketResponseDto[]>(
    "/api/tickets",
    fetcher,
    { revalidateOnFocus: false }
  );
  const { data: apiUsers } = useSWR<UserResponseDto[]>("/api/users", fetcher, { revalidateOnFocus: false });
  const { data: apiEvents } = useSWR<EventResponseDto[]>("/api/events/all", fetcher, { revalidateOnFocus: false });

  useEffect(() => {
    if (error) setUseDemo(true);
  }, [error]);

  const tickets = useDemo ? demoTickets : (apiTickets || []);
  const users = useDemo ? demoUsers : (apiUsers || []);
  const events = useDemo ? demoEvents : (apiEvents || []);

  const filteredTickets = tickets.filter((ticket) => {
    const matchesSearch =
      ticket.seatNumber.toLowerCase().includes(searchQuery.toLowerCase()) ||
      ticket.eventTitle.toLowerCase().includes(searchQuery.toLowerCase()) ||
      ticket.userName.toLowerCase().includes(searchQuery.toLowerCase());
    const matchesUser = filterUser === "all" || ticket.userName === filterUser;
    return matchesSearch && matchesUser;
  });

  const openDialog = () => {
    setSeatNumber("");
    setUserId("");
    setEventId("");
    setIsDialogOpen(true);
  };

  const handleSubmit = useCallback(async (e: React.FormEvent) => {
    e.preventDefault();
    const data: TicketRequestDto = {
      seatNumber,
      userId: parseInt(userId),
      eventId: parseInt(eventId),
    };

    if (useDemo) {
      alert("Демо-режим: покупка билетов недоступна. Подключите API.");
      setIsDialogOpen(false);
      return;
    }

    try {
      await ticketsApi.create(data);
      mutate("/api/tickets");
      setIsDialogOpen(false);
    } catch (error) {
      console.error("Failed to create ticket:", error);
    }
  }, [seatNumber, userId, eventId, useDemo]);

  const handleDelete = useCallback(async (id: number) => {
    if (useDemo) {
      alert("Демо-режим: возврат билетов недоступен. Подключите API.");
      return;
    }
    if (!confirm("Вернуть билет?")) return;
    try {
      await ticketsApi.delete(id);
      mutate("/api/tickets");
    } catch (error) {
      console.error("Failed to delete ticket:", error);
    }
  }, [useDemo]);

  const columns = [
    { key: "id" as const, header: "ID" },
    {
      key: "seatNumber" as const,
      header: "Место",
      render: (ticket: TicketResponseDto) => (
        <Badge variant="outline">{ticket.seatNumber}</Badge>
      ),
    },
    { key: "userName" as const, header: "Пользователь" },
    { key: "eventTitle" as const, header: "Мероприятие" },
  ];

  const uniqueUsers = [...new Set(tickets.map((t) => t.userName))];

  return (
    <div className="min-h-screen bg-background">
      <Header />
      <main className="mx-auto max-w-7xl px-4 py-8 sm:px-6 lg:px-8">
        <div className="mb-8 flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
          <div>
            <h1 className="text-3xl font-bold text-foreground">Билеты</h1>
            <p className="mt-1 text-muted-foreground">
              {useDemo && (
                <span className="mr-2 rounded bg-yellow-100 px-2 py-0.5 text-xs text-yellow-800">
                  Демо-режим
                </span>
              )}
              Купленные билеты (OneToMany: User → Ticket, Event → Ticket)
            </p>
          </div>
          <Button onClick={openDialog}>
            <Plus className="mr-2 h-4 w-4" />
            Купить билет
          </Button>
        </div>

        <div className="mb-6 flex flex-col gap-4 sm:flex-row sm:items-center">
          <div className="relative flex-1 max-w-sm">
            <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
            <Input
              placeholder="Поиск по месту, событию, пользователю..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="pl-10"
            />
          </div>
          <div className="flex items-center gap-2">
            <Filter className="h-4 w-4 text-muted-foreground" />
            <Select value={filterUser} onValueChange={setFilterUser}>
              <SelectTrigger className="w-48">
                <SelectValue placeholder="Пользователь" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">Все пользователи</SelectItem>
                {uniqueUsers.map((userName) => (
                  <SelectItem key={userName} value={userName}>
                    {userName}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>
        </div>

        <DataTable
          data={filteredTickets}
          columns={columns}
          onDelete={handleDelete}
        />

        {/* OneToMany relationships visualization */}
        <div className="mt-8 grid gap-4 md:grid-cols-2">
          <div className="rounded-lg border border-border bg-card p-6">
            <h2 className="mb-4 text-lg font-semibold text-foreground">
              Связь User → Ticket (OneToMany)
            </h2>
            <p className="text-sm text-muted-foreground">
              Один пользователь может владеть множеством билетов.
            </p>
          </div>
          <div className="rounded-lg border border-border bg-card p-6">
            <h2 className="mb-4 text-lg font-semibold text-foreground">
              Связь Event → Ticket (OneToMany)
            </h2>
            <p className="text-sm text-muted-foreground">
              На одно мероприятие выпускается множество билетов.
            </p>
          </div>
        </div>
      </main>

      <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
        <DialogContent className="sm:max-w-[425px]">
          <DialogHeader>
            <DialogTitle>Купить билет</DialogTitle>
            <DialogDescription>
              Выберите мероприятие, пользователя и место
            </DialogDescription>
          </DialogHeader>
          <form onSubmit={handleSubmit}>
            <div className="grid gap-4 py-4">
              <div className="grid gap-2">
                <Label htmlFor="event">Мероприятие</Label>
                <Select value={eventId} onValueChange={setEventId} required>
                  <SelectTrigger>
                    <SelectValue placeholder="Выберите мероприятие" />
                  </SelectTrigger>
                  <SelectContent>
                    {events.map((event) => (
                      <SelectItem key={event.id} value={String(event.id)}>
                        {event.title} - {event.formattedPrice}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>
              <div className="grid gap-2">
                <Label htmlFor="user">Пользователь</Label>
                <Select value={userId} onValueChange={setUserId} required>
                  <SelectTrigger>
                    <SelectValue placeholder="Выберите пользователя" />
                  </SelectTrigger>
                  <SelectContent>
                    {users.map((user) => (
                      <SelectItem key={user.id} value={String(user.id)}>
                        {user.username} ({user.email})
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>
              <div className="grid gap-2">
                <Label htmlFor="seat">Номер места</Label>
                <Input
                  id="seat"
                  value={seatNumber}
                  onChange={(e) => setSeatNumber(e.target.value)}
                  placeholder="A-15, VIP-1, B-22..."
                  required
                />
              </div>
            </div>
            <DialogFooter>
              <Button type="button" variant="outline" onClick={() => setIsDialogOpen(false)}>
                Отмена
              </Button>
              <Button type="submit">Купить</Button>
            </DialogFooter>
          </form>
        </DialogContent>
      </Dialog>
    </div>
  );
}
