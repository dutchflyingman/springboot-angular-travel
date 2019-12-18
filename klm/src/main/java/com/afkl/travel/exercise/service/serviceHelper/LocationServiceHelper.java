package com.afkl.travel.exercise.service.serviceHelper;

import com.afkl.travel.exercise.model.Location;
import com.afkl.travel.exercise.model.Translation;
import com.afkl.travel.exercise.service.transferobjects.LocationTransferObject;
import com.afkl.travel.exercise.service.transferobjects.TranslationTransferObject;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class LocationServiceHelper {


    public List<LocationTransferObject> marshalLocationModelToTransferObject(List<Location> locationResultList) {
        return locationResultList.stream().filter(Objects::nonNull).map(location ->createLocationTransferObject(location,createTranslationDetailsForLocation(location.getTranslation())))
                .collect(Collectors.toList());

    }

    public TranslationTransferObject createTranslationDetailsForLocation(List<Translation> translationList) {
       return translationList.stream().filter(Objects::nonNull).map(translation -> TranslationTransferObject.builder().description(translation.getDescription())
                .language(translation.getLanguage())
                .name(translation.getName()).build()).findFirst().orElse(null);
    }


    public LocationTransferObject createLocationTransferObjectFromModel(Location locationObject) {
        return createLocationTransferObject(locationObject,createTranslationDetailsForLocation(locationObject.getTranslation()));
    }

    private LocationTransferObject createLocationTransferObject(Location location, TranslationTransferObject translationDetailsForLocation)
    {
        return  LocationTransferObject.builder().code(location.getCode())
                .latitude(location.getLatitude()).longitude(location.getLongitude()).type(location.getType())
                .parentLocationCode(location.getParent() !=null ? location.getParent().getCode() : null)
                .parentLocationType(location.getParent() != null ? location.getParent().getType(): null)
                .name(translationDetailsForLocation.getName())
                .description(translationDetailsForLocation.getDescription())
                .build();
    }
}
