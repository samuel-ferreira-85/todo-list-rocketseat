package com.samuel.todolist.utils;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class Utils {

    public static void copyNonNullProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    /*
     * Método para obter os nomes das propriedades nulas de um objeto
     */
    public static String[] getNullPropertyNames(Object source) {

        /*
        * BeanWrapper é uma classe do Spring Framework que fornece métodos convenientes
        * para acessar e manipular as propriedades de um objeto Java de forma reflexiva.
        */
        final BeanWrapper src = new BeanWrapperImpl(source);

        /*
         * getPropertyDescriptors() - Obtém todas as propriedades do objeto de origem
         * retorna um array de objetos PropertyDescriptor 
         * cada um representando uma propriedade do objeto. 
         */
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();

        // itera no pds e verifica se a propriedade é nula, caso seja, ela é adicionada no emptyNames.
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }

        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);

    }
    
}
