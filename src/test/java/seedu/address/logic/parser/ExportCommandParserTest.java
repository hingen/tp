package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ExportCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.storage.JsonTrackerStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.Storage;
import seedu.address.storage.StorageManager;

public class ExportCommandParserTest {
    private static final String TEST_FILE = "test.json";
    private ExportCommandParser parser = new ExportCommandParser();

    private Storage storage;

    @BeforeEach
    public void setUp() {
        JsonTrackerStorage archiveStorage = new JsonTrackerStorage(Paths.get("lt"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(Paths.get("prefs"));
        storage = new StorageManager(archiveStorage, userPrefsStorage);
    }

    @Test
    public void parseCommand_doNotOverwriteFile_successful() throws ParseException {
        ExportCommand expectedCommand = new ExportCommand(TEST_FILE, false);
        assertEquals(expectedCommand, parser.parse(TEST_FILE));
    }

    @Test
    public void parseCommand_overwriteFileCommand_successful() throws ParseException {
        String argument = TEST_FILE + " /overwrite";
        ExportCommand expectedCommand = new ExportCommand(TEST_FILE, true);
        assertEquals(expectedCommand, parser.parse(argument));
    }

    @Test
    public void parseCommand_emptyFileName_throwParseException() {
        assertThrows(ParseException.class, () -> parser.parse(""));
    }
}