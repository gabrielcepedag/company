package com.example.clients.controller.v1;

import com.example.clients.dto.request.ClientRequest;
import com.example.clients.dto.request.CompanyRequest;
import com.example.clients.service.ClientService;
import com.example.clients.service.CompanyService;
import com.example.clients.utils.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("v1/clients")
public class ClientController {
    private ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("")
    private ResponseEntity<ApiResponse> listAll(){
        return clientService.findAll();
    }
    @PostMapping("")
    private ResponseEntity<ApiResponse> createClient(@RequestBody @Valid ClientRequest clientRequest){
        return clientService.createClient(clientRequest);
    }
    @DeleteMapping("")
    private ResponseEntity<ApiResponse> deleteClient(@RequestParam("id") Long id){
        return clientService.deleteClient(id);
    }

}
