package com.book.api.service;

import com.book.api.dto.BookDto;
import com.book.api.entity.Book;
import com.book.api.repository.BookRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService{

    @Value("${project.images}")
    private String path;

    @Value("${base.url}")
    private String baseUrl;

    private final BookRepository bookRepository;
    private final FileService fileService;

    public BookServiceImpl(BookRepository bookRepository, FileService fileService) {
        this.bookRepository = bookRepository;
        this.fileService = fileService;
    }


    @Override
    public BookDto addBook(BookDto bookDto, MultipartFile file) throws IOException {
    if(file != null && Files.exists(Paths.get(path + File.separator + file.getOriginalFilename()))) {
        throw new FileAlreadyExistsException("File already exists. Please give another file.");
    }

    String uploadedFilename =  fileService.uploadFile(path, file);
    String bookCoverUrl = baseUrl + "/api/v1/file/" + uploadedFilename;

    bookDto.setBookCover(uploadedFilename);
    bookDto.setBookCoverUrl(bookCoverUrl);

    Book book = convertToBook(bookDto);

    Book savedBook = bookRepository.save(book);

        return convertToBookDto(savedBook);
    }

    @Override
    public BookDto getBook(Long isbn) {
        Book book = bookRepository.findById(isbn).orElseThrow(()-> new RuntimeException("Book not found with isbn: " + isbn));

        return convertToBookDto(book);
    }

    @Override
    public List<BookDto> getAllBooks() {
        return bookRepository.findAll().stream()
                .map((b) -> convertToBookDto(b))
                .toList();
    }

    @Override
    public BookDto updateBook(Long isbn, BookDto bookDto, MultipartFile file) throws IOException {
        Book book = bookRepository.findById(isbn).orElseThrow(() -> new RuntimeException("Book not found with isbn: " + isbn));

        String bookCover = bookDto.getBookCover();
        String bookCoverUrl = bookDto.getBookCoverUrl();

        if(file != null) {
            Files.deleteIfExists(Paths.get(path + File.separator + bookCover));
            bookCover = fileService.uploadFile(path, file);
            bookCoverUrl = baseUrl + "/api/v1/file"+bookCover;
            bookDto.setBookCover(bookCover);
            bookDto.setBookCoverUrl(bookCoverUrl);
        }

        book.setTitle(bookDto.getTitle());
        book.setAuthor(bookDto.getAuthor());
        book.setPrice(bookDto.getPrice());
        book.setDescription(bookDto.getDescription());
        book.setCategory(bookDto.getCategory());
        book.setQuantity(bookDto.getQuantity());

        Book updatedBook = bookRepository.save(book);

        return convertToBookDto(updatedBook);
    }

    @Override
    public String deleteBook(Long isbn) throws IOException {
        Book book = bookRepository.findById(isbn).orElseThrow(() -> new RuntimeException("Book not found with isbn: " + isbn));
        Files.deleteIfExists(Paths.get(path + File.separator + book.getBookCover()));
        bookRepository.delete(book);
        return "Book deleted successfully with isbn: " + isbn;
    }

    private BookDto convertToBookDto(Book book) {
        return BookDto.builder()
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .author(book.getAuthor())
                .price(book.getPrice())
                .description(book.getDescription())
                .category(book.getCategory())
                .quantity(book.getQuantity())
                .bookCover(book.getBookCover())
                .bookCoverUrl(book.getBookCoverUrl())
                .build();
    }

    private Book convertToBook(BookDto bookDto) {
        return Book.builder()
                .isbn(bookDto.getIsbn())
                .title(bookDto.getTitle())
                .author(bookDto.getAuthor())
                .price(bookDto.getPrice())
                .description(bookDto.getDescription())
                .category(bookDto.getCategory())
                .quantity(bookDto.getQuantity())
                .bookCover(bookDto.getBookCover())
                .bookCoverUrl(bookDto.getBookCoverUrl())
                .build();
    }
}
