package ir.maktab.service;

import ir.maktab.data.model.MainService;
import ir.maktab.data.repository.MainServicesRepository;
import ir.maktab.util.exception.ExistException;
import ir.maktab.util.exception.NotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MainServicesService {
    private final MainServicesRepository mainServicesRepository;

    public void addServices(MainService mainService) {
        mainServicesRepository.save(mainService);
    }

    public List<MainService> getAllServices() {
        return mainServicesRepository.findAll();
    }

    public MainService updateServices(MainService mainService) {
        return mainServicesRepository.save(mainService);
    }

    public void deleteServices(MainService mainService) {
        mainServicesRepository.delete(mainService);
    }

    public MainService saveNewService(MainService mainService) {
        if (mainServicesRepository.findByName(mainService.getName()).isPresent())
            throw new ExistException("This service is exist !");
        return mainServicesRepository.save(mainService);
    }

    public MainService findByName(String name) {
        return mainServicesRepository.findByName(name).orElseThrow(() -> new NotFound("not found this service"));
    }

    public MainService findById(Long id) {
        return mainServicesRepository.findById(id).orElseThrow(() -> new NotFound("not found this service"));
    }
}
