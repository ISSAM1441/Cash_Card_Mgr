package com.shieldguard.csdps.sbcc.famillycashcard.cashcard;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.UUID;

@JsonTest //This provides extensive JSON testing and parsing support.
// It also establishes all the related behavior to test JSON objects.
public class CashCardJsonTest {

    @Autowired
    //@Autowired is an annotation that directs Spring to create an object of the requested type.
    private JacksonTester<CashCard> json;
    //JacksonTester is a convenience wrapper to the Jackson JSON parsing library.
    // It handles serialization and deserialization of JSON objects.

    @Test
    void cashCardSerializationTest() throws IOException {
        // Parse the raw text string into a native java.util.UUID object
        UUID targetUuid = UUID.fromString("3b926f79-bd2d-4cd5-b3e5-be3101ccd0c3");
        CashCard cashCard = new CashCard(targetUuid, 823.45);

        // 1. Validates structural equality against your updated target file
        assertThat(json.write(cashCard))
                .isStrictlyEqualToJson("expected.json");

        // 2. Checks JSON path constraints (changed from Number to String for the ID)
        assertThat(json.write(cashCard))
                .hasJsonPathStringValue("@.id");

        assertThat(json.write(cashCard))
                .extractingJsonPathStringValue("@.id")
                .isEqualTo(targetUuid.toString());

        // 3. Amount checks remain numeric
        assertThat(json.write(cashCard)).hasJsonPathNumberValue("@.amount");
        assertThat(json.write(cashCard)).extractingJsonPathNumberValue("@.amount")
                .isEqualTo(823.45);
    }

    @Test
    void cashCardDeserializationTest() throws IOException {
        String expected = """
                {
                    "id": "3b926f79-bd2d-4cd5-b3e5-be3101ccd0c3",
                    "amount": 123.45
                }
                """;
        UUID targetUuid = UUID.fromString("3b926f79-bd2d-4cd5-b3e5-be3101ccd0c3");

        assertThat(json.parse(expected))
                .isEqualTo(
                        new CashCard(
                                targetUuid,
                                123.45
                        )
                );

        // Direct property validation
        assertThat(json.parseObject(expected).id()).isEqualTo(targetUuid);
        assertThat(json.parseObject(expected).amount()).isEqualTo(123.45);
    }
    @Test
    void createAndReadJsonFile() throws Exception {

        System.out.println("Current directory:");
        System.out.println(Paths.get("").toAbsolutePath());

        String jsonContent = """
                {
                  "id":"3b926f79-bd2d-4cd5-b3e5-be3101ccd0c3",
                  "amount":423.45
                }
                """;

        Path file = Paths.get("expected.json");

        Files.writeString(file, jsonContent);

        System.out.println("File created at:");
        System.out.println(file.toAbsolutePath());

        assertThat(Files.exists(file)).isTrue();

        String loadedContent = Files.readString(file);

        assertThat(loadedContent).contains("423.45");
    }
}