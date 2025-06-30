package aivlelibraryminiproj.domain;

import aivlelibraryminiproj.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class BestsellerArchived extends AbstractEvent {

    private Long id;
    private Boolean isBestSeller;
}
