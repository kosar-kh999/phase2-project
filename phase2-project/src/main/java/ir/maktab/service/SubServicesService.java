package ir.maktab.service;

import ir.maktab.data.model.SubServices;
import ir.maktab.data.repository.SubServicesRepository;
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

    public void updateSubServices(SubServices subServices) {
        subServicesRepository.save(subServices);
    }

    public void deleteSubServices(SubServices subServices) {
        subServicesRepository.delete(subServices);
    }

    public SubServices getByName(String name) throws NotFound {
        return subServicesRepository.findBySubName(name).orElseThrow(() -> new NotFound("This email is not exist ! "));
    }

    public void updatePrice(double price, String name) {
        subServicesRepository.updatePrice(price, name);
    }

    public void updateBrief(String brief, String name) {
        subServicesRepository.updateBrief(brief, name);
    }
}
