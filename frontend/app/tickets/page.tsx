"use client";
import { useState } from "react";
import useSWR from "swr";
import { DashboardLayout } from "@/components/dashboard-layout";
import { ticketsApi } from "@/lib/api";
import { useAuth } from "@/lib/auth-context";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";
import { Button } from "@/components/ui/button";
import { Trash2, Loader2, ShieldAlert } from "lucide-react";

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

export default function TicketsManagementPage() {
    const { isAdmin } = useAuth();
    const { data: tickets, isLoading, mutate } = useSWR("/tickets", ticketsApi.getAll);

    const [ticketToDelete, setTicketToDelete] = useState<number | null>(null);

    const executeDelete = async () => {
        if (!ticketToDelete) return;
        try {
            await ticketsApi.delete(ticketToDelete);
            mutate();
            setTicketToDelete(null);
        } catch (error: any) {
            alert(error.message);
        }
    };

    if (!isAdmin) {
        return (
            <DashboardLayout>
                <div className="flex flex-col items-center justify-center py-20 text-muted-foreground">
                    <ShieldAlert className="h-16 w-16 mb-4 text-destructive opacity-50" />
                    <h1 className="text-2xl font-bold text-foreground mb-2">Доступ запрещен</h1>
                    <p>У вас нет прав для просмотра всех билетов системы.</p>
                </div>
            </DashboardLayout>
        );
    }

    return (
        <DashboardLayout>
            <div className="flex flex-col space-y-6">
                <h1 className="text-2xl font-bold">Все забронированные билеты в системе</h1>

                <div className="bg-background rounded-md border">
                    <Table>
                        <TableHeader>
                            <TableRow>
                                <TableHead className="w-[80px]">ID</TableHead>
                                <TableHead>Событие</TableHead>
                                <TableHead>Покупатель (User)</TableHead>
                                <TableHead>Место</TableHead>
                                <TableHead className="text-right">Действия</TableHead>
                            </TableRow>
                        </TableHeader>
                        <TableBody>
                            {isLoading ? (
                                <TableRow>
                                    <TableCell colSpan={5} className="text-center py-10">
                                        <Loader2 className="h-6 w-6 animate-spin mx-auto text-muted-foreground" />
                                    </TableCell>
                                </TableRow>
                            ) : !tickets || tickets.length === 0 ? (
                                <TableRow>
                                    <TableCell colSpan={5} className="text-center py-10 text-muted-foreground">Билетов в системе нет</TableCell>
                                </TableRow>
                            ) : (
                                tickets.map((ticket) => (
                                    <TableRow key={ticket.id}>
                                        <TableCell className="font-medium">#{ticket.id}</TableCell>
                                        <TableCell>{ticket.eventTitle}</TableCell>
                                        <TableCell className="font-semibold">{ticket.userName}</TableCell>
                                        <TableCell>
                                            <span className="px-2 py-1 bg-secondary text-secondary-foreground rounded text-xs">
                                                {ticket.seatNumber}
                                            </span>
                                        </TableCell>
                                        <TableCell className="text-right">
                                            <Button variant="destructive" size="icon" onClick={() => setTicketToDelete(ticket.id)}>
                                                <Trash2 className="h-4 w-4" />
                                            </Button>
                                        </TableCell>
                                    </TableRow>
                                ))
                            )}
                        </TableBody>
                    </Table>
                </div>
            </div>

            <AlertDialog open={!!ticketToDelete} onOpenChange={(open) => !open && setTicketToDelete(null)}>
                <AlertDialogContent>
                    <AlertDialogHeader>
                        <AlertDialogTitle>Аннулировать билет?</AlertDialogTitle>
                        <AlertDialogDescription>
                            Вы уверены, что хотите аннулировать это бронирование билета? Это действие нельзя отменить.
                        </AlertDialogDescription>
                    </AlertDialogHeader>
                    <AlertDialogFooter>
                        <AlertDialogCancel>Отмена</AlertDialogCancel>
                        <AlertDialogAction onClick={executeDelete} className="bg-destructive text-white hover:bg-destructive/90">
                            Аннулировать
                        </AlertDialogAction>
                    </AlertDialogFooter>
                </AlertDialogContent>
            </AlertDialog>
        </DashboardLayout>
    );
}