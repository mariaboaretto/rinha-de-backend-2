package com.mariaboaretto.rinhaapi.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerExtractInfo {
    private Integer total;
    private LocalDateTime data_extrato;
    private Integer limite;
}
