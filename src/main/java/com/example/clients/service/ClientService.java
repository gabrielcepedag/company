package com.example.clients.service;

import com.example.clients.dto.request.ClientRequest;
import com.example.clients.dto.request.CompanyRequest;
import com.example.clients.dto.response.ClientResponse;
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
    private final CompanyService companyService;
    private ModelMapper modelMapper;
    private CustResponseBuilder custResponseBuilder;

    public ClientService(ClientRepository clientRepository, ModelMapper modelMapper, CustResponseBuilder custResponseBuilder, CompanyService companyService) {
        this.clientRepository = clientRepository;
        this.modelMapper = modelMapper;
        this.custResponseBuilder = custResponseBuilder;
        this.companyService = companyService;
    }

    public ResponseEntity<ApiResponse> findAll(){
        List<Client> clients = clientRepository.findAll();
        List<ClientResponse> clientResponses = Arrays.asList(modelMapper.map(clients, ClientResponse[].class));

        return custResponseBuilder.buildResponse(HttpStatus.OK.value(), clientResponses);
    }
    public ResponseEntity<ApiResponse> createClient(ClientRequest request){
        ResponseEntity<ApiResponse> response;
        final Client c1 = new Client();
        try {
            Company company = companyService.findCompanyById(request.getCompany());
            List<Address> addresses = Arrays.asList(modelMapper.map(request.getAddresses(), Address[].class));
            addresses.forEach(address -> address.setClient(c1));
            modelMapper.map(request, c1);
            c1.setAddresses(addresses);
            c1.setCompany(company);
            Client responseClient = clientRepository.save(c1);
            ClientResponse clientResponse = modelMapper.map(responseClient, ClientResponse.class);

            response = custResponseBuilder.buildResponse(HttpStatus.OK.value(), clientResponse);
        }catch (EntityNotFoundException exception){
            response = custResponseBuilder.buildResponse(HttpStatus.NOT_FOUND.value(), "Company Not Found With ID: "+request.getCompany());
        }
        catch (Exception e){
            response = custResponseBuilder.buildResponse(HttpStatus.BAD_REQUEST.value(), "Error creating Client", e.getMessage());
        }
        return response;
    }

    public ResponseEntity<ApiResponse> deleteClient(Long id){
        ResponseEntity<ApiResponse> response;
        try {
            Client c1 = findClientById(id);;
            clientRepository.delete(c1);
            response = custResponseBuilder.buildResponse(HttpStatus.OK.value(), "Client Deleted!");
        }catch (EntityNotFoundException exception){
            response = custResponseBuilder.buildResponse(HttpStatus.NOT_FOUND.value(), "Client Not Found With ID: "+id);
        }catch (Exception e){
            response = custResponseBuilder.buildResponse(HttpStatus.BAD_REQUEST.value(), "Error deleting Client", e.getMessage());
        }
        return response;
    }

    private Client findClientById(Long id) {
        return clientRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Client not found with id: "+id));
    }
}
