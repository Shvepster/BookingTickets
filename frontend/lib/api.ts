import type {
  EventResponseDto,
  EventRequestDto,
  VenueResponseDto,
  VenueRequestDto,
  CategoryResponseDto,
  CategoryRequestDto,
  UserResponseDto,
  UserRequestDto,
  TicketResponseDto,
  TicketRequestDto,
  PageResponse,
} from "./types";

// Configure your backend URL here
const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080";

async function fetchApi<T>(
  endpoint: string,
  options?: RequestInit
): Promise<T> {
  const response = await fetch(`${API_BASE_URL}${endpoint}`, {
    ...options,
    headers: {
      "Content-Type": "application/json",
      ...options?.headers,
    },
  });

  if (!response.ok) {
    const error = await response.json().catch(() => ({}));
    throw new Error(error.message || `HTTP error! status: ${response.status}`);
  }

  if (response.status === 204) {
    return {} as T;
  }

  return response.json();
}

// Events API
export const eventsApi = {
  getAll: () => fetchApi<EventResponseDto[]>("/api/events/all"),
  
  getPaged: (page = 0, size = 10) =>
    fetchApi<PageResponse<EventResponseDto>>(
      `/api/events/paged?page=${page}&size=${size}`
    ),
  
  getById: (id: number) => fetchApi<EventResponseDto>(`/api/events/${id}`),
  
  search: (title: string) =>
    fetchApi<EventResponseDto[]>(`/api/events/search?title=${encodeURIComponent(title)}`),
  
  searchComplex: (venueName: string, categoryName: string, page = 0, size = 10) =>
    fetchApi<PageResponse<EventResponseDto>>(
      `/api/events/search-complex?venueName=${encodeURIComponent(venueName)}&categoryName=${encodeURIComponent(categoryName)}&page=${page}&size=${size}`
    ),
  
  create: (data: EventRequestDto) =>
    fetchApi<EventResponseDto>("/api/events", {
      method: "POST",
      body: JSON.stringify(data),
    }),
  
  update: (id: number, data: EventRequestDto) =>
    fetchApi<EventResponseDto>(`/api/events/${id}`, {
      method: "PUT",
      body: JSON.stringify(data),
    }),
  
  delete: (id: number) =>
    fetchApi<void>(`/api/events/${id}`, { method: "DELETE" }),
};

// Venues API
export const venuesApi = {
  getAll: () => fetchApi<VenueResponseDto[]>("/api/venues"),
  
  getById: (id: number) => fetchApi<VenueResponseDto>(`/api/venues/${id}`),
  
  search: (name: string) =>
    fetchApi<VenueResponseDto[]>(`/api/venues/search?name=${encodeURIComponent(name)}`),
  
  create: (data: VenueRequestDto) =>
    fetchApi<VenueResponseDto>("/api/venues", {
      method: "POST",
      body: JSON.stringify(data),
    }),
  
  update: (id: number, data: VenueRequestDto) =>
    fetchApi<VenueResponseDto>(`/api/venues/${id}`, {
      method: "PUT",
      body: JSON.stringify(data),
    }),
  
  delete: (id: number) =>
    fetchApi<void>(`/api/venues/${id}`, { method: "DELETE" }),
};

// Categories API
export const categoriesApi = {
  getAll: () => fetchApi<CategoryResponseDto[]>("/api/categories"),
  
  getById: (id: number) => fetchApi<CategoryResponseDto>(`/api/categories/${id}`),
  
  search: (name: string) =>
    fetchApi<CategoryResponseDto[]>(`/api/categories/search?name=${encodeURIComponent(name)}`),
  
  create: (data: CategoryRequestDto) =>
    fetchApi<CategoryResponseDto>("/api/categories", {
      method: "POST",
      body: JSON.stringify(data),
    }),
  
  update: (id: number, data: CategoryRequestDto) =>
    fetchApi<CategoryResponseDto>(`/api/categories/${id}`, {
      method: "PUT",
      body: JSON.stringify(data),
    }),
  
  delete: (id: number) =>
    fetchApi<void>(`/api/categories/${id}`, { method: "DELETE" }),
};

// Users API
export const usersApi = {
  getAll: () => fetchApi<UserResponseDto[]>("/api/users"),
  
  getById: (id: number) => fetchApi<UserResponseDto>(`/api/users/${id}`),
  
  search: (username: string) =>
    fetchApi<UserResponseDto[]>(`/api/users/search?username=${encodeURIComponent(username)}`),
  
  create: (data: UserRequestDto) =>
    fetchApi<UserResponseDto>("/api/users", {
      method: "POST",
      body: JSON.stringify(data),
    }),
  
  update: (id: number, data: UserRequestDto) =>
    fetchApi<UserResponseDto>(`/api/users/${id}`, {
      method: "PUT",
      body: JSON.stringify(data),
    }),
  
  delete: (id: number) =>
    fetchApi<void>(`/api/users/${id}`, { method: "DELETE" }),
};

// Tickets API
export const ticketsApi = {
  getAll: () => fetchApi<TicketResponseDto[]>("/api/tickets"),
  
  getById: (id: number) => fetchApi<TicketResponseDto>(`/api/tickets/${id}`),
  
  getByUserId: (userId: number) =>
    fetchApi<TicketResponseDto[]>(`/api/tickets/user/${userId}`),
  
  create: (data: TicketRequestDto) =>
    fetchApi<TicketResponseDto>("/api/tickets", {
      method: "POST",
      body: JSON.stringify(data),
    }),
  
  createBulk: (data: TicketRequestDto[]) =>
    fetchApi<void>("/api/tickets/bulk", {
      method: "POST",
      body: JSON.stringify(data),
    }),
  
  delete: (id: number) =>
    fetchApi<void>(`/api/tickets/${id}`, { method: "DELETE" }),
};

// SWR fetchers
export const fetcher = <T>(url: string): Promise<T> => fetchApi<T>(url);
