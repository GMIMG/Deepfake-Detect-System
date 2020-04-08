package com.gmimg.multicampus.springboot.domain;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
//import org.springframework.test.context.junit.jupiter.*;
//import static org.junit.jupiter.api.Assertions.*;
//import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

//@RunWith(SpringRunner.class)
@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    BookRepository repository;

    @Test
    public void testSave() {
        Book book = new Book();
        book.setName("boot-spring-boot");
        book.setIsbn10("0123456789");
        book.setIsbn13("0123456789012");

        //assertTrue(book.isNew());
        //assertThat("hi", is(book.isNew()));

        repository.save(book);

        List<Book> books = repository.findByNameLike("boot%");
        assertTrue(!books.isEmpty());
        
        books = repository.findByNameLike("book");
        assertTrue(books.isEmpty());

    }
}