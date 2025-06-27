package aivlelibraryminiproj.domain;

import aivlelibraryminiproj.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class ScriptCreated extends AbstractEvent {

    private Long id;
    private Long authorId;
    private String contents;
    private String status;
    private String title;
    private Date createdAt;
    private Date updatedAt;
}
