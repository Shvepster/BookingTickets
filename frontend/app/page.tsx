"use client";

import { useState } from "react";
import useSWR, { mutate } from "swr";
import { DashboardLayout } from "@/components/dashboard-layout";
import { eventsApi, venuesApi, categoriesApi, ticketsApi } from "@/lib/api";
import { useAuth } from "@/lib/auth-context";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from "@/components/ui/card";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from "@/components/ui/dialog";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Badge } from "@/components/ui/badge";
import { Calendar, MapPin, Tags, Search, X, Loader2, Ticket } from "lucide-react";
import type { EventResponseDto } from "@/lib/types";

export default function EventsPage() {
    const { user } = useAuth();

    const [selectedVenue, setSelectedVenue] = useState<string>("");
    const [selectedCategory, setSelectedCategory] = useState<string>("");
    const [useNative, setUseNative] = useState<boolean>(false);
    const [page, setPage] = useState(0);

    const { data: venues } = useSWR("/venues", venuesApi.getAll);
    const { data: categories } = useSWR("/categories", categoriesApi.getAll);

    const { data: pagedEvents, isLoading } = useSWR(
        `/events/search-complex?venue=${selectedVenue}&category=${selectedCategory}&page=${page}&native=${useNative}`,
        () => eventsApi.searchComplex(selectedVenue, selectedCategory, page, 6, useNative)
    );

    const [isBuyOpen, setIsBuyOpen] = useState(false);
    const [selectedEvent, setSelectedEvent] = useState<EventResponseDto | null>(null);
    const [isSubmitting, setIsSubmitting] = useState(false);

    const handleBuyTicket = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        if (!selectedEvent || !user) return;
        setIsSubmitting(true);

        const formData = new FormData(e.currentTarget);
        const seatNumber = formData.get("seatNumber") as string;

        try {
            await ticketsApi.create({
                seatNumber,
                eventId: selectedEvent.id,
                userId: user.id
            });
            alert(`Билет на место ${seatNumber} успешно оформлен!`);
            setIsBuyOpen(false);
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

    return (
        <DashboardLayout>
            <div className="flex flex-col space-y-6">
                <div className="flex justify-between items-center">
                    <h1 className="text-2xl font-bold">Афиша мероприятий</h1>
                </div>

                {/* Панель фильтрации (Демонстрирует Блок 3 и Блок 7) */}
                <Card className="p-4">
                    <div className="grid grid-cols-1 md:grid-cols-4 gap-4 items-end">
                        <div className="space-y-2">
                            <Label>Площадка (OneToMany)</Label>
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
                            <Label>Категория (ManyToMany)</Label>
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

                        <div className="flex items-center space-x-2 h-10">
                            <input
                                type="checkbox"
                                id="native"
                                checked={useNative}
                                onChange={(e) => setUseNative(e.target.checked)}
                                className="h-4 w-4 rounded border-gray-300"
                            />
                            <Label htmlFor="native" className="cursor-pointer">Использовать Native Query (Лаб. 3)</Label>
                        </div>

                        <div className="flex space-x-2">
                            <Button variant="outline" className="w-full" onClick={clearFilters}>
                                <X className="mr-2 h-4 w-4" /> Сбросить
                            </Button>
                        </div>
                    </div>
                </Card>

                {/* Сетка мероприятий */}
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
                                <CardHeader>
                                    <div className="flex justify-between items-start mb-2">
                                        <span className="text-sm font-bold text-primary">{event.formattedPrice}</span>
                                        <div className="flex flex-wrap gap-1 justify-end max-w-[60%]">
                                            {event.categories?.map((cat) => (
                                                <Badge key={cat} variant="secondary" className="text-[10px]">{cat}</Badge>
                                            ))}
                                        </div>
                                    </div>
                                    <CardTitle className="text-xl line-clamp-1">{event.title}</CardTitle>
                                </CardHeader>
                                <CardContent className="space-y-3 text-sm text-muted-foreground">
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
                                    <Button className="w-full" onClick={() => { setSelectedEvent(event); setIsBuyOpen(true); }}>
                                        <Ticket className="mr-2 h-4 w-4" /> Купить билет
                                    </Button>
                                </CardFooter>
                            </Card>
                        ))}
                    </div>
                )}

                {/* Пагинация */}
                {pagedEvents && pagedEvents.totalPages > 1 && (
                    <div className="flex justify-center space-x-2 mt-4">
                        <Button variant="outline" disabled={page === 0} onClick={() => setPage(p => p - 1)}>Назад</Button>
                        <span className="flex items-center px-4 text-sm font-medium">Страница {page + 1} из {pagedEvents.totalPages}</span>
                        <Button variant="outline" disabled={page >= pagedEvents.totalPages - 1} onClick={() => setPage(p => p + 1)}>Вперед</Button>
                    </div>
                )}
            </div>

            {/* Модалка покупки билета (OneToMany пользователь-билет) */}
            <Dialog open={isBuyOpen} onOpenChange={setIsBuyOpen}>
                <DialogContent>
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
                            <div className="space-y-2">
                                <Label htmlFor="seatNumber">Номер места (например, VIP-12 или Ряд 3 Место 5)</Label>
                                <Input id="seatNumber" name="seatNumber" placeholder="Укажите место" required />
                            </div>
                            <DialogFooter>
                                <Button type="button" variant="outline" onClick={() => setIsBuyOpen(false)}>Отмена</Button>
                                <Button type="submit" disabled={isSubmitting}>
                                    {isSubmitting ? <Loader2 className="mr-2 h-4 w-4 animate-spin" /> : "Забронировать"}
                                </Button>
                            </DialogFooter>
                        </form>
                    )}
                </DialogContent>
            </Dialog>
        </DashboardLayout>
    );
}