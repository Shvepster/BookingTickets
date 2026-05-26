"use client";

import { useState } from "react";
import useSWR from "swr";
import { DashboardLayout } from "@/components/dashboard-layout";
import { venuesApi } from "@/lib/api";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from "@/components/ui/dialog";
import { Plus, Pencil, Trash2, Loader2, MapPin } from "lucide-react";
import type { VenueResponseDto } from "@/lib/types";

export default function VenuesPage() {
    const { data: venues, isLoading, mutate } = useSWR("/venues", venuesApi.getAll);
    const [isOpen, setIsOpen] = useState(false);
    const [editingVenue, setEditingVenue] = useState<VenueResponseDto | null>(null);
    const [isSubmitting, setIsSubmitting] = useState(false);

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setIsSubmitting(true);
        const formData = new FormData(e.currentTarget);
        const name = formData.get("name") as string;
        const address = formData.get("address") as string;

        try {
            if (editingVenue) {
                await venuesApi.update(editingVenue.id, { name, address });
            } else {
                await venuesApi.create({ name, address });
            }
            mutate();
            setIsOpen(false);
        } catch (error: any) {
            alert(error.message);
        } finally {
            setIsSubmitting(false);
        }
    };

    const handleDelete = async (id: number) => {
        if (!confirm("Удалить эту площадку?")) return;
        try {
            await venuesApi.delete(id);
            mutate();
        } catch (error: any) {
            alert(error.message);
        }
    };

    const openDialog = (venue?: VenueResponseDto) => {
        setEditingVenue(venue || null);
        setIsOpen(true);
    };

    return (
        <DashboardLayout>
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-2xl font-bold">Площадки</h1>
                <Button onClick={() => openDialog()}>
                    <Plus className="mr-2 h-4 w-4" /> Добавить
                </Button>
            </div>

            <div className="bg-background rounded-md border">
                <Table>
                    <TableHeader>
                        <TableRow>
                            <TableHead className="w-[80px]">ID</TableHead>
                            <TableHead>Название</TableHead>
                            <TableHead>Адрес</TableHead>
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
                        ) : venues?.length === 0 ? (
                            <TableRow>
                                <TableCell colSpan={4} className="text-center py-10 text-muted-foreground">Площадок пока нет</TableCell>
                            </TableRow>
                        ) : (
                            venues?.map((venue) => (
                                <TableRow key={venue.id}>
                                    <TableCell className="font-medium">{venue.id}</TableCell>
                                    <TableCell>{venue.name}</TableCell>
                                    <TableCell>
                                        <div className="flex items-center text-muted-foreground">
                                            <MapPin className="mr-1 h-3 w-3" /> {venue.address}
                                        </div>
                                    </TableCell>
                                    <TableCell className="text-right space-x-2">
                                        <Button variant="outline" size="icon" onClick={() => openDialog(venue)}>
                                            <Pencil className="h-4 w-4" />
                                        </Button>
                                        <Button variant="destructive" size="icon" onClick={() => handleDelete(venue.id)}>
                                            <Trash2 className="h-4 w-4" />
                                        </Button>
                                    </TableCell>
                                </TableRow>
                            ))
                        )}
                    </TableBody>
                </Table>
            </div>

            <Dialog open={isOpen} onOpenChange={setIsOpen}>
                <DialogContent>
                    <DialogHeader>
                        <DialogTitle>{editingVenue ? "Редактировать площадку" : "Добавить площадку"}</DialogTitle>
                    </DialogHeader>
                    <form onSubmit={handleSubmit} className="space-y-4">
                        <div className="space-y-2">
                            <Label htmlFor="name">Название</Label>
                            <Input id="name" name="name" defaultValue={editingVenue?.name} required />
                        </div>
                        <div className="space-y-2">
                            <Label htmlFor="address">Адрес</Label>
                            <Input id="address" name="address" defaultValue={editingVenue?.address} required />
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
        </DashboardLayout>
    );
}