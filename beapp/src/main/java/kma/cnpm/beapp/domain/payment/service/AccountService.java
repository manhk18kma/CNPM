package kma.cnpm.beapp.domain.payment.service;

import kma.cnpm.beapp.domain.common.dto.BankDTO;
import kma.cnpm.beapp.domain.common.exception.AppErrorCode;
import kma.cnpm.beapp.domain.common.exception.AppException;
import kma.cnpm.beapp.domain.payment.dto.request.AddBankRequest;
import kma.cnpm.beapp.domain.payment.dto.response.AccountResponse;
import kma.cnpm.beapp.domain.payment.entity.Account;
import kma.cnpm.beapp.domain.payment.entity.AccountHasBank;
import kma.cnpm.beapp.domain.payment.entity.Bank;
import kma.cnpm.beapp.domain.payment.repository.AccountHasBankRepository;
import kma.cnpm.beapp.domain.payment.repository.AccountRepository;
import kma.cnpm.beapp.domain.payment.repository.BankRepository;
import kma.cnpm.beapp.domain.user.service.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountService {
    final AccountRepository accountRepository;
    final AuthService authService;
    final BankRepository bankRepository;
    final AccountHasBankRepository accountHasBankRepository;

    public Account getAccount(){
        String userId =  authService.getAuthenticationName();
        return accountRepository.findAccountByUserId(Long.valueOf(userId))
                .orElseThrow(() -> new AppException(AppErrorCode.ACCOUNT_NOT_EXIST));
    }

    public AccountHasBank getAccountHasBank(Long id){
        return  accountHasBankRepository.findByIdOptional(id)
                .orElseThrow(() -> new AppException(AppErrorCode.ACCOUNT_HAS_BANK_NOT_EXIST));
    }
    @Transactional
    public void initAccount(Long userId){
        Account account = Account.builder()
                .userId(userId)
                .balance(BigDecimal.valueOf(0))
                .build();
        accountRepository.save(account);
    }

    @Transactional
    public AccountResponse addBankToAccount(AddBankRequest request) {
        Account account = getAccount();
        Bank bank = bankRepository.findBankById(request.getBankId())
                .orElseThrow(() -> new AppException(AppErrorCode.BANK_NOT_EXIST));

        if (accountHasBankRepository.existsAccountHasBankByAccountIdAndBankId(account.getId(), request.getBankId())) {
            throw new AppException(AppErrorCode.ACCOUNT_HAS_BANK_EXIST);
        }
        AccountHasBank accountHasBank = AccountHasBank.builder()
                .accountNumber(request.getAccountNumber())
                .account(account)
                .bank(bank)
                .build();
        accountHasBankRepository.save(accountHasBank);

        return AccountResponse.builder()
                .accountId(account.getId())
                .build();
    }
    @Transactional
    public void removeBankAccount(Long id) {
        Account account = getAccount();
        AccountHasBank accountHasBank = accountHasBankRepository.findByIdOptional(id)
                .orElseThrow(() -> new AppException(AppErrorCode.ACCOUNT_HAS_BANK_NOT_EXIST));

        if (!accountHasBank.getAccount().getId().equals(account.getId())) {
            throw new AppException(AppErrorCode.UNAUTHORIZED);
        }

        accountHasBankRepository.deleteByIdCus(accountHasBank.getId());
    }

    @Transactional
    public void updateBalance(BigDecimal amount, Account account, boolean plusOrMinus) {
        if (plusOrMinus) {
            BigDecimal newBalance = account.getBalance().add(amount);
            account.setBalance(newBalance);
        } else {
            BigDecimal newBalance = account.getBalance().subtract(amount);
            if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                throw new AppException(AppErrorCode.BALANCE_NOT_ENOUGH);
            }
            account.setBalance(newBalance);
        }
        accountRepository.save(account);
    }


    public List<BankDTO> getBanksOfUser(Long userId){
        List<AccountHasBank> accountHasBanks = accountHasBankRepository.getBankOfUser(userId);
        return  accountHasBanks.stream().map(accountHasBank -> {
            return BankDTO.builder()
                    .accountHasBankId(accountHasBank.getId())
                    .bankCode(accountHasBank.getBank().getBankCode())
                    .bankName(accountHasBank.getBank().getBankName())
                    .bankNumber(accountHasBank.getAccountNumber())
                    .bankAvt(accountHasBank.getBank().getBankAvt())
                    .build();
        }).collect(Collectors.toList());
    }

    public BigDecimal getBalance(Long userId){
        return accountRepository.getBalance(userId);
    }
}

