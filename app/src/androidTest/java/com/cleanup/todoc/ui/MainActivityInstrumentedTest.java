package com.cleanup.todoc.ui;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import com.cleanup.todoc.R;
import com.cleanup.todoc.data.ToDocDatabase;
import com.cleanup.todoc.ui.add_task.AddTaskViewStateItem;
import com.cleanup.todoc.utils.ViewAssertions;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static com.cleanup.todoc.utils.ViewAssertions.hasDrawableRes;
import static com.cleanup.todoc.utils.ViewAssertions.hasRecyclerViewItemCount;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.core.internal.deps.dagger.Module;
import androidx.test.espresso.matcher.ViewMatchers;

import java.io.IOException;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MainActivityInstrumentedTest {

    private final static String FIRST_TASK = "Tâche : 1";
    private final static String SECOND_TASK = "Tâche : 2";
    private final static String THIRD_TASK = "Tâche : 3";
    private final static String FOURTH_TASK = "Tâche : 4";
    private final static String FIFTH_TASK = "Tâche : 5";
    private final static String SIXTH_TASK = "Tâche : 6";
    private ToDocDatabase db;


    @Before
    public void setup() {
        ActivityScenario.launch(MainActivity.class);
    }

    @Test
    public void empty() {
        assertIsDisplayingEmptyState();
    }
    @Test
    public void addTask() throws InterruptedException {
        assertIsDisplayingEmptyState();

        addTask(Project.LUCIDIA, FIRST_TASK);
        onView(ViewAssertions.withIndex(ViewMatchers.withId(R.id.list_tasks),1))
                .check(hasRecyclerViewItemCount(1));
        deleteItemAtPosition(0);
    }
    @Test
    public void deleteTask() throws InterruptedException {
        assertIsDisplayingEmptyState();

        addTask(Project.LUCIDIA, FIRST_TASK);
        onView(ViewAssertions.withIndex(ViewMatchers.withId(R.id.list_tasks),1))
                .check(hasRecyclerViewItemCount(1));
        deleteItemAtPosition(0);
    }

    @Test
    public void sortTaskByProject() throws InterruptedException {
        assertIsDisplayingEmptyState();

        addTask(Project.LUCIDIA, FIRST_TASK);
        addTask(Project.CIRCUS, SECOND_TASK);
        addTask(Project.TARTAMPION, THIRD_TASK);
        addTask(Project.LUCIDIA, FOURTH_TASK);
        addTask(Project.CIRCUS, FIFTH_TASK);
        addTask(Project.TARTAMPION, SIXTH_TASK);

        onView(ViewAssertions.withIndex(ViewMatchers.withId(R.id.list_tasks),1))
                .check(hasRecyclerViewItemCount(6));

        onView(
                allOf(withId(R.id.action_filter),
                        childAtPosition(
                                childAtPosition(
                                        withId(com.google.android.material.R.id.action_bar),
                                        1),
                                0),
                        isDisplayed())).perform(click());
        Thread.sleep(1_000);
        onView(
                allOf(withId(androidx.transition.R.id.title), withText(R.string.sort_by_project),
                        childAtPosition(
                                childAtPosition(
                                        withId(com.google.android.material.R.id.content),
                                        0),
                                0),
                        isDisplayed())).perform(click());

        Thread.sleep(1_000);

        onView(ViewAssertions.withIndex(withId(R.id.list_tasks),1))
                .check(hasRecyclerViewItemCount(6));

        onView(ViewAssertions.withIndex(withId(R.id.lbl_task_name),0))
                .check(matches(withText(THIRD_TASK)));

        onView(ViewAssertions.withIndex(withId(R.id.lbl_task_name),1))
                .check(matches(withText(SIXTH_TASK)));

        onView(ViewAssertions.withIndex(withId(R.id.lbl_task_name),2))
                .check(matches(withText(FIRST_TASK)));

        onView(ViewAssertions.withIndex(withId(R.id.lbl_task_name),3))
                .check(matches(withText(FOURTH_TASK)));

        onView(ViewAssertions.withIndex(withId(R.id.lbl_task_name),4))
                .check(matches(withText(SECOND_TASK)));

        onView(ViewAssertions.withIndex(withId(R.id.lbl_task_name),5))
                .check(matches(withText(FIFTH_TASK)));

        for (int i = 0; i < 6; i++) {
            deleteItemAtPosition(0);
            Thread.sleep(1_000);
        }
    }
    @Test
    public void sortTaskByAlphabeticalAsc() throws InterruptedException {
        assertIsDisplayingEmptyState();

        addTask(Project.TARTAMPION, THIRD_TASK);
        addTask(Project.CIRCUS, SECOND_TASK);
        addTask(Project.LUCIDIA, FIRST_TASK);

        onView(ViewAssertions.withIndex(ViewMatchers.withId(R.id.list_tasks),1))
                .check(hasRecyclerViewItemCount(3));

        onView(
                allOf(withId(R.id.action_filter),
                        childAtPosition(
                                childAtPosition(
                                        withId(com.google.android.material.R.id.action_bar),
                                        1),
                                0),
                        isDisplayed())).perform(click());
        Thread.sleep(1_000);
        onView(
                allOf(withId(androidx.transition.R.id.title), withText(R.string.sort_alphabetical),
                        childAtPosition(
                                childAtPosition(
                                        withId(com.google.android.material.R.id.content),
                                        0),
                                0),
                        isDisplayed())).perform(click());

        Thread.sleep(1_000);

        onView(ViewAssertions.withIndex(withId(R.id.list_tasks),1))
                .check(hasRecyclerViewItemCount(3));

        onView(ViewAssertions.withIndex(withId(R.id.lbl_task_name),0))
                .check(matches(withText(FIRST_TASK)));

        onView(ViewAssertions.withIndex(withId(R.id.lbl_task_name),1))
                .check(matches(withText(SECOND_TASK)));

        onView(ViewAssertions.withIndex(withId(R.id.lbl_task_name),2))
                .check(matches(withText(THIRD_TASK)));

        for (int i = 0; i < 3; i++) {
            deleteItemAtPosition(0);
            Thread.sleep(1_000);
        }
    }

    @Test
    public void sortTaskByAlphabeticalDesc() throws InterruptedException {
        assertIsDisplayingEmptyState();

        addTask(Project.LUCIDIA, FIRST_TASK);
        addTask(Project.TARTAMPION, THIRD_TASK);
        addTask(Project.CIRCUS, SECOND_TASK);

        onView(ViewAssertions.withIndex(ViewMatchers.withId(R.id.list_tasks),1))
                .check(hasRecyclerViewItemCount(3));

        onView(
                allOf(withId(R.id.action_filter),
                        childAtPosition(
                                childAtPosition(
                                        withId(com.google.android.material.R.id.action_bar),
                                        1),
                                0),
                        isDisplayed())).perform(click());
        Thread.sleep(1_000);
        onView(
                allOf(withId(androidx.transition.R.id.title), withText(R.string.sort_alphabetical_invert),
                        childAtPosition(
                                childAtPosition(
                                        withId(com.google.android.material.R.id.content),
                                        0),
                                0),
                        isDisplayed())).perform(click());

        Thread.sleep(1_000);

        onView(ViewAssertions.withIndex(withId(R.id.list_tasks),1))
                .check(hasRecyclerViewItemCount(3));

        onView(ViewAssertions.withIndex(withId(R.id.lbl_task_name),0))
                .check(matches(withText(THIRD_TASK)));

        onView(ViewAssertions.withIndex(withId(R.id.lbl_task_name),1))
                .check(matches(withText(SECOND_TASK)));

        onView(ViewAssertions.withIndex(withId(R.id.lbl_task_name),2))
                .check(matches(withText(FIRST_TASK)));

        for (int i = 0; i < 3; i++) {
            deleteItemAtPosition(0);
            Thread.sleep(1_000);
        }
    }

    @Test
    public void sortTaskByChronologicalAsc() throws InterruptedException {
        assertIsDisplayingEmptyState();

        addTask(Project.LUCIDIA, FIRST_TASK);
        addTask(Project.CIRCUS, SECOND_TASK);
        addTask(Project.TARTAMPION, THIRD_TASK);

        onView(ViewAssertions.withIndex(ViewMatchers.withId(R.id.list_tasks),1))
                .check(hasRecyclerViewItemCount(3));

        //change order before to see if the chronological filter works
        onView(
                allOf(withId(R.id.action_filter),
                        childAtPosition(
                                childAtPosition(
                                        withId(com.google.android.material.R.id.action_bar),
                                        1),
                                0),
                        isDisplayed())).perform(click());
        Thread.sleep(1_000);
        onView(
                allOf(withId(androidx.transition.R.id.title), withText(R.string.sort_alphabetical_invert),
                        childAtPosition(
                                childAtPosition(
                                        withId(com.google.android.material.R.id.content),
                                        0),
                                0),
                        isDisplayed())).perform(click());

        Thread.sleep(1_000);

        //change order to older first
        onView(
                allOf(withId(R.id.action_filter),
                        childAtPosition(
                                childAtPosition(
                                        withId(com.google.android.material.R.id.action_bar),
                                        1),
                                0),
                        isDisplayed())).perform(click());
        Thread.sleep(1_000);
        onView(
                allOf(withId(androidx.transition.R.id.title), withText(R.string.sort_oldest_first),
                        childAtPosition(
                                childAtPosition(
                                        withId(com.google.android.material.R.id.content),
                                        0),
                                0),
                        isDisplayed())).perform(click());

        Thread.sleep(1_000);

        onView(ViewAssertions.withIndex(withId(R.id.lbl_task_name),0))
                .check(matches(withText(FIRST_TASK)));

        onView(ViewAssertions.withIndex(withId(R.id.lbl_task_name),1))
                .check(matches(withText(SECOND_TASK)));

        onView(ViewAssertions.withIndex(withId(R.id.lbl_task_name),2))
                .check(matches(withText(THIRD_TASK)));

        for (int i = 0; i < 3; i++) {
            deleteItemAtPosition(0);
            Thread.sleep(1_000);
        }
    }

    @Test
    public void sortTaskByChronologicalDesc() throws InterruptedException {
        assertIsDisplayingEmptyState();

        addTask(Project.LUCIDIA, FIRST_TASK);
        addTask(Project.CIRCUS, SECOND_TASK);
        addTask(Project.TARTAMPION, THIRD_TASK);

        onView(ViewAssertions.withIndex(ViewMatchers.withId(R.id.list_tasks),1))
                .check(hasRecyclerViewItemCount(3));


        onView(
                allOf(withId(R.id.action_filter),
                        childAtPosition(
                                childAtPosition(
                                        withId(com.google.android.material.R.id.action_bar),
                                        1),
                                0),
                        isDisplayed())).perform(click());
        Thread.sleep(1_000);
        onView(
                allOf(withId(androidx.transition.R.id.title), withText(R.string.sort_recent_first),
                        childAtPosition(
                                childAtPosition(
                                        withId(com.google.android.material.R.id.content),
                                        0),
                                0),
                        isDisplayed())).perform(click());

        Thread.sleep(1_000);

        onView(ViewAssertions.withIndex(withId(R.id.list_tasks),1))
                .check(hasRecyclerViewItemCount(3));

        onView(ViewAssertions.withIndex(withId(R.id.lbl_task_name),0))
                .check(matches(withText(THIRD_TASK)));

        onView(ViewAssertions.withIndex(withId(R.id.lbl_task_name),1))
                .check(matches(withText(SECOND_TASK)));

        onView(ViewAssertions.withIndex(withId(R.id.lbl_task_name),2))
                .check(matches(withText(FIRST_TASK)));

        for (int i = 0; i < 3; i++) {
            deleteItemAtPosition(0);
            Thread.sleep(1_000);
        }
    }


    private void assertIsDisplayingEmptyState() {

        onView(
                allOf(withId(R.id.task_empty_state_item_text_view),
                        withParent(withParent(withId(R.id.list_tasks))),
                        isDisplayed()))
                .check(matches(withText(R.string.no_task)));

        onView(
                allOf(withId(R.id.task_empty_state_item_image_view_check),
                        withParent(withParent(withId(R.id.list_tasks))),
                        isDisplayed()))
                .check(hasDrawableRes(R.drawable.ic_check_128, R.color.black));

        onView(ViewAssertions.withIndex(withId(R.id.list_tasks),1))
                .check(hasRecyclerViewItemCount(1));

    }

    private void addTask(@NonNull Project project, @NonNull String name) throws InterruptedException {
        onView(
                allOf(withId(R.id.fab_add_task),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.main_layout),
                                        1),
                                1),
                        isDisplayed())).perform(click());

        Thread.sleep(0_500);

        //onView(withId(R.id.txt_task_name)).perform(click());
        onView(
                allOf(withId(R.id.txt_task_name),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.txt_task_name_layout),
                                        0),
                                0),
                        isDisplayed())).perform(replaceText(name), closeSoftKeyboard());

        Thread.sleep(1_000);

        onView(withId(R.id.project_autocomplete)).perform(click());

        onData(isA(AddTaskViewStateItem.class))
                .inRoot(isPlatformPopup())
                .atPosition(project.spinnerIndex)
                .perform(click());

        onView(
                allOf(withId(R.id.create_task_button_ok), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.RelativeLayout")),
                                        3),
                                1),
                        isDisplayed())).perform(click());

        Thread.sleep(1_000);
    }

    private void deleteItemAtPosition(int i) {
        onView(
                allOf(withId(R.id.img_delete),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.list_tasks),
                                        i),
                                1),
                        isDisplayed())).perform(click());
    }

    private enum Project {
        TARTAMPION(0, "Projet Tartampion"),
        LUCIDIA(1, "Projet Lucidia"),
        CIRCUS(2, "Projet Circus");

        private final int spinnerIndex;
        private final String name;

        Project(int spinnerIndex, @StringRes String name) {
            this.spinnerIndex = spinnerIndex;
            this.name = name;
        }
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
