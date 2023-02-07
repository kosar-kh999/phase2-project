package ir.maktab.controller;

import ir.maktab.data.dto.ExpertSignInDto;
import ir.maktab.data.dto.MainServiceDto;
import ir.maktab.data.dto.SubServiceDto;
import ir.maktab.data.model.Expert;
import ir.maktab.data.model.MainService;
import ir.maktab.data.model.SubServices;
import ir.maktab.service.AdminService;
import ir.maktab.service.SubServicesService;
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
    private final ModelMapper modelMapper;

    @PostMapping("/add_service")
    public ResponseEntity<String> addService(@RequestBody MainServiceDto mainServiceDto) {
        MainService mainService = modelMapper.map(mainServiceDto, MainService.class);
        adminService.addServices(mainService);
        return ResponseEntity.ok().body(mainService.getName() + " " + " is added");
    }

    @Transactional
    @PostMapping("/add_sub_service")
    public ResponseEntity<String> addSubService(@RequestBody SubServiceDto subServiceDto,
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
                                              @RequestParam(value = "name") String name) {
        subServicesService.updatePrice(price, name);
        return ResponseEntity.ok().body("the price has been update");
    }

    @PutMapping("/update_brief")
    public ResponseEntity<String> updateBrief(@RequestParam(value = "brief") String brief,
                                              @RequestParam(value = "name") String name) {
        subServicesService.updateBrief(brief, name);
        return ResponseEntity.ok().body("the brief has been update");
    }
}
