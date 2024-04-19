package com.example.clients.service;

import com.example.clients.dto.request.CompanyRequest;
import com.example.clients.dto.response.CompanyResponse;
import com.example.clients.entity.Company;
import com.example.clients.repository.CompanyRepository;
import com.example.clients.utils.response.ApiResponse;
import com.example.clients.utils.response.CustResponseBuilder;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private ModelMapper modelMapper;
    private CustResponseBuilder custResponseBuilder;

    public CompanyService(CompanyRepository companyRepository, ModelMapper modelMapper, CustResponseBuilder custResponseBuilder) {
        this.companyRepository = companyRepository;
        this.modelMapper = modelMapper;
        this.custResponseBuilder = custResponseBuilder;
    }

    public ResponseEntity<ApiResponse> findAll(){
        List<Company> companies = companyRepository.findAll();
        List<CompanyResponse> companyResponses = Arrays.asList(modelMapper.map(companies, CompanyResponse[].class));
        return custResponseBuilder.buildResponse(HttpStatus.OK.value(), companyResponses);
    }
    public ResponseEntity<ApiResponse> createCompany(CompanyRequest request){
        ResponseEntity<ApiResponse> response;
        Company c1 = new Company();
        try {
            modelMapper.map(request, c1);
            c1 = companyRepository.save(c1);
            CompanyResponse companyResponse = modelMapper.map(c1, CompanyResponse.class);
            response = custResponseBuilder.buildResponse(HttpStatus.OK.value(), companyResponse);
        }catch (Exception e){
            response = custResponseBuilder.buildResponse(HttpStatus.BAD_REQUEST.value(), "Error creating Company", e.getMessage());
        }
        return response;
    }

    public ResponseEntity<ApiResponse> deleteCompany(Long id){
        ResponseEntity<ApiResponse> response;
        try {
            Company c1 = findCompanyById(id);;
            companyRepository.delete(c1);
            response = custResponseBuilder.buildResponse(HttpStatus.OK.value(), "Company Deleted!");
        }catch (EntityNotFoundException exception){
            response = custResponseBuilder.buildResponse(HttpStatus.NOT_FOUND.value(), "Company Not Found With ID: "+id);
        }
        catch (Exception e){
            response = custResponseBuilder.buildResponse(HttpStatus.BAD_REQUEST.value(), "Error deleting Company", e.getMessage());
        }
        return response;
    }

    public Company findCompanyById(Long id) {
        return companyRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Company not found with id: "+id));
    }
}
