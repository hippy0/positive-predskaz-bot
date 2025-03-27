package me.hippy.prdsz.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import me.hippy.prdsz.enums.UserRoleKey;

@Getter
@Setter
@Builder
public class InputUserDto {

    private UserRoleKey roleKey;

    private Long telegramId;
}
