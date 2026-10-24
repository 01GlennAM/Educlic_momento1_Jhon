package com.cesde.educlic.model.enums;
import com.cesde.educlic.exception.BusinessException;

public enum TallaTenis {
    T_36("36"),
    T_37("37"),
    T_38("38"),
    T_39("39"),
    T_40("40"),
    T_41("41"),
    T_42("42"),
    T_43("43"),
    T_44("44");

    private final String valor;

    TallaTenis(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public static TallaTenis desdeValor(String valor){
        for(TallaTenis talla: TallaTenis.values()){
            if(talla.valor.equals(valor)){
                return talla;
            }
        }

        throw new BusinessException("la talla" + valor + "no es válida para los tenis");
    }
}
