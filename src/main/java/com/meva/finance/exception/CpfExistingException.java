package com.meva.finance.exception;

public class CpfExistingException extends RuntimeException {
    public CpfExistingException(String cpf) {
        super(String.format("O Cpf %s já existe!", cpf));
    }
}
