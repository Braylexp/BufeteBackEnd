package com.bufete.backend.Dtos;

import lombok.Data;
import java.util.UUID;

@Data
public class MoveNodeRequest {
    private UUID targetParentId;
}
