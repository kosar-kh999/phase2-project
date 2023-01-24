package ir.maktab.service;

import ir.maktab.data.model.SubServices;
import ir.maktab.data.repository.SubServicesRepository;
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
}
