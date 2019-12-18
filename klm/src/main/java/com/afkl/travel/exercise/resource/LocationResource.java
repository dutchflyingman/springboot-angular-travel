package com.afkl.travel.exercise.resource;

import com.afkl.travel.exercise.model.LocationType;
import com.afkl.travel.exercise.service.LocationService;
import com.afkl.travel.exercise.service.transferobjects.LocationTransferObject;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping(value = {"locations",""})
public class LocationResource {

    private static final Logger LOGGER= LoggerFactory.getLogger(LocationResource.class);

    private final LocationService locationService;

    public LocationResource(LocationService locationService) {
        this.locationService = locationService;
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LocationTransferObject>> getAllLocations(@RequestHeader("accept-language") String language,Authentication authentication){
        UUID uuid = UUID.randomUUID();
        List<LocationTransferObject> locationTransferObjectList;
        try {
            locationTransferObjectList = locationService.getAllLocations(language);
            LOGGER.info("User {}- Search Process #ID {} For All locations returned with {} records",authentication.getName(), uuid,locationTransferObjectList.size());
        } catch (Exception e) {
            LOGGER.error("Exception: {}", e.getMessage());
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(locationTransferObjectList);
    }

    @GetMapping(value = "{type}/{code}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LocationTransferObject> getLocationByTypeAndCode(@RequestHeader("accept-language") String language, @PathVariable("type") LocationType locationType, @PathVariable String code) {
        UUID uuid = UUID.randomUUID();
        LOGGER.info("Search Process #ID {} for Locations By Type : {} and Code : {}",uuid,locationType,code);
        try {
            final Optional<LocationTransferObject> locationSearchResultByTypeAndCode = locationService.getAllLocationsByTypeAndCode(language, locationType, code);
            LOGGER.info("Search Process #ID {} returned Successfully? :{} ", uuid, locationSearchResultByTypeAndCode.isPresent());
            LOGGER.debug("Search Process #ID {} returns with location {}",uuid,locationSearchResultByTypeAndCode.orElse(null));
            return locationSearchResultByTypeAndCode.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception exception){
            LOGGER.error("Error while retrieving records due to {} ",exception.getMessage());
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value = "airports/{code}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LocationTransferObject>> getAirportsByCountry(@RequestHeader("accept-language") String language, @PathVariable String code) {
        UUID uuid = UUID.randomUUID();
       log.info("Process #ID {} executing Search  Airports By country  Code : {}  ",uuid,code);
        try {
            final List<LocationTransferObject> airportsList = locationService.getAllAiportsByCountry(Optional.ofNullable(language),code);
            log.info("Process #ID {} Search For Airport  returned Successfully? :{} with {} airports ", uuid, !airportsList.isEmpty(),airportsList.size());
            return airportsList.isEmpty()? ResponseEntity.notFound().build() : ResponseEntity.ok(airportsList) ;
        } catch (Exception exception) {
            log.error("Process #ID {} has encountered error while retrieving airports due to {}  ",uuid, exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
