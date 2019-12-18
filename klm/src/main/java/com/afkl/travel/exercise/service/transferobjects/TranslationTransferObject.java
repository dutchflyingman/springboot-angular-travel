package com.afkl.travel.exercise.service.transferobjects;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TranslationTransferObject {

    String language;

    String name;

    String description;
}
