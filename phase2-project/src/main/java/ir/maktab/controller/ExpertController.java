package ir.maktab.controller;

import ir.maktab.data.dto.*;
import ir.maktab.data.enums.OrderStatus;
import ir.maktab.data.model.Expert;
import ir.maktab.data.model.OrderSystem;
import ir.maktab.data.model.Suggestion;
import ir.maktab.service.*;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/expert")
public class ExpertController {
    private final ExpertService expertService;
    private final SuggestionService suggestionService;
    private final OrderSystemService orderSystemService;
    private final OpinionService opinionService;
    private final ImageService imageService;
    private final ModelMapper modelMapper;

    public ExpertController(ExpertService expertService, SuggestionService suggestionService,
                            OrderSystemService orderSystemService, OpinionService opinionService,
                            ImageService imageService, ModelMapper modelMapper) {
        this.expertService = expertService;
        this.suggestionService = suggestionService;
        this.orderSystemService = orderSystemService;
        this.opinionService = opinionService;
        this.imageService = imageService;
        this.modelMapper = modelMapper;
    }

    @PostMapping(value = "/add_expert")
    public ResponseEntity<String> addExpert(@Valid @RequestBody ExpertDto expertDto, HttpServletRequest request) throws
            MessagingException, UnsupportedEncodingException {
        Expert expert = modelMapper.map(expertDto, Expert.class);
        expertService.signUp(expert, getSiteURL(request));
        return ResponseEntity.ok().body(expert.getFirstName() + " " + " sign up successfully");
    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    @GetMapping("/verify")
    public String verifyUser(@Param("code") String code) {
        if (expertService.verify(code)) {
            return "verify_success";
        } else {
            return "verify_fail";
        }
    }

    @GetMapping("/get_all")
    public ResponseEntity<List<ExpertSignInDto>> getAllExpert() {
        return ResponseEntity.ok().body(expertService.getAll().stream().map(expert -> modelMapper.
                map(expert, ExpertSignInDto.class)).collect(Collectors.toList()));
    }

    @PutMapping("/update_expert")
    public ResponseEntity<ExpertDto> updateExpert(@Valid @RequestBody ExpertDto expertDto) {
        Expert expert = modelMapper.map(expertDto, Expert.class);
        Expert update = expertService.update(expert);
        ExpertDto dto = modelMapper.map(update, ExpertDto.class);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("delete_expert")
    public ResponseEntity<String> deleteExpert(@Valid @RequestBody ExpertDto expertDto) {
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
                                                          @RequestParam("confirmedPassword") String confirmedPassword) {
        Expert principal = (Expert) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Expert expert = expertService.changePasswordExpert(newPassword, confirmedPassword, principal.getEmail());
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
    public ResponseEntity<SuggestionDto> addSuggestion(@Valid @RequestBody SuggestionDto suggestionDto,
                                                       @RequestParam(value = "subId") Long subId) {
        Expert principal = (Expert) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Suggestion suggestion = modelMapper.map(suggestionDto, Suggestion.class);
        Suggestion suggestionFromExpert = suggestionService.sendSuggestionFromExpert(suggestion, subId,
                principal.getId());
        SuggestionDto dto = modelMapper.map(suggestionFromExpert, SuggestionDto.class);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping("/set_order")
    public ResponseEntity<SuggestionDto> setOrder(@Valid @RequestBody OrderSuggestionDto orderSuggestionDto) {
        Suggestion suggestion = suggestionService.setOrder(orderSuggestionDto.getSuggestionId(), orderSuggestionDto.
                getOrderId());
        SuggestionDto dto = modelMapper.map(suggestion, SuggestionDto.class);
        return ResponseEntity.ok().body(dto);
    }

    @PutMapping("/status_expert_selection")
    public ResponseEntity<OrderSystemDto> changeToSelection(@RequestParam(value = "orderId") Long orderId) {
        OrderSystem orderSystem = orderSystemService.changeOrderStatus(orderId);
        OrderSystemDto dto = modelMapper.map(orderSystem, OrderSystemDto.class);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping("/add_image")
    public ResponseEntity<ExpertDto> addImage(@RequestParam("image") MultipartFile file) throws IOException {
        Expert principal = (Expert) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Expert expert = imageService.uploadImage(file, principal.getId());
        ExpertDto dto = modelMapper.map(expert, ExpertDto.class);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/show-by-score")
    public ResponseEntity<List<OpinionByScoreDto>> showByScore() {
        Expert principal = (Expert) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok().body(opinionService.showByScore(principal.getId()).stream().map(opinion -> modelMapper.
                map(opinion, OpinionByScoreDto.class)).collect(Collectors.toList()));
    }

    @GetMapping("get_image")
    public ResponseEntity<byte[]> getImage() {
        Expert principal = (Expert) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        byte[] image = imageService.getImage(principal.getEmail());
        return ResponseEntity.ok().contentType(MediaType.valueOf("image/jpeg")).body(image);
    }

    @GetMapping("view_history_order")
    public ResponseEntity<List<OrderSystemDto>> viewOrderCustomer(@RequestParam("status") OrderStatus orderStatus) {
        Expert principal = (Expert) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok().body(orderSystemService.viewOrderExpert(principal.getEmail(), orderStatus).stream().map(
                orderSystem -> modelMapper.map(orderSystem, OrderSystemDto.class)).collect(Collectors.toList()));
    }

    @GetMapping("view_credit")
    public ResponseEntity<Double> viewCreditCustomer() {
        Expert principal = (Expert) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok().body(expertService.viewCredit(principal.getEmail()));
    }

}
