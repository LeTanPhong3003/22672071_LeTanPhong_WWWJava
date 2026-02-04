package phong.lab5.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import phong.lab5.model.Student;

/**
 * StudentRepository là một Spring Bean được tạo tự động bởi Spring Data JPA.
 * Annotation @Repository đánh dấu đây là một Bean thuộc tầng Data Access Layer.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
}

