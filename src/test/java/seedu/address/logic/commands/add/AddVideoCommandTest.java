package seedu.address.logic.commands.add;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_LECTURE_DOES_NOT_EXIST;
import static seedu.address.commons.core.Messages.MESSAGE_MODULE_DOES_NOT_EXIST;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.lecture.Lecture;
import seedu.address.model.lecture.LectureName;
import seedu.address.model.lecture.ReadOnlyLecture;
import seedu.address.model.module.ModuleCode;
import seedu.address.model.module.ReadOnlyModule;
import seedu.address.model.video.Video;
import seedu.address.model.video.VideoName;
import seedu.address.testutil.ModelStub;
import seedu.address.testutil.TypicalLectures;
import seedu.address.testutil.TypicalModules;
import seedu.address.testutil.TypicalVideos;

public class AddVideoCommandTest {

    @Test
    public void constructor_nullModuleCode_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new AddVideoCommand(null, TypicalLectures.getCs2040sWeek1().getName(),
                        TypicalVideos.CONTENT_VIDEO));
    }

    @Test
    public void constructor_nullLectureName_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new AddVideoCommand(TypicalModules.getCs2040s().getCode(), null,
                        TypicalVideos.CONTENT_VIDEO));
    }

    @Test
    public void constructor_nullVideo_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new AddVideoCommand(TypicalModules.getCs2040s().getCode(),
                        TypicalLectures.getCs2040sWeek1().getName(), null));
    }

    @Test
    public void execute_videoAcceptedByModel_addSuccessful() throws CommandException {
        ModuleCode moduleCode = TypicalModules.getCs2040s().getCode();
        Lecture lecture = TypicalLectures.getCs2040sWeek1();
        LectureName lectureName = lecture.getName();
        Video video = TypicalVideos.REVISION_VIDEO;

        ModelStubAcceptingVideoAdded modelStub = new ModelStubAcceptingVideoAdded();

        CommandResult result = new AddVideoCommand(moduleCode, lectureName, video).execute(modelStub);

        assertEquals(String.format(AddVideoCommand.MESSAGE_SUCCESS, moduleCode, lectureName, video),
                result.getFeedbackToUser());
        assertEquals(Arrays.asList(lecture), modelStub.lecturesAddedTo);
        assertEquals(Arrays.asList(video), modelStub.videosAdded);
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        AddVideoCommand command = new AddVideoCommand(TypicalModules.getCs2040s().getCode(),
                TypicalLectures.getCs2040sWeek1().getName(), TypicalVideos.CONTENT_VIDEO);

        assertThrows(NullPointerException.class, () -> command.execute(null));
    }

    @Test
    public void execute_moduleDoesNotExist_throwsCommandException() {
        ModuleCode moduleCode = TypicalModules.getCs2040s().getCode();
        LectureName lectureName = TypicalLectures.getCs2040sWeek1().getName();
        Video video = TypicalVideos.CONTENT_VIDEO;

        ModelStub modelStub = new ModelStubNoModule();
        AddVideoCommand command = new AddVideoCommand(moduleCode, lectureName, video);

        assertThrows(CommandException.class,
                String.format(MESSAGE_MODULE_DOES_NOT_EXIST, moduleCode), () -> command.execute(modelStub));
    }

    @Test
    public void execute_lectureDoesNotExist_throwsCommandException() {
        ModuleCode moduleCode = TypicalModules.getCs2040s().getCode();
        LectureName lectureName = TypicalLectures.getCs2040sWeek1().getName();
        Video video = TypicalVideos.CONTENT_VIDEO;

        ModelStub modelStub = new ModelStubNoLecture();
        AddVideoCommand command = new AddVideoCommand(moduleCode, lectureName, video);

        assertThrows(CommandException.class,
                String.format(MESSAGE_LECTURE_DOES_NOT_EXIST, lectureName, moduleCode), () ->
                command.execute(modelStub));
    }

    @Test
    public void execute_duplicateVideo_throwsCommandException() {
        ModuleCode moduleCode = TypicalModules.getCs2040s().getCode();
        Lecture lecture = TypicalLectures.getCs2040sWeek1();
        LectureName lectureName = lecture.getName();
        Video video = TypicalVideos.CONTENT_VIDEO;

        ModelStub modelStub = new ModelStubWithVideo(lecture, video);
        AddVideoCommand command = new AddVideoCommand(moduleCode, lectureName, video);

        assertThrows(CommandException.class,
                String.format(AddVideoCommand.MESSAGE_DUPLICATE_VIDEO, lectureName, moduleCode), () ->
                command.execute(modelStub));
    }

    @Test
    public void equals() {
        ModuleCode moduleCode = TypicalModules.getCs2040s().getCode();
        LectureName lectureName = TypicalLectures.getCs2040sWeek1().getName();
        AddVideoCommand addContentVideoCommand =
                new AddVideoCommand(moduleCode, lectureName, TypicalVideos.CONTENT_VIDEO);
        AddVideoCommand addAnalysisVideoCommand =
                new AddVideoCommand(moduleCode, lectureName, TypicalVideos.ANALYSIS_VIDEO);

        // same object -> returns true
        assertTrue(addContentVideoCommand.equals(addContentVideoCommand));

        // same values -> returns true
        AddVideoCommand addContentVideoCommandCopy =
                new AddVideoCommand(moduleCode, lectureName, TypicalVideos.CONTENT_VIDEO);
        assertTrue(addContentVideoCommand.equals(addContentVideoCommandCopy));

        // different types -> returns false
        assertFalse(addContentVideoCommand.equals(1));

        // null -> returns false
        assertFalse(addContentVideoCommand.equals(null));

        // different videos -> return false
        assertFalse(addContentVideoCommand.equals(addAnalysisVideoCommand));
    }

    /**
     * A {@code Model} stub that always accepts the video being added.
     */
    private class ModelStubAcceptingVideoAdded extends ModelStub {
        private final ArrayList<ReadOnlyLecture> lecturesAddedTo = new ArrayList<>();
        private final ArrayList<Video> videosAdded = new ArrayList<>();

        @Override
        public ReadOnlyModule getModule(ModuleCode code) {
            return TypicalModules.getTypicalTracker().getModule(code);
        }

        @Override
        public boolean hasModule(ModuleCode moduleCode) {
            return true;
        }

        @Override
        public boolean hasLecture(ModuleCode moduleCode, LectureName lectureName) {
            return true;
        }

        @Override
        public boolean hasVideo(ModuleCode moduleCode, LectureName lectureName, VideoName videoName) {
            return false;
        }

        @Override
        public void addVideo(ReadOnlyLecture lecture, Video toAdd) {
            requireAllNonNull(lecture, toAdd);

            lecturesAddedTo.add(lecture);
            videosAdded.add(toAdd);
        }
    }

    /**
     * A {@code Model} stub that contains no module.
     */
    private class ModelStubNoModule extends ModelStub {
        @Override
        public boolean hasModule(ModuleCode code) {
            return false;
        }
    }

    /**
     * A {@code Model} stub that contains no lecture.
     */
    private class ModelStubNoLecture extends ModelStub {
        @Override
        public ReadOnlyModule getModule(ModuleCode code) {
            return TypicalModules.getTypicalTracker().getModule(code);
        }

        @Override
        public boolean hasModule(ModuleCode code) {
            return true;
        }

        @Override
        public boolean hasLecture(ModuleCode moduleCode, LectureName lectureName) {
            return false;
        }
    }

    /**
     * A {@code Model} stub that contains a single video.
     */
    private class ModelStubWithVideo extends ModelStub {
        private final ReadOnlyLecture lecture;
        private final Video video;

        public ModelStubWithVideo(ReadOnlyLecture lecture, Video video) {
            requireAllNonNull(lecture, video);

            this.lecture = lecture;
            this.video = video;
        }

        @Override
        public ReadOnlyModule getModule(ModuleCode code) {
            return TypicalModules.getTypicalTracker().getModule(code);
        }

        @Override
        public boolean hasModule(ModuleCode code) {
            return true;
        }

        @Override
        public boolean hasLecture(ModuleCode moduleCode, LectureName lectureName) {
            return true;
        }

        @Override
        public boolean hasVideo(ModuleCode moduleCode, LectureName lectureName, VideoName videoName) {
            return this.lecture.getName().equals(lectureName) && this.video.getName().equals(videoName);
        }
    }

}