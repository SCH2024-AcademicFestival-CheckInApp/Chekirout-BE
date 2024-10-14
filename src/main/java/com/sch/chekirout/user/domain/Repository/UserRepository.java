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

    boolean existsByEmail(String email);  // 이메일 중복 검사 추가

    Optional<User> findByEmail(String email);  // 이메일로 사용자 조회 메서드 추가
}
