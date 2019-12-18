package com.afkl.travel.exercise.service;

import com.afkl.travel.exercise.model.LocationType;
import com.afkl.travel.exercise.service.transferobjects.LocationTransferObject;

import java.util.List;
import java.util.Optional;

public interface LocationService {
    List<LocationTransferObject> getAllLocations(String language) throws Exception;

    Optional<LocationTransferObject> getAllLocationsByTypeAndCode(String language, LocationType locationType, String code) throws Exception;

    List<LocationTransferObject> getAllAiportsByCountry(Optional<String> language,String countryCode) throws Exception;
}
