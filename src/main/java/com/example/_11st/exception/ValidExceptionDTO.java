package com.example._11st.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.Map;

// Valid 어노테이션을 위한 DTO
@Getter
@Builder
public class ValidExceptionDTO {

    @NotEmpty
    String exception;

    Map<String, String> errors;

    public static ValidExceptionDTO toDto(BindException e){
        e.printStackTrace();
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors()
                .forEach(c -> errors.put(((FieldError) c).getField(), c.getDefaultMessage()));

        return ValidExceptionDTO.builder()
                .exception(e.getClass().getSimpleName())
                .errors(errors)
                .build();
    }
}
