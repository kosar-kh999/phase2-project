package ir.maktab.service;

import ir.maktab.data.model.SubServices;
import ir.maktab.data.repository.SubServicesRepository;
import ir.maktab.util.exception.ExistException;
import ir.maktab.util.exception.NotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubServicesService {

    private final SubServicesRepository subServicesRepository;

    public void saveSubService(SubServices subServices) {
        subServicesRepository.save(subServices);
    }

    public List<SubServices> getAllSubServices() {
        return subServicesRepository.findAll();
    }

    public SubServices updateSubServices(SubServices subServices) {
        return subServicesRepository.save(subServices);
    }

    public void deleteSubServices(SubServices subServices) {
        subServicesRepository.delete(subServices);
    }

    public SubServices saveNewSubService(SubServices subServices) {
        if (subServicesRepository.findBySubName(subServices.getSubName()).isPresent())
            throw new ExistException("This sub service is exist");
        return subServicesRepository.save(subServices);
    }

    public SubServices findByName(String name) {
        return subServicesRepository.findBySubName(name).orElseThrow(() -> new NotFound("not found this sub service"));
    }

    public SubServices findById(Long id) {
        return subServicesRepository.findById(id).orElseThrow(() -> new NotFound("not found this sub service"));
    }

    public void updatePrice(double price, Long id) {
        SubServices subServices = findById(id);
        subServicesRepository.updatePrice(price, subServices.getSubName());
    }

    public void updateBrief(String brief, Long id) {
        SubServices subServices = findById(id);
        subServicesRepository.updateBrief(brief, subServices.getSubName());
    }

    public List<SubServices> getSubServiceByMainService(String name) {
        return subServicesRepository.findSubService(name);
    }

}
