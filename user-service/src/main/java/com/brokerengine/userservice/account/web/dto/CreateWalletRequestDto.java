package com.brokerengine.userservice.account.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateWalletRequestDto {
    private String userId;
}
