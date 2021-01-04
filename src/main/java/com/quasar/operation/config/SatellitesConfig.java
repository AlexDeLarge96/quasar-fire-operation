package com.quasar.operation.config;

import com.quasar.operation.domain.Satellite;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties("quasar")
public class SatellitesConfig {

    private List<Satellite> satellites;
}
