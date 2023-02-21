package ir.maktab.controller;

import ir.maktab.data.dto.*;
import ir.maktab.data.model.*;
import ir.maktab.service.AdminService;
import ir.maktab.service.SubServicesService;
import ir.maktab.service.SuggestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final SubServicesService subServicesService;
    private final SuggestionService suggestionService;
    private final ModelMapper modelMapper;

    @PostMapping("/add_admin")
    public ResponseEntity<String> addAdmin(@RequestBody AdminDto adminDto) {
        Admin admin = modelMapper.map(adminDto, Admin.class);
        adminService.saveAdmin(admin);
        return ResponseEntity.ok().body(admin.getUsername() + " " + "sign up successfully");
    }

    @PostMapping("/add_service")
    public ResponseEntity<String> addService(@Valid @RequestBody MainServiceDto mainServiceDto) {
        MainService mainService = modelMapper.map(mainServiceDto, MainService.class);
        adminService.addServices(mainService);
        return ResponseEntity.ok().body(mainService.getName() + " " + " is added");
    }

    @Transactional
    @PostMapping("/add_sub_service")
    public ResponseEntity<String> addSubService(@Valid @RequestBody SubServiceDto subServiceDto,
                                                @RequestParam(value = "id") Long id) {
        SubServices subServices = modelMapper.map(subServiceDto, SubServices.class);
        adminService.addSubServiceToService(subServices, id);
        return ResponseEntity.ok().body(subServices.getSubName() + " " + " is added");
    }

    @Transactional
    @PostMapping("/add_expert_to_sub_service")
    public ResponseEntity<String> addExpertToSubService(@RequestParam(value = "idSub") Long idSub,
                                                        @RequestParam(value = "id") Long id) {
        Expert expert = adminService.addExpertToSubService(id, idSub);
        return ResponseEntity.ok().body(expert.getFirstName() + " " + "added to sub service");
    }

    @DeleteMapping("/delete_expert_from_sub_service")
    public ResponseEntity<String> deleteExert(@RequestParam(value = "idSub") Long idSub,
                                              @RequestParam(value = "id") Long id) {
        adminService.deleteExpertFromSubServices(id, idSub);
        return ResponseEntity.ok().body("expert removed from sub service");
    }

    @GetMapping("/get_main_services")
    public ResponseEntity<List<MainServiceDto>> gatAllServices() {
        return ResponseEntity.ok().body(adminService.showAllServices().stream().map(mainService -> modelMapper.
                map(mainService, MainServiceDto.class)).collect(Collectors.toList()));
    }

    @PutMapping("/edit_to_confirmed")
    public ResponseEntity<ExpertSignInDto> editToConfirmed(@RequestParam(value = "email") String email) {
        Expert expert = adminService.editStatus(email);
        ExpertSignInDto dto = modelMapper.map(expert, ExpertSignInDto.class);
        return ResponseEntity.ok().body(dto);
    }

    @PutMapping("/update_price")
    public ResponseEntity<String> updatePrice(@RequestParam(value = "price") double price,
                                              @RequestParam(value = "id") Long id) {
        subServicesService.updatePrice(price, id);
        return ResponseEntity.ok().body("The price has been update");
    }

    @PutMapping("/update_brief")
    public ResponseEntity<String> updateBrief(@RequestParam(value = "brief") String brief,
                                              @RequestParam(value = "id") Long id) {
        subServicesService.updateBrief(brief, id);
        return ResponseEntity.ok().body("The brief has been update");
    }

    @PutMapping("/withdraw_credit")
    public ResponseEntity<CustomerDto> withdraw(@RequestParam(value = "idCustomer") Long idCustomer,
                                                @RequestParam(value = "idSuggestion") Long idSuggestion) {
        Customer customer = suggestionService.withdraw(idCustomer, idSuggestion);
        CustomerDto dto = modelMapper.map(customer, CustomerDto.class);
        return ResponseEntity.ok().body(dto);
    }

    @PutMapping("/deposit_credit")
    public ResponseEntity<ExpertSignInDto> deposit(@RequestParam(value = "idExpert") Long idExpert,
                                                   @RequestParam(value = "idSuggestion") Long idSuggestion) {
        Expert expert = suggestionService.deposit(idExpert, idSuggestion);
        ExpertSignInDto dto = modelMapper.map(expert, ExpertSignInDto.class);
        return ResponseEntity.ok().body(dto);
    }
}
