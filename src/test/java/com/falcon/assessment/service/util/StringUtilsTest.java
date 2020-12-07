package com.falcon.assessment.service.util;

import static com.falcon.assessment.service.util.StringUtils.alphabeticSubstrings;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class StringUtilsTest {

    @Test
    public void alphabeticalSubstrings() {
        assertThat(alphabeticSubstrings("   abc   ")).contains("abc");
        assertThat(alphabeticSubstrings("abc123def")).contains("abc", "def");
        assertThat(alphabeticSubstrings("abc&!#?def")).contains("abc", "def");
    }

}
