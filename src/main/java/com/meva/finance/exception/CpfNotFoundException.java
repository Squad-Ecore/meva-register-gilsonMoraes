package com.meva.finance.exception;

public class CpfNotFoundException extends RuntimeException{
    public CpfNotFoundException(String cpf){
        super(String.format("O Cpf %s não foi encontrado!", cpf));
    }
}
