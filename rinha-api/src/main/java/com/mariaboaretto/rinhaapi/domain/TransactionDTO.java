package com.mariaboaretto.rinhaapi.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionDTO {
    private Integer valor;
    private char tipo;
    private String descricao;
    private Date realizada_em;

    public TransactionDTO(Integer valor, char tipo, String descricao) {
        this.descricao = descricao;
        this.tipo = tipo;
        this.valor = valor;
    }
}
