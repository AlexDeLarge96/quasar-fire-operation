package com.quasar.operation.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Position {
    @ApiModelProperty(example = "-58.315252587138595")
    private double x;
    @ApiModelProperty(example = "-69.55141837312165")
    private double y;

    public double[] toArray() {
        return new double[]{x, y};
    }
}
