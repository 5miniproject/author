package aivlelibraryminiproj.domain;

import aivlelibraryminiproj.domain.*;
import aivlelibraryminiproj.infra.AbstractEvent;
import java.util.*;
import lombok.*;

@Data
@ToString
public class BookPublishRequested extends AbstractEvent {

    private Long id;
    private Long authorId;
    private String contents;
    private String status;
    private String authorname;
    private String title;
}
