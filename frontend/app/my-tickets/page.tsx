"use client";

import useSWR from "swr";
import { DashboardLayout } from "@/components/dashboard-layout";
import { ticketsApi } from "@/lib/api";
import { useAuth } from "@/lib/auth-context";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";
import { Button } from "@/components/ui/button";
import { Ticket, Trash2, Loader2 } from "lucide-react";

export default function MyTicketsPage() {
    const { user } = useAuth();

    // Получаем билеты только текущего авторизованного пользователя
    const { data: tickets, isLoading, mutate } = useSWR(
        user ? `/tickets/user/${user.id}` : null,
        () => ticketsApi.getByUserId(user!.id)
    );

    const handleRefund = async (id: number) => {
        if (!confirm("Вы действительно хотите вернуть этот билет?")) return;
        try {
            await ticketsApi.delete(id);
            mutate();
            alert("Возврат успешно оформлен!");
        } catch (error: any) {
            alert(error.message);
        }
    };

    return (
        <DashboardLayout>
            <div className="flex flex-col space-y-6">
                <div className="flex items-center gap-3">
                    <div className="p-2 bg-primary/10 rounded-full text-primary">
                        <Ticket className="h-6 w-6" />
                    </div>
                    <h1 className="text-2xl font-bold">Мои забронированные билеты</h1>
                </div>

                <div className="bg-background rounded-md border">
                    <Table>
                        <TableHeader>
                            <TableRow>
                                <TableHead className="w-[100px]">ID Билета</TableHead>
                                <TableHead>Событие</TableHead>
                                <TableHead>Место</TableHead>
                                <TableHead className="text-right">Действия</TableHead>
                            </TableRow>
                        </TableHeader>
                        <TableBody>
                            {isLoading ? (
                                <TableRow>
                                    <TableCell colSpan={4} className="text-center py-10">
                                        <Loader2 className="h-6 w-6 animate-spin mx-auto text-muted-foreground" />
                                    </TableCell>
                                </TableRow>
                            ) : !tickets || tickets.length === 0 ? (
                                <TableRow>
                                    <TableCell colSpan={4} className="text-center py-10 text-muted-foreground">
                                        У вас пока нет забронированных билетов
                                    </TableCell>
                                </TableRow>
                            ) : (
                                tickets.map((ticket) => (
                                    <TableRow key={ticket.id}>
                                        <TableCell className="font-medium">#{ticket.id}</TableCell>
                                        <TableCell className="font-semibold">{ticket.eventTitle}</TableCell>
                                        <TableCell>
                      <span className="px-2 py-1 bg-secondary text-secondary-foreground rounded text-xs font-mono">
                        {ticket.seatNumber}
                      </span>
                                        </TableCell>
                                        <TableCell className="text-right">
                                            <Button variant="destructive" size="sm" onClick={() => handleRefund(ticket.id)}>
                                                <Trash2 className="mr-2 h-4 w-4" /> Вернуть билет
                                            </Button>
                                        </TableCell>
                                    </TableRow>
                                ))
                            )}
                        </TableBody>
                    </Table>
                </div>
            </div>
        </DashboardLayout>
    );
}