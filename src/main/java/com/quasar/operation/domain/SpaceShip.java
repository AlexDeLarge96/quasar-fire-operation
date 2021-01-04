package com.quasar.operation.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpaceShip {

    private Position position;
    @ApiModelProperty(example = "this is a secret message")
    private String message;
}
