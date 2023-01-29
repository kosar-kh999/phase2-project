package ir.maktab.service;

import ir.maktab.data.enums.ExpertStatus;
import ir.maktab.data.model.Expert;
import ir.maktab.data.model.MainService;
import ir.maktab.data.model.SubServices;
import ir.maktab.util.exception.NotFound;
import ir.maktab.util.exception.NotFoundUser;
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

    public void addServices(MainService mainService) throws NotFound {
        MainService service = mainServicesService.getByName(mainService);
        mainServicesService.addServices(service);
    }

    public void addSubService(SubServices subServices) throws NotFound {
        SubServices services = subServicesService.getByName(subServices);
        subServicesService.saveSubService(services);
    }

    @Transactional
    public Expert addExpertToSubService(Expert expert, SubServices subServices) throws NotFound, SubServicesException {
        SubServices services = subServicesService.findByName(subServices.getSubName());
        if (expert.getSubServices().contains(services))
            throw new SubServicesException("This sub service was added before");
        expert.getSubServices().add(services);
        expertService.update(expert);
        return expert;
    }

    public boolean deleteExpertFromSubServices(Expert expert, SubServices subServices) {
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

    public Expert editStatus(String email) throws NotFoundUser, StatusException {
        Expert expert = expertService.getExpertByEmail(email);
        if (!(expert.getExpertStatus().equals(ExpertStatus.NEW)))
            throw new StatusException("This user is not new");
        expert.setExpertStatus(ExpertStatus.CONFIRMED);
        return expertService.update(expert);
    }
}
