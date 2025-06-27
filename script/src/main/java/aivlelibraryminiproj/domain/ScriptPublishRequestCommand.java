package aivlelibraryminiproj.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class ScriptPublishRequestCommand {

    private Long id;
    private Long authorId;
    private String contents;
    private String status;
    private String authorname;
    private String title;
}
