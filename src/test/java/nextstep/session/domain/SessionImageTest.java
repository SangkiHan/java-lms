package nextstep.session.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SessionImageTest {

    @Test
    void 생성_성공() {
        // expect
        assertThat(SessionImage.of("test.gif", 1024 * 1024, 300, 200))
                .isInstanceOf(SessionImage.class);
    }

    @Test
    void 생성_유효하지_않은_형식_실패() {
        // expect
        assertThatThrownBy(() -> SessionImage.of("test.txt", 1024 * 1024, 300, 200))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 생성_용량초과_실패() {
        // expect
        assertThatThrownBy(() -> SessionImage.of("S3://test.gif", 1024 * 1024 + 1, 300, 200))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 생성_pixel_실패() {
        // expect
        assertThatThrownBy(() -> SessionImage.of("S3://test.gif", 1024 * 1024, 299, 200))
                .isInstanceOf(IllegalArgumentException.class);
        // expect
        assertThatThrownBy(() -> SessionImage.of("S3://test.gif", 1024 * 1024, 300, 199))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 생성_비율_실패() {
        // expect
        assertThatThrownBy(() -> SessionImage.of("S3://test.gif", 1024 * 1024, 400, 200))
                .isInstanceOf(IllegalArgumentException.class);
    }
}