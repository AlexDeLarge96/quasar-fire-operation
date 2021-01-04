package com.quasar.operation.domain;

import com.quasar.operation.utils.annotations.ValidSatelliteMessage;
import com.quasar.operation.utils.annotations.ValidSatelliteName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("SpellCheckingInspection")
public class SatelliteInput {

    @ApiModelProperty(example = "kenobi")
    @ValidSatelliteName
    @NotBlank(message = "Satellite name must not be null or blank. ")
    private String name;

    @ApiModelProperty(example = "100.0")
    @Min(value = 0, message = "The minimum distance value is zero. ")
    private double distance = -1;

    @ApiModelProperty(example = "[\"this\", \"\", \"\", \"secret\", \"\"]")
    @ValidSatelliteMessage
    private List<String> message;
}
