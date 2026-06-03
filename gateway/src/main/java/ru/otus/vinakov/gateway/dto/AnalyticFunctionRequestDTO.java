package ru.otus.vinakov.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticFunctionRequestDTO {

    private String functionKey;
    private Map<String, Object> params;

}
