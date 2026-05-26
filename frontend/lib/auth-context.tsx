"use client";

import { createContext, useContext, useEffect, useState, ReactNode } from "react";
import { useRouter } from "next/navigation";
import { authApi } from "./api";
import type { LoginRequestDto, UserRequestDto, UserResponseDto } from "./types";

interface AuthContextType {
    user: UserResponseDto | null;
    token: string | null;
    isLoading: boolean;
    login: (data: LoginRequestDto) => Promise<void>;
    register: (data: UserRequestDto) => Promise<void>;
    logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
    const [user, setUser] = useState<UserResponseDto | null>(null);
    const [token, setToken] = useState<string | null>(null);
    const [isLoading, setIsLoading] = useState(true);
    const router = useRouter();

    useEffect(() => {
        // Восстанавливаем сессию при перезагрузке страницы
        const storedToken = localStorage.getItem("token");
        const storedUser = localStorage.getItem("user");

        if (storedToken && storedUser) {
            setToken(storedToken);
            setUser(JSON.parse(storedUser));
        }
        setIsLoading(false);
    }, []);

    const login = async (data: LoginRequestDto) => {
        const response = await authApi.login(data);
        const userData = { id: response.userId, username: response.username, email: response.email };

        localStorage.setItem("token", response.token);
        localStorage.setItem("user", JSON.stringify(userData));

        setToken(response.token);
        setUser(userData);
        router.push("/");
    };

    const register = async (data: UserRequestDto) => {
        const response = await authApi.register(data);
        const userData = { id: response.userId, username: response.username, email: response.email };

        localStorage.setItem("token", response.token);
        localStorage.setItem("user", JSON.stringify(userData));

        setToken(response.token);
        setUser(userData);
        router.push("/");
    };

    const logout = () => {
        localStorage.removeItem("token");
        localStorage.removeItem("user");
        setToken(null);
        setUser(null);
        router.push("/login");
    };

    return (
        <AuthContext.Provider value={{ user, token, isLoading, login, register, logout }}>
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth() {
    const context = useContext(AuthContext);
    if (context === undefined) {
        throw new Error("useAuth must be used within an AuthProvider");
    }
    return context;
}