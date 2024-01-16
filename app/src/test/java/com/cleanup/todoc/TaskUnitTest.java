package com.cleanup.todoc;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Executor;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.cleanup.todoc.data.BuildConfigResolver;
import com.cleanup.todoc.data.ToDocRepository;
import com.cleanup.todoc.data.entity.ProjectEntity;
import com.cleanup.todoc.data.entity.ProjectTasksRelation;
import com.cleanup.todoc.data.entity.TaskEntity;
import com.cleanup.todoc.ui.viewmodel.TasksViewModel;
import com.cleanup.todoc.ui.viewmodel.TasksViewState;
import com.cleanup.todoc.utils.LiveDataTestUtils;
import com.cleanup.todoc.utils.TestExecutor;

/**
 * Unit tests for tasks
 *
 * @author Hakim AKKOUCHE
 */
@RunWith(MockitoJUnitRunner.class)
public class TaskUnitTest {

    private static final int NB_PROJECT_TASKS_COUNT = 3;

    private static final String BASE_PROJECT_NAME = "Project : ";
    private static final String BASE_TASK_NAME = "Task : ";

    private static final long DATETIME = 1704850881;

    private static final int[] COLORS = new int[]{Color.parseColor("#3177E1"), Color.parseColor("#CE2BE1"), Color.parseColor("#7AE62D")};

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private final ToDocRepository toDocRepository = Mockito.mock(ToDocRepository.class);
    private final BuildConfigResolver buildConfigResolver = Mockito.mock(BuildConfigResolver.class);
    private final Executor executor = Mockito.spy(new TestExecutor());

    private final MutableLiveData<List<ProjectTasksRelation>> projectTasksRelationMutableLiveData = new MutableLiveData<>();

    private TasksViewModel tasksViewModel;

    @Before
    public void setup() {
        Mockito.doReturn(projectTasksRelationMutableLiveData).when(toDocRepository).getAllTasksProjectLiveData();

        projectTasksRelationMutableLiveData.setValue(getDefaultProjectTasksRelation());

        tasksViewModel = new TasksViewModel(toDocRepository, executor);

        Mockito.verify(toDocRepository).getAllTasksProjectLiveData();
    }


    @Test
    public void basic_test() {
        List<TasksViewState> tasksViewStates = LiveDataTestUtils.getValueForTesting(tasksViewModel.getViewStateLiveData());

        assertEquals(getDefaultTasksViewStates(), tasksViewStates);

        Mockito.verify(executor, Mockito.never()).execute(any());
        Mockito.verifyNoMoreInteractions(toDocRepository, executor);
    }

    @Test
    public void empty_tasks() {
        projectTasksRelationMutableLiveData.setValue(new ArrayList<>());

        List<TasksViewState> tasksViewStates = LiveDataTestUtils.getValueForTesting(tasksViewModel.getViewStateLiveData());

        assertEquals(Collections.singletonList(new TasksViewState.EmptyState()), tasksViewStates);
    }

    @Test
    public void sortByProject() {
        tasksViewModel.onByProjectSortClicked();

        List<TasksViewState> tasksViewStates = LiveDataTestUtils.getValueForTesting(tasksViewModel.getViewStateLiveData());

        assertEquals(getDefaultTaskViewStatesSort(), tasksViewStates);
    }

    @Test
    public void sortAlphabeticalAsc() {
        tasksViewModel.onAlphabeticalSortAscClicked();

        List<TasksViewState> tasksViewStates = LiveDataTestUtils.getValueForTesting(tasksViewModel.getViewStateLiveData());

        assertEquals(getDefaultTaskViewStatesSort(), tasksViewStates);
    }
    @Test
    public void sortAlphabeticalDesc() {
        tasksViewModel.onAlphabeticalSortDescClicked();

        List<TasksViewState> tasksViewStates = LiveDataTestUtils.getValueForTesting(tasksViewModel.getViewStateLiveData());

        assertEquals(getTaskViewStatesAlphabeticalDesc(), tasksViewStates);
    }
    @Test
    public void sortChronologicalAsc() {
        tasksViewModel.onChronologicalSortAscClicked();

        List<TasksViewState> tasksViewStates = LiveDataTestUtils.getValueForTesting(tasksViewModel.getViewStateLiveData());

        assertEquals(getTaskViewStatesAlphabeticalAsc(), tasksViewStates);
    }
    @Test
    public void sortChronologicalDesc() {
        tasksViewModel.onChronologicalSortDescClicked();

        List<TasksViewState> tasksViewStates = LiveDataTestUtils.getValueForTesting(tasksViewModel.getViewStateLiveData());

        assertEquals(getTaskViewStatesAlphabeticalDesc(), tasksViewStates);
    }

    @Test
    public void onDeleteTaskButtonClicked() {
        long idTask = 3;

        tasksViewModel.onDeleteTaskButtonClicked(idTask);

        Mockito.verify(toDocRepository).deleteTask(idTask);
        Mockito.verify(executor).execute(any());
        Mockito.verifyNoMoreInteractions(toDocRepository,executor);
    }

    @NonNull
    private List<ProjectTasksRelation> getDefaultProjectTasksRelation() {
        List<ProjectTasksRelation> projectTasksRelations = new ArrayList<>();

        int idTask = 0;

        for (int i = 0; i < NB_PROJECT_TASKS_COUNT; i++) {

            ProjectEntity projectEntity = new ProjectEntity(i, BASE_PROJECT_NAME + i, COLORS[i]);
            List<TaskEntity> taskEntities = new ArrayList<>();

            for (int j = 0; j < NB_PROJECT_TASKS_COUNT; j++) {
                idTask++;
                taskEntities.add(
                        new TaskEntity(idTask, i, BASE_TASK_NAME + idTask,
                                DATETIME + idTask
                        )
                );
            }
            projectTasksRelations.add(new ProjectTasksRelation(projectEntity, taskEntities));
        }
        return projectTasksRelations;
    }

    @NonNull
    private List<TasksViewState> getDefaultTasksViewStates() {
        List<TasksViewState> tasksViewStates = new ArrayList<>();

        int idTask = 0;

        for (int i = 0; i < NB_PROJECT_TASKS_COUNT; i++) {
            for (int j = 0; j < NB_PROJECT_TASKS_COUNT; j++) {
                idTask++;
                tasksViewStates.add(
                        new TasksViewState.Task( BASE_PROJECT_NAME + i, COLORS[i], idTask,
                                BASE_TASK_NAME + idTask,
                                DATETIME + idTask
                        )
                );
            }
        }
        return tasksViewStates;
    }

    @NonNull
    private List<TasksViewState> getDefaultTaskViewStatesSort() {
        List<TasksViewState> taskViewStates = new ArrayList<>();

        int idTask = 0;
        for (int i = 0; i < NB_PROJECT_TASKS_COUNT; i++) {
            for (int j = 0; j < NB_PROJECT_TASKS_COUNT; j++) {
                idTask++;
                taskViewStates.add(
                        new TasksViewState.Task(BASE_PROJECT_NAME + i, COLORS[i], idTask,
                                BASE_TASK_NAME + idTask,
                                DATETIME + idTask
                        )
                );
            }
        }

        return taskViewStates;
    }

    @NonNull
    private List<TasksViewState> getTaskViewStatesAlphabeticalDesc() {
        List<TasksViewState> taskViewStates = new ArrayList<>();

        int idTask = 0;
        Set<TasksViewState.Task> set = new TreeSet<>((o1, o2) -> Long.compare(o2.getCreationTimestamp(), o1.getCreationTimestamp()));
        for (int i = 0; i < NB_PROJECT_TASKS_COUNT; i++) {
            for (int j = 0; j < NB_PROJECT_TASKS_COUNT; j++) {
                idTask++;
                set.add(
                        new TasksViewState.Task( BASE_PROJECT_NAME + i,COLORS[i], idTask,
                                BASE_TASK_NAME + idTask,
                                DATETIME + idTask
                        )
                );
            }
        }
        taskViewStates.addAll(set);
        return taskViewStates;
    }
    @NonNull
    private List<TasksViewState> getTaskViewStatesAlphabeticalAsc() {
        List<TasksViewState> taskViewStates = new ArrayList<>();

        int idTask = 0;
        Set<TasksViewState.Task> set = new TreeSet<>((o1, o2) -> Long.compare(o1.getCreationTimestamp(), o2.getCreationTimestamp()));
        for (int i = 0; i < NB_PROJECT_TASKS_COUNT; i++) {
            for (int j = 0; j < NB_PROJECT_TASKS_COUNT; j++) {
                idTask++;
                set.add(
                        new TasksViewState.Task( BASE_PROJECT_NAME + i,COLORS[i], idTask,
                                BASE_TASK_NAME + idTask,
                                DATETIME + idTask
                        )
                );
            }
        }
        taskViewStates.addAll(set);
        return taskViewStates;
    }
}