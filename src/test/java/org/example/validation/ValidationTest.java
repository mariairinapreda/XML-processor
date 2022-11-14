package org.example.validation;

import org.example.display.Display;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ValidationTest {
    private static Validation validation;

    @BeforeAll
    public static void initialize(){
        validation=Validation.getInstance(Display.getInstance());
    }

    @Test
    void getInstanceShouldReturnInstanceOfValidationClass() {
        assertThat(validation).isInstanceOf(Validation.class);
    }

    @Test
    void isFileNameCorrectShouldReturnTrueForValidFilename() {
        assertThat(validation.isFileNameCorrect("orders23.xml")).isTrue();
    }

    @Test
    void isFileNameCorrectShouldReturnFalseForInvalidLength() {
        assertThat(validation.isLengthValid("orders2grre3.xml")).isFalse();
    }
    @Test
    void isFileNameCorrectShouldReturnFalseForNotStartingWithPrefix() {
        assertThat(validation.hasPrefix("test23.xml")).isFalse();
    }
    @Test
    void isFileNameCorrectShouldReturnFalseForNotEndingWithSuffix() {
        assertThat(validation.hasSuffix("orders23test")).isFalse();
    }

    @Test
    void isFileNameCorrectShouldReturnFalseForNotIncludingNumbers() {
        assertThat(validation.isFileNameCorrect("orderswer.xml")).isFalse();
    }



}