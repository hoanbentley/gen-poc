package com.sia.poc.web.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sia.poc.IntegrationTest;
import com.sia.poc.domain.dto.PassengerDTO;
import com.sia.poc.domain.entity.Passenger;
import com.sia.poc.repository.PassengerRepository;

import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.MySQLContainer;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.test.context.SpringBootTest;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import org.junit.jupiter.api.Test;

import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

//@IntegrationTest
@Transactional
@AutoConfigureMockMvc
@Testcontainers
@Slf4j
@SpringBootTest
public class PassengerControllerIT {

    private static final String ENTITY_API_URL = "/api/passengers";



    @Container
    public static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0.33")
            .withUsername("dbuser")
            .withPassword("dbpassword")
            .withReuse(true);

    static {
        String dockerHost = System.getenv("DOCKER_HOST");
        if(dockerHost != null) {
            log.info("Setting Docker host to {}", dockerHost);
            System.setProperty("docker.host", dockerHost);
            //DockerClientConfigUtils.getDefaultDockerClientConfig();

            //DockerClientFactory.instance().client().infoCmd().exec();
            //System.setProperty("testcontainers.docker.host", dockerHost);
        }
    }
    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private MockMvc restPassengerMockMvc;

    public static Passenger getMockPassenger() {
        return new Passenger();
    }

    public static PassengerDTO getMockPassengerDTO() {
        return new PassengerDTO();
    }

    @Test
    void getPassenger() throws Exception {
        // Initialize the database
        Passenger passenger = getMockPassenger();
        passenger.setUid(NanoIdUtils.randomNanoId());
        passengerRepository.saveAndFlush(passenger);
        int databaseSizeBeforeGet = passengerRepository.findAll().size();

        // Get the Passenger
        restPassengerMockMvc
                .perform(get(ENTITY_API_URL + "/{id}", passenger.getUid()))
                .andExpect(status().isOk());

        // Validate the Passenger in the database
        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeGet);
        Passenger testPassenger = passengerList.get(passengerList.size() - 1);
        assertNotNull(testPassenger.getId());
        assertNotNull(testPassenger.getUid());
    }

    @Test
    void createPassenger() throws Exception {
        int databaseSizeBeforeCreate = passengerRepository.findAll().size();
        PassengerDTO passengerDTO = getMockPassengerDTO();

        // Create the Passenger
        restPassengerMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(passengerDTO)))
                .andExpect(status().isCreated());

        // Validate the Passenger in the database
        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeCreate + 1);
        Passenger testPassenger = passengerList.get(passengerList.size() - 1);
        assertNotNull(testPassenger.getId());
        assertNotNull(testPassenger.getUid());
    }

    @Test
    void updatePassenger() throws Exception {
        // Initialize the database
        Passenger passenger = getMockPassenger();
        passenger.setUid(NanoIdUtils.randomNanoId());
        passengerRepository.saveAndFlush(passenger);

        int databaseSizeBeforeUpdate = passengerRepository.findAll().size();

        Passenger retrievedPassenger = passengerRepository.findById(passenger.getId()).orElseThrow();
        PassengerDTO passengerDTO = new PassengerDTO();
        // TODO: Please add code to update PassengerDTO

        // Update the Passenger
        restPassengerMockMvc
                .perform(
                        put(ENTITY_API_URL + "/{uid}", retrievedPassenger.getUid())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(passengerDTO)))
                .andExpect(status().isOk());

        // Validate the Passenger in the database
        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeUpdate);
        Passenger testPassenger = passengerList.get(passengerList.size() - 1);
        assertNotNull(testPassenger.getId());
        assertNotNull(testPassenger.getUid());
    }


    @Test
    void deletePassenger() throws Exception {
        // Initialize the database
        Passenger passenger = getMockPassenger();
        passenger.setUid(NanoIdUtils.randomNanoId());
        passengerRepository.saveAndFlush(passenger);
        int databaseSizeBeforeDelete = passengerRepository.findAll().size();

        // Delete the Passenger
        restPassengerMockMvc
            .perform(
                delete(ENTITY_API_URL + "/{uid}", passenger.getUid()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeDelete - 1);
    }

}
