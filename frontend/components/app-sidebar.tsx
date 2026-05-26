"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import { Calendar, Ticket, MapPin, Tags, Users, LogOut } from "lucide-react";
import { useAuth } from "@/lib/auth-context";
import {
    Sidebar, SidebarContent, SidebarFooter, SidebarGroup,
    SidebarGroupContent, SidebarGroupLabel, SidebarHeader,
    SidebarMenu, SidebarMenuButton, SidebarMenuItem,
} from "@/components/ui/sidebar";

const items = [
    { title: "Афиша", url: "/", icon: Calendar },
    { title: "Мои билеты", url: "/my-tickets", icon: Ticket },
    { title: "Площадки", url: "/venues", icon: MapPin },
    { title: "Категории", url: "/categories", icon: Tags },
    { title: "Пользователи", url: "/users", icon: Users },
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

export function AppSidebar() {
    const pathname = usePathname();
    const { logout, user, isAdmin } = useAuth();

    return (
        <Sidebar>
            <SidebarHeader className="p-4 border-b">
                <div className="flex items-center gap-2 font-bold text-xl">
                    {/* ИСПРАВЛЕНО: заменено на фирменную синюю иконку */}
                    <TicketproIcon className="h-6 w-6 shrink-0" />
                    <span>BookingTickets</span>
                </div>
            </SidebarHeader>
            <SidebarContent>
                <SidebarGroup>
                    <SidebarGroupLabel>Навигация</SidebarGroupLabel>
                    <SidebarGroupContent>
                        <SidebarMenu>
                            {items.map((item) => {
                                // Скрываем "Пользователи" от не-админов
                                if (item.title === "Пользователи" && !isAdmin) return null;
                                return (
                                    <SidebarMenuItem key={item.title}>
                                        <SidebarMenuButton asChild isActive={pathname === item.url}>
                                            <Link href={item.url}>
                                                <item.icon className="h-4 w-4" />
                                                <span>{item.title}</span>
                                            </Link>
                                        </SidebarMenuButton>
                                    </SidebarMenuItem>
                                );
                            })}
                        </SidebarMenu>
                    </SidebarGroupContent>
                </SidebarGroup>
            </SidebarContent>
            <SidebarFooter className="p-4 border-t">
                <div className="flex items-center justify-between">
                    <div className="flex flex-col">
                        <span className="text-sm font-medium truncate w-32">
                            {user?.username || "Гость"} {isAdmin && " (Админ)"}
                        </span>
                        <span className="text-xs text-muted-foreground truncate w-32">{user?.email || ""}</span>
                    </div>
                    <SidebarMenuButton
                        onClick={logout}
                        className="w-auto h-auto p-2 hover:bg-destructive/10 hover:text-destructive transition-colors"
                        title="Выйти"
                    >
                        <LogOut className="h-4 w-4" />
                    </SidebarMenuButton>
                </div>
            </SidebarFooter>
        </Sidebar>
    );
}