// API Types based on your Java backend

export interface EventResponseDto {
  id: number;
  title: string;
  formattedPrice: string;
  venueName: string;
  eventDate: string;
  categories: string[];
}

export interface EventRequestDto {
  title: string;
  price: number;
  venueId: number;
  categoryIds: number[];
  eventDate: string;
}

export interface VenueResponseDto {
  id: number;
  name: string;
  address: string;
}

export interface VenueRequestDto {
  name: string;
  address: string;
}

export interface CategoryResponseDto {
  id: number;
  name: string;
}

export interface CategoryRequestDto {
  name: string;
}

export interface UserResponseDto {
  id: number;
  username: string;
  email: string;
}

export interface UserRequestDto {
  username: string;
  password?: string;
  email: string;
}

export interface TicketResponseDto {
  id: number;
  seatNumber: string;
  userName: string;
  eventTitle: string;
}

export interface TicketRequestDto {
  seatNumber: string;
  userId: number;
  eventId: number;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

export interface AuthResponseDto {
    token: string;
    userId: number;
    username: string;
    email: string;
}

export interface LoginRequestDto {
    login: string;
    password?: string;
}