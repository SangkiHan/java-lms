package nextstep.session.domain;

import java.time.LocalDateTime;

public class Duration {

    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    public Duration(LocalDateTime startDate, LocalDateTime endDate) {
        validate(startDate, endDate);

        this.startDate = startDate;
        this.endDate = endDate;
    }

    private void validate(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("시작일자는 과거일 수 없습니다.");
        }

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("시작일자가 종료일자보다 미래일 수 없습니다.");
        }
    }

    public boolean isAvailable(LocalDateTime queryDateFit) {
        return queryDateFit.isAfter(startDate) && queryDateFit.isBefore(endDate);
    }

    public Duration changeStartDate(LocalDateTime changedStartDate) {
        return new Duration(changedStartDate, this.endDate);
    }

    public Duration changeEndDate(LocalDateTime changedEndDate) {
        return new Duration(this.startDate, changedEndDate);
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }
}
