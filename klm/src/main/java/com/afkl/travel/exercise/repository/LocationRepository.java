package com.afkl.travel.exercise.repository;

import com.afkl.travel.exercise.model.Location;
import com.afkl.travel.exercise.model.LocationType;
import com.afkl.travel.exercise.service.transferobjects.LocationTransferObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {


    //TODO: Discuss in review to see if pagination is required
    //Note Get all translations at once for the language, Get all Locations and then combine two list to form one. Performance can be better. Enable Cache may be
    @SuppressWarnings("SpringDataRepositoryMethodReturnTypeInspection")
    @Query(value = "select new com.afkl.travel.exercise.service.transferobjects.LocationTransferObject(location.code , location.type , location.latitude, location.longitude, translation.name  ,translation.description, parent.code, parent.type) " +
            "from Location location left outer join location.translation translation left join location.parent parent " +
            "where upper(translation.language)=upper(:language)")
    List<LocationTransferObject> findAllByTranslationLanguage(@Param("language") String language);

    Optional<Location> findByTypeAndCodeIgnoreCaseAndTranslationLanguageIgnoreCase(LocationType type, String code, String language);

    List<Location> findByTypeAndParentParentCodeIgnoreCase(LocationType type, String code);

    List<Location> findByTypeAndTranslationLanguageIgnoreCaseAndParentParentCodeIgnoreCase(LocationType type,String language, String code);




}
