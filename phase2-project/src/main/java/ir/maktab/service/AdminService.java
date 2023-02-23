package ir.maktab.service;

import ir.maktab.data.enums.ActiveExpert;
import ir.maktab.data.enums.ExpertStatus;
import ir.maktab.data.enums.Role;
import ir.maktab.data.model.Admin;
import ir.maktab.data.model.Expert;
import ir.maktab.data.model.MainService;
import ir.maktab.data.model.SubServices;
import ir.maktab.data.repository.AdminRepository;
import ir.maktab.util.exception.NotFound;
import ir.maktab.util.exception.StatusException;
import ir.maktab.util.exception.SubServicesException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {

    private final MainServicesService mainServicesService;
    private final SubServicesService subServicesService;
    private final ExpertService expertService;

    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void saveAdmin(Admin admin) {
        admin.setRole(Role.ROLE_ADMIN);
        admin.setPassword(bCryptPasswordEncoder.encode(admin.getPassword()));
        adminRepository.save(admin);
    }

    public Admin findByUsername(String username) {
        return adminRepository.findByUsername(username).orElseThrow(() -> new NotFound("This user is not found!"));
    }

    public void addServices(MainService mainService) {
        MainService service = mainServicesService.saveNewService(mainService);
        mainServicesService.addServices(service);
    }

    @Transactional
    public void addSubServiceToService(SubServices subServices, Long id) {
        MainService mainService = mainServicesService.findById(id);
        SubServices services = subServicesService.saveNewSubService(subServices);
        services.setMainService(mainService);
        subServicesService.saveSubService(services);
    }

    @Transactional
    public Expert addExpertToSubService(Long expertId, Long subId) {
        Expert expert = expertService.getExpertById(expertId);
        SubServices services = subServicesService.findById(subId);
        if (expert.getSubServices().contains(services))
            throw new SubServicesException("This sub service was added before");
        expert.getSubServices().add(services);
        expertService.update(expert);
        return expert;
    }

    public void deleteExpertFromSubServices(Long expertId, Long subId) {
        Expert expert = expertService.getExpertById(expertId);
        SubServices subServices = subServicesService.findById(subId);
        if (!(expert.getSubServices().contains(subServices)))
            throw new NotFound("This sub service not found for this expert");
        expert.getSubServices().remove(subServices);
        expertService.update(expert);
    }

    public List<MainService> showAllServices() {
        return mainServicesService.getAllServices();
    }

    public Expert editStatus(String email) {
        Expert expert = expertService.getExpertByEmail(email);
        if (!(expert.getExpertStatus().equals(ExpertStatus.NEW)))
            throw new StatusException("This user is not new");
        expert.setExpertStatus(ExpertStatus.CONFIRMED);
        expert.setActiveExpert(ActiveExpert.ACTIVE);
        return expertService.update(expert);
    }
}
