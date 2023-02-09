package ir.maktab.controller;

import ir.maktab.data.dto.*;
import ir.maktab.data.enums.OrderStatus;
import ir.maktab.data.model.Expert;
import ir.maktab.data.model.OrderSystem;
import ir.maktab.data.model.Suggestion;
import ir.maktab.service.ExpertService;
import ir.maktab.service.OpinionService;
import ir.maktab.service.OrderSystemService;
import ir.maktab.service.SuggestionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/expert")
@RequiredArgsConstructor
public class ExpertController {
    private final ExpertService expertService;
    private final SuggestionService suggestionService;
    private final OrderSystemService orderSystemService;
    private final OpinionService opinionService;
    private final ModelMapper modelMapper;

    @PostMapping("/add_expert")
    public ResponseEntity<String> addExpert(@RequestBody ExpertDto expertDto) {
        Expert expert = modelMapper.map(expertDto, Expert.class);
        expertService.signUp(expert);
        return ResponseEntity.ok().body(expert.getFirstName() + " " + " sign up successfully");
    }

    @GetMapping("/sign_In_expert")
    public ResponseEntity<ExpertSignInDto> getByEmail(@RequestParam("email") String email,
                                                      @RequestParam("password") String password) {
        Expert expert = expertService.signIn(email, password);
        ExpertSignInDto dto = modelMapper.map(expert, ExpertSignInDto.class);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/get_all")
    public ResponseEntity<List<ExpertSignInDto>> getAllExpert() {
        return ResponseEntity.ok().body(expertService.getAll().stream().map(expert -> modelMapper.
                map(expert, ExpertSignInDto.class)).collect(Collectors.toList()));
    }

    @PutMapping("/update_expert")
    public ResponseEntity<ExpertDto> updateExpert(@RequestBody ExpertDto expertDto) {
        Expert expert = modelMapper.map(expertDto, Expert.class);
        Expert update = expertService.update(expert);
        ExpertDto dto = modelMapper.map(update, ExpertDto.class);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("delete_expert")
    public ResponseEntity<String> deleteExpert(@RequestBody ExpertDto expertDto) {
        Expert expert = modelMapper.map(expertDto, Expert.class);
        expertService.delete(expert);
        return ResponseEntity.ok().body("This expert delete");
    }

    @GetMapping("find_expert")
    public ResponseEntity<ExpertSignInDto> findExpertByEmail(@RequestParam("email") String email) {
        Expert expert = expertService.getExpertByEmail(email);
        ExpertSignInDto dto = modelMapper.map(expert, ExpertSignInDto.class);
        return ResponseEntity.ok().body(dto);
    }

    @PutMapping("/change_password")
    public ResponseEntity<ExpertSignInDto> updatePassword(@RequestParam("newPassword") String newPassword,
                                                          @RequestParam("confirmedPassword") String confirmedPassword,
                                                          @RequestParam("email") String email) {
        Expert expert = expertService.changePasswordExpert(newPassword, confirmedPassword, email);
        ExpertSignInDto dto = modelMapper.map(expert, ExpertSignInDto.class);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/sub_service_and_status")
    public ResponseEntity<List<OrderSystemDto>> findByServiceAndStatus(@RequestParam("id") Long id,
                                                                       @RequestParam("status") OrderStatus orderStatus,
                                                                       @RequestParam("status1") OrderStatus orderStatus1) {

        return ResponseEntity.ok().body(orderSystemService.findBySubAndStatus(id, orderStatus, orderStatus1).stream().
                map(orderSystem -> modelMapper.map(orderSystem, OrderSystemDto.class)).collect(Collectors.toList()));
    }

    @PostMapping("/suggestion_add")
    public ResponseEntity<SuggestionDto> addSuggestion(@RequestBody SuggestionDto suggestionDto,
                                                       @RequestParam(value = "subId") Long subId,
                                                       @RequestParam(value = "expertId") Long expertId) {
        Suggestion suggestion = modelMapper.map(suggestionDto, Suggestion.class);
        Suggestion suggestionFromExpert = suggestionService.sendSuggestionFromExpert(suggestion, subId, expertId);
        SuggestionDto dto = modelMapper.map(suggestionFromExpert, SuggestionDto.class);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping("/set_order")
    public ResponseEntity<SuggestionDto> setOrder(@RequestParam(value = "suggestionId") Long suggestionId,
                                                  @RequestParam(value = "orderId") Long orderId) {
        Suggestion suggestion = suggestionService.setOrder(suggestionId, orderId);
        SuggestionDto dto = modelMapper.map(suggestion, SuggestionDto.class);
        return ResponseEntity.ok().body(dto);
    }

    @PutMapping("/status_expert_selection")
    public ResponseEntity<OrderSystemDto> changeToSelection(@RequestParam(value = "orderId") Long orderId) {
        OrderSystem orderSystem = orderSystemService.changeOrderStatus(orderId);
        OrderSystemDto dto = modelMapper.map(orderSystem, OrderSystemDto.class);
        return ResponseEntity.ok().body(dto);
    }

    @PutMapping("/add_image")
    public ResponseEntity<ExpertDto> addImage(@RequestParam(value = "id") Long id) {
        Expert expert = expertService.saveImage(id);
        ExpertDto dto = modelMapper.map(expert, ExpertDto.class);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/show-by-score")
    public ResponseEntity<List<OpinionByScoreDto>> showByScore(@RequestParam(value = "id") Long id) {
        return ResponseEntity.ok().body(opinionService.showByScore(id).stream().map(opinion -> modelMapper.
                map(opinion, OpinionByScoreDto.class)).collect(Collectors.toList()));
    }

}
