package ir.maktab.data.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.Duration;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SuggestionDto {

    private double price;
    @JsonFormat(pattern = "yyy-MM-dd HH:mm:ss")
    private String suggestionsStartedTime;
    private Duration duration;
}
