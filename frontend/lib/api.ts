import type {
    EventResponseDto, EventRequestDto,
    VenueResponseDto, VenueRequestDto,
    CategoryResponseDto, CategoryRequestDto,
    UserResponseDto, UserRequestDto,
    TicketResponseDto, TicketRequestDto,
    PageResponse, AuthResponseDto, LoginRequestDto
} from "./types";

const API_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080/api";

export class ApiError extends Error {
    constructor(public status: number, message: string) {
        super(message);
        this.name = "ApiError";
    }
}

function getAuthToken(): string | null {
    if (typeof window !== "undefined") {
        return localStorage.getItem("token");
    }
    return null;
}

export async function fetchApi<T>(endpoint: string, options: RequestInit = {}): Promise<T> {
    const token = getAuthToken();
    const headers = new Headers(options.headers);

    if (!(options.body instanceof FormData)) {
        headers.set("Content-Type", "application/json");
    }

    if (token) {
        headers.set("Authorization", `Bearer ${token}`);
    }

    const response = await fetch(`${API_URL}${endpoint}`, {
        ...options,
        headers,
    });

    if (!response.ok) {
        let errorMessage = "Произошла ошибка при запросе";
        try {
            const errorData = await response.json();
            errorMessage = errorData.message || (errorData.details && errorData.details[0]) || errorMessage;
        } catch {
            // Игнорируем ошибку парсинга
        }
        throw new ApiError(response.status, errorMessage);
    }

    if (response.status === 204 || response.headers.get("content-length") === "0") {
        return {} as T;
    }

    return response.json();
}

export const fetcher = <T>(url: string): Promise<T> => fetchApi<T>(url);

// --- Полноценные API-обертки для всех контроллеров бэкенда ---

export const authApi = {
    login: (data: LoginRequestDto) => fetchApi<AuthResponseDto>("/auth/login", { method: "POST", body: JSON.stringify(data) }),
    register: (data: UserRequestDto) => fetchApi<AuthResponseDto>("/auth/register", { method: "POST", body: JSON.stringify(data) }),
};

export const eventsApi = {
    getAll: () => fetchApi<EventResponseDto[]>("/events/all"),
    getPaged: (page: number, size: number) => fetchApi<PageResponse<EventResponseDto>>(`/events/paged?page=${page}&size=${size}`),
    searchByTitle: (title: string) => fetchApi<EventResponseDto[]>(`/events/search?title=${encodeURIComponent(title)}`),
    searchComplex: (venueName = "", categoryName = "", page = 0, size = 10, useNative = false) => {
        const params = new URLSearchParams({
            page: page.toString(),
            size: size.toString(),
            useNative: useNative.toString(),
            venueName: venueName,
            categoryName: categoryName
        });
        return fetchApi<PageResponse<EventResponseDto>>(`/events/search-complex?${params.toString()}`);
    },
    getById: (id: number) => fetchApi<EventResponseDto>(`/events/${id}`),
    create: (data: EventRequestDto) => fetchApi<EventResponseDto>("/events", { method: "POST", body: JSON.stringify(data) }),
    update: (id: number, data: EventRequestDto) => fetchApi<EventResponseDto>(`/events/${id}`, { method: "PUT", body: JSON.stringify(data) }),
    delete: (id: number) => fetchApi<void>(`/events/${id}`, { method: "DELETE" }),
};

export const categoriesApi = {
    getAll: () => fetchApi<CategoryResponseDto[]>("/categories"),
    search: (name: string) => fetchApi<CategoryResponseDto[]>(`/categories/search?name=${encodeURIComponent(name)}`),
    getById: (id: number) => fetchApi<CategoryResponseDto>(`/categories/${id}`),
    create: (data: CategoryRequestDto) => fetchApi<CategoryResponseDto>("/categories", { method: "POST", body: JSON.stringify(data) }),
    update: (id: number, data: CategoryRequestDto) => fetchApi<CategoryResponseDto>(`/categories/${id}`, { method: "PUT", body: JSON.stringify(data) }),
    delete: (id: number) => fetchApi<void>(`/categories/${id}`, { method: "DELETE" }),
};

export const venuesApi = {
    getAll: () => fetchApi<VenueResponseDto[]>("/venues"),
    search: (name: string) => fetchApi<VenueResponseDto[]>(`/venues/search?name=${encodeURIComponent(name)}`),
    getById: (id: number) => fetchApi<VenueResponseDto>(`/venues/${id}`),
    create: (data: VenueRequestDto) => fetchApi<VenueResponseDto>("/venues", { method: "POST", body: JSON.stringify(data) }),
    update: (id: number, data: VenueRequestDto) => fetchApi<VenueResponseDto>(`/venues/${id}`, { method: "PUT", body: JSON.stringify(data) }),
    delete: (id: number) => fetchApi<void>(`/venues/${id}`, { method: "DELETE" }),
};

export const ticketsApi = {
    getAll: () => fetchApi<TicketResponseDto[]>("/tickets"),
    getByUserId: (userId: number) => fetchApi<TicketResponseDto[]>(`/tickets/user/${userId}`),
    getById: (id: number) => fetchApi<TicketResponseDto>(`/tickets/${id}`),
    create: (data: TicketRequestDto) => fetchApi<TicketResponseDto>("/tickets", { method: "POST", body: JSON.stringify(data) }),
    createBulk: (data: TicketRequestDto[]) => fetchApi<void>("/tickets/bulk", { method: "POST", body: JSON.stringify(data) }), // Метод для 5 лабы
    delete: (id: number) => fetchApi<void>(`/tickets/${id}`, { method: "DELETE" }),
};

export const usersApi = {
    getAll: () => fetchApi<UserResponseDto[]>("/users"),
    search: (username: string) => fetchApi<UserResponseDto[]>(`/users/search?username=${encodeURIComponent(username)}`),
    getById: (id: number) => fetchApi<UserResponseDto>(`/users/${id}`),
    create: (data: UserRequestDto) => fetchApi<UserResponseDto>("/users", { method: "POST", body: JSON.stringify(data) }),
    update: (id: number, data: UserRequestDto) => fetchApi<UserResponseDto>(`/users/${id}`, { method: "PUT", body: JSON.stringify(data) }),
    delete: (id: number) => fetchApi<void>(`/users/${id}`, { method: "DELETE" }),
};