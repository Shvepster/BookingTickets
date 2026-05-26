"use client";

import useSWR from "swr";
import { DashboardLayout } from "@/components/dashboard-layout";
import { ticketsApi } from "@/lib/api";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";
import { Button } from "@/components/ui/button";
import { Trash2, Loader2 } from "lucide-react";

export default function TicketsManagementPage() {
    const { data: tickets, isLoading, mutate } = useSWR("/tickets", ticketsApi.getAll);

    const handleDelete = async (id: number) => {
        if (!confirm("Аннулировать это бронирование билета?")) return;
        try {
            await ticketsApi.delete(id);
            mutate();
        } catch (error: any) {
            alert(error.message);
        }
    };

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
                                            <Button variant="destructive" size="icon" onClick={() => handleDelete(ticket.id)}>
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
        </DashboardLayout>
    );
}