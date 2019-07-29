package saedc.example.com.Di;

import android.app.Application;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.annotation.NonNull;

import saedc.example.com.Model.Database.AppDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class AppModule {
    private Application application;


    public AppModule(Application application) {
        this.application = application;
    }

    @Singleton
    @Provides
    Application providesApplication() {
        return application;
    }

    @Singleton
    @Provides
    AppDatabase provideAppDatabase(Application application) {

        return Room.databaseBuilder(application, AppDatabase.class, "app_database")
.addMigrations(MIGRATION_1_2,MIGRATION_2_3,MIGRATION_3_4)
//                .addMigrations(MIGRATION_1_2,MIGRATION_2_3)
//                .allowMainThreadQueries()
                .addCallback(new RoomDatabase.Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        String Food="طعام";
                        String Bills="فاتورة";
                        String Occasions="مناسبة";
                        String travel="سفر";
                        String Transportation="مواصلات";
                        String Shopping="تسوق";
                        String debt="دين";
                        String healthcare="عناية صحية";
                        String Fixes="إصلاحات";
                        String Other="اخرى";


                        String query = "INSERT INTO SPENDING_GROUP VALUES ('1', '"+Food+"'), ('2', '"+Bills+"')," +
                                "('3', '"+Occasions+"') , ('4', '"+travel+"')," +
                                "('5', '"+Transportation+"'),('6', '"+Shopping+"')," +
                                "('7', '"+debt+"'), ('8', '"+healthcare+"')," +
                                "('9', '"+Fixes+"'), ('10', '"+Other+"')";
                        db.execSQL(query);
                    }

                    @Override
                    public void onOpen(@NonNull SupportSQLiteDatabase db) {
                        super.onOpen(db);


                    }
                })
                .build();


    }
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `user` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT, `salary` REAL, `salary_start_date` INTEGER)");


// Since we didn't alter the table, there's nothing else to do here.
        }
    };

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `account` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT, `price` REAL, `date` INTEGER)");


// Since we didn't alter the table, there's nothing else to do here.
        }
    };


    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
          database.execSQL("ALTER TABLE account "
                   + " ADD COLUMN type TEXT");

// Since we didn't alter the table, there's nothing else to do here.
        }
    };


//    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
//        @Override
//        public void migrate(SupportSQLiteDatabase database) {
//            database.execSQL("ALTER TABLE spending "
//                    + " ADD COLUMN IncomeSource TEXT");
//// Since we didn't alter the table, there's nothing else to do here.
//        }
//    };


}
