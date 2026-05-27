// frontend/app/users/page.tsx
"use client";
import { useState } from "react";
import useSWR from "swr";
import { DashboardLayout } from "@/components/dashboard-layout";
import { usersApi } from "@/lib/api";
import { useAuth } from "@/lib/auth-context";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from "@/components/ui/dialog";
import { Pencil, Trash2, Loader2, Mail } from "lucide-react";
import type { UserResponseDto } from "@/lib/types";
import {
    AlertDialog,
    AlertDialogAction,
    AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription,
    AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogTitle,
} from "@/components/ui/alert-dialog";

export default function UsersPage() {
    const { isAdmin } = useAuth();
    const { data: users, isLoading, mutate } = useSWR("/users", usersApi.getAll);
    const [isOpen, setIsOpen] = useState(false);
    const [editingUser, setEditingUser] = useState<UserResponseDto | null>(null);
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [userToDelete, setUserToDelete] = useState<number | null>(null);

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setIsSubmitting(true);
        const formData = new FormData(e.currentTarget);
        const username = formData.get("username") as string;
        const email = formData.get("email") as string;
        const password = formData.get("password") as string;
        try {
            if (editingUser) {
                await usersApi.update(editingUser.id, { username, email, password: password || undefined });
            } else {
                await usersApi.create({ username, email, password });
            }
            mutate();
            setIsOpen(false);
        } catch (error: any) {
            alert(error.message);
        } finally {
            setIsSubmitting(false);
        }
    };

    const executeDelete = async () => {
        if (!userToDelete) return;
        try {
            await usersApi.delete(userToDelete);
            mutate();
            setUserToDelete(null);
        } catch (error: any) {
            alert(error.message);
        }
    };

    const openDialog = (user?: UserResponseDto) => {
        setEditingUser(user || null);
        setIsOpen(true);
    };

    return (
        <DashboardLayout>
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-2xl font-bold">Пользователи</h1>
            </div>
            <div className="bg-background rounded-md border">
                <Table>
                    <TableHeader>
                        <TableRow>
                            <TableHead>Логин</TableHead>
                            <TableHead>Email</TableHead>
                            {isAdmin && <TableHead className="text-right">Действия</TableHead>}
                        </TableRow>
                    </TableHeader>
                    <TableBody>
                        {isLoading ? (
                            <TableRow>
                                <TableCell colSpan={isAdmin ? 3 : 2} className="text-center py-10">
                                    <Loader2 className="h-6 w-6 animate-spin mx-auto text-muted-foreground" />
                                </TableCell>
                            </TableRow>
                        ) : users?.length === 0 ? (
                            <TableRow>
                                <TableCell colSpan={isAdmin ? 3 : 2} className="text-center py-10 text-muted-foreground">Пользователей пока нет</TableCell>
                            </TableRow>
                        ) : (
                            users?.map((user) => (
                                <TableRow key={user.id}>
                                    <TableCell>{user.username}</TableCell>
                                    <TableCell>
                                        <div className="flex items-center text-muted-foreground">
                                            <Mail className="mr-2 h-4 w-4" /> {user.email}
                                        </div>
                                    </TableCell>
                                    {isAdmin && (
                                        <TableCell className="text-right space-x-2">
                                            <Button variant="outline" size="icon" onClick={() => openDialog(user)}>
                                                <Pencil className="h-4 w-4" />
                                            </Button>
                                            <Button variant="destructive" size="icon" onClick={() => setUserToDelete(user.id)}>
                                                <Trash2 className="h-4 w-4" />
                                            </Button>
                                        </TableCell>
                                    )}
                                </TableRow>
                            ))
                        )}
                    </TableBody>
                </Table>
            </div>
            <Dialog open={isOpen} onOpenChange={setIsOpen}>
                <DialogContent>
                    <DialogHeader>
                        <DialogTitle>{editingUser ? "Редактировать пользователя" : "Добавить пользователя"}</DialogTitle>
                    </DialogHeader>
                    <form onSubmit={handleSubmit} className="space-y-4">
                        <div className="space-y-2">
                            <Label htmlFor="username">Логин</Label>
                            <Input id="username" name="username" defaultValue={editingUser?.username} required />
                        </div>
                        <div className="space-y-2">
                            <Label htmlFor="email">Email</Label>
                            <Input id="email" name="email" type="email" defaultValue={editingUser?.email} required />
                        </div>
                        <div className="space-y-2">
                            <Label htmlFor="password">{editingUser ? "Новый пароль (оставьте пустым, если не меняете)" : "Пароль"}</Label>
                            <Input id="password" name="password" type="password" required={!editingUser} />
                        </div>
                        <DialogFooter>
                            <Button type="button" variant="outline" onClick={() => setIsOpen(false)}>Отмена</Button>
                            <Button type="submit" disabled={isSubmitting}>
                                {isSubmitting ? <Loader2 className="mr-2 h-4 w-4 animate-spin" /> : "Сохранить"}
                            </Button>
                        </DialogFooter>
                    </form>
                </DialogContent>
            </Dialog>
            <AlertDialog open={!!userToDelete} onOpenChange={(open) => !open && setUserToDelete(null)}>
                <AlertDialogContent>
                    <AlertDialogHeader>
                        <AlertDialogTitle>Удалить пользователя?</AlertDialogTitle>
                        <AlertDialogDescription>
                            Вы уверены, что хотите удалить этого пользователя? Это действие нельзя отменить.
                        </AlertDialogDescription>
                    </AlertDialogHeader>
                    <AlertDialogFooter>
                        <AlertDialogCancel>Отмена</AlertDialogCancel>
                        <AlertDialogAction onClick={executeDelete} className="bg-destructive text-white hover:bg-destructive/90">
                            Удалить
                        </AlertDialogAction>
                    </AlertDialogFooter>
                </AlertDialogContent>
            </AlertDialog>
        </DashboardLayout>
    );
}