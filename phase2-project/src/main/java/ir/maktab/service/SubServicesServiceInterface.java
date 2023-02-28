package ir.maktab.service;

import ir.maktab.data.model.SubServices;

import java.util.List;

public interface SubServicesServiceInterface {

    void saveSubService(SubServices subServices);

    List<SubServices> getAllSubServices();

    SubServices updateSubServices(SubServices subServices);

    void deleteSubServices(SubServices subServices);

    SubServices saveNewSubService(SubServices subServices);

    SubServices findByName(String name);

    SubServices findById(Long id);

    void updatePrice(double price, Long id);

    void updateBrief(String brief, Long id);

    List<SubServices> getSubServiceByMainService(String name);
}
