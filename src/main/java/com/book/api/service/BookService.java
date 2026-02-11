package com.book.api.service;

import com.book.api.dto.BookDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookService {
    BookDto addBook(BookDto bookDto, MultipartFile file);

    BookDto getBook(Long isbn);

    List<BookDto> getAllBooks();

    BookDto updateBook(Long isbn, BookDto bookDto, MultipartFile file);

    String deleteBook(Long isbn);

}
