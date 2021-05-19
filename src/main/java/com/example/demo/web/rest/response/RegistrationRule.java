package com.example.demo.web.rest.response;

import com.example.demo.util.RegistrationRulesTypeEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegistrationRule {
    private RegistrationRulesTypeEnum type;
    private String value;
}
