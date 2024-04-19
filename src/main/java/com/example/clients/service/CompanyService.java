package com.example.clients.service;

import com.example.clients.dto.request.CompanyRequest;
import com.example.clients.entity.Company;
import com.example.clients.repository.CompanyRepository;
import com.example.clients.utils.response.ApiResponse;
import com.example.clients.utils.response.CustResponseBuilder;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
        return custResponseBuilder.buildResponse(HttpStatus.OK.value(), companies);
    }
    public ResponseEntity<ApiResponse> createCompany(CompanyRequest request){
        ResponseEntity<ApiResponse> response;
        Company c1 = new Company();
        try {
            modelMapper.map(request, c1);
            c1 = companyRepository.save(c1);
            response = custResponseBuilder.buildResponse(HttpStatus.OK.value(), c1);
        }catch (Exception e){
            response = custResponseBuilder.buildResponse(HttpStatus.BAD_REQUEST.value(), "Error creating Company", e.getMessage());
        }
        return response;
    }

    public ResponseEntity<ApiResponse> deleteCompany(Long id){
        ResponseEntity<ApiResponse> response;
        Company c1 = findCompanyById(id);;
        try {
            companyRepository.delete(c1);
            response = custResponseBuilder.buildResponse(HttpStatus.OK.value(), "Company Deleted!", c1);
        }catch (Exception e){
            response = custResponseBuilder.buildResponse(HttpStatus.BAD_REQUEST.value(), "Error deleting Company", e.getMessage());
        }
        return response;
    }

    private Company findCompanyById(Long id) {
        return companyRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Company not found with id: "+id));
    }
}
