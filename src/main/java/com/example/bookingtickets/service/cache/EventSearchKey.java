package com.example.bookingtickets.service.cache;

public record EventSearchKey(
    String venueName,
    String categoryName,
    int page,
    int size,
    String sort,
    boolean useNative
) {
}