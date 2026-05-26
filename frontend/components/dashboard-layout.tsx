"use client";

import { AppSidebar } from "./app-sidebar";
import { AuthGuard } from "./auth-guard";
import { SidebarProvider, SidebarTrigger } from "@/components/ui/sidebar";

interface DashboardLayoutProps {
    children: React.ReactNode;
}

export function DashboardLayout({ children }: DashboardLayoutProps) {
    return (
        <AuthGuard>
            <SidebarProvider>
                <AppSidebar />
                <main className="flex-1 flex flex-col h-screen overflow-hidden bg-muted/10">
                    <div className="p-4 flex items-center border-b bg-background">
                        <SidebarTrigger className="mr-4" />
                        <h1 className="text-lg font-semibold text-muted-foreground">Панель управления</h1>
                    </div>
                    <div className="flex-1 overflow-auto p-6">
                        {children}
                    </div>
                </main>
            </SidebarProvider>
        </AuthGuard>
    );
}