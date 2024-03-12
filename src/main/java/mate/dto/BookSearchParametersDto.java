package mate.dto;

public record BookSearchParametersDto(String[] titles,
                                      String[] authors,
                                      String[] isbn,
                                      String[] price,
                                      String[] description) {
}
