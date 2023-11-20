package com.gym.domain.mapper;

import com.gym.domain.dto.ClientDTO;
import com.gym.domain.dto.ClientRequest;
import com.gym.domain.dto.PlanDTO;
import com.gym.domain.entity.Client;
import com.gym.domain.entity.Plan;

import java.time.LocalDate;

public class ClientConverter {

    public static ClientDTO toDTO (Client client){
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(client.getId());
        clientDTO.setName(client.getName());
        clientDTO.setEmail(client.getEmail());
        clientDTO.setDateOfBirth(client.getDateOfBirth());
        clientDTO.setDateJoined(client.getDateJoined());
        clientDTO.setWeight(client.getWeight());
        clientDTO.setHeight(client.getHeight());
        clientDTO.setPlanDTO(PlanConverter.toDTO(client.getPlan()));
        return clientDTO;
    }

    public static Client toEntity(ClientRequest clientRequest, Plan plan){
        Client client = new Client();
        client.setName(clientRequest.getName());
        client.setEmail(clientRequest.getEmail());
        client.setDateOfBirth(LocalDate.parse(clientRequest.getDateOfBirth()));
        client.setDateJoined(LocalDate.parse(clientRequest.getDateJoined()));
        client.setWeight(clientRequest.getWeight());
        client.setHeight(clientRequest.getHeight());
        client.setPlan(plan);
        return client;
    }

    public static Client fromDTOtoEntity(ClientDTO clientDTO){
        Client client = new Client();
        client.setId(clientDTO.getId());
        client.setName(clientDTO.getName());
        client.setEmail(clientDTO.getEmail());
        client.setDateOfBirth(clientDTO.getDateOfBirth());
        client.setDateJoined(clientDTO.getDateJoined());
        client.setWeight(clientDTO.getWeight());
        client.setHeight(clientDTO.getHeight());
        client.setPlan(PlanConverter.fromDTOtoEntity(clientDTO.getPlanDTO()));
        return client;
    }
}
