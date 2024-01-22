package com.cleanup.todoc.data;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.cleanup.todoc.R;
import com.cleanup.todoc.data.dao.ProjectDao;
import com.cleanup.todoc.data.dao.TaskDao;
import com.cleanup.todoc.data.entity.ProjectEntity;
import com.cleanup.todoc.data.entity.TaskEntity;

import java.util.concurrent.Executor;

@Database(
        entities = {
                ProjectEntity.class,
                TaskEntity.class
        },
        version = 1,
        exportSchema = false
)
public abstract class ToDocDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "ToDoc_Database";

    private static ToDocDatabase sInstance;

    public static ToDocDatabase getInstance(@NonNull Application application, @NonNull Executor executor, BuildConfigResolver buildConfigResolver){
        if (sInstance == null){
            synchronized (ToDocDatabase.class){
                if(sInstance == null){
                    sInstance = createDatabase(application, executor, buildConfigResolver);
                }
            }
        }
        return sInstance;
    }
    private static ToDocDatabase createDatabase(@NonNull Application application, @NonNull Executor executor, BuildConfigResolver buildConfigResolver) {
        Builder<ToDocDatabase> builder;
        if (buildConfigResolver.isRunningAndroidTest()) {
            // Use a different database configuration for testing
            builder = Room.inMemoryDatabaseBuilder(application, ToDocDatabase.class)
                    .allowMainThreadQueries();
        } else {
            // Use the regular database configuration
            builder = Room.databaseBuilder(application, ToDocDatabase.class, DATABASE_NAME);
        }

        builder.addCallback(new Callback() {
            /**
             * Called when the database is created for the first time. This is called after all the
             * tables are created.
             *
             * @param db The database.
             */
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                executor.execute(() -> {
                    ProjectDao projectDao = sInstance.getProjectDao();

                    projectDao.insert(
                            new ProjectEntity(
                                    "Project Tartampion",
                            ResourcesCompat.getColor(application.getResources(), R.color.Tartampion,null))
                    );
                    projectDao.insert(
                            new ProjectEntity(
                                    "Project Lucidia",
                                    ResourcesCompat.getColor(application.getResources(), R.color.Lucidia,null))
                    );
                    projectDao.insert(
                            new ProjectEntity(
                                    "Project Circus",
                                    ResourcesCompat.getColor(application.getResources(), R.color.Circus,null))
                    );
                });
            }
        });
        if (buildConfigResolver.isDebug()) {
            builder.fallbackToDestructiveMigration();
        }
        return builder.build();
    }

    public abstract ProjectDao getProjectDao();
    public abstract TaskDao getTaskDao();
}
