package ir.maktab.service;

import ir.maktab.data.model.MainService;

import java.util.List;

public interface MainServicesServiceInterface {

    void addServices(MainService mainService);

    List<MainService> getAllServices();

    MainService updateServices(MainService mainService);

    void deleteServices(MainService mainService);

    MainService saveNewService(MainService mainService);

    MainService findByName(String name);

    MainService findById(Long id);
}
