"use client";

import { useState } from "react";
import Link from "next/link";
import { useAuth } from "@/lib/auth-context";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from "@/components/ui/card";
import { Alert, AlertDescription } from "@/components/ui/alert";
import { Ticket, Loader2, AlertCircle } from "lucide-react";

export default function LoginPage() {
    const [error, setError] = useState("");
    const [isLoading, setIsLoading] = useState(false);
    const { login } = useAuth();

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setError("");
        setIsLoading(true);

        const formData = new FormData(e.currentTarget);
        const loginValue = formData.get("login") as string;
        const passwordValue = formData.get("password") as string;

        try {
            await login({ login: loginValue, password: passwordValue });
            // Перенаправление происходит внутри функции login в auth-context
        } catch (err: any) {
            setError(err.message || "Ошибка входа. Проверьте логин и пароль.");
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="flex min-h-screen items-center justify-center bg-muted/30 p-4">
            <Card className="w-full max-w-md">
                <CardHeader className="space-y-1 items-center">
                    <div className="flex h-12 w-12 items-center justify-center rounded-full bg-primary/10 mb-4">
                        <Ticket className="h-6 w-6 text-primary" />
                    </div>
                    <CardTitle className="text-2xl font-bold">С возвращением</CardTitle>
                    <CardDescription>Введите свои данные для входа в систему</CardDescription>
                </CardHeader>
                <form onSubmit={handleSubmit}>
                    <CardContent className="space-y-4">
                        {error && (
                            <Alert variant="destructive">
                                <AlertCircle className="h-4 w-4" />
                                <AlertDescription>{error}</AlertDescription>
                            </Alert>
                        )}
                        <div className="space-y-2">
                            <Label htmlFor="login">Email или Имя пользователя</Label>
                            <Input id="login" name="login" placeholder="m@example.com или username" required />
                        </div>
                        <div className="space-y-2">
                            <Label htmlFor="password">Пароль</Label>
                            <Input id="password" name="password" type="password" required />
                        </div>
                    </CardContent>
                    <CardFooter className="flex flex-col space-y-4">
                        <Button type="submit" className="w-full" disabled={isLoading}>
                            {isLoading ? <Loader2 className="mr-2 h-4 w-4 animate-spin" /> : "Войти"}
                        </Button>
                        <div className="text-center text-sm text-muted-foreground">
                            Нет аккаунта?{" "}
                            <Link href="/register" className="text-primary hover:underline underline-offset-4">
                                Зарегистрироваться
                            </Link>
                        </div>
                    </CardFooter>
                </form>
            </Card>
        </div>
    );
}