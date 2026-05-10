package com.expense.tracker.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RefreshTokenRequestDto {
    String refreshToken;
}
