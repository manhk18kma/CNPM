package kma.cnpm.beapp.domain.payment.service;

import kma.cnpm.beapp.domain.payment.entity.Account;
import kma.cnpm.beapp.domain.payment.repository.AccountRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountService {
    final AccountRepository accountRepository;
    public void initAccount(Long userId){
        Account account = Account.builder()
                .userId(userId)
                .balance(BigDecimal.valueOf(0))
                .build();
        accountRepository.save(account);
    }
}
