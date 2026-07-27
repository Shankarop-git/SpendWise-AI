package com.spendwiseai.service;

import com.spendwiseai.dto.AuthDto;
import com.spendwiseai.entity.Budget;
import com.spendwiseai.entity.Transaction;
import com.spendwiseai.entity.User;
import com.spendwiseai.repository.BudgetRepository;
import com.spendwiseai.repository.TransactionRepository;
import com.spendwiseai.repository.UserRepository;
import com.spendwiseai.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final BudgetRepository budgetRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;

    public AuthDto.AuthResponse register(AuthDto.RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email address is already in use");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail().toLowerCase().trim())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .isDemo(false)
                .build();

        User saved = userRepository.save(user);
        String token = tokenProvider.generateToken(saved.getEmail());

        return AuthDto.AuthResponse.builder()
                .token(token)
                .user(mapToUserResponse(saved))
                .build();
    }

    public AuthDto.AuthResponse login(AuthDto.LoginRequest request) {
        String email = request.getEmail().toLowerCase().trim();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, request.getPassword()));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        String token = tokenProvider.generateToken(user.getEmail());

        return AuthDto.AuthResponse.builder()
                .token(token)
                .user(mapToUserResponse(user))
                .build();
    }

    public AuthDto.AuthResponse getDemoAccount() {
        String demoEmail = "demo@spendwise.ai";
        User demoUser = userRepository.findByEmail(demoEmail).orElseGet(() -> {
            User newDemo = User.builder()
                    .name("Demo Student User")
                    .email(demoEmail)
                    .passwordHash(passwordEncoder.encode("demo12345"))
                    .isDemo(true)
                    .build();
            User savedDemo = userRepository.save(newDemo);
            seedDemoData(savedDemo);
            return savedDemo;
        });

        String token = tokenProvider.generateToken(demoUser.getEmail());

        return AuthDto.AuthResponse.builder()
                .token(token)
                .user(mapToUserResponse(demoUser))
                .build();
    }

    public AuthDto.UserResponse getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return mapToUserResponse(user);
    }

    private void seedDemoData(User demoUser) {
        LocalDate now = LocalDate.now();
        int month = now.getMonthValue();
        int year = now.getYear();

        // Seed Budgets
        budgetRepository.saveAll(List.of(
                Budget.builder().user(demoUser).category(Transaction.Category.FOOD).monthlyLimit(BigDecimal.valueOf(25000)).month(month).year(year).build(),
                Budget.builder().user(demoUser).category(Transaction.Category.SHOPPING).monthlyLimit(BigDecimal.valueOf(15000)).month(month).year(year).build(),
                Budget.builder().user(demoUser).category(Transaction.Category.ENTERTAINMENT).monthlyLimit(BigDecimal.valueOf(10000)).month(month).year(year).build(),
                Budget.builder().user(demoUser).category(Transaction.Category.TRANSPORT).monthlyLimit(BigDecimal.valueOf(8000)).month(month).year(year).build()
        ));

        // Seed Transactions
        transactionRepository.saveAll(List.of(
                Transaction.builder().user(demoUser).type(Transaction.TransactionType.INCOME).amount(BigDecimal.valueOf(250000)).category(Transaction.Category.SALARY).description("Monthly Salary Deposit").date(now.minusDays(20)).paymentMethod(Transaction.PaymentMethod.BANK_TRANSFER).build(),
                Transaction.builder().user(demoUser).type(Transaction.TransactionType.INCOME).amount(BigDecimal.valueOf(45000)).category(Transaction.Category.FREELANCE).description("Web Design Gig").date(now.minusDays(10)).paymentMethod(Transaction.PaymentMethod.BANK_TRANSFER).build(),
                Transaction.builder().user(demoUser).type(Transaction.TransactionType.EXPENSE).amount(BigDecimal.valueOf(8500)).category(Transaction.Category.FOOD).description("Grocery Restock").date(now.minusDays(15)).paymentMethod(Transaction.PaymentMethod.DEBIT_CARD).build(),
                Transaction.builder().user(demoUser).type(Transaction.TransactionType.EXPENSE).amount(BigDecimal.valueOf(3500)).category(Transaction.Category.FOOD).description("Dinner with Friends").date(now.minusDays(8)).paymentMethod(Transaction.PaymentMethod.UPI).build(),
                Transaction.builder().user(demoUser).type(Transaction.TransactionType.EXPENSE).amount(BigDecimal.valueOf(7000)).category(Transaction.Category.SHOPPING).description("Winter Jacket").date(now.minusDays(5)).paymentMethod(Transaction.PaymentMethod.CREDIT_CARD).build(),
                Transaction.builder().user(demoUser).type(Transaction.TransactionType.EXPENSE).amount(BigDecimal.valueOf(2500)).category(Transaction.Category.ENTERTAINMENT).description("Movie Tickets").date(now.minusDays(3)).paymentMethod(Transaction.PaymentMethod.UPI).build(),
                Transaction.builder().user(demoUser).type(Transaction.TransactionType.EXPENSE).amount(BigDecimal.valueOf(4000)).category(Transaction.Category.TRANSPORT).description("Monthly Metro Pass").date(now.minusDays(18)).paymentMethod(Transaction.PaymentMethod.CREDIT_CARD).build()
        ));
    }

    private AuthDto.UserResponse mapToUserResponse(User user) {
        return AuthDto.UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .isDemo(user.isDemo())
                .build();
    }
}
