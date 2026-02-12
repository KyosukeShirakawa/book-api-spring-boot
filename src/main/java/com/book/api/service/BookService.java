package com.book.api.service;

import com.book.api.dto.BookDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BookService {
    BookDto addBook(BookDto bookDto, MultipartFile file) throws IOException;

    BookDto getBook(Long isbn);

    List<BookDto> getAllBooks();

    BookDto updateBook(Long isbn, BookDto bookDto, MultipartFile file);

    String deleteBook(Long isbn);

}
