package com.afkl.travel.exercise.test.resource;

import com.afkl.travel.exercise.model.Location;
import com.afkl.travel.exercise.resource.LocationResource;
import com.afkl.travel.exercise.service.LocationService;
import com.afkl.travel.exercise.service.transferobjects.LocationTransferObject;
import com.afkl.travel.exercise.test.TestDataUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class LocationResourceTest {

    private List<Location> locationList;

    private LocationService locationService=  Mockito.mock(LocationService.class);
    private List<LocationTransferObject> transferObjectList= new ArrayList<>();

    private  LocationResource locationResource;


    @Before
    public void init()  {
        transferObjectList=TestDataUtil.getLocationTransferObjectList();
        this.locationResource = new LocationResource(locationService);
    }

    @Test
    public void getAllLocationsHappyFlowTest() throws Exception {
        when(locationService.getAllLocations(any())).thenReturn(transferObjectList);
        ResponseEntity<List<LocationTransferObject>> responseEntity = locationResource.getAllLocations(any(),any());
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertFalse(responseEntity.getBody().isEmpty());
    }
    @Test
    public void getAllLocationsNoRecordsFoundTest() throws Exception {
        when(locationService.getAllLocations(any())).thenReturn(Collections.emptyList());
        ResponseEntity<List<LocationTransferObject>> responseEntity = locationResource.getAllLocations(any(),any());
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertTrue(responseEntity.getBody().isEmpty());
    }

    @Test
    public void getLocationByTypeCodeHappyFlow() throws Exception {
        when(locationService.getAllLocationsByTypeAndCode(any(),any(),any())).thenReturn(java.util.Optional.ofNullable(transferObjectList.get(0)));
        ResponseEntity responseEntity = locationResource.getLocationByTypeAndCode(any(),any(),any());
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void getLocationByTypeCode4xxUnHappyFlow() throws Exception {
        when(locationService.getAllLocationsByTypeAndCode(any(),any(),any())).thenReturn(Optional.empty());
        ResponseEntity responseEntity = locationResource.getLocationByTypeAndCode(any(),any(),any());
        Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void getLocationServiceThrowsException() throws Exception
    {
        when(locationService.getAllLocationsByTypeAndCode(any(),any(),any())).thenThrow(Exception.class);
        ResponseEntity responseEntity = locationResource.getLocationByTypeAndCode(any(),any(),any());
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        when(locationService.getAllAiportsByCountry(any(),any())).thenThrow(Exception.class);
        responseEntity=locationResource.getAirportsByCountry("us","US");
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void getAirportHappyFlow() throws Exception {
        when(locationService.getAllAiportsByCountry(any(),any())).thenReturn(transferObjectList);
        ResponseEntity responseEntity = locationResource.getAirportsByCountry("EN","US");
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        responseEntity = locationResource.getAirportsByCountry(null,"US");
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());


    }
}
