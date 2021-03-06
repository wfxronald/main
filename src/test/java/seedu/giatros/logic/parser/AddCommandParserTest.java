package seedu.giatros.logic.parser;

import static seedu.giatros.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.giatros.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.giatros.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static seedu.giatros.logic.commands.CommandTestUtil.ALLERGY_DESC_AMPICILLIN;
import static seedu.giatros.logic.commands.CommandTestUtil.ALLERGY_DESC_IBUPROFEN;
import static seedu.giatros.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.giatros.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.giatros.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.giatros.logic.commands.CommandTestUtil.INVALID_ALLERGY_DESC;
import static seedu.giatros.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.giatros.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.giatros.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.giatros.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.giatros.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.giatros.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.giatros.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.giatros.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.giatros.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.giatros.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.giatros.logic.commands.CommandTestUtil.VALID_ALLERGY_AMPICILLIN;
import static seedu.giatros.logic.commands.CommandTestUtil.VALID_ALLERGY_IBUPROFEN;
import static seedu.giatros.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.giatros.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.giatros.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.giatros.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.giatros.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.giatros.testutil.TypicalPatients.AMY;
import static seedu.giatros.testutil.TypicalPatients.BOB;

import org.junit.Test;

import seedu.giatros.logic.commands.AddCommand;
import seedu.giatros.model.allergy.Allergy;
import seedu.giatros.model.patient.Address;
import seedu.giatros.model.patient.Email;
import seedu.giatros.model.patient.Name;
import seedu.giatros.model.patient.Patient;
import seedu.giatros.model.patient.Phone;
import seedu.giatros.testutil.PatientBuilder;

public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Patient expectedPatient = new PatientBuilder(BOB).withAllergies(VALID_ALLERGY_IBUPROFEN).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + ALLERGY_DESC_IBUPROFEN, new AddCommand(expectedPatient));

        // multiple names - last name accepted
        assertParseSuccess(parser, NAME_DESC_AMY + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + ALLERGY_DESC_IBUPROFEN, new AddCommand(expectedPatient));

        // multiple phones - last phone accepted
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_AMY + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + ALLERGY_DESC_IBUPROFEN, new AddCommand(expectedPatient));

        // multiple emails - last email accepted
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_AMY + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + ALLERGY_DESC_IBUPROFEN, new AddCommand(expectedPatient));

        // multiple addresses - last address accepted
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_AMY
                + ADDRESS_DESC_BOB + ALLERGY_DESC_IBUPROFEN, new AddCommand(expectedPatient));

        // multiple allergies - all accepted
        Patient expectedPatientMultipleAllergies = new PatientBuilder(BOB)
                .withAllergies(VALID_ALLERGY_IBUPROFEN, VALID_ALLERGY_AMPICILLIN).build();
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + ALLERGY_DESC_AMPICILLIN + ALLERGY_DESC_IBUPROFEN, new AddCommand(expectedPatientMultipleAllergies));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero allergies
        Patient expectedPatient = new PatientBuilder(AMY).withAllergies().build();
        assertParseSuccess(parser, NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY,
                new AddCommand(expectedPatient));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB,
                expectedMessage);

        // missing phone prefix
        assertParseFailure(parser, NAME_DESC_BOB + VALID_PHONE_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB,
                expectedMessage);

        // missing email prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + VALID_EMAIL_BOB + ADDRESS_DESC_BOB,
                expectedMessage);

        // missing address prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + VALID_ADDRESS_BOB,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_BOB + VALID_PHONE_BOB + VALID_EMAIL_BOB + VALID_ADDRESS_BOB,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + ALLERGY_DESC_AMPICILLIN + ALLERGY_DESC_IBUPROFEN, Name.MESSAGE_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser, NAME_DESC_BOB + INVALID_PHONE_DESC + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + ALLERGY_DESC_AMPICILLIN + ALLERGY_DESC_IBUPROFEN, Phone.MESSAGE_CONSTRAINTS);

        // invalid email
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + INVALID_EMAIL_DESC + ADDRESS_DESC_BOB
                + ALLERGY_DESC_AMPICILLIN + ALLERGY_DESC_IBUPROFEN, Email.MESSAGE_CONSTRAINTS);

        // invalid address
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + INVALID_ADDRESS_DESC
                + ALLERGY_DESC_AMPICILLIN + ALLERGY_DESC_IBUPROFEN, Address.MESSAGE_CONSTRAINTS);

        // invalid allergy
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + INVALID_ALLERGY_DESC + VALID_ALLERGY_IBUPROFEN, Allergy.MESSAGE_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB + INVALID_ADDRESS_DESC,
                Name.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + ALLERGY_DESC_AMPICILLIN + ALLERGY_DESC_IBUPROFEN,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
    }
}
