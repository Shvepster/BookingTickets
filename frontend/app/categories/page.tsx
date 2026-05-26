"use client";

import { useState } from "react";
import useSWR from "swr";
import { DashboardLayout } from "@/components/dashboard-layout";
import { categoriesApi, eventsApi } from "@/lib/api";
import { useAuth } from "@/lib/auth-context";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from "@/components/ui/dialog";
import { Plus, Pencil, Trash2, Loader2, Tags, Music } from "lucide-react";
import type { CategoryResponseDto } from "@/lib/types";

export default function CategoriesPage() {
    const { isAdmin } = useAuth();

    // Исправили ключи SWR и вызовы методов на безопасные стрелочные функции
    const { data: categories, isLoading, mutate: mutateCategories } = useSWR("/categories", () => categoriesApi.getAll());
    const { data: events } = useSWR("/events", () => eventsApi.getAll());

    const [isOpen, setIsOpen] = useState(false);
    const [editingCategory, setEditingCategory] = useState<CategoryResponseDto | null>(null);
    const [isSubmitting, setIsSubmitting] = useState(false);

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setIsSubmitting(true);
        const formData = new FormData(e.currentTarget);
        const name = formData.get("name") as string;
        try {
            if (editingCategory) {
                await categoriesApi.update(editingCategory.id, { name });
            } else {
                await categoriesApi.create({ name });
            }
            mutateCategories();
            setIsOpen(false);
        } catch (error: any) {
            alert(error.message);
        } finally {
            setIsSubmitting(false);
        }
    };

    const handleDelete = async (id: number) => {
        if (!confirm("Вы уверены, что хотите удалить эту категорию? Мероприятия потеряют этот тег.")) return;
        try {
            await categoriesApi.delete(id);
            mutateCategories();
        } catch (error: any) {
            alert(error.message);
        }
    };

    const openDialog = (category?: CategoryResponseDto) => {
        setEditingCategory(category || null);
        setIsOpen(true);
    };

    return (
        <DashboardLayout>
            <div className="flex justify-between items-center mb-6">
                <div>
                    <h1 className="text-3xl font-bold tracking-tight">Категории</h1>
                    <p className="mt-1 text-muted-foreground">Теги и жанры мероприятий (ManyToMany)</p>
                </div>
                {isAdmin && (
                    <Button onClick={() => openDialog()}>
                        <Plus className="mr-2 h-4 w-4" /> Создать
                    </Button>
                )}
            </div>

            {isLoading ? (
                <div className="flex justify-center py-20">
                    <Loader2 className="h-8 w-8 animate-spin text-primary" />
                </div>
            ) : categories?.length === 0 ? (
                <Card>
                    <CardContent className="flex flex-col items-center justify-center py-12">
                        <Tags className="h-12 w-12 text-muted-foreground mb-4 opacity-50" />
                        <h3 className="text-lg font-semibold">Категорий пока нет</h3>
                        {isAdmin && <p className="text-sm text-muted-foreground">Добавьте первую категорию (например, "Рок" или "Стендап")</p>}
                    </CardContent>
                </Card>
            ) : (
                <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
                    {categories?.map((category) => {
                        // Находим мероприятия, которые содержат эту категорию
                        const categoryEvents = events?.filter(e => e.categories?.includes(category.name)) || [];

                        return (
                            <Card key={category.id} className="group relative hover:border-primary/50 transition-colors">
                                <CardHeader className="pb-3 flex flex-row items-center justify-between space-y-0">
                                    <div className="flex items-center gap-2">
                                        <div className="p-2 bg-primary/10 rounded-md">
                                            <Music className="h-4 w-4 text-primary" />
                                        </div>
                                        <CardTitle className="text-lg">{category.name}</CardTitle>
                                    </div>

                                    {isAdmin && (
                                        <div className="flex gap-1 opacity-0 transition-opacity group-hover:opacity-100">
                                            <Button variant="ghost" size="icon" className="h-7 w-7" onClick={() => openDialog(category)}>
                                                <Pencil className="h-3.5 w-3.5" />
                                            </Button>
                                            <Button variant="ghost" size="icon" className="h-7 w-7 text-destructive hover:bg-destructive/10 hover:text-destructive" onClick={() => handleDelete(category.id)}>
                                                <Trash2 className="h-3.5 w-3.5" />
                                            </Button>
                                        </div>
                                    )}
                                </CardHeader>
                                <CardContent>
                                    <div className="flex items-center gap-2 mb-2">
                                        <span className="text-2xl font-bold">{categoryEvents.length}</span>
                                        <span className="text-sm text-muted-foreground">событий</span>
                                    </div>
                                    <div className="flex flex-wrap gap-1">
                                        {categoryEvents.slice(0, 3).map(e => (
                                            <Badge key={e.id} variant="secondary" className="text-[10px]">
                                                {e.title}
                                            </Badge>
                                        ))}
                                        {categoryEvents.length > 3 && (
                                            <Badge variant="outline" className="text-[10px]">
                                                +{categoryEvents.length - 3}
                                            </Badge>
                                        )}
                                    </div>
                                </CardContent>
                            </Card>
                        );
                    })}
                </div>
            )}

            <Dialog open={isOpen} onOpenChange={setIsOpen}>
                <DialogContent className="sm:max-w-[425px]">
                    <DialogHeader>
                        <DialogTitle>{editingCategory ? "Редактировать жанр" : "Новый жанр / категория"}</DialogTitle>
                    </DialogHeader>
                    <form onSubmit={handleSubmit} className="space-y-4">
                        <div className="space-y-2">
                            <Label htmlFor="name">Название категории</Label>
                            <Input id="name" name="name" placeholder="Например: Классика" defaultValue={editingCategory?.name} required />
                        </div>
                        <DialogFooter>
                            <Button type="button" variant="outline" onClick={() => setIsOpen(false)}>Отмена</Button>
                            <Button type="submit" disabled={isSubmitting}>
                                {isSubmitting ? <Loader2 className="mr-2 h-4 w-4 animate-spin" /> : "Сохранить"}
                            </Button>
                        </DialogFooter>
                    </form>
                </DialogContent>
            </Dialog>
        </DashboardLayout>
    );
}