package com.afkl.travel.exercise.service;

import com.afkl.travel.exercise.model.Location;
import com.afkl.travel.exercise.model.LocationType;
import com.afkl.travel.exercise.repository.LocationRepository;
import com.afkl.travel.exercise.service.serviceHelper.LocationServiceHelper;
import com.afkl.travel.exercise.service.transferobjects.LocationTransferObject;
import com.afkl.travel.exercise.util.SupportedLocale;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class LocationServiceImpl implements LocationService {

    private final LocationServiceHelper locationServiceHelper;
    private final LocationRepository locationRepository;

    public LocationServiceImpl(LocationServiceHelper locationServiceHelper, LocationRepository locationRepository) {
        this.locationServiceHelper = locationServiceHelper;
        this.locationRepository = locationRepository;
    }

    @Override
    public List<LocationTransferObject> getAllLocations(String language)  {
        return locationRepository.findAllByTranslationLanguage(SupportedLocale.getLanguage(language));
    }

    @Override
    public Optional<LocationTransferObject> getAllLocationsByTypeAndCode(String language, LocationType locationType, String code) throws Exception {
        try {
            log.info("Inside Locations service ::::");
            Optional<Location> searchResult = (locationRepository.findByTypeAndCodeIgnoreCaseAndTranslationLanguageIgnoreCase(locationType, code, SupportedLocale.getLanguage(language)));
            log.debug("Search Result returned with values ?--> {} ",searchResult.isPresent());
            return searchResult.map(locationServiceHelper::createLocationTransferObjectFromModel);
        } catch (Exception exception) {
            log.error("Search Result service has an excetpion due to {}",exception.getMessage());
             throw new Exception("Unknown error occured while fetching locations by Type and Code due to ", exception);
        }
    }

    @Override
    public List<LocationTransferObject> getAllAiportsByCountry(Optional<String> language,String countryCode) throws Exception {
        try {
            List<Location> searchResult = (locationRepository.findByTypeAndParentParentCodeIgnoreCase(LocationType.airport, countryCode));
            List<Location> secondResult = (locationRepository.findByTypeAndTranslationLanguageIgnoreCaseAndParentParentCodeIgnoreCase(LocationType.airport, SupportedLocale.getLanguage( language.orElse("EN")), countryCode));
            return locationServiceHelper.marshalLocationModelToTransferObject(searchResult);
        } catch (Exception exception) {
            throw new Exception("Unknown error occured while fetching locations by Type and Code due to ", exception);
        }
    }
}
