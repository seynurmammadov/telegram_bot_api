package az.code.telegram_bot_api.utils;

import org.hibernate.engine.jdbc.ReaderInputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(URL.class)
class ConverterUtilTest {
    ConverterUtil converterUtil;

    @BeforeEach
    void data() {
        converterUtil = new ConverterUtil();
    }

    @Test
    @DisplayName("ConverterUtil - get image by url to byte array - Valid")
    void getImage() throws Exception {
        String test = "test";
        InputStream fakeStream = new ReaderInputStream(new StringReader(test));
        URL url = Mockito.mock(URL.class);
        when(url.openStream()).thenReturn(fakeStream);
        assertArrayEquals(test.getBytes(), converterUtil.getImage(url));
    }

    @Test
    @DisplayName("ConverterUtil -replace method - Valid")
    void replace() {
        String text ="testing {replace} some text";
        String textExcepted ="testing replaced some text";
        StringBuilder sb = new StringBuilder(text);
        converterUtil.replace(sb,"replaced","{replace}");
        assertEquals(textExcepted, sb.toString());
    }
}