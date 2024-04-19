package com.example.clients.controller.v1;

import com.example.clients.dto.request.CompanyRequest;
import com.example.clients.service.CompanyService;
import com.example.clients.utils.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/v1/companies")
public class CompanyController {
    private CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("")
    private ResponseEntity<ApiResponse> listAll(){
        return companyService.findAll();
    }
    @GetMapping("/{id}")
    private ResponseEntity<ApiResponse> getOneById(@PathVariable("id") Long id){
        return companyService.getOneById(id);
    }
    @PostMapping("")
    private ResponseEntity<ApiResponse> createCompany(@RequestBody @Valid CompanyRequest companyRequest){
        return companyService.createCompany(companyRequest);
    }
    @DeleteMapping("")
    private ResponseEntity<ApiResponse> deleteCompany(@RequestParam("id") Long id){
        return companyService.deleteCompany(id);
    }
    @PutMapping("/{id}")
    private ResponseEntity<ApiResponse> updateCompany(@PathVariable("id") Long id, @RequestBody @Valid CompanyRequest request){
        return companyService.updateCompany(id, request);
    }
}
