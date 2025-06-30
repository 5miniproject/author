package aivlelibraryminiproj.domain;

import aivlelibraryminiproj.domain.*;
import aivlelibraryminiproj.infra.AbstractEvent;
import java.util.*;
import lombok.*;

@Data
@ToString
public class PointShorted extends AbstractEvent {

    private Long userId;
    private Integer point;
    private Long subcribeId;
}
