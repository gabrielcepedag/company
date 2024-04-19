package com.example.clients.dto.request;

import com.example.clients.entity.Address;
import com.example.clients.entity.Company;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Data

public class ClientRequest {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private List<AddressRequest> addresses;
    private Long company;
}
