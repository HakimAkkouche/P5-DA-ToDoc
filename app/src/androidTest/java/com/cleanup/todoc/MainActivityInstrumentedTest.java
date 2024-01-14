package com.cleanup.todoc;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withChild;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import com.cleanup.todoc.data.ToDocRepository;
import com.cleanup.todoc.data.entity.ProjectEntity;
import com.cleanup.todoc.ui.MainActivity;
import com.cleanup.todoc.ui.add_task.AddTaskViewStateItem;
import com.cleanup.todoc.ui.viewmodel.TasksViewState;
import com.cleanup.todoc.utils.ClickChildViewWithId;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.cleanup.todoc.R.id.fab_add_task;
import static com.cleanup.todoc.R.id.parent;
import static com.cleanup.todoc.R.id.position;
import static com.cleanup.todoc.R.id.postLayout;
import static com.cleanup.todoc.utils.ViewAssertions.hasDrawableRes;
import static com.cleanup.todoc.utils.ViewAssertions.hasRecyclerViewItemCount;
import static com.cleanup.todoc.utils.ViewAssertions.onRecyclerViewItem;
import static com.cleanup.todoc.utils.ViewAssertions.withIndex;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.RecyclerViewActions;

public class MainActivityInstrumentedTest {

    private final static String FIRST_TASK = "Tâche : 1";
    private final static String SECOND_TASK = "Tâche : 2";
    private final static String THIRD_TASK = "Tâche : 3";
    private final static String FOURTH_TASK = "Tâche : 4";
    private final static String FIFTH_TASK = "Tâche : 5";
    private final static String SIXTH_TASK = "Tâche : 6";

    @Before
    public void setup() {
        ActivityScenario.launch(MainActivity.class);
    }
    @Test
    public void addAndRemoveTask() throws InterruptedException {
        assertIsDisplayingEmptyState();

        addTask(Project.LUCIDIA, FIRST_TASK);
        addTask(Project.CIRCUS, SECOND_TASK);
        addTask(Project.TARTAMPION, THIRD_TASK);
        addTask(Project.LUCIDIA, FOURTH_TASK);
        addTask(Project.CIRCUS, FIFTH_TASK);
        addTask(Project.TARTAMPION, SIXTH_TASK);

        // Assert 6 tasks are present
        /*onView(
                allOf(withId(R.id.list_tasks),
                        isCompletelyDisplayed()))
                .check(hasRecyclerViewItemCount(6))*/
                /*.check(
                        onRecyclerViewItem(
                                0,
                                R.id.lbl_task_name,
                                withText(FIRST_TASK)
                        )
                )
                .check(
                        onRecyclerViewItem(
                                1,
                                R.id.lbl_task_name,
                                withText(SECOND_TASK)
                        )
                )
                .check(
                        onRecyclerViewItem(
                                2,
                                R.id.lbl_task_name,
                                withText(THIRD_TASK)
                        )
                ).check(
                        onRecyclerViewItem(
                                3,
                                R.id.lbl_task_name,
                                withText(FOURTH_TASK)
                        )
                ).check(
                        onRecyclerViewItem(
                                4,
                                R.id.lbl_task_name,
                                withText(FIFTH_TASK)
                        )
                ).check(
                        onRecyclerViewItem(
                                5,
                                R.id.lbl_task_name,
                                withText(SIXTH_TASK)
                        )
                )*/;
/*
        // Sort by project
        onView(withId(R.id.action_filter))
                .perform(click());
        Thread.sleep(1_000);
        onView(withId(R.id.sort_by_project))
                .perform(click());

*//*
        // Delete 1 task (Tartampion)
        onView(withId(R.id.list_tasks))
                .perform(RecyclerViewActions.actionOnItemAtPosition(3, new ClickChildViewWithId(R.id.img_delete)));


        Thread.sleep(1_000);
*//*
        // 6 tasks are present
        onView(withId(R.id.list_tasks))
                .check(hasRecyclerViewItemCount(6))
                .check(
                        onRecyclerViewItem(
                                0,
                                R.id.lbl_task_name,
                                withText(SECOND_TASK)
                        )
                )
                .check(
                        onRecyclerViewItem(
                                1,
                                R.id.lbl_task_name,
                                withText(FIFTH_TASK)
                        )
                ).check(
                        onRecyclerViewItem(
                                2,
                                R.id.lbl_task_name,
                                withText(FIRST_TASK)
                        )
                )
                .check(
                        onRecyclerViewItem(
                                3,
                                R.id.lbl_task_name,
                                withText(FOURTH_TASK)
                        )
                ).check(
                        onRecyclerViewItem(
                                4,
                                R.id.lbl_task_name,
                                withText(THIRD_TASK)
                        )
                )
                .check(
                        onRecyclerViewItem(
                                5,
                                R.id.lbl_task_name,
                                withText(SIXTH_TASK)
                        )
                );

        // Sort by chronological desc
        onView(withId(R.id.action_filter))
                .perform(click());

        Thread.sleep(1_000);
        onView(withId(R.id.sort_recent_first))
                .perform(click());

        // Assert 6 tasks are present
        onView(withId(R.id.list_tasks))
                .check(hasRecyclerViewItemCount(6))
                .check(
                        onRecyclerViewItem(
                                0,
                                R.id.lbl_task_name,
                                withText(SIXTH_TASK)
                        )
                )
                .check(
                        onRecyclerViewItem(
                                1,
                                R.id.lbl_task_name,
                                withText(FIFTH_TASK)
                        )
                )
                .check(
                        onRecyclerViewItem(
                                2,
                                R.id.lbl_task_name,
                                withText(FOURTH_TASK)
                        )
                )
                .check(
                        onRecyclerViewItem(
                                3,
                                R.id.lbl_task_name,
                                withText(THIRD_TASK)
                        )
                ).check(
                        onRecyclerViewItem(
                                4,
                                R.id.lbl_task_name,
                                withText(SECOND_TASK)
                        )
                )
                .check(
                        onRecyclerViewItem(
                                5,
                                R.id.lbl_task_name,
                                withText(FIRST_TASK)
                        )
                );

        // Delete 1 task (Circus)
        onView(withId(R.id.list_tasks))
                .perform(RecyclerViewActions.actionOnItemAtPosition(3, new ClickChildViewWithId(R.id.img_delete)));
        Thread.sleep(1_000);


        // Sort by date asc
        onView(withId(R.id.action_filter))
                .perform(click());
        Thread.sleep(1_000);
        onView(withId(R.id.sort_alphabetical))
                .perform(click());
        // Assert 5 task is present
        onView(withId(R.id.list_tasks))
                .check(hasRecyclerViewItemCount(5))
                .check(
                        onRecyclerViewItem(
                                0,
                                R.id.lbl_task_name,
                                withText(FIRST_TASK)
                        )
                )
                .check(
                        onRecyclerViewItem(
                                1,
                                R.id.lbl_task_name,
                                withText(SECOND_TASK)
                        )
                )
                .check(
                        onRecyclerViewItem(
                                3,
                                R.id.lbl_task_name,
                                withText(THIRD_TASK)
                        )
                ).check(
                        onRecyclerViewItem(
                                4,
                                R.id.lbl_task_name,
                                withText(FIFTH_TASK)
                        )
                )
                .check(
                        onRecyclerViewItem(
                                5,
                                R.id.lbl_task_name,
                                withText(SIXTH_TASK)
                        )
                );

        // Sort by date asc
        onView(withId(R.id.action_filter))
                .perform(click());
        Thread.sleep(1_000);
        onView(withId(R.id.sort_recent_first))
                .perform(click());

        // 6 task are present
        onView(withId(R.id.list_tasks))
                .check(hasRecyclerViewItemCount(5))
                .check(
                        onRecyclerViewItem(
                                0,
                                R.id.lbl_task_name,
                                withText(SIXTH_TASK)
                        )
                ).check(
                        onRecyclerViewItem(
                                1,
                                R.id.lbl_task_name,
                                withText(FIFTH_TASK)
                        )
                ).check(
                        onRecyclerViewItem(
                                2,
                                R.id.lbl_task_name,
                                withText(THIRD_TASK)
                        )
                ).check(
                        onRecyclerViewItem(
                                3,
                                R.id.lbl_task_name,
                                withText(SECOND_TASK)
                        )
                ).check(
                        onRecyclerViewItem(
                                4,
                                R.id.lbl_task_name,
                                withText(FIRST_TASK)
                        )
                );

        // Delete 1 task
        onView(withId(R.id.list_tasks))
                .perform(RecyclerViewActions.actionOnItemAtPosition(3, new ClickChildViewWithId(R.id.img_delete)));

        Thread.sleep(1_000);

        // Sort by alphabetical desc
        onView(withId(R.id.action_filter))
                .perform(click());
        Thread.sleep(1_000);
        onView(withId(R.id.sort_alphabetical))
                .perform(click());

        // Assert 1 header & 1 task are present
        onView(withId(R.id.list_tasks))
                .check(hasRecyclerViewItemCount(4))
                .check(
                        onRecyclerViewItem(
                                0,
                                R.id.lbl_task_name,
                                withText(FIRST_TASK)
                        )
                ).check(
                        onRecyclerViewItem(
                                1,
                                R.id.lbl_task_name,
                                withText(THIRD_TASK)
                        )
                ).check(
                        onRecyclerViewItem(
                                2,
                                R.id.lbl_task_name,
                                withText(THIRD_TASK)
                        )
                ).check(
                        onRecyclerViewItem(
                                3,
                                R.id.lbl_task_name,
                                withText(FIRST_TASK)
                        )
                );

        onView(withId(R.id.list_tasks))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, new ClickChildViewWithId(R.id.img_delete)));

        Thread.sleep(1_000);
        onView(withId(R.id.list_tasks))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, new ClickChildViewWithId(R.id.img_delete)));

        Thread.sleep(1_000);
        onView(withId(R.id.list_tasks))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, new ClickChildViewWithId(R.id.img_delete)));

        Thread.sleep(1_000);
        onView(withId(R.id.list_tasks))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, new ClickChildViewWithId(R.id.img_delete)));

        Thread.sleep(1_000);

        // Back to empty state
        assertIsDisplayingEmptyState();

        // Sort by task (chronological)
        onView(withId(R.id.action_filter))
                .perform(click());
        onView(withId(R.id.sort_alphabetical_inverted))
                .perform(click());

        // Still on empty state
        assertIsDisplayingEmptyState();*/
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

        onView(
                allOf(withParent(allOf(withId(R.id.list_tasks),
                                withParent(withId(R.id.list_tasks)))),
                        isDisplayed()))
                .check(hasRecyclerViewItemCount(1));
/*        onView(
                allOf(
                        withId(R.id.task_empty_state_item_image_view_check),
                        isCompletelyDisplayed()
                )
        ).check(hasRecyclerViewItemCount(1));*/
    }

    private void addTask(@NonNull Project project, @NonNull String name) throws InterruptedException {
        onView(
                allOf(withId(R.id.fab_add_task), withContentDescription("Ajouter une tâche"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.main_layout),
                                        1),
                                1),
                        isDisplayed())).perform(click());

        Thread.sleep(1_000);

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

        Thread.sleep(2_000);
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
