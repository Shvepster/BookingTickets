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
    { title: "Афиша (События)", url: "/", icon: Calendar },
    { title: "Мои билеты", url: "/my-tickets", icon: Ticket },
    { title: "Площадки", url: "/venues", icon: MapPin },
    { title: "Категории", url: "/categories", icon: Tags },
    { title: "Пользователи", url: "/users", icon: Users },
];

export function AppSidebar() {
    const pathname = usePathname();
    const { logout, user, isAdmin } = useAuth();

    return (
        <Sidebar>
            <SidebarHeader className="p-4 border-b">
                <div className="flex items-center gap-2 font-bold text-xl">
                    <Ticket className="h-6 w-6 text-primary" />
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