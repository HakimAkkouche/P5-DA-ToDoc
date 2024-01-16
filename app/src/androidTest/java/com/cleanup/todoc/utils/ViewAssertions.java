package com.cleanup.todoc.utils;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.ViewAssertion;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class ViewAssertions {
    public static ViewAssertion hasDrawableRes(
            @DrawableRes int expectedDrawableId,
            @Nullable @ColorRes Integer tintColorRes
    ) {
        return matches(new TypeSafeMatcher<View>() {
            private String resourceName;

            @Override
            protected boolean matchesSafely(View target) {
                Resources resources = target.getContext().getResources();
                resourceName = resources.getResourceEntryName(expectedDrawableId);

                if (!(target instanceof ImageView)) {
                    return false;
                }

                ImageView imageView = (ImageView) target;

                if (expectedDrawableId == -1) {
                    return imageView.getDrawable() == null;
                }

                Drawable expectedDrawable = resources.getDrawable(expectedDrawableId, null);

                if (expectedDrawable == null) {
                    return false;
                }

                if (tintColorRes != null) {
                    expectedDrawable.setTint(resources.getColor(tintColorRes, null));
                }

                Bitmap bitmap = getBitmap(imageView.getDrawable());
                Bitmap otherBitmap = getBitmap(expectedDrawable);

                return bitmap.sameAs(otherBitmap);
            }

            private Bitmap getBitmap(@NonNull Drawable drawable) {
                Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);
                return bitmap;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with drawable from resource id: ");
                description.appendValue(expectedDrawableId);
                if (resourceName != null) {
                    description.appendText("[");
                    description.appendText(resourceName);
                    description.appendText("]");
                }
            }
        });
    }

    public static ViewAssertion hasRecyclerViewItemCount(int itemCount) {
        return (view, noViewFoundException) -> {
            if (noViewFoundException != null) {
                throw noViewFoundException;
            }

            RecyclerView recyclerView = (RecyclerView) view;
            if (recyclerView.getAdapter() != null) {
                assertThat(recyclerView.getAdapter().getItemCount(), is(itemCount));
            }
        };
    }
    public static Matcher<View> withIndex(final Matcher<View> matcher, final int index) {
        return new TypeSafeMatcher<View>() {
            int currentIndex = 0;

            @Override
            public void describeTo(Description description) {
                description.appendText("with index: ");
                description.appendValue(index);
                matcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                return matcher.matches(view) && currentIndex++ == index;
            }
        };
    }
}
