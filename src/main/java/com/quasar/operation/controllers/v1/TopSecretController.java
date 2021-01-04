package com.quasar.operation.controllers.v1;

import com.quasar.operation.domain.CustomResponse;
import com.quasar.operation.domain.SatelliteInput;
import com.quasar.operation.domain.SatellitesWrapper;
import com.quasar.operation.domain.SpaceShip;
import com.quasar.operation.services.SatellitesStoredService;
import com.quasar.operation.services.SpaceShipService;
import com.quasar.operation.utils.annotations.ValidSatelliteName;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static com.quasar.operation.utils.QuasarUtils.base64Encode;
import static com.quasar.operation.utils.QuasarUtils.objectToJson;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Validated
@RestController
@RequestMapping(TopSecretController.ENDPOINT)
@Api(produces = APPLICATION_JSON_VALUE)
@SuppressWarnings("SpellCheckingInspection")
public class TopSecretController {

    static final String ENDPOINT = "/quasar-fire-operation/v1";
    private static final String SATELLITES = "satellites";
    private final SpaceShipService spaceShipService;
    private final SatellitesStoredService satellitesStoredService;

    @Autowired
    public TopSecretController(SpaceShipService spaceShipService, SatellitesStoredService satellitesStoredService) {
        this.spaceShipService = spaceShipService;
        this.satellitesStoredService = satellitesStoredService;
    }

    @ApiOperation("Obtain Spaceship using provided satellites distances and messages")
    @PostMapping("/topsecret")
    public ResponseEntity<SpaceShip> obtainSpaceShip(@Valid @RequestBody SatellitesWrapper satellites) {
        return ResponseEntity.ok(spaceShipService.getSpaceShip(satellites.getSatellites()));
    }

    @ApiOperation("Store Satellite information")
    @PostMapping("/topsecret_split/{satellite_name}")
    public ResponseEntity<SatelliteInput> storeSatelliteDistanceAndMessage(
            HttpServletResponse response,
            @ApiParam(name = "satellite_name", defaultValue = "kenobi", required = true)
            @PathVariable(value = "satellite_name") @ValidSatelliteName String satelliteName,
            @CookieValue(name = "satellites", required = false, defaultValue = "") String storedSatellites,
            @RequestBody SatelliteInput newSatellite) {
        newSatellite.setName(satelliteName);
        SatellitesWrapper newSatellites = satellitesStoredService.storeSatellite(storedSatellites, newSatellite);
        String newStoredSatellites = base64Encode(objectToJson(newSatellites));
        Cookie storedSatellitesCookie = new Cookie(SATELLITES, newStoredSatellites);
        response.addCookie(storedSatellitesCookie);
        return ResponseEntity.ok().body(newSatellite);
    }

    @ApiOperation("Get Spaceship using stored satellites distances and messages")
    @GetMapping("/topsecret_split")
    public ResponseEntity<SpaceShip> obtainSpaceShipUsingStoredInfo(
            @CookieValue(name = "satellites", required = false, defaultValue = "") String satellites) {
        SatellitesWrapper satellitesList = satellitesStoredService.getStoredSatellites(satellites);
        return ResponseEntity.ok(spaceShipService.getSpaceShip(satellitesList.getSatellites()));
    }

    @ApiOperation("Delete the satellites info cookie")
    @DeleteMapping("/topsecret_split/satellites")
    public ResponseEntity<CustomResponse> deleteStoredSatellitesInfo(HttpServletResponse response) {
        Cookie storedSatellitesCookie = new Cookie(SATELLITES, "");
        storedSatellitesCookie.setMaxAge(0);
        response.addCookie(storedSatellitesCookie);
        return ResponseEntity.ok(new CustomResponse("Stored satellites deleted."));
    }
}
