package com.spendwiseai.service;

import com.spendwiseai.dto.TransactionDto;
import com.spendwiseai.entity.Transaction;
import com.spendwiseai.entity.User;
import com.spendwiseai.repository.TransactionRepository;
import com.spendwiseai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @Transactional
    public TransactionDto.Response createTransaction(Long userId, TransactionDto.Request request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Transaction transaction = Transaction.builder()
                .user(user)
                .type(request.getType())
                .amount(request.getAmount())
                .category(request.getCategory())
                .description(request.getDescription())
                .date(request.getDate())
                .paymentMethod(request.getPaymentMethod())
                .build();

        Transaction saved = transactionRepository.save(transaction);
        return mapToResponse(saved);
    }

    public Page<TransactionDto.Response> getTransactions(
            Long userId,
            Transaction.TransactionType type,
            Transaction.Category category,
            LocalDate startDate,
            LocalDate endDate,
            String search,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending().and(Sort.by("id").descending()));
        Page<Transaction> pageResult = transactionRepository.findFiltered(userId, type, category, startDate, endDate, search, pageable);
        return pageResult.map(this::mapToResponse);
    }

    public TransactionDto.Response getTransactionById(Long userId, Long transactionId) {
        Transaction transaction = transactionRepository.findByIdAndUserId(transactionId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found or access denied"));
        return mapToResponse(transaction);
    }

    @Transactional
    public TransactionDto.Response updateTransaction(Long userId, Long transactionId, TransactionDto.Request request) {
        Transaction transaction = transactionRepository.findByIdAndUserId(transactionId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found or access denied"));

        transaction.setType(request.getType());
        transaction.setAmount(request.getAmount());
        transaction.setCategory(request.getCategory());
        transaction.setDescription(request.getDescription());
        transaction.setDate(request.getDate());
        transaction.setPaymentMethod(request.getPaymentMethod());

        Transaction updated = transactionRepository.save(transaction);
        return mapToResponse(updated);
    }

    @Transactional
    public void deleteTransaction(Long userId, Long transactionId) {
        Transaction transaction = transactionRepository.findByIdAndUserId(transactionId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found or access denied"));
        transactionRepository.delete(transaction);
    }

    public String exportCsv(Long userId) {
        List<Transaction> list = transactionRepository.findByUserIdAndDateBetweenOrderByDateDesc(
                userId, LocalDate.of(2000, 1, 1), LocalDate.of(2099, 12, 31));

        StringBuilder csv = new StringBuilder();
        csv.append("ID,Date,Type,Category,Amount,PaymentMethod,Description\n");
        for (Transaction t : list) {
            csv.append(t.getId()).append(",")
               .append(t.getDate()).append(",")
               .append(t.getType()).append(",")
               .append(t.getCategory()).append(",")
               .append(t.getAmount()).append(",")
               .append(t.getPaymentMethod()).append(",")
               .append("\"").append(t.getDescription() != null ? t.getDescription().replace("\"", "\"\"") : "").append("\"\n");
        }
        return csv.toString();
    }

    private TransactionDto.Response mapToResponse(Transaction t) {
        return TransactionDto.Response.builder()
                .id(t.getId())
                .type(t.getType())
                .amount(t.getAmount())
                .category(t.getCategory())
                .description(t.getDescription())
                .date(t.getDate())
                .paymentMethod(t.getPaymentMethod())
                .build();
    }
}
