package com.afkl.travel.exercise.test;

import com.afkl.travel.exercise.model.Location;
import com.afkl.travel.exercise.model.LocationType;
import com.afkl.travel.exercise.model.Translation;
import com.afkl.travel.exercise.service.transferobjects.LocationTransferObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestDataUtil {


    public TestDataUtil()  {
    }

    public static Location createLocationObject(String code, Long id,boolean isParentRequired) {
        Location location=new Location();
        location.setCode(code);
        location.setId(id);
        location.setLatitude(BigDecimal.valueOf(123.12));
        location.setLongitude(BigDecimal.valueOf(123.12));
        if(isParentRequired)
            location.setParent(createLocationObject("NL",123L,false));
        location.setTranslation(createTranslationList(location,"EN"));
        location.setType(LocationType.city);
        return location;
    }

    public static List<Translation> createTranslationList(Location location, String language) {
        Translation translation= new Translation();
        translation.setLocation(location);
        translation.setDescription("Amsterdam");
        translation.setLanguage(language);
        translation.setName("Amsterdam");
        return Collections.singletonList(translation);
    }

    public static List<Location> getLocationList() {
         List<Location> locationList=new ArrayList<>();
        locationList.add(createLocationObject("NL",123L,false));
        locationList.add(createLocationObject("AMS", 100L,true));
        return  locationList;
    }

    public static List<LocationTransferObject> getLocationTransferObjectList() {
        List<LocationTransferObject> locationTransferObjects= new ArrayList<>();
        Location location=createLocationObject("us",101L,false);
        locationTransferObjects.add(createTo(location.getCode(),location.getType(),location.getLatitude(),location.getLongitude(),"US","USA",null,null
        ));
        location=createLocationObject("ams",100L,true);
        locationTransferObjects.add(createTo(location.getCode(),location.getType(),location.getLatitude(),location.getLongitude(),"NL","Netherlands",location.getParent().getCode()
                ,location.getParent().getType()));
        return locationTransferObjects;
    }

    public static LocationTransferObject createTo(String code, LocationType type, BigDecimal latitude, BigDecimal longitude, String name,
                                                  String description, String parentLocationCode, LocationType parentLocType){
        return LocationTransferObject.builder().code(code)
                .latitude(latitude).longitude(longitude).type(type)
                .parentLocationCode(parentLocationCode)
                .parentLocationType(parentLocType)
                .name(name)
                .description(description)
                .build();
    }
}
