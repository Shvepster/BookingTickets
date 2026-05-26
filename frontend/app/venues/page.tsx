"use client";

import { useState } from "react";
import useSWR from "swr";
import { DashboardLayout } from "@/components/dashboard-layout";
import { venuesApi, eventsApi } from "@/lib/api";
import { useAuth } from "@/lib/auth-context";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from "@/components/ui/dialog";
import { Plus, Pencil, Trash2, Loader2, MapPin, CalendarDays, Building2 } from "lucide-react";
import type { VenueResponseDto } from "@/lib/types";

export default function VenuesPage() {
    const { isAdmin } = useAuth();

    // Безопасный вызов SWR с оберткой в стрелочные функции и правильными путями
    const { data: venues, isLoading, mutate: mutateVenues } = useSWR("/venues", () => venuesApi.getAll());
    const { data: events } = useSWR("/events", () => eventsApi.getAll());

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
            mutateVenues();
            setIsOpen(false);
        } catch (error: any) {
            alert(error.message);
        } finally {
            setIsSubmitting(false);
        }
    };

    const handleDelete = async (id: number) => {
        if (!confirm("Удалить эту площадку? Это может привести к отмене мероприятий!")) return;
        try {
            await venuesApi.delete(id);
            mutateVenues();
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
                <div>
                    <h1 className="text-3xl font-bold tracking-tight">Площадки</h1>
                    <p className="mt-1 text-muted-foreground">Места проведения мероприятий (OneToMany)</p>
                </div>
                {isAdmin && (
                    <Button onClick={() => openDialog()}>
                        <Plus className="mr-2 h-4 w-4" /> Добавить
                    </Button>
                )}
            </div>

            {isLoading ? (
                <div className="flex justify-center py-20">
                    <Loader2 className="h-8 w-8 animate-spin text-primary" />
                </div>
            ) : venues?.length === 0 ? (
                <Card>
                    <CardContent className="flex flex-col items-center justify-center py-12">
                        <Building2 className="h-12 w-12 text-muted-foreground mb-4 opacity-50" />
                        <h3 className="text-lg font-semibold">Площадок пока нет</h3>
                        {isAdmin && <p className="text-sm text-muted-foreground">Добавьте первую площадку для запуска мероприятий</p>}
                    </CardContent>
                </Card>
            ) : (
                <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
                    {venues?.map((venue) => {
                        // Находим мероприятия, которые проходят на этой площадке
                        const venueEvents = events?.filter(e => e.venueName === venue.name) || [];

                        return (
                            <Card key={venue.id} className="group relative hover:shadow-md transition-shadow flex flex-col">
                                <CardHeader className="pb-3">
                                    <div className="flex items-start justify-between">
                                        <div className="space-y-1">
                                            <CardTitle className="text-xl line-clamp-1" title={venue.name}>
                                                {venue.name}
                                            </CardTitle>
                                            <div className="flex items-center text-sm text-muted-foreground line-clamp-1" title={venue.address}>
                                                <MapPin className="mr-1 h-3.5 w-3.5 shrink-0" />
                                                {venue.address}
                                            </div>
                                        </div>

                                        {isAdmin && (
                                            <div className="flex gap-1 opacity-0 transition-opacity group-hover:opacity-100">
                                                <Button variant="secondary" size="icon" className="h-8 w-8" onClick={() => openDialog(venue)}>
                                                    <Pencil className="h-4 w-4" />
                                                </Button>
                                                <Button variant="destructive" size="icon" className="h-8 w-8" onClick={() => handleDelete(venue.id)}>
                                                    <Trash2 className="h-4 w-4" />
                                                </Button>
                                            </div>
                                        )}
                                    </div>
                                </CardHeader>
                                <CardContent className="flex-1">
                                    <div className="flex items-center gap-2 mb-3">
                                        <CalendarDays className="h-4 w-4 text-primary" />
                                        <span className="text-sm font-medium">
                                            {venueEvents.length} запланировано
                                        </span>
                                    </div>

                                    {venueEvents.length > 0 ? (
                                        <div className="flex flex-wrap gap-1.5">
                                            {venueEvents.slice(0, 4).map(e => (
                                                <Badge key={e.id} variant="outline" className="text-[10px] font-normal">
                                                    {e.title}
                                                </Badge>
                                            ))}
                                            {venueEvents.length > 4 && (
                                                <Badge variant="secondary" className="text-[10px]">
                                                    +{venueEvents.length - 4} еще
                                                </Badge>
                                            )}
                                        </div>
                                    ) : (
                                        <p className="text-xs text-muted-foreground italic">Мероприятий пока нет</p>
                                    )}
                                </CardContent>
                            </Card>
                        );
                    })}
                </div>
            )}

            <Dialog open={isOpen} onOpenChange={setIsOpen}>
                <DialogContent>
                    <DialogHeader>
                        <DialogTitle>{editingVenue ? "Редактировать площадку" : "Добавить площадку"}</DialogTitle>
                    </DialogHeader>
                    <form onSubmit={handleSubmit} className="space-y-4">
                        <div className="space-y-2">
                            <Label htmlFor="name">Название площадки</Label>
                            <Input id="name" name="name" placeholder="Например: Минск-Арена" defaultValue={editingVenue?.name} required />
                        </div>
                        <div className="space-y-2">
                            <Label htmlFor="address">Физический адрес</Label>
                            <Input id="address" name="address" placeholder="Например: пр-т Победителей 111" defaultValue={editingVenue?.address} required />
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