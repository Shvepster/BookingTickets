"use client";

import Link from "next/link";
import { usePathname, useRouter } from "next/navigation";
import { cn } from "@/lib/utils";
import { useAuth } from "@/lib/auth-context";
import { Button } from "@/components/ui/button";
import { LogOut, User } from "lucide-react";

const navigation = [
    { name: "Мероприятия", href: "/" },
    { name: "Площадки", href: "/venues" },
    { name: "Категории", href: "/categories" },
    { name: "Мои билеты", href: "/my-tickets" },
];

function TicketproIcon({ className }: { className?: string }) {
    return (
        <svg
            className={className}
            viewBox="0 0 100 100"
            fill="none"
            xmlns="http://www.w3.org/2000/svg"
        >
            <rect width="100" height="100" rx="24" fill="#005CA9" />
            <path
                d="M30 35H70C72.2 35 74 36.8 74 39V44C70 44 67 47 67 51C67 55 70 58 74 58V63C74 65.2 72.2 67 70 67H30C27.8 67 26 65.2 26 63V58C30 58 33 55 33 51C33 47 30 44 26 44V39C26 36.8 27.8 35 30 35Z"
                fill="white"
            />
            <circle cx="50" cy="51" r="5" fill="#005CA9" />
        </svg>
    );
}

export function Header() {
    const pathname = usePathname();
    const router = useRouter();
    const { user, logout } = useAuth();

    const handleLogout = () => {
        logout();
        router.push("/login");
    };

    return (
        <header className="border-b border-border bg-card">
            <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
                <div className="flex h-16 items-center justify-between">
                    <Link href="/" className="flex items-center gap-2">
                        {/* ИСПРАВЛЕНО: заменено на фирменную синюю иконку */}
                        <TicketproIcon className="h-8 w-8 shrink-0" />
                        <span className="text-xl font-bold text-foreground">BookingTickets</span>
                    </Link>
                    <nav className="flex items-center gap-1">
                        {navigation.map((item) => (
                            <Link
                                key={item.href}
                                href={item.href}
                                className={cn(
                                    "rounded-md px-3 py-2 text-sm font-medium transition-colors",
                                    pathname === item.href
                                        ? "bg-primary text-primary-foreground"
                                        : "text-muted-foreground hover:bg-secondary hover:text-foreground"
                                )}
                            >
                                {item.name}
                            </Link>
                        ))}
                    </nav>
                    <div className="flex items-center gap-3">
                        {user && (
                            <>
                                <div className="flex items-center gap-2 text-sm text-muted-foreground">
                                    <User className="h-4 w-4" />
                                    <span>{user.username}</span>
                                </div>
                                <Button variant="outline" size="sm" onClick={handleLogout}>
                                    <LogOut className="mr-2 h-4 w-4" />
                                    Выйти
                                </Button>
                            </>
                        )}
                    </div>
                </div>
            </div>
        </header>
    );
}