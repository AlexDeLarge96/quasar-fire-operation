package com.quasar.operation.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomResponse {

    @ApiModelProperty(example = "Stored satellites deleted.")
    private String message;
}
