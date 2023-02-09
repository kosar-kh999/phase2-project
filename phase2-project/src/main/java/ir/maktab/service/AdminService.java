package ir.maktab.service;

import ir.maktab.data.enums.ActiveExpert;
import ir.maktab.data.enums.ExpertStatus;
import ir.maktab.data.model.Expert;
import ir.maktab.data.model.MainService;
import ir.maktab.data.model.SubServices;
import ir.maktab.util.exception.StatusException;
import ir.maktab.util.exception.SubServicesException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final MainServicesService mainServicesService;
    private final SubServicesService subServicesService;
    private final ExpertService expertService;

    public void addServices(MainService mainService) {
        MainService service = mainServicesService.getByName(mainService);
        mainServicesService.addServices(service);
    }

    @Transactional
    public void addSubServiceToService(SubServices subServices, Long id) {
        MainService mainService = mainServicesService.findById(id);
        SubServices services = subServicesService.getByName(subServices);
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

    public boolean deleteExpertFromSubServices(Long expertId, Long subId) {
        Expert expert = expertService.getExpertById(expertId);
        SubServices subServices = subServicesService.findById(subId);
        boolean flag = true;
        if (!(expert.getSubServices().contains(subServices)))
            return false;
        expert.getSubServices().remove(subServices);
        expertService.update(expert);
        return flag;
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
