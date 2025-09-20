package registration.model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DatabaseService { // คลาสสำหรับจัดการการอ่านเขียนไฟล์ข้อมูล
    private static final String STUDENT_FILE = "data/students.csv"; // ชื่อไฟล์ข้อมูลนักเรียน
    private static final String SUBJECT_FILE = "data/subjects.csv"; // ชื่อไฟล์ข้อมูลวิชา
    private static final String REGISTRATION_FILE = "data/registrations.csv"; // ชื่อไฟล์ข้อมูลการลงทะเบียน

    public List<Student> loadStudents() { // methodโหลดข้อมูลนักเรียน
        return loadData(STUDENT_FILE, line -> { // เรียกใช้method loadDataเพื่ออ่านไฟล์
            String[] p = line.split(","); // แยกข้อมูลแต่ละคอลัมน์
            return new Student(p[0], p[1], p[2], p[3], p[4], p[5], p[6]); // สร้างobjectนักเรียน
        });
    }

    public List<Subject> loadSubjects() { // methodโหลดข้อมูลวิชา
        return loadData(SUBJECT_FILE, line -> { // เรียกใช้method loadDataเพื่ออ่านไฟล์
            String[] p = line.split(","); // แยกข้อมูลแต่ละคอลัมน์
            return new Subject(p[0], p[1], Integer.parseInt(p[2]), p[3], p[4], Integer.parseInt(p[5]), Integer.parseInt(p[6])); // สร้างobjectวิชา
        });
    }

    public List<Registration> loadRegistrations() { // methodโหลดข้อมูลการลงทะเบียน
        return loadData(REGISTRATION_FILE, line -> { // เรียกใช้method loadDataเพื่ออ่านไฟล์
            String[] p = line.split(",", -1); // แยกข้อมูลโดยรวมค่าว่างท้ายบรรทัดด้วย
            return new Registration(p[0], p[1], p.length > 2 ? p[2] : ""); // สร้างobjectการลงทะเบียน
        });
    }

    public void saveSubjects(List<Subject> subjects) { // methodบันทึกข้อมูลวิชา
        List<String> lines = subjects.stream().map(Subject::toCsvString).collect(Collectors.toList()); // แปลงobjectเป็นข้อความ
        saveData(SUBJECT_FILE, lines); // เรียกใช้method saveDataเพื่อเขียนไฟล์
    }

    public void saveRegistrations(List<Registration> registrations) { // methodบันทึกข้อมูลการลงทะเบียน
        List<String> lines = registrations.stream().map(Registration::toCsvString).collect(Collectors.toList()); // แปลงobjectเป็นข้อความ
        saveData(REGISTRATION_FILE, lines); // เรียกใช้method saveDataเพื่อเขียนไฟล์
    }

    private <T> List<T> loadData(String filePath, java.util.function.Function<String, T> mapper) { // methodสำหรับอ่านข้อมูล
        List<T> list = new ArrayList<>(); // สร้างlistว่างเพื่อเก็บข้อมูล
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) { // เปิดไฟล์เพื่ออ่าน
            String line = br.readLine(); // อ่านหัวตารางทิ้งไป
            while ((line = br.readLine()) != null) { // วนลูปอ่านข้อมูลทีละบรรทัด
                list.add(mapper.apply(line)); // แปลงบรรทัดเป็นobjectแล้วเพิ่มลงlist
            }
        } catch (IOException e) { // กรณีเกิดข้อผิดพลาดในการอ่านไฟล์
            e.printStackTrace(); // แสดงข้อผิดพลาด
        }
        return list; // คืนค่าlistข้อมูล
    }

    private void saveData(String filePath, List<String> lines) { // methodสำหรับเขียนข้อมูล
        String header = ""; // เตรียมตัวแปรเก็บหัวตาราง
        try {
            header = Files.lines(Paths.get(filePath)).findFirst().orElse(""); // อ่านหัวตารางจากไฟล์เดิม
        } catch (IOException e) { // กรณีเกิดข้อผิดพลาด
            e.printStackTrace(); // แสดงข้อผิดพลาด
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) { // เปิดไฟล์เพื่อเขียน
            bw.write(header); // เขียนหัวตารางกลับไปก่อน
            bw.newLine(); // ขึ้นบรรทัดใหม่
            for (String line : lines) { // วนลูปเขียนข้อมูลทั้งหมด
                bw.write(line); // เขียนข้อมูลหนึ่งบรรทัด
                bw.newLine(); // ขึ้นบรรทัดใหม่
            }
        } catch (IOException e) { // กรณีเกิดข้อผิดพลาด
            e.printStackTrace(); // แสดงข้อผิดพลาด
        }
    }
}