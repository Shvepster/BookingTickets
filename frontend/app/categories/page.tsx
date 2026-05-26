"use client";

import { useState } from "react";
import useSWR from "swr";
import { DashboardLayout } from "@/components/dashboard-layout";
import { categoriesApi } from "@/lib/api";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger, DialogFooter } from "@/components/ui/dialog";
import { Plus, Pencil, Trash2, Loader2 } from "lucide-react";
import type { CategoryResponseDto } from "@/lib/types";

export default function CategoriesPage() {
    const { data: categories, isLoading, mutate } = useSWR("/categories", categoriesApi.getAll);
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
            mutate();
            setIsOpen(false);
        } catch (error: any) {
            alert(error.message);
        } finally {
            setIsSubmitting(false);
        }
    };

    const handleDelete = async (id: number) => {
        if (!confirm("Вы уверены, что хотите удалить эту категорию?")) return;
        try {
            await categoriesApi.delete(id);
            mutate();
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
                <h1 className="text-2xl font-bold">Категории</h1>
                <Button onClick={() => openDialog()}>
                    <Plus className="mr-2 h-4 w-4" /> Добавить
                </Button>
            </div>

            <div className="bg-background rounded-md border">
                <Table>
                    <TableHeader>
                        <TableRow>
                            <TableHead className="w-[100px]">ID</TableHead>
                            <TableHead>Название</TableHead>
                            <TableHead className="text-right">Действия</TableHead>
                        </TableRow>
                    </TableHeader>
                    <TableBody>
                        {isLoading ? (
                            <TableRow>
                                <TableCell colSpan={3} className="text-center py-10">
                                    <Loader2 className="h-6 w-6 animate-spin mx-auto text-muted-foreground" />
                                </TableCell>
                            </TableRow>
                        ) : categories?.length === 0 ? (
                            <TableRow>
                                <TableCell colSpan={3} className="text-center py-10 text-muted-foreground">
                                    Категорий пока нет
                                </TableCell>
                            </TableRow>
                        ) : (
                            categories?.map((category) => (
                                <TableRow key={category.id}>
                                    <TableCell className="font-medium">{category.id}</TableCell>
                                    <TableCell>{category.name}</TableCell>
                                    <TableCell className="text-right space-x-2">
                                        <Button variant="outline" size="icon" onClick={() => openDialog(category)}>
                                            <Pencil className="h-4 w-4" />
                                        </Button>
                                        <Button variant="destructive" size="icon" onClick={() => handleDelete(category.id)}>
                                            <Trash2 className="h-4 w-4" />
                                        </Button>
                                    </TableCell>
                                </TableRow>
                            ))
                        )}
                    </TableBody>
                </Table>
            </div>

            <Dialog open={isOpen} onOpenChange={setIsOpen}>
                <DialogContent>
                    <DialogHeader>
                        <DialogTitle>{editingCategory ? "Редактировать категорию" : "Добавить категорию"}</DialogTitle>
                    </DialogHeader>
                    <form onSubmit={handleSubmit} className="space-y-4">
                        <div className="space-y-2">
                            <Label htmlFor="name">Название</Label>
                            <Input id="name" name="name" defaultValue={editingCategory?.name} required />
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