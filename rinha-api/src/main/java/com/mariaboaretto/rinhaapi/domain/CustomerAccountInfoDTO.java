package com.mariaboaretto.rinhaapi.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAccountInfoDTO {
    private Integer saldo;
    private Integer limite;
}
