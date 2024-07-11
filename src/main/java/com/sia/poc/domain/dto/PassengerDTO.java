package com.sia.poc.domain.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.sia.poc.util.jackson.JsonViews;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PassengerDTO {

    @JsonView(JsonViews.Read.class)
    private String uid;


}
