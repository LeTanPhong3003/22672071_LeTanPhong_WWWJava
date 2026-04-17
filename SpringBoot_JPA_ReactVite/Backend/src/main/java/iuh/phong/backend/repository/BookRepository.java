package iuh.phong.backend.repository;

import iuh.phong.backend.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("""
            select b from Book b
            where (:keyword is null or :keyword = ''
            or lower(b.title) like lower(concat('%', :keyword, '%'))
            or lower(b.author) like lower(concat('%', :keyword, '%'))
            or lower(b.category) like lower(concat('%', :keyword, '%')))
            order by b.id desc
            """)
    List<Book> search(@Param("keyword") String keyword);
}

