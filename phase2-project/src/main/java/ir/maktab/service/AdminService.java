package ir.maktab.service;

import ir.maktab.data.enums.ExpertStatus;
import ir.maktab.data.model.Expert;
import ir.maktab.data.model.Services;
import ir.maktab.data.model.SubServices;
import ir.maktab.util.exception.NotFound;
import ir.maktab.util.exception.NotFoundUser;
import ir.maktab.util.exception.StatusException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final ServicesService servicesService;
    private final SubServicesService subServicesService;
    private final ExpertService expertService;

    public void addServices(String name) throws NotFound {
        Services serviceByName = servicesService.getByName(name);
        servicesService.addServices(serviceByName);
    }

    public void addSubService(String name, String subName) throws NotFound {
        servicesService.getByName(name);
        SubServices subServicesByName = subServicesService.getByName(subName);
        subServicesService.saveSubService(subServicesByName);
    }

    public Expert addExpertToSubService(Expert expert, String name) throws NotFound {
        SubServices subServicesByName = subServicesService.getByName(name);
        expert.getSubServices().add(subServicesByName);
        expertService.update(expert);
        return expert;
    }

    public void deleteExpertFromSubServices(Expert expert, SubServices subServices) throws NotFound {
        if (!(expert.getSubServices().contains(subServices)))
            throw new NotFound("not found this sub service for this expert");
        expert.getSubServices().remove(subServices);
        expertService.update(expert);
    }

    public List<Services> showAllServices() {
        return servicesService.getAllServices();
    }

    public SubServices updatePriceAndBrief(String name, double price, String brief) throws NotFound {
        SubServices subServiceByName = subServicesService.getByName(name);
        subServiceByName.setPrice(price);
        subServiceByName.setBriefExplanation(brief);
        subServicesService.updateSubServices(subServiceByName);
        return subServiceByName;
    }

    public Expert editStatus(String email) throws NotFoundUser, StatusException {
        Expert expert = expertService.getExpertByEmail(email);
        if (!(expert.equals(ExpertStatus.NEW)))
            throw new StatusException("This user is not new");
        expert.setExpertStatus(ExpertStatus.CONFIRMED);
        return expertService.update(expert);
    }
}
