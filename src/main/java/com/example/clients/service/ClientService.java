package com.example.clients.service;

import com.example.clients.dto.request.ClientRequest;
import com.example.clients.dto.request.CompanyRequest;
import com.example.clients.entity.Address;
import com.example.clients.entity.Client;
import com.example.clients.entity.Company;
import com.example.clients.repository.ClientRepository;
import com.example.clients.repository.CompanyRepository;
import com.example.clients.utils.response.ApiResponse;
import com.example.clients.utils.response.CustResponseBuilder;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ClientService {
    private final ClientRepository clientRepository;
    private ModelMapper modelMapper;
    private CustResponseBuilder custResponseBuilder;

    public ClientService(ClientRepository clientRepository, ModelMapper modelMapper, CustResponseBuilder custResponseBuilder) {
        this.clientRepository = clientRepository;
        this.modelMapper = modelMapper;
        this.custResponseBuilder = custResponseBuilder;
    }

    public ResponseEntity<ApiResponse> findAll(){
        List<Client> clients = clientRepository.findAll();
        return custResponseBuilder.buildResponse(HttpStatus.OK.value(), clients);
    }
    public ResponseEntity<ApiResponse> createClient(ClientRequest request){
        ResponseEntity<ApiResponse> response;
        Client c1 = new Client();

        try {
            List<Address> addresses = Arrays.asList(modelMapper.map(request.getAddresses(), Address[].class));
            modelMapper.map(request, c1);
            c1.setAddresses(addresses);
            c1 = clientRepository.save(c1);
            response = custResponseBuilder.buildResponse(HttpStatus.OK.value(), c1);
        }catch (Exception e){
            response = custResponseBuilder.buildResponse(HttpStatus.BAD_REQUEST.value(), "Error creating Client", e.getMessage());
        }
        return response;
    }

    public ResponseEntity<ApiResponse> deleteClient(Long id){
        ResponseEntity<ApiResponse> response;
        Client c1 = findClientById(id);;
        try {
            clientRepository.delete(c1);
            response = custResponseBuilder.buildResponse(HttpStatus.OK.value(), "Client Deleted!", c1);
        }catch (Exception e){
            response = custResponseBuilder.buildResponse(HttpStatus.BAD_REQUEST.value(), "Error deleting Client", e.getMessage());
        }
        return response;
    }

    private Client findClientById(Long id) {
        return clientRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Client not found with id: "+id));
    }
}
