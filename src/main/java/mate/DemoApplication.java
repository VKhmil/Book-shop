package mate;

import java.math.BigDecimal;
import mate.model.Book;
import mate.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book harryPotter = new Book();
            harryPotter.setTitle("HarryPotter and the Half-blood prince");
            harryPotter.setAuthor("J.K. ROWLING");
            harryPotter.setIsbn("978-0-0000-0000-1");
            harryPotter.setPrice(BigDecimal.valueOf(300));
            harryPotter.setDescription("In the book 'Harry Potter and the Half-Blood Prince, "
                    + "young wizard Harry Potter returns to Hogwarts for his sixth year of study ");
            harryPotter.setCoverImage("https://example.com/cover_image.jpg");

            bookService.save(harryPotter);

            System.out.println(bookService.findAll());
        };
    }
}
