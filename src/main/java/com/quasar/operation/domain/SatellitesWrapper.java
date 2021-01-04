package com.quasar.operation.domain;

import com.quasar.operation.utils.annotations.ValidSatellitesList;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SatellitesWrapper {

    @Valid
    @NotNull(message = "Satellites list can't be null. ")
    @ValidSatellitesList
    @ApiModelProperty(example = "[" +
            "        {" +
            "            \"name\": \"kenobi\"," +
            "            \"distance\": 100," +
            "            \"message\": [\"this\",\"\",\"\",\"secret\",\"\"]" +
            "        }," +
            "         {" +
            "            \"name\": \"skywalker\"," +
            "            \"distance\": 115.5," +
            "            \"message\": [\"\",\"is\",\"\",\"\",\"message\"]" +
            "        }," +
            "        {" +
            "            \"name\":\"sato\"," +
            "            \"distance\": 142.7," +
            "            \"message\": [\"this\",\"\",\"a\",\"\",\"\"]" +
            "        }" +
            "    ]")
    List<SatelliteInput> satellites;
}
