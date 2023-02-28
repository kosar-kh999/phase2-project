package ir.maktab.service;

import ir.maktab.data.model.Admin;
import ir.maktab.data.model.Expert;
import ir.maktab.data.model.MainService;
import ir.maktab.data.model.SubServices;

import java.util.List;

public interface AdminServiceInterface {

    void saveAdmin(Admin admin);

    Admin findByUsername(String username);

    void addServices(MainService mainService);

    void addSubServiceToService(SubServices subServices, Long id);

    Expert addExpertToSubService(Long expertId, Long subId);

    void deleteExpertFromSubServices(Long expertId, Long subId);

    List<MainService> showAllServices();

    Expert editStatus(String email);
}
