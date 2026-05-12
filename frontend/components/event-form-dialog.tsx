"use client";

import { useState, useEffect } from "react";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import type { EventResponseDto, EventRequestDto, VenueResponseDto, CategoryResponseDto } from "@/lib/types";

interface EventFormDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  event?: EventResponseDto | null;
  venues: VenueResponseDto[];
  categories: CategoryResponseDto[];
  onSubmit: (data: EventRequestDto) => void;
}

export function EventFormDialog({
  open,
  onOpenChange,
  event,
  venues,
  categories,
  onSubmit,
}: EventFormDialogProps) {
  const [title, setTitle] = useState("");
  const [price, setPrice] = useState("");
  const [venueId, setVenueId] = useState<string>("");
  const [selectedCategories, setSelectedCategories] = useState<number[]>([]);
  const [eventDate, setEventDate] = useState("");

  useEffect(() => {
    if (event) {
      setTitle(event.title);
      const priceMatch = event.formattedPrice.match(/[\d.]+/);
      setPrice(priceMatch ? priceMatch[0] : "");
      const venue = venues.find((v) => v.name === event.venueName);
      setVenueId(venue ? String(venue.id) : "");
      const cats = categories.filter((c) => event.categories?.includes(c.name));
      setSelectedCategories(cats.map((c) => c.id));
      if (event.eventDate) {
        const date = new Date(event.eventDate);
        setEventDate(date.toISOString().slice(0, 16));
      }
    } else {
      setTitle("");
      setPrice("");
      setVenueId("");
      setSelectedCategories([]);
      setEventDate("");
    }
  }, [event, venues, categories]);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSubmit({
      title,
      price: parseFloat(price),
      venueId: parseInt(venueId),
      categoryIds: selectedCategories,
      eventDate: new Date(eventDate).toISOString(),
    });
    onOpenChange(false);
  };

  const toggleCategory = (categoryId: number) => {
    setSelectedCategories((prev) =>
      prev.includes(categoryId)
        ? prev.filter((id) => id !== categoryId)
        : [...prev, categoryId]
    );
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-[500px]">
        <DialogHeader>
          <DialogTitle>
            {event ? "Редактировать мероприятие" : "Добавить мероприятие"}
          </DialogTitle>
          <DialogDescription>
            {event
              ? "Измените данные мероприятия"
              : "Заполните данные для нового мероприятия"}
          </DialogDescription>
        </DialogHeader>
        <form onSubmit={handleSubmit}>
          <div className="grid gap-4 py-4">
            <div className="grid gap-2">
              <Label htmlFor="title">Название</Label>
              <Input
                id="title"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
                placeholder="Концерт группы..."
                required
              />
            </div>
            <div className="grid gap-2">
              <Label htmlFor="price">Цена (BYN)</Label>
              <Input
                id="price"
                type="number"
                step="0.01"
                min="0"
                value={price}
                onChange={(e) => setPrice(e.target.value)}
                placeholder="50.00"
                required
              />
            </div>
            <div className="grid gap-2">
              <Label htmlFor="venue">Площадка</Label>
              <Select value={venueId} onValueChange={setVenueId} required>
                <SelectTrigger>
                  <SelectValue placeholder="Выберите площадку" />
                </SelectTrigger>
                <SelectContent>
                  {venues.map((venue) => (
                    <SelectItem key={venue.id} value={String(venue.id)}>
                      {venue.name}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>
            <div className="grid gap-2">
              <Label>Категории</Label>
              <div className="flex flex-wrap gap-2">
                {categories.map((category) => (
                  <Button
                    key={category.id}
                    type="button"
                    variant={
                      selectedCategories.includes(category.id)
                        ? "default"
                        : "outline"
                    }
                    size="sm"
                    onClick={() => toggleCategory(category.id)}
                  >
                    {category.name}
                  </Button>
                ))}
              </div>
            </div>
            <div className="grid gap-2">
              <Label htmlFor="date">Дата и время</Label>
              <Input
                id="date"
                type="datetime-local"
                value={eventDate}
                onChange={(e) => setEventDate(e.target.value)}
                required
              />
            </div>
          </div>
          <DialogFooter>
            <Button type="button" variant="outline" onClick={() => onOpenChange(false)}>
              Отмена
            </Button>
            <Button type="submit">
              {event ? "Сохранить" : "Создать"}
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
}
