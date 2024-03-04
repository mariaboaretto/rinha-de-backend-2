package com.mariaboaretto.rinhaapi.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mariaboaretto.rinhaapi.domain.deserialization.TransactionValueDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionDTO {
    // Uses custom deserializer to make sure JSON payload is deserialized to Integer
    @JsonDeserialize(using = TransactionValueDeserializer.class)
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
