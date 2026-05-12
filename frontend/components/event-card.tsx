"use client";

import { Card, CardContent, CardFooter, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Calendar, MapPin, Pencil, Trash2 } from "lucide-react";
import { format } from "date-fns";
import { ru } from "date-fns/locale";
import type { EventResponseDto } from "@/lib/types";

interface EventCardProps {
  event: EventResponseDto;
  onEdit?: (event: EventResponseDto) => void;
  onDelete?: (id: number) => void;
  onBuyTicket?: (event: EventResponseDto) => void;
}

export function EventCard({ event, onEdit, onDelete, onBuyTicket }: EventCardProps) {
  const formatDate = (dateString: string) => {
    try {
      return format(new Date(dateString), "d MMMM yyyy, HH:mm", { locale: ru });
    } catch {
      return dateString;
    }
  };

  return (
    <Card className="flex flex-col">
      <CardHeader className="pb-3">
        <div className="flex items-start justify-between gap-2">
          <CardTitle className="line-clamp-2 text-lg">{event.title}</CardTitle>
          <span className="shrink-0 rounded-md bg-primary px-2 py-1 text-sm font-semibold text-primary-foreground">
            {event.formattedPrice}
          </span>
        </div>
      </CardHeader>
      <CardContent className="flex-1 space-y-3">
        <div className="flex items-center gap-2 text-sm text-muted-foreground">
          <MapPin className="h-4 w-4" />
          <span>{event.venueName}</span>
        </div>
        <div className="flex items-center gap-2 text-sm text-muted-foreground">
          <Calendar className="h-4 w-4" />
          <span>{formatDate(event.eventDate)}</span>
        </div>
        {event.categories && event.categories.length > 0 && (
          <div className="flex flex-wrap gap-1">
            {event.categories.map((category) => (
              <Badge key={category} variant="secondary">
                {category}
              </Badge>
            ))}
          </div>
        )}
      </CardContent>
      <CardFooter className="flex gap-2 border-t pt-4">
        {onBuyTicket && (
          <Button 
            onClick={() => onBuyTicket(event)} 
            className="flex-1"
          >
            Купить билет
          </Button>
        )}
        {onEdit && (
          <Button
            variant="outline"
            size="icon"
            onClick={() => onEdit(event)}
          >
            <Pencil className="h-4 w-4" />
          </Button>
        )}
        {onDelete && (
          <Button
            variant="outline"
            size="icon"
            onClick={() => onDelete(event.id)}
          >
            <Trash2 className="h-4 w-4" />
          </Button>
        )}
      </CardFooter>
    </Card>
  );
}
