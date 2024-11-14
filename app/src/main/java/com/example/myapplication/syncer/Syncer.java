package com.example.myapplication.syncer;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.myapplication.models.BookingClassModel;
import com.example.myapplication.models.BookingModel;
import com.example.myapplication.models.ClassModel;
import com.example.myapplication.models.CourseModel;
import com.example.myapplication.models.UserModel;
import com.example.myapplication.sqlite.BookingClassRepository;
import com.example.myapplication.sqlite.BookingRepository;
import com.example.myapplication.sqlite.ClassRepository;
import com.example.myapplication.sqlite.CourseRepository;
import com.example.myapplication.sqlite.UserRepository;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.sql.Time;
import java.util.List;
import java.util.Map;

public class Syncer extends Worker {

    private final String USERS_COLLECTION = "users";
    private final String COURSES_COLLECTION = "courses";
    private final String CLASSES_COLLECTION = "classes";
    private final String BOOKINGS_COLLECTION = "bookings";

    private FirebaseFirestore db;
    private CourseRepository courseRepository;
    private ClassRepository classRepository;
    private UserRepository userRepository;
    private BookingRepository bookingRepository;
    private BookingClassRepository bookingClassRepository;

    public Syncer(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        db = FirebaseFirestore.getInstance();
        courseRepository = new CourseRepository(context);
        classRepository = new ClassRepository(context);
        userRepository = new UserRepository(context);
        bookingRepository = new BookingRepository(context);
        bookingClassRepository = new BookingClassRepository(context);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            syncFromSQLiteToFirestore(this::syncFromFirestoreToSQLite);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure();
        }
    }

    private void syncFromSQLiteToFirestore(Runnable onComplete) {
        List<CourseModel> localCourses = courseRepository.getAllCourses();
        List<ClassModel> localClasses = classRepository.getAllClasses();
        List<UserModel> localUsers = userRepository.getAllUsers();

        int totalTasks = localClasses.size() + localCourses.size() + localUsers.size() + localUsers.size();

        if (totalTasks == 0) {
            onComplete.run();
            return;
        }

        final int[] completedTasks = {0};

        for (CourseModel localCourse : localCourses) {
            Map<String, Object> courseData = localCourse.toMap();
            db.collection(COURSES_COLLECTION)
                    .document(localCourse.getId())
                    .set(courseData)
                    .addOnSuccessListener(aVoid -> {
                        completedTasks[0]++;
                        if (completedTasks[0] == totalTasks) {
                            onComplete.run();
                        }
                    }).addOnFailureListener(e -> {
                        completedTasks[0]++;
                        if (completedTasks[0] == totalTasks) {
                            onComplete.run();
                        }
                    });
        }

        for (ClassModel localClass : localClasses) {
            Map<String, Object> classData = localClass.toMap();
            db.collection(CLASSES_COLLECTION)
                    .document(localClass.getId())
                    .set(classData)
                    .addOnSuccessListener(aVoid -> {
                        completedTasks[0]++;
                        if (completedTasks[0] == totalTasks) {
                            onComplete.run();
                        }
                    })
                    .addOnFailureListener(e -> {
                        completedTasks[0]++;
                        if (completedTasks[0] == totalTasks) {
                            onComplete.run();
                        }
                    });
        }

        for (UserModel localUser : localUsers) {
            Map<String, Object> userData = localUser.toMap();
            db.collection(USERS_COLLECTION)
                    .document(localUser.getId())
                    .set(userData)
                    .addOnSuccessListener(aVoid -> {
                        completedTasks[0]++;
                        if (completedTasks[0] == totalTasks) {
                            onComplete.run();
                        }
                    }).addOnFailureListener(e -> {
                        completedTasks[0]++;
                        if (completedTasks[0] == totalTasks) {
                            onComplete.run();
                        }
                    });
        }
    }

    private void syncFromFirestoreToSQLite() {
        CollectionReference usersRef = db.collection(USERS_COLLECTION);
        usersRef.get().addOnSuccessListener(snapshots -> {
            for (QueryDocumentSnapshot snapshot : snapshots) {
                UserModel userModel = new UserModel();
                userModel.setId(snapshot.getString("id"));
                userModel.setEmail(snapshot.getString("email"));
                userModel.setFullName(snapshot.getString("fullName"));
                userModel.setPhoneNumber(snapshot.getString("phoneNumber"));
                Long role = snapshot.getLong("role");
                if (role != null) {
                    userModel.setRole(role.intValue());
                }
                userRepository.addUser(userModel);
                Log.d("Syncer", "User added: " + userModel.getId());
            }
        });

        CollectionReference coursesRef = db.collection(COURSES_COLLECTION);
        coursesRef.get().addOnSuccessListener(snapshots -> {
            for (QueryDocumentSnapshot snapshot : snapshots) {
                CourseModel courseModel = new CourseModel();
                courseModel.setId(snapshot.getString("id"));
                courseModel.setDayOfWeek(snapshot.getString("dayOfWeek"));
                courseModel.setStartTime(Time.valueOf(snapshot.getString("startTime")));
                courseModel.setCapacity(snapshot.getLong("capacity").intValue());
                courseModel.setDuration(snapshot.getLong("duration").intValue());
                courseModel.setClassCount(snapshot.getLong("classCount").intValue());
                courseModel.setType(snapshot.getString("type"));
                courseModel.setStartAt(snapshot.getLong("startAt"));
                courseModel.setEndAt(snapshot.getLong("endAt"));
                courseModel.setPrice(snapshot.getLong("price").intValue());
                courseRepository.addCourse(courseModel);
                Log.d("Syncer", "Course added: " + courseModel.getId());
            }
        });

        CollectionReference classesRef = db.collection(CLASSES_COLLECTION);
        classesRef.get().addOnSuccessListener(snapshots -> {
            for (QueryDocumentSnapshot snapshot : snapshots) {
                ClassModel classModel = new ClassModel();
                classModel.setId(snapshot.getString("id"));
                classModel.setCourseId(snapshot.getString("courseId"));
                classModel.setTeacherId(snapshot.getString("teacherId"));
                classModel.setStartDate(snapshot.getLong("startDate"));
                classModel.setPrice(snapshot.getLong("price").intValue());
                classRepository.addClass(classModel);
                Log.d("Syncer", "Class added: " + classModel.getId());
            }
        });

        CollectionReference bookingsRef = db.collection(BOOKINGS_COLLECTION);
        bookingsRef.get().addOnSuccessListener(snapshots -> {
            for (QueryDocumentSnapshot snapshot : snapshots) {
                BookingModel bookingModel = new BookingModel();
                bookingModel.setId(snapshot.getString("id"));
                bookingModel.setUserId(snapshot.getString("userId"));
                bookingRepository.addBooking(bookingModel);

                List<String> classIds = (List<String>) snapshot.get("classIds");
                for (String classId : classIds) {
                    BookingClassModel bookingClassModel = new BookingClassModel();
                    bookingClassModel.setBookingId(bookingModel.getId());
                    bookingClassModel.setClassId(classId);
                    bookingClassRepository.addBookingClass(bookingClassModel);
                }
                Log.d("Syncer", "Booking added: " + bookingModel.getId());
            }
        });
    }
}
