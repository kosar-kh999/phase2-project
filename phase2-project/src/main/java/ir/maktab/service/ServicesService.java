package ir.maktab.service;

import ir.maktab.data.model.Services;
import ir.maktab.data.repository.ServicesRepository;
import ir.maktab.util.exception.NotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicesService {
    private final ServicesRepository servicesRepository;

    public void addServices(Services services) {
        servicesRepository.save(services);
    }

    public List<Services> getAllServices() {
        return servicesRepository.findAll();
    }

    public void updateServices(Services services) {
        servicesRepository.save(services);
    }

    public void deleteServices(Services services) {
        servicesRepository.delete(services);
    }

    public Services getByName(String name) throws NotFound {
        return servicesRepository.findByName(name).orElseThrow(() -> new NotFound("This service is not exist ! "));
    }
}
