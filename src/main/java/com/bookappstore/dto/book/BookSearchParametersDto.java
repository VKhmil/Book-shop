package com.bookappstore.dto.book;

public record BookSearchParametersDto(String[] titles,
                                      String[] authors,
                                      String[] isbn,
                                      String[] price,
                                      String[] description,
                                      String[] categories) {
}
