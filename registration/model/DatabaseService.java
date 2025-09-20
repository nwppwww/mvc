package registration.model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DatabaseService {
    private static final String STUDENT_FILE = "data/students.csv";
    private static final String SUBJECT_FILE = "data/subjects.csv";
    private static final String REGISTRATION_FILE = "data/registrations.csv";

    public List<Student> loadStudents() {
        return loadData(STUDENT_FILE, line -> {
            String[] p = line.split(",");
            return new Student(p[0], p[1], p[2], p[3], p[4], p[5], p[6]);
        });
    }

    public List<Subject> loadSubjects() {
        return loadData(SUBJECT_FILE, line -> {
            String[] p = line.split(",");
            return new Subject(p[0], p[1], Integer.parseInt(p[2]), p[3], p[4], Integer.parseInt(p[5]), Integer.parseInt(p[6]));
        });
    }

    public List<Registration> loadRegistrations() {
        return loadData(REGISTRATION_FILE, line -> {
            String[] p = line.split(",", -1); // Include trailing empty strings
            return new Registration(p[0], p[1], p.length > 2 ? p[2] : "");
        });
    }

    public void saveSubjects(List<Subject> subjects) {
        List<String> lines = subjects.stream().map(Subject::toCsvString).collect(Collectors.toList());
        saveData(SUBJECT_FILE, lines);
    }

    public void saveRegistrations(List<Registration> registrations) {
        List<String> lines = registrations.stream().map(Registration::toCsvString).collect(Collectors.toList());
        saveData(REGISTRATION_FILE, lines);
    }

    private <T> List<T> loadData(String filePath, java.util.function.Function<String, T> mapper) {
        List<T> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                list.add(mapper.apply(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void saveData(String filePath, List<String> lines) {
        String header = "";
        try {
            header = Files.lines(Paths.get(filePath)).findFirst().orElse("");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write(header);
            bw.newLine();
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}