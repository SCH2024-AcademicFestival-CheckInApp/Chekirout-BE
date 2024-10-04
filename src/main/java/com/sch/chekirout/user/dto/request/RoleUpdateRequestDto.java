package com.sch.chekirout.user.dto.request;


import com.sch.chekirout.user.domain.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleUpdateRequestDto {
    private UserRole role;
}
