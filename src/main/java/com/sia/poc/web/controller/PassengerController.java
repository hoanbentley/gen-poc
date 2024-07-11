package com.sia.poc.web.controller;

import java.net.URI;
import java.net.URISyntaxException;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.sia.poc.domain.dto.PassengerDTO;
import com.sia.poc.service.PassengerService;
import com.sia.poc.util.jackson.JsonViewPage;
import com.sia.poc.util.jackson.JsonViews;
import com.sia.poc.web.util.HeaderUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/passengers")
@Slf4j
public class PassengerController {

    private final PassengerService passengerService;

    @Value("${spring.application.name}")
    private String appName;
    private static final String ENTITY_NAME = "Passenger";

    @Autowired
    public PassengerController(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    @JsonView(JsonViews.Read.class)
    @GetMapping("/{uid}")
    public ResponseEntity<PassengerDTO> getPassengerByUid(@PathVariable("uid") String uid) {
        return passengerService
                .findPassengerByUid(uid)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @JsonView(JsonViews.Read.class)
    @GetMapping
    public JsonViewPage<PassengerDTO> getAllPassengers(
            @ParameterObject Pageable pageable) {
        return passengerService.findAllPassengers(pageable);
    }

    @JsonView(JsonViews.Read.class)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PassengerDTO> createPassenger(
            @JsonView(JsonViews.Create.class) @RequestBody @Validated PassengerDTO passengerDTO)
            throws URISyntaxException {
        PassengerDTO result = passengerService.savePassenger(passengerDTO);
        return ResponseEntity
                .created(new URI("/api/passengers" + result.getUid()))
                .headers(HeaderUtil.createEntityCreationAlert(appName, true, ENTITY_NAME,
                        result.getUid()))
                .body(result);
    }

    @JsonView(JsonViews.Read.class)
    @PutMapping("/{uid}")
    public ResponseEntity<PassengerDTO> updatePassenger(
            @PathVariable("uid") String uid, @JsonView(JsonViews.Update.class) @RequestBody PassengerDTO passengerDTO) {
        passengerDTO.setUid(uid);
        PassengerDTO result = passengerService.updatePassenger(passengerDTO);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(appName, true, ENTITY_NAME,
                        result.getUid()))
                .body(result);
    }

    @DeleteMapping("/{uid}")
    public ResponseEntity<PassengerDTO> deletePassenger(@PathVariable("uid") String uid) {
        passengerService.deletePassengerByUid(uid);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(appName, true, ENTITY_NAME,
                        uid))
                .build();
    }

}
