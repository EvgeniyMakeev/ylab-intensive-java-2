package dev.makeev.training_diary_app.in;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for the {@link InputImpl} class.
 */
public class InputImplTest {

    /**
     * Test case for the {@link InputImpl#getInt(int, int)} method
     * with valid input, should return the expected integer.
     */
    @Test
    @DisplayName("Get int from console - Should get integer not higher max value")
    void getInt_validInput_returnsInteger() {
        String inputString = "5\n";
        InputStream in = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(in);
        InputImpl input = new InputImpl();
        int result = input.getInt(0, 9);

        assertThat(result).isEqualTo(5);
    }

    /**
     * Test case for the {@link InputImpl#getString()} method
     * with valid input, should return the expected string.
     */
    @Test
    @DisplayName("Get string from console - Should get string")
    void getString_validInput_returnsString() {
        String inputString = "Hello\n";
        InputStream in = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(in);
        InputImpl input = new InputImpl();

        String result = input.getString();

        assertThat(result).isEqualTo("Hello");
    }

    /**
     * Test case for the {@link InputImpl#getDouble()} method
     * with valid input, should return the expected double.
     */
    @Test
    @DisplayName("Get double from console - Should get double")
    void getDouble_validInput_returnsDouble() {
        String inputString = "3.14\n";
        InputStream in = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(in);
        InputImpl input = new InputImpl();

        double result = input.getDouble();

        assertThat(result).isEqualTo(3.14);
    }

    /**
     * Test case for the {@link InputImpl#getInteger(int, int, int)} method
     * with valid input, should return the expected integer.
     */
    @Test
    @DisplayName("Get a integer with a given minimum, maximum value and a certain number of characters" +
            " - Should get integer")
    void getInteger_validInput_returnsInteger() {
        String inputString = "12345\n";
        InputStream in = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(in);
        InputImpl input = new InputImpl();

        int result = input.getInteger(5, 1000, 99999);

        assertThat(result).isEqualTo(12345);
    }
}