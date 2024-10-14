package com.sch.chekirout.device.Repository;


import com.sch.chekirout.device.domain.UserDevice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDeviceRepository extends JpaRepository<UserDevice, Long> {

    // 특정 사용자 ID로 UserDevice 조회
    Optional<UserDevice> findByUserId(Long userId);

    // User가 존재하는지 확인하기 위해 존재 여부를 반환
    boolean existsByUserId(Long userId);
}