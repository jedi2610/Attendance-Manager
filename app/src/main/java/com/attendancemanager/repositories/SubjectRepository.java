package com.attendancemanager.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.attendancemanager.model.Subject;
import com.attendancemanager.model.SubjectDao;
import com.attendancemanager.model.SubjectDataBase;

import java.util.List;
import java.util.concurrent.Callable;

import javax.security.auth.callback.Callback;

public class SubjectRepository {

    private static SubjectRepository instance;

    private SubjectDao subjectDao;
    private LiveData<List<Subject>> allSubjects;
    private SubjectDataBase dataBase;

    public static SubjectRepository getInstance(Application application) {
        if (instance == null) {
            instance = new SubjectRepository(application);
        }
        return instance;
    }

    public SubjectRepository(Application application) {

        dataBase = SubjectDataBase.getInstance(application);
        subjectDao = dataBase.subjectDao();
        allSubjects = subjectDao.getAllSubjects();
    }

    public void insertSubject(Subject subject) {
        new Thread(new InsertSubjectRunnable(subjectDao, subject)).start();
    }

    public void deleteSubject(Subject subject) {
        new Thread(new DeleteSubjectRunnable(subjectDao, subject)).start();
    }

    public void updateSubject(Subject subject) {
        new Thread(new UpdateSubjectRunnable(subjectDao, subject)).start();
    }

    public boolean containsSubject(String subjectName) {
        try {
            Subject result = ((Callable<Subject>) () -> subjectDao.containsSubject(subjectName)).call();
            return result != null;
        } catch (Exception e) {
            return false;
        }
    }

    public Subject getSubject(String subjectName) {
        try {
            return ((Callable<Subject>) () -> subjectDao.getSubject(subjectName)).call();
        } catch (Exception e) {
            return null;
        }
    }

    public void deleteAllSubjects() {
        new Thread(new DeleteAllSubjectRunnable(subjectDao)).start();
    }

    public LiveData<List<Subject>> getAllSubjects() {
        return allSubjects;
    }

    public void closeDB() {
        if (dataBase.isOpen())
            dataBase.close();
    }

    private static class InsertSubjectRunnable implements Runnable {

        private SubjectDao subjectDao;
        private Subject subject;

        InsertSubjectRunnable(SubjectDao subjectDao, Subject subject) {
            this.subjectDao = subjectDao;
            this.subject = subject;
        }

        @Override
        public void run() {
            subjectDao.insert(subject);
        }
    }

    private static class DeleteSubjectRunnable implements Runnable {

        private SubjectDao subjectDao;
        private Subject subject;

        DeleteSubjectRunnable(SubjectDao subjectDao, Subject subject) {
            this.subjectDao = subjectDao;
            this.subject = subject;
        }

        @Override
        public void run() {
            subjectDao.delete(subject);
        }
    }

    private static class UpdateSubjectRunnable implements Runnable {

        private SubjectDao subjectDao;
        private Subject subject;

        UpdateSubjectRunnable(SubjectDao subjectDao, Subject subject) {
            this.subjectDao = subjectDao;
            this.subject = subject;
        }

        @Override
        public void run() {
            subjectDao.update(subject);
        }
    }

    private static class GetSubjectRunnable implements Runnable {

        private SubjectDao subjectDao;
        private String subjectName;

        GetSubjectRunnable(SubjectDao subjectDao, String subjectName) {
            this.subjectDao = subjectDao;
            this.subjectName = subjectName;
        }

        @Override
        public void run() {
            subjectDao.getSubject(subjectName);
        }
    }

    private static class DeleteAllSubjectRunnable implements Runnable {

        private SubjectDao subjectDao;

        DeleteAllSubjectRunnable(SubjectDao subjectDao) {
            this.subjectDao = subjectDao;
        }

        @Override
        public void run() {
            subjectDao.deleteAllSubjects();
        }
    }
}