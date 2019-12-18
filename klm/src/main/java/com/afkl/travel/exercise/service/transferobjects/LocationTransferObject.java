package com.afkl.travel.exercise.service.transferobjects;

import com.afkl.travel.exercise.model.LocationType;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class LocationTransferObject {

     String code;

     LocationType type;

     BigDecimal latitude;

     BigDecimal longitude;

     String name;

     String description;

     String parentLocationCode;

     LocationType parentLocationType;



     public LocationTransferObject(String code, LocationType locationType, BigDecimal latitude, BigDecimal longitude,String name, String description, String parentCode, LocationType parentType) {
          this.code = code;
          this.type = locationType;
          this.latitude = latitude;
          this.longitude = longitude;
          this.name = name;
          this.description = description;
          this.parentLocationCode = parentCode;
          this.parentLocationType = parentType;
     }

}
