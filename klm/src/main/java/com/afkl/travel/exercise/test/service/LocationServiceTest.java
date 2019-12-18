package com.afkl.travel.exercise.test.service;

import com.afkl.travel.exercise.model.Location;
import com.afkl.travel.exercise.model.LocationType;
import com.afkl.travel.exercise.repository.LocationRepository;
import com.afkl.travel.exercise.service.LocationService;
import com.afkl.travel.exercise.service.LocationServiceImpl;
import com.afkl.travel.exercise.service.serviceHelper.LocationServiceHelper;
import com.afkl.travel.exercise.service.transferobjects.LocationTransferObject;
import com.afkl.travel.exercise.test.TestDataUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class LocationServiceTest {

    private LocationRepository locationRepository = Mockito.mock(LocationRepository.class);


    private LocationService locationService;
    private List<Location> locationList;
    private List<LocationTransferObject> transferObjectList= new ArrayList<>();
    @Before
    public void init()  {
        locationList= TestDataUtil.getLocationList();
        LocationServiceHelper locationServiceHelper = new LocationServiceHelper();
        locationService= new LocationServiceImpl(locationServiceHelper,locationRepository);
        transferObjectList= locationServiceHelper.marshalLocationModelToTransferObject(locationList);
    }

    @Test
    public void getLocationHappyFlow() throws Exception {
        when(locationRepository.findAllByTranslationLanguage(any())).thenReturn(transferObjectList);
        List<LocationTransferObject> resultList=locationService.getAllLocations(any());
        Assert.assertFalse(resultList.isEmpty());
    }

    @Test
    public void getLocationEmptyResultFlow() throws  Exception{
        List<LocationTransferObject> locationTransferObjects=Collections.EMPTY_LIST;
        when(locationRepository.findAllByTranslationLanguage(any())).thenReturn(locationTransferObjects);
        List<LocationTransferObject> resultList=locationService.getAllLocations(Locale.ENGLISH.getLanguage());
        Assert.assertTrue(resultList.isEmpty());
    }

    @Test
    public void getLocationByTypeAndCodeHappyFlow() throws Exception{

        when(locationRepository.findByTypeAndCodeIgnoreCaseAndTranslationLanguageIgnoreCase(any(),any(),any())).thenReturn(
                Optional.of(locationList.get(0)));
        Optional<LocationTransferObject> lcoationObject=locationService.getAllLocationsByTypeAndCode("EN", LocationType.city,"AMS");
        Assert.assertTrue(lcoationObject.isPresent());
    }

    @Test
    public void getLocationByTypeCodeUnhappyFlow() throws  Exception{
        when(locationRepository.findByTypeAndCodeIgnoreCaseAndTranslationLanguageIgnoreCase(any(),any(),any())).thenReturn(
                Optional.empty());
        Optional<LocationTransferObject> lcoationObject=locationService.getAllLocationsByTypeAndCode("EN", LocationType.city,"AMS");
        Assert.assertTrue(lcoationObject.isEmpty());
    }

    @Test
    public void getAirportsHappyFlow() throws  Exception{
        List<LocationTransferObject> locationTransferObjects=Collections.EMPTY_LIST;
        when(locationRepository.findByTypeAndParentParentCodeIgnoreCase(any(),any())).thenReturn(locationList);
        List<LocationTransferObject> resultList=locationService.getAllAiportsByCountry(Optional.of("EN"),"US");
        Assert.assertFalse(resultList.isEmpty());
    }


}
