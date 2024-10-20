package com.sch.chekirout.stampCard.domain.repository;

import com.sch.chekirout.stampCard.domain.StampCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StampCardRepository extends JpaRepository<StampCard, Long> {

    Optional<StampCard> findByUserId(Long userId);

    boolean existsByUserId(Long userId);

    Long countAllBy();

    Long countByCompletedAtIsNotNull();

    @Query("SELECT sc FROM StampCard sc " +
            "ORDER BY SIZE(sc.stamps) ASC, sc.completedAt ASC")
    Page<StampCard> findAllOrderByStampsSizeAndCompletedAt(Pageable pageable);

    Page<StampCard> findAllByCompletedAtIsNotNull(Pageable pageable);

    List<StampCard> findAllByExclusivePrizeClaimedAtIsNull();

    @Query(value = "SELECT u.department AS department, COUNT(s.id) AS stamp_card_count " +
            "FROM users_info u " +
            "LEFT JOIN stamp_card s ON u.id = s.user_id " +
            "GROUP BY u.department " +
            "ORDER BY stamp_card_count DESC", nativeQuery = true)
    List<Object[]> countStampCardsByDepartment();

    @Query(value = "SELECT u.department AS department, SUM(st.stamp_count) AS total_stamps " +
            "FROM users_info u " +
            "JOIN stamp_card sc ON u.id = sc.user_id " +
            "JOIN (SELECT stamp_card_id, COUNT(*) AS stamp_count FROM stamp GROUP BY stamp_card_id) st " +
            "ON sc.id = st.stamp_card_id " +
            "GROUP BY u.department " +
            "ORDER BY total_stamps DESC", nativeQuery = true)
    List<Object[]> countTotalStampsByDepartment();
}
