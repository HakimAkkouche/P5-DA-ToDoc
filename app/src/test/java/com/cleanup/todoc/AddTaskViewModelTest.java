package com.cleanup.todoc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import android.app.Application;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.cleanup.todoc.data.BuildConfigResolver;
import com.cleanup.todoc.data.ToDocRepository;
import com.cleanup.todoc.data.entity.ProjectEntity;
import com.cleanup.todoc.data.entity.TaskEntity;
import com.cleanup.todoc.ui.add_task.AddTaskViewModel;
import com.cleanup.todoc.ui.add_task.AddTaskViewState;
import com.cleanup.todoc.ui.add_task.AddTaskViewStateItem;
import com.cleanup.todoc.utils.LiveDataTestUtils;
import com.cleanup.todoc.utils.TestExecutor;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

@RunWith(MockitoJUnitRunner.class)
public class AddTaskViewModelTest {
    private static final int NB_PROJECT_COUNT = 3;

    private static final String BASE_PROJECT_NAME = "Project : ";
    private static final String ERROR_ADD_TASK_RES_STRING = "error_add_task";

    private static final long DATETIME = 1704850881;

    private static final int[] COLORS = new int[]{Color.parseColor("#3177E1"), Color.parseColor("#CE2BE1"), Color.parseColor("#7AE62D")};

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private final Application application = Mockito.mock(Application.class);
    private final ToDocRepository toDocRepository = Mockito.mock(ToDocRepository.class);
    private final BuildConfigResolver buildConfigResolver = Mockito.mock(BuildConfigResolver.class);
    private final Executor executor = Mockito.spy(new TestExecutor());
    private final Executor mainExecutor = Mockito.spy(new TestExecutor());

    private final MutableLiveData<List<ProjectEntity>> projectsMutableLiveData = new MutableLiveData<>();

    private AddTaskViewModel tasksViewModel;

    @Before
    public void setup() {
        Mockito.doReturn(projectsMutableLiveData).when(toDocRepository).getAllProjectsLiveData();

        projectsMutableLiveData.setValue(getDefaultProjectEntities());

        tasksViewModel = new AddTaskViewModel(application, toDocRepository, buildConfigResolver, mainExecutor, executor);

        Mockito.verify(toDocRepository).getAllProjectsLiveData();
    }
    @Test
    public void addTaskSuccessfully() {
        long selectedProjectId = 1;
        String taskDescription = "taskDescription";

        tasksViewModel.onProjectSelected(selectedProjectId);
        tasksViewModel.onTaskDescriptionChanged(taskDescription);
        tasksViewModel.onOkButtonClicked();

        AddTaskViewState addTaskViewState = LiveDataTestUtils.getValueForTesting(tasksViewModel.getAddTaskViewStateLiveData());
        String toastMessage = LiveDataTestUtils.getValueForTesting(tasksViewModel.getDisplayToastMessageSingleLiveEvent());
        Boolean dismissDialog = LiveDataTestUtils.getValueForTesting(tasksViewModel.getDismissDialogSingleLiveEvent());

        assertEquals(new AddTaskViewState(getDefaultAddTaskViewStateItems(), false), addTaskViewState);
        assertNull(toastMessage);
        assertTrue(dismissDialog);
        Mockito.verify(executor).execute(any());
        Mockito.verify(toDocRepository).addTask(new TaskEntity(selectedProjectId, taskDescription, DATETIME));
        Mockito.verify(mainExecutor, Mockito.times(2)).execute(any());
        Mockito.verifyNoMoreInteractions(toDocRepository, buildConfigResolver, mainExecutor, executor);
    }
    @Test
    public void emptyProjects() {
        projectsMutableLiveData.setValue(new ArrayList<>());

        AddTaskViewState addTaskViewState = LiveDataTestUtils.getValueForTesting(tasksViewModel.getAddTaskViewStateLiveData());
        String toastMessage = LiveDataTestUtils.getValueForTesting(tasksViewModel.getDisplayToastMessageSingleLiveEvent());
        Boolean dismissDialog = LiveDataTestUtils.getValueForTesting(tasksViewModel.getDismissDialogSingleLiveEvent());

        assertEquals(new AddTaskViewState(new ArrayList<>(), false), addTaskViewState);
        assertNull(toastMessage);
        assertNull(dismissDialog);
    }
    @Test
    public void nullProjects() {
        projectsMutableLiveData.setValue(null);

        AddTaskViewState addTaskViewState = LiveDataTestUtils.getValueForTesting(tasksViewModel.getAddTaskViewStateLiveData());
        String toastMessage = LiveDataTestUtils.getValueForTesting(tasksViewModel.getDisplayToastMessageSingleLiveEvent());
        Boolean dismissDialog = LiveDataTestUtils.getValueForTesting(tasksViewModel.getDismissDialogSingleLiveEvent());

        assertNull(addTaskViewState);
        assertNull(toastMessage);
        assertNull(dismissDialog);
    }
    @Test
    public void addTaskFailedNoSelectedProject() {
        String taskDescription = "taskDescription";

        tasksViewModel.onTaskDescriptionChanged(taskDescription);
        tasksViewModel.onOkButtonClicked();

        AddTaskViewState addTaskViewState = LiveDataTestUtils.getValueForTesting(tasksViewModel.getAddTaskViewStateLiveData());
        String toastMessage = LiveDataTestUtils.getValueForTesting(tasksViewModel.getDisplayToastMessageSingleLiveEvent());
        Boolean dismissDialog = LiveDataTestUtils.getValueForTesting(tasksViewModel.getDismissDialogSingleLiveEvent());

        assertEquals(new AddTaskViewState(getDefaultAddTaskViewStateItems(), false), addTaskViewState);
        assertNull(toastMessage);
        assertNull(dismissDialog);
        Mockito.verifyNoMoreInteractions(toDocRepository, buildConfigResolver, mainExecutor, executor);
    }
    @Test
    public void addTaskFailedNoName() {

        long selectedProjectId = 1;

        tasksViewModel.onProjectSelected(selectedProjectId);
        tasksViewModel.onOkButtonClicked();

        AddTaskViewState addTaskViewState = LiveDataTestUtils.getValueForTesting(tasksViewModel.getAddTaskViewStateLiveData());
        String toastMessage = LiveDataTestUtils.getValueForTesting(tasksViewModel.getDisplayToastMessageSingleLiveEvent());
        Boolean dismissDialog = LiveDataTestUtils.getValueForTesting(tasksViewModel.getDismissDialogSingleLiveEvent());

        assertEquals(new AddTaskViewState(getDefaultAddTaskViewStateItems(), false), addTaskViewState);
        assertNull(toastMessage);
        assertNull(dismissDialog);
        Mockito.verifyNoMoreInteractions(toDocRepository, buildConfigResolver, mainExecutor, executor);
    }
    @Test
    public void addTaskFailedEmptyName() {

        long selectedProjectId = 1;

        tasksViewModel.onProjectSelected(selectedProjectId);
        tasksViewModel.onTaskDescriptionChanged("");
        tasksViewModel.onOkButtonClicked();

        AddTaskViewState addTaskViewState = LiveDataTestUtils.getValueForTesting(tasksViewModel.getAddTaskViewStateLiveData());
        String toastMessage = LiveDataTestUtils.getValueForTesting(tasksViewModel.getDisplayToastMessageSingleLiveEvent());
        Boolean dismissDialog = LiveDataTestUtils.getValueForTesting(tasksViewModel.getDismissDialogSingleLiveEvent());

        // Then
        assertEquals(new AddTaskViewState(getDefaultAddTaskViewStateItems(), false), addTaskViewState);
        assertNull(toastMessage);
        assertNull(dismissDialog);
        Mockito.verifyNoMoreInteractions(toDocRepository, buildConfigResolver, mainExecutor, executor);
    }
    @Test
    public void add_task_failed_because_SQL_exception_in_debug() {
        // Given
        SQLiteException exception = Mockito.mock(SQLiteException.class);
        Mockito.doThrow(exception).when(toDocRepository).addTask(any());
        Mockito.doReturn(ERROR_ADD_TASK_RES_STRING).when(application).getString(R.string.error_add_task);
        Mockito.doReturn(true).when(buildConfigResolver).isDebug();

        // When
        tasksViewModel.onProjectSelected(1);
        tasksViewModel.onTaskDescriptionChanged("taskDescription");
        tasksViewModel.onOkButtonClicked();

        AddTaskViewState addTaskViewState = LiveDataTestUtils.getValueForTesting(tasksViewModel.getAddTaskViewStateLiveData());
        String toastMessage = LiveDataTestUtils.getValueForTesting(tasksViewModel.getDisplayToastMessageSingleLiveEvent());
        Boolean dismissDialog = LiveDataTestUtils.getValueForTesting(tasksViewModel.getDismissDialogSingleLiveEvent());

        // Then
        assertEquals(new AddTaskViewState(getDefaultAddTaskViewStateItems(), false), addTaskViewState);
        assertEquals(ERROR_ADD_TASK_RES_STRING, toastMessage);
        assertNull(dismissDialog);
        Mockito.verify(executor).execute(any());
        Mockito.verify(toDocRepository).addTask(any());
        Mockito.verify(buildConfigResolver).isDebug();
        Mockito.verify(exception).printStackTrace();
        Mockito.verify(mainExecutor, Mockito.times(2)).execute(any());
        Mockito.verifyNoMoreInteractions(exception, toDocRepository, buildConfigResolver, mainExecutor, executor);
    }

    @Test
    public void add_task_failed_because_SQL_exception_in_release() {
        // Given
        SQLiteException exception = Mockito.mock(SQLiteException.class);
        Mockito.doThrow(exception).when(toDocRepository).addTask(any());
        Mockito.doReturn(ERROR_ADD_TASK_RES_STRING).when(application).getString(R.string.error_add_task);
        Mockito.doReturn(false).when(buildConfigResolver).isDebug();

        // When
        tasksViewModel.onProjectSelected(1);
        tasksViewModel.onTaskDescriptionChanged("taskDescription");
        tasksViewModel.onOkButtonClicked();

        AddTaskViewState addTaskViewState = LiveDataTestUtils.getValueForTesting(tasksViewModel.getAddTaskViewStateLiveData());
        String toastMessage = LiveDataTestUtils.getValueForTesting(tasksViewModel.getDisplayToastMessageSingleLiveEvent());
        Boolean dismissDialog = LiveDataTestUtils.getValueForTesting(tasksViewModel.getDismissDialogSingleLiveEvent());

        // Then
        assertEquals(new AddTaskViewState(getDefaultAddTaskViewStateItems(), false), addTaskViewState);
        assertEquals(ERROR_ADD_TASK_RES_STRING, toastMessage);
        assertNull(dismissDialog);
        Mockito.verify(executor).execute(any());
        Mockito.verify(toDocRepository).addTask(any());
        Mockito.verify(buildConfigResolver).isDebug();
        Mockito.verify(mainExecutor, Mockito.times(2)).execute(any());
        Mockito.verifyNoMoreInteractions(exception, toDocRepository, buildConfigResolver, mainExecutor, executor);
    }

    @NonNull
    private List<ProjectEntity> getDefaultProjectEntities() {
        List<ProjectEntity> projectEntities = new ArrayList<>();

        for (int i = 0; i < NB_PROJECT_COUNT; i++) {
            projectEntities.add(new ProjectEntity(i, BASE_PROJECT_NAME + i, COLORS[i]));
        }

        return projectEntities;
    }
    @NonNull
    private List<AddTaskViewStateItem> getDefaultAddTaskViewStateItems() {

        List<AddTaskViewStateItem> addTaskViewStateItems = new ArrayList<>();

        for (int i = 0; i < NB_PROJECT_COUNT; i++) {
            addTaskViewStateItems.add(new AddTaskViewStateItem(i, COLORS[i], BASE_PROJECT_NAME + i));
        }
        return addTaskViewStateItems;
    }
}
