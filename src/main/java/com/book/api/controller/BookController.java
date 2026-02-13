package com.book.api.controller;

import com.book.api.dto.BookDto;
import com.book.api.repository.BookRepository;
import com.book.api.service.BookService;
import org.springframework.boot.json.JsonParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService bookService;


    public BookController(BookService bookService, BookRepository bookRepository) {
        this.bookService = bookService;
    }

    @PostMapping("/add-book")
    public ResponseEntity<BookDto> addNewBook(@RequestPart String bookDtoJson, @RequestPart(required = false) MultipartFile file) throws IOException {
        if(file != null && file.isEmpty()) {
            file = null;
        }
        BookDto bookDto = convertJsonToBookDto(bookDtoJson);

        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.addBook(bookDto, file));
    }

    @GetMapping
    public ResponseEntity<List<BookDto>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{isbn}")
    public ResponseEntity<BookDto> getBookByIsbn(@PathVariable Long isbn) {
        return ResponseEntity.ok(bookService.getBook(isbn));
    }


    private BookDto convertJsonToBookDto(String bookDtoJson) {
        BookDto bookDto = new BookDto();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
           bookDto = objectMapper.readValue(bookDtoJson, BookDto.class);
        } catch (JsonParseException e) {
            System.out.println("Failed to parse JSON: " + e.getMessage());

        }
        return bookDto;
    }
}
