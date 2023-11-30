package com.example.project.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class BaseResponse {

    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;


}
