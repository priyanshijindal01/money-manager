package com.project.moneymanager.controller;

import com.project.moneymanager.dto.FilterDTO;
import com.project.moneymanager.service.ExpenseService;
import com.project.moneymanager.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/filter")
public class FilterController {

    private final ExpenseService expenseService;
    private final IncomeService incomeService;

    @PostMapping
    public ResponseEntity<?> filterTransactions(@RequestBody FilterDTO filterDTO) {
        LocalDate startDate = filterDTO.getStartDate() != null ? filterDTO.getStartDate() : LocalDate.MIN;
        LocalDate endDate = filterDTO.getEndDate() != null ? filterDTO.getEndDate() : LocalDate.now();
        String keyword = filterDTO.getKeyword() != null ? filterDTO.getKeyword() : "";
        Sort.Direction direction = "desc".equalsIgnoreCase(filterDTO.getSortOrder()) ? Sort.Direction.DESC : Sort.Direction.ASC;
        String sortField = filterDTO.getSortField() != null ? filterDTO.getSortField() : "date";
        Sort sort = Sort.by(direction, sortField);
        if (filterDTO.getType().equalsIgnoreCase("income")) {
            return ResponseEntity.ok(incomeService.filterIncomes(startDate,
                                                                 endDate,
                                                                 keyword,
                                                                 sort));
        } else if (filterDTO.getType().equalsIgnoreCase("expense")) {
            return ResponseEntity.ok(expenseService.filterExpenses(startDate,
                                                                   endDate,
                                                                   keyword,
                                                                   sort));
        } else {
            return ResponseEntity.badRequest()
                    .body("Invalid type. Must be income or expense");
        }
    }
}
