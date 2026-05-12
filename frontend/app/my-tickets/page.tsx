"use client";

import { useState, useCallback } from "react";
import useSWR, { mutate } from "swr";
import { Header } from "@/components/header";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
  AlertDialogTrigger,
} from "@/components/ui/alert-dialog";
import { Trash2, Ticket, User } from "lucide-react";
import { ticketsApi, fetcher } from "@/lib/api";
import type { TicketResponseDto, UserResponseDto } from "@/lib/types";

// Demo data
const demoUsers: UserResponseDto[] = [
  { id: 1, username: "ivan_petrov", email: "ivan@example.com" },
  { id: 2, username: "anna_sidorova", email: "anna@example.com" },
  { id: 3, username: "sergey_kozlov", email: "sergey@example.com" },
];

const demoTickets: TicketResponseDto[] = [
  { id: 1, seatNumber: "A1", userName: "ivan_petrov", eventTitle: "Концерт группы Кино" },
  { id: 2, seatNumber: "A2", userName: "ivan_petrov", eventTitle: "Концерт группы Кино" },
  { id: 3, seatNumber: "B5", userName: "ivan_petrov", eventTitle: "Балет Лебединое озеро" },
  { id: 4, seatNumber: "C3", userName: "anna_sidorova", eventTitle: "Стендап шоу" },
  { id: 5, seatNumber: "D10", userName: "sergey_kozlov", eventTitle: "Опера Кармен" },
];

export default function MyTicketsPage() {
  const [selectedUserId, setSelectedUserId] = useState<number | null>(null);
  const [useDemo, setUseDemo] = useState(false);

  // Fetch users
  const { data: usersData, error: usersError } = useSWR<UserResponseDto[]>(
    "/api/users",
    fetcher,
    {
      onError: () => setUseDemo(true),
      revalidateOnFocus: false,
    }
  );

  // Fetch tickets for selected user
  const { data: ticketsData, error: ticketsError } = useSWR<TicketResponseDto[]>(
    selectedUserId ? `/api/tickets/user/${selectedUserId}` : null,
    fetcher,
    {
      onError: () => setUseDemo(true),
      revalidateOnFocus: false,
    }
  );

  const users = useDemo ? demoUsers : (usersData || []);
  
  // Filter demo tickets by selected user
  const tickets = useDemo 
    ? (selectedUserId 
        ? demoTickets.filter(t => t.userName === demoUsers.find(u => u.id === selectedUserId)?.username)
        : [])
    : (ticketsData || []);

  const selectedUser = users.find(u => u.id === selectedUserId);

  const handleDeleteTicket = useCallback(async (ticketId: number) => {
    if (useDemo) {
      alert("Демо-режим: удаление билетов недоступно. Подключите API.");
      return;
    }
    try {
      await ticketsApi.delete(ticketId);
      mutate(`/api/tickets/user/${selectedUserId}`);
    } catch (error) {
      console.error("Failed to delete ticket:", error);
    }
  }, [selectedUserId, useDemo]);

  return (
    <div className="min-h-screen bg-background">
      <Header />
      <main className="mx-auto max-w-7xl px-4 py-8 sm:px-6 lg:px-8">
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-foreground">Мои билеты</h1>
          <p className="mt-2 text-muted-foreground">
            Просмотр и управление вашими билетами
          </p>
        </div>

        {/* User Selection */}
        <Card className="mb-8">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <User className="h-5 w-5" />
              Выберите пользователя
            </CardTitle>
            <CardDescription>
              Выберите вашу учетную запись для просмотра билетов
            </CardDescription>
          </CardHeader>
          <CardContent>
            <Select
              value={selectedUserId?.toString() || ""}
              onValueChange={(value) => setSelectedUserId(Number(value))}
            >
              <SelectTrigger className="w-full max-w-sm">
                <SelectValue placeholder="Выберите пользователя..." />
              </SelectTrigger>
              <SelectContent>
                {users.map((user) => (
                  <SelectItem key={user.id} value={user.id.toString()}>
                    {user.username} ({user.email})
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          </CardContent>
        </Card>

        {/* Tickets List */}
        {selectedUserId && (
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Ticket className="h-5 w-5" />
                Билеты пользователя {selectedUser?.username}
              </CardTitle>
              <CardDescription>
                {tickets.length > 0 
                  ? `Найдено билетов: ${tickets.length}`
                  : "У вас пока нет билетов"
                }
              </CardDescription>
            </CardHeader>
            <CardContent>
              {tickets.length > 0 ? (
                <Table>
                  <TableHeader>
                    <TableRow>
                      <TableHead>Мероприятие</TableHead>
                      <TableHead>Место</TableHead>
                      <TableHead className="w-24 text-right">Действия</TableHead>
                    </TableRow>
                  </TableHeader>
                  <TableBody>
                    {tickets.map((ticket) => (
                      <TableRow key={ticket.id}>
                        <TableCell className="font-medium">
                          {ticket.eventTitle}
                        </TableCell>
                        <TableCell>{ticket.seatNumber}</TableCell>
                        <TableCell className="text-right">
                          <AlertDialog>
                            <AlertDialogTrigger asChild>
                              <Button variant="destructive" size="sm">
                                <Trash2 className="h-4 w-4" />
                              </Button>
                            </AlertDialogTrigger>
                            <AlertDialogContent>
                              <AlertDialogHeader>
                                <AlertDialogTitle>Удалить билет?</AlertDialogTitle>
                                <AlertDialogDescription>
                                  Вы уверены, что хотите удалить билет на &quot;{ticket.eventTitle}&quot; (место {ticket.seatNumber})? 
                                  Это действие нельзя отменить.
                                </AlertDialogDescription>
                              </AlertDialogHeader>
                              <AlertDialogFooter>
                                <AlertDialogCancel>Отмена</AlertDialogCancel>
                                <AlertDialogAction
                                  onClick={() => handleDeleteTicket(ticket.id)}
                                >
                                  Удалить
                                </AlertDialogAction>
                              </AlertDialogFooter>
                            </AlertDialogContent>
                          </AlertDialog>
                        </TableCell>
                      </TableRow>
                    ))}
                  </TableBody>
                </Table>
              ) : (
                <div className="flex flex-col items-center justify-center py-12 text-center">
                  <Ticket className="mb-4 h-12 w-12 text-muted-foreground/50" />
                  <p className="text-muted-foreground">
                    У вас пока нет купленных билетов
                  </p>
                  <Button variant="link" className="mt-2" asChild>
                    <a href="/">Перейти к мероприятиям</a>
                  </Button>
                </div>
              )}
            </CardContent>
          </Card>
        )}

        {!selectedUserId && (
          <Card>
            <CardContent className="flex flex-col items-center justify-center py-12 text-center">
              <User className="mb-4 h-12 w-12 text-muted-foreground/50" />
              <p className="text-muted-foreground">
                Выберите пользователя, чтобы увидеть ваши билеты
              </p>
            </CardContent>
          </Card>
        )}

        {useDemo && (
          <p className="mt-4 text-center text-sm text-muted-foreground">
            Демо-режим: данные отображаются локально. Подключите API для работы с реальными данными.
          </p>
        )}
      </main>
    </div>
  );
}
