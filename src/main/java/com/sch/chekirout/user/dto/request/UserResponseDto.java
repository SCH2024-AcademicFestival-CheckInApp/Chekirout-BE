package com.sch.chekirout.user.dto.request;

import com.sch.chekirout.user.domain.*;
import lombok.Getter;
import lombok.Setter;


// 필요한 필드만 포함하는 DTO 클래스
@Getter
public class UserResponseDto {
    private String username;
    private Department department;
    private String name;
    private Boolean isEligibleForPrize;
    private Boolean isWinner;
    private Boolean isNotificationEnabled;


    public UserResponseDto(User user) {
        this.username = user.getUsername();
        this.department = user.getDepartment();
        this.name = user.getName();

        // Boolean 필드 대신, 상태에 따라 true/false 반환하도록 변경
        UserPrizeInfo prizeInfo = user.getPrizeInfo();
        if (prizeInfo != null) {
            this.isEligibleForPrize = (prizeInfo.getPrizeEligibilityTimestamp() != null);  // 경품 자격이 있는지 여부
            this.isWinner = (prizeInfo.getPrizeStatus() == UserPrizeStatus.WINNER);  // 경품 상태가 WINNER인지 여부
        } else {
            this.isEligibleForPrize = false;  // 기본값 설정
            this.isWinner = false;  // 기본값 설정
        }


        UserNotification notification = user.getNotification();
        this.isNotificationEnabled = (notification != null && notification.getNotificationEnabledAt() != null);
    }
}