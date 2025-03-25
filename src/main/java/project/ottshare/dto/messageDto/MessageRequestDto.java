package project.ottshare.dto.messageDto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequestDto {

    @NotNull(message = "Message cannot be null")
    private String message;

}
