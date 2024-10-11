package com.sch.chekirout.user.domain.Repository;




import com.sch.chekirout.user.domain.Department;
import com.sch.chekirout.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    Page<User> findByDepartment(Department department, Pageable pageable);
}
