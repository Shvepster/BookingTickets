"use client";

import { useState, useEffect, useCallback } from "react";
import useSWR, { mutate } from "swr";
import { Header } from "@/components/header";
import { DataTable } from "@/components/data-table";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Plus, Search } from "lucide-react";
import { usersApi, fetcher } from "@/lib/api";
import type { UserResponseDto, UserRequestDto } from "@/lib/types";

const demoUsers: UserResponseDto[] = [
  { id: 1, username: "ivan_petrov", email: "ivan@example.com" },
  { id: 2, username: "maria_sidorova", email: "maria@example.com" },
  { id: 3, username: "alex_kozlov", email: "alex@example.com" },
  { id: 4, username: "elena_novikova", email: "elena@example.com" },
  { id: 5, username: "dmitry_volkov", email: "dmitry@example.com" },
];

export default function UsersPage() {
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [editingUser, setEditingUser] = useState<UserResponseDto | null>(null);
  const [searchQuery, setSearchQuery] = useState("");
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [useDemo, setUseDemo] = useState(false);

  const { data: apiUsers, error } = useSWR<UserResponseDto[]>(
    "/api/users",
    fetcher,
    { revalidateOnFocus: false }
  );

  useEffect(() => {
    if (error) setUseDemo(true);
  }, [error]);

  const users = useDemo ? demoUsers : (apiUsers || []);

  const filteredUsers = users.filter(
    (user) =>
      user.username.toLowerCase().includes(searchQuery.toLowerCase()) ||
      user.email.toLowerCase().includes(searchQuery.toLowerCase())
  );

  const openDialog = (user?: UserResponseDto) => {
    if (user) {
      setEditingUser(user);
      setUsername(user.username);
      setEmail(user.email);
      setPassword("");
    } else {
      setEditingUser(null);
      setUsername("");
      setEmail("");
      setPassword("");
    }
    setIsDialogOpen(true);
  };

  const handleSubmit = useCallback(async (e: React.FormEvent) => {
    e.preventDefault();
    const data: UserRequestDto = { username, email, password };

    if (useDemo) {
      alert("Демо-режим: операции сохранения недоступны. Подключите API.");
      setIsDialogOpen(false);
      return;
    }

    try {
      if (editingUser) {
        await usersApi.update(editingUser.id, data);
      } else {
        await usersApi.create(data);
      }
      mutate("/api/users");
      setIsDialogOpen(false);
    } catch (error) {
      console.error("Failed to save user:", error);
    }
  }, [username, email, password, editingUser, useDemo]);

  const handleDelete = useCallback(async (id: number) => {
    if (useDemo) {
      alert("Демо-режим: удаление недоступно. Подключите API.");
      return;
    }
    if (!confirm("Удалить пользователя?")) return;
    try {
      await usersApi.delete(id);
      mutate("/api/users");
    } catch (error) {
      console.error("Failed to delete user:", error);
    }
  }, [useDemo]);

  const columns = [
    { key: "id" as const, header: "ID" },
    { key: "username" as const, header: "Имя пользователя" },
    { key: "email" as const, header: "Email" },
  ];

  return (
    <div className="min-h-screen bg-background">
      <Header />
      <main className="mx-auto max-w-7xl px-4 py-8 sm:px-6 lg:px-8">
        <div className="mb-8 flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
          <div>
            <h1 className="text-3xl font-bold text-foreground">Пользователи</h1>
            <p className="mt-1 text-muted-foreground">
              {useDemo && (
                <span className="mr-2 rounded bg-yellow-100 px-2 py-0.5 text-xs text-yellow-800">
                  Демо-режим
                </span>
              )}
              Управление профилями клиентов (OneToMany: User → Ticket)
            </p>
          </div>
          <Button onClick={() => openDialog()}>
            <Plus className="mr-2 h-4 w-4" />
            Добавить
          </Button>
        </div>

        <div className="mb-6">
          <div className="relative max-w-sm">
            <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
            <Input
              placeholder="Поиск по имени или email..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="pl-10"
            />
          </div>
        </div>

        <DataTable
          data={filteredUsers}
          columns={columns}
          onEdit={openDialog}
          onDelete={handleDelete}
        />
      </main>

      <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
        <DialogContent className="sm:max-w-[425px]">
          <DialogHeader>
            <DialogTitle>
              {editingUser ? "Редактировать пользователя" : "Добавить пользователя"}
            </DialogTitle>
            <DialogDescription>
              {editingUser
                ? "Измените данные пользователя"
                : "Заполните данные для регистрации"}
            </DialogDescription>
          </DialogHeader>
          <form onSubmit={handleSubmit}>
            <div className="grid gap-4 py-4">
              <div className="grid gap-2">
                <Label htmlFor="username">Имя пользователя</Label>
                <Input
                  id="username"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  placeholder="ivan_petrov"
                  required
                />
              </div>
              <div className="grid gap-2">
                <Label htmlFor="email">Email</Label>
                <Input
                  id="email"
                  type="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  placeholder="ivan@example.com"
                  required
                />
              </div>
              <div className="grid gap-2">
                <Label htmlFor="password">
                  Пароль {editingUser && "(оставьте пустым, чтобы не менять)"}
                </Label>
                <Input
                  id="password"
                  type="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  placeholder="••••••••"
                  required={!editingUser}
                />
              </div>
            </div>
            <DialogFooter>
              <Button type="button" variant="outline" onClick={() => setIsDialogOpen(false)}>
                Отмена
              </Button>
              <Button type="submit">
                {editingUser ? "Сохранить" : "Создать"}
              </Button>
            </DialogFooter>
          </form>
        </DialogContent>
      </Dialog>
    </div>
  );
}
