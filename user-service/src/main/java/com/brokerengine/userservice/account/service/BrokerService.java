package com.brokerengine.userservice.account.service;

import com.brokerengine.userservice.account.web.dto.CreateWalletRequestDto;
import com.brokerengine.userservice.account.web.dto.WalletDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "BROKER-SERVICE")
public interface BrokerService {

    @PostMapping("/wallet")
    WalletDto createWallet(CreateWalletRequestDto walletDto);
}
