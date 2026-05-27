"use client";
import { useState, useMemo } from "react";
import useSWR, { mutate } from "swr";
import { DashboardLayout } from "@/components/dashboard-layout";
import { eventsApi, venuesApi, categoriesApi, ticketsApi } from "@/lib/api";
import { useAuth } from "@/lib/auth-context";
import { cn } from "@/lib/utils";
import { Button } from "@/components/ui/button";
import { Label } from "@/components/ui/label";
import { Card, CardContent, CardFooter, CardHeader, CardTitle } from "@/components/ui/card";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from "@/components/ui/dialog";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Badge } from "@/components/ui/badge";
import { Calendar, MapPin, X, Loader2, Ticket, Plus, Pencil, Trash2 } from "lucide-react";
import type { EventResponseDto, EventRequestDto } from "@/lib/types";

// Импортируем окно формы мероприятий и AlertDialog для удаления
import { EventFormDialog } from "@/components/event-form-dialog";
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

export default function EventsPage() {
    const { user, isAdmin } = useAuth();
    const [selectedVenue, setSelectedVenue] = useState<string>("");
    const [selectedCategory, setSelectedCategory] = useState<string>("");
    const [page, setPage] = useState(0);

    const { data: venues } = useSWR("/venues", venuesApi.getAll);
    const { data: categories } = useSWR("/categories", categoriesApi.getAll);
    const { data: allTickets } = useSWR("/tickets", ticketsApi.getAll);

    // Запрашиваем мероприятия с native=false по умолчанию
    const { data: pagedEvents, isLoading, mutate: mutateEvents } = useSWR(
        `/events/search-complex?venue=${selectedVenue}&category=${selectedCategory}&page=${page}&native=false`,
        () => eventsApi.searchComplex(selectedVenue, selectedCategory, page, 6, false)
    );

    // Состояния для покупки билета
    const [isBuyOpen, setIsBuyOpen] = useState(false);
    const [selectedEvent, setSelectedEvent] = useState<EventResponseDto | null>(null);
    const [selectedSeat, setSelectedSeat] = useState<string>("");
    const [isSubmitting, setIsSubmitting] = useState(false);

    // Состояния для администрирования (создание, редактирование, удаление)
    const [isEventFormOpen, setIsEventFormOpen] = useState(false);
    const [editingEvent, setEditingEvent] = useState<EventResponseDto | null>(null);
    const [eventToDelete, setEventToDelete] = useState<number | null>(null);

    const ALL_SEATS = useMemo(() => {
        const rows = ['A', 'B', 'C'];
        const seats = [];
        for (const row of rows) {
            for (let i = 1; i <= 10; i++) {
                seats.push(`${row}-${i}`);
            }
        }
        return seats;
    }, []);

    // --- Обработчики покупки билетов ---
    const handleBuyTicket = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        if (!selectedEvent || !user) return;
        if (!selectedSeat) {
            alert("Пожалуйста, выберите место!");
            return;
        }

        setIsSubmitting(true);
        try {
            await ticketsApi.create({
                seatNumber: selectedSeat,
                eventId: selectedEvent.id,
                userId: user.id
            });
            const row = selectedSeat.split('-')[0];
            const num = selectedSeat.split('-')[1];
            alert(`Билет на Ряд ${row}, Место ${num} успешно оформлен!`);

            setIsBuyOpen(false);
            setSelectedSeat("");
            mutate("/tickets");
        } catch (error: any) {
            alert(error.message);
        } finally {
            setIsSubmitting(false);
        }
    };

    const clearFilters = () => {
        setSelectedVenue("");
        setSelectedCategory("");
        setPage(0);
    };

    // --- Обработчики администрирования мероприятий ---
    const openCreateEvent = () => {
        setEditingEvent(null);
        setIsEventFormOpen(true);
    };

    const openEditEvent = (event: EventResponseDto) => {
        setEditingEvent(event);
        setIsEventFormOpen(true);
    };

    const handleEventSubmit = async (data: EventRequestDto) => {
        try {
            if (editingEvent) {
                await eventsApi.update(editingEvent.id, data);
            } else {
                await eventsApi.create(data);
            }
            mutateEvents();
            setIsEventFormOpen(false);
        } catch (error: any) {
            alert(error.message);
        }
    };

    const executeDeleteEvent = async () => {
        if (!eventToDelete) return;
        try {
            await eventsApi.delete(eventToDelete);
            mutateEvents();
            setEventToDelete(null);
        } catch (error: any) {
            alert(error.message);
        }
    };

    return (
        <DashboardLayout>
            <div className="flex flex-col space-y-6">
                <div className="flex justify-between items-center">
                    <h1 className="text-2xl font-bold">Афиша мероприятий</h1>
                    {isAdmin && (
                        <Button onClick={openCreateEvent}>
                            <Plus className="mr-2 h-4 w-4" /> Добавить
                        </Button>
                    )}
                </div>

                <Card className="p-4">
                    <div className="grid grid-cols-1 md:grid-cols-3 gap-4 items-end">
                        <div className="space-y-2">
                            <Label>Площадка</Label>
                            <Select value={selectedVenue} onValueChange={setSelectedVenue}>
                                <SelectTrigger>
                                    <SelectValue placeholder="Все площадки" />
                                </SelectTrigger>
                                <SelectContent>
                                    {venues?.map(v => (
                                        <SelectItem key={v.id} value={v.name}>{v.name}</SelectItem>
                                    ))}
                                </SelectContent>
                            </Select>
                        </div>
                        <div className="space-y-2">
                            <Label>Категория</Label>
                            <Select value={selectedCategory} onValueChange={setSelectedCategory}>
                                <SelectTrigger>
                                    <SelectValue placeholder="Все категории" />
                                </SelectTrigger>
                                <SelectContent>
                                    {categories?.map(c => (
                                        <SelectItem key={c.id} value={c.name}>{c.name}</SelectItem>
                                    ))}
                                </SelectContent>
                            </Select>
                        </div>

                        <div className="flex space-x-2">
                            <Button variant="outline" className="w-full" onClick={clearFilters}>
                                <X className="mr-2 h-4 w-4" /> Сбросить
                            </Button>
                        </div>
                    </div>
                </Card>

                {isLoading ? (
                    <div className="flex justify-center py-20">
                        <Loader2 className="h-8 w-8 animate-spin text-primary" />
                    </div>
                ) : !pagedEvents?.content || pagedEvents.content.length === 0 ? (
                    <div className="text-center py-20 text-muted-foreground">Мероприятий не найдено</div>
                ) : (
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                        {pagedEvents.content.map((event) => (
                            <Card key={event.id} className="flex flex-col h-full justify-between hover:shadow-lg transition-shadow">
                                <CardHeader className="pb-3">
                                    <div className="flex justify-between items-start mb-2">
                                        <span className="text-sm font-bold text-primary">{event.formattedPrice}</span>
                                        <div className="flex flex-wrap gap-1 justify-end max-w-[60%]">
                                            {event.categories?.map((cat) => (
                                                <Badge key={cat} variant="secondary" className="text-[10px]">{cat}</Badge>
                                            ))}
                                        </div>
                                    </div>
                                    <div className="flex justify-between items-start gap-2">
                                        <CardTitle className="text-xl line-clamp-1">{event.title}</CardTitle>
                                        {isAdmin && (
                                            <div className="flex gap-1 shrink-0">
                                                <Button variant="ghost" size="icon" className="h-7 w-7" onClick={() => openEditEvent(event)}>
                                                    <Pencil className="h-3.5 w-3.5" />
                                                </Button>
                                                <Button variant="ghost" size="icon" className="h-7 w-7 text-destructive hover:bg-destructive/10 hover:text-destructive" onClick={() => setEventToDelete(event.id)}>
                                                    <Trash2 className="h-3.5 w-3.5" />
                                                </Button>
                                            </div>
                                        )}
                                    </div>
                                </CardHeader>
                                <CardContent className="space-y-3 text-sm text-muted-foreground flex-1">
                                    <div className="flex items-center gap-2">
                                        <MapPin className="h-4 w-4 shrink-0 text-muted-foreground" />
                                        <span className="truncate">{event.venueName}</span>
                                    </div>
                                    <div className="flex items-center gap-2">
                                        <Calendar className="h-4 w-4 shrink-0 text-muted-foreground" />
                                        <span>{new Date(event.eventDate).toLocaleDateString("ru-RU", { day: "numeric", month: "long", year: "numeric", hour: "2-digit", minute: "2-digit" })}</span>
                                    </div>
                                </CardContent>
                                <CardFooter className="pt-0">
                                    <Button className="w-full" onClick={() => {
                                        setSelectedEvent(event);
                                        setSelectedSeat("");
                                        setIsBuyOpen(true);
                                    }}>
                                        <Ticket className="mr-2 h-4 w-4" /> Купить билет
                                    </Button>
                                </CardFooter>
                            </Card>
                        ))}
                    </div>
                )}

                {pagedEvents && pagedEvents.totalPages > 1 && (
                    <div className="flex justify-center space-x-2 mt-4">
                        <Button variant="outline" disabled={page === 0} onClick={() => setPage(p => p - 1)}>Назад</Button>
                        <span className="flex items-center px-4 text-sm font-medium">Страница {page + 1} из {pagedEvents.totalPages}</span>
                        <Button variant="outline" disabled={page >= pagedEvents.totalPages - 1} onClick={() => setPage(p => p + 1)}>Вперед</Button>
                    </div>
                )}
            </div>

            {/* Диалог покупки билета */}
            <Dialog open={isBuyOpen} onOpenChange={setIsBuyOpen}>
                <DialogContent className="sm:max-w-xl">
                    <DialogHeader>
                        <DialogTitle>Покупка билета</DialogTitle>
                    </DialogHeader>
                    {selectedEvent && (
                        <form onSubmit={handleBuyTicket} className="space-y-4">
                            <div className="p-4 bg-muted/50 rounded-lg space-y-2">
                                <div className="font-semibold text-lg">{selectedEvent.title}</div>
                                <div className="text-sm text-muted-foreground">{selectedEvent.venueName}</div>
                                <div className="text-sm font-bold text-primary">{selectedEvent.formattedPrice}</div>
                            </div>

                            <div className="space-y-4">
                                <Label>Выберите место в зале</Label>
                                <div className="flex flex-col gap-3 overflow-x-auto pb-2 border p-4 rounded-lg bg-muted/20">
                                    {['A', 'B', 'C'].map(row => (
                                        <div key={row} className="flex items-center gap-4">
                                            <div className="w-12 shrink-0 text-sm font-medium text-muted-foreground">
                                                Ряд {row}
                                            </div>
                                            <div className="flex gap-2">
                                                {Array.from({ length: 10 }, (_, i) => i + 1).map(num => {
                                                    const seat = `${row}-${num}`;
                                                    const isTaken = allTickets?.some(
                                                        t => t.eventTitle === selectedEvent.title && t.seatNumber === seat
                                                    );
                                                    return (
                                                        <button
                                                            key={seat}
                                                            type="button"
                                                            disabled={isTaken}
                                                            onClick={() => setSelectedSeat(seat)}
                                                            className={cn(
                                                                "h-9 w-9 text-xs rounded-md border font-mono transition-colors flex items-center justify-center shrink-0 shadow-sm",
                                                                isTaken ? "bg-destructive/20 text-destructive border-destructive/20 cursor-not-allowed" :
                                                                    selectedSeat === seat ? "bg-primary text-white border-primary scale-110" : "bg-card hover:bg-accent"
                                                            )}
                                                            title={isTaken ? "Место занято" : "Свободно"}
                                                        >
                                                            {num}
                                                        </button>
                                                    );
                                                })}
                                            </div>
                                        </div>
                                    ))}
                                </div>
                                <div className="flex gap-4 text-xs text-muted-foreground mt-2">
                                    <div className="flex items-center gap-1"><div className="w-3 h-3 rounded bg-card border"></div> Свободно</div>
                                    <div className="flex items-center gap-1"><div className="w-3 h-3 rounded bg-primary"></div> Выбрано</div>
                                    <div className="flex items-center gap-1"><div className="w-3 h-3 rounded bg-destructive/20"></div> Занято</div>
                                </div>

                                {selectedSeat && (
                                    <div className="mt-4 p-3 bg-primary/10 rounded-md border border-primary/20 text-sm flex items-center gap-2">
                                        <Ticket className="h-4 w-4 text-primary"/>
                                        <span>Выбранный билет: <span className="font-bold">Ряд {selectedSeat.split('-')[0]}, Место {selectedSeat.split('-')[1]}</span></span>
                                    </div>
                                )}

                                <input type="hidden" name="seatNumber" value={selectedSeat} />
                            </div>

                            <DialogFooter>
                                <Button type="button" variant="outline" onClick={() => setIsBuyOpen(false)}>Отмена</Button>
                                <Button type="submit" disabled={isSubmitting || !selectedSeat}>
                                    {isSubmitting ? <Loader2 className="mr-2 h-4 w-4 animate-spin" /> : "Забронировать"}
                                </Button>
                            </DialogFooter>
                        </form>
                    )}
                </DialogContent>
            </Dialog>

            {/* Диалог формы (создание/редактирование мероприятия) */}
            {isAdmin && (
                <EventFormDialog
                    open={isEventFormOpen}
                    onOpenChange={setIsEventFormOpen}
                    event={editingEvent}
                    venues={venues || []}
                    categories={categories || []}
                    onSubmit={handleEventSubmit}
                />
            )}

            {/* Диалог подтверждения удаления */}
            <AlertDialog open={!!eventToDelete} onOpenChange={(open) => !open && setEventToDelete(null)}>
                <AlertDialogContent>
                    <AlertDialogHeader>
                        <AlertDialogTitle>Удалить мероприятие?</AlertDialogTitle>
                        <AlertDialogDescription>
                            Вы уверены, что хотите отменить это мероприятие? Все связанные билеты будут удалены безвозвратно.
                        </AlertDialogDescription>
                    </AlertDialogHeader>
                    <AlertDialogFooter>
                        <AlertDialogCancel>Отмена</AlertDialogCancel>
                        <AlertDialogAction onClick={executeDeleteEvent} className="bg-destructive text-white hover:bg-destructive/90">
                            Удалить
                        </AlertDialogAction>
                    </AlertDialogFooter>
                </AlertDialogContent>
            </AlertDialog>

        </DashboardLayout>
    );
}