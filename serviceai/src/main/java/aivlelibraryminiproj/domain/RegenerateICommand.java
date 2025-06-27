package aivlelibraryminiproj.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class RegenerateICommand {

    private Long id;
    private Long authorId;
    private Long scriptId;
    private String contents;
    private String status;
    private String title;
}
