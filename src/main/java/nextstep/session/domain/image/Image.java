package nextstep.session.domain.image;

import nextstep.DateDomain;

import java.time.LocalDateTime;

public class Image {

    private Long id;
    private final Long sessionId;
    private final String name;
    private final ImageSize size;
    private final ImageCapacity capacity;
    private final DateDomain dateDomain;

    public Image(Long sessionId, String name, int width, int height, int capacity) {
        this.sessionId = sessionId;
        ImageExtension.confirmImageExtension(name);
        this.name = name;
        this.size = new ImageSize(width, height);
        this.capacity = new ImageCapacity(capacity);
        this.dateDomain = new DateDomain();
    }

    public Image(Long id, Long sessionId, String name, int width, int height, int capacity, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.sessionId = sessionId;
        this.name = name;
        this.size = new ImageSize(width, height);
        this.capacity = new ImageCapacity(capacity);
        this.dateDomain = new DateDomain(createdAt, updatedAt);
    }

    public Long getSessionId() {
        return sessionId;
    }

    public String getName() {
        return name;
    }

    public ImageSize getSize() {
        return size;
    }

    public ImageCapacity getCapacity() {
        return capacity;
    }

    public DateDomain getDateDomain() {
        return dateDomain;
    }
}
