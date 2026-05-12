"use client";

import { useState } from "react";
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
import type { EventResponseDto, TicketRequestDto, UserResponseDto } from "@/lib/types";

interface BuyTicketDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  event: EventResponseDto | null;
  users: UserResponseDto[];
  onSubmit: (data: TicketRequestDto) => void;
}

export function BuyTicketDialog({
  open,
  onOpenChange,
  event,
  users,
  onSubmit,
}: BuyTicketDialogProps) {
  const [userId, setUserId] = useState<string>("");
  const [seatNumber, setSeatNumber] = useState("");

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!event) return;
    
    onSubmit({
      userId: parseInt(userId),
      eventId: event.id,
      seatNumber,
    });
    onOpenChange(false);
    setUserId("");
    setSeatNumber("");
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-[400px]">
        <DialogHeader>
          <DialogTitle>Купить билет</DialogTitle>
          <DialogDescription>
            {event ? `Мероприятие: ${event.title}` : ""}
          </DialogDescription>
        </DialogHeader>
        <form onSubmit={handleSubmit}>
          <div className="grid gap-4 py-4">
            <div className="grid gap-2">
              <Label htmlFor="user">Пользователь</Label>
              <Select value={userId} onValueChange={setUserId} required>
                <SelectTrigger>
                  <SelectValue placeholder="Выберите пользователя" />
                </SelectTrigger>
                <SelectContent>
                  {users.map((user) => (
                    <SelectItem key={user.id} value={String(user.id)}>
                      {user.username} ({user.email})
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>
            <div className="grid gap-2">
              <Label htmlFor="seat">Номер места</Label>
              <Input
                id="seat"
                value={seatNumber}
                onChange={(e) => setSeatNumber(e.target.value)}
                placeholder="A-15"
                required
              />
            </div>
            {event && (
              <div className="rounded-md bg-muted p-3">
                <p className="text-sm text-muted-foreground">
                  Цена билета: <span className="font-semibold text-foreground">{event.formattedPrice}</span>
                </p>
              </div>
            )}
          </div>
          <DialogFooter>
            <Button type="button" variant="outline" onClick={() => onOpenChange(false)}>
              Отмена
            </Button>
            <Button type="submit">Купить</Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
}
