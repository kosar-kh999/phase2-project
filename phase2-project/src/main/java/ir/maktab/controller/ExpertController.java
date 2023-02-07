package ir.maktab.controller;

import ir.maktab.data.dto.ExpertDto;
import ir.maktab.data.dto.ExpertSignInDto;
import ir.maktab.data.dto.SuggestionDto;
import ir.maktab.data.model.Expert;
import ir.maktab.data.model.Suggestion;
import ir.maktab.service.ExpertService;
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

    @PostMapping("/suggestion_add")
    public ResponseEntity<String> addSuggestion(@RequestBody SuggestionDto suggestionDto,
                                                @RequestParam(value = "id") Long id) {
        Suggestion suggestion = modelMapper.map(suggestionDto, Suggestion.class);
        suggestionService.sendSuggestionFromExpert(suggestion, id);
        return ResponseEntity.ok().body("suggestion add");
    }
}
