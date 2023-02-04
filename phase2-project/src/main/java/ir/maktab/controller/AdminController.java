package ir.maktab.controller;

import ir.maktab.data.dto.MainServiceDto;
import ir.maktab.data.dto.SubServiceDto;
import ir.maktab.data.model.Expert;
import ir.maktab.data.model.MainService;
import ir.maktab.data.model.SubServices;
import ir.maktab.service.AdminService;
import ir.maktab.service.ExpertService;
import ir.maktab.service.MainServicesService;
import ir.maktab.service.SubServicesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final ExpertService expertService;
    private final MainServicesService mainServicesService;
    private final SubServicesService subServicesService;
    private final ModelMapper modelMapper;

    @PostMapping("/add_service")
    public ResponseEntity<String> addService(@RequestBody MainServiceDto mainServiceDto) {
        MainService mainService = modelMapper.map(mainServiceDto, MainService.class);
        adminService.addServices(mainService);
        return ResponseEntity.ok().body("This service is added");
    }

    @Transactional
    @PostMapping("/add_sub_service")
    public ResponseEntity<String> addSubService(@Valid @RequestBody SubServiceDto subServiceDto,
                                                @RequestParam(value = "id") Long id) {
        SubServices subServices = modelMapper.map(subServiceDto, SubServices.class);
        MainService mainService = mainServicesService.findById(id);
        adminService.addSubServiceToService(subServices, mainService);
        return ResponseEntity.ok().body("This sub service is added" + " " + mainService.getName());
    }

    @Transactional
    @PostMapping("/add_expert_to_sub_service")
    public ResponseEntity<String> addExpertToSubService(@RequestParam(value = "idSub") Long idSub,
                                                        @RequestParam(value = "id") Long id) {
        SubServices subServices = subServicesService.findById(idSub);
        Expert expert = expertService.getExpertById(id);
        adminService.addExpertToSubService(expert, subServices);
        return ResponseEntity.ok().body(expert.getFirstName() + " " + "added" + " " +subServices.getSubName());
    }
}
