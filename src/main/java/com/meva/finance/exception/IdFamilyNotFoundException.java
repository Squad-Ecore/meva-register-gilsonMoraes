package com.meva.finance.exception;

public class IdFamilyNotFoundException extends RuntimeException{
    public IdFamilyNotFoundException(Long idFamily){
        super(String.format("A família %s não existe", idFamily));
    }
}
