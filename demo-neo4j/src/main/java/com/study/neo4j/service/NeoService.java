package com.study.neo4j.service;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.study.neo4j.repository.ClassRepository;
import com.study.neo4j.repository.LessonRepository;
import com.study.neo4j.repository.StudentRepository;
import com.study.neo4j.repository.TeacherRepository;
import com.study.neo4j.model.Class;
import com.study.neo4j.model.Lesson;
import com.study.neo4j.model.Student;
import com.study.neo4j.model.Teacher;
import com.study.neo4j.payload.ClassmateInfoGroupByLesson;
import com.study.neo4j.payload.TeacherStudent;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * NeoService
 * </p>
 *
 * @author yangz
 * @date Created in 2018-12-24 15:19
 */
@Service
public class NeoService {
    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private Session session;

    /**
     * 初始化数据
     */
    @Transactional
    public void initData() {
        // 初始化老师
        Teacher akai = new Teacher("迈特凯");
        Teacher kakaxi = new Teacher("旗木卡卡西");
        Teacher zilaiye = new Teacher("自来也");
        Teacher gangshou = new Teacher("纲手");
        Teacher dashewan = new Teacher("大蛇丸");
        teacherRepository.save(akai);
        teacherRepository.save(kakaxi);
        teacherRepository.save(zilaiye);
        teacherRepository.save(gangshou);
        teacherRepository.save(dashewan);

        // 初始化课程
        Lesson tishu = new Lesson("体术", akai);
        Lesson huanshu = new Lesson("幻术", kakaxi);
        Lesson shoulijian = new Lesson("手里剑", kakaxi);
        Lesson luoxuanwan = new Lesson("螺旋丸", zilaiye);
        Lesson xianshu = new Lesson("仙术", zilaiye);
        Lesson yiliao = new Lesson("医疗", gangshou);
        Lesson zhouyin = new Lesson("咒印", dashewan);
        lessonRepository.save(tishu);
        lessonRepository.save(huanshu);
        lessonRepository.save(shoulijian);
        lessonRepository.save(luoxuanwan);
        lessonRepository.save(xianshu);
        lessonRepository.save(yiliao);
        lessonRepository.save(zhouyin);

        // 初始化班级
        Class three = new Class("第三班", akai);
        Class seven = new Class("第七班", kakaxi);
        classRepository.save(three);
        classRepository.save(seven);

        // 初始化学生
        List<Student> threeClass = Lists.newArrayList(new Student("漩涡鸣人", Lists.newArrayList(tishu, shoulijian, luoxuanwan, xianshu), seven), new Student("宇智波佐助", Lists.newArrayList(huanshu, zhouyin, shoulijian), seven), new Student("春野樱", Lists.newArrayList(tishu, yiliao, shoulijian), seven));
        List<Student> sevenClass = Lists.newArrayList(new Student("李洛克", Lists.newArrayList(tishu), three), new Student("日向宁次", Lists.newArrayList(tishu), three), new Student("天天", Lists.newArrayList(tishu), three));

        studentRepository.saveAll(threeClass);
        studentRepository.saveAll(sevenClass);

    }

    /**
     * 删除数据
     */
    @Transactional
    public void delete() {
        // 使用语句删除

        Transaction transaction = session.beginTransaction();
        session.run("match (n)-[r]-() delete n,r", Maps.newHashMap());
        session.run("match (n)-[r]-() delete r", Maps.newHashMap());
        session.run("match (n) delete n", Maps.newHashMap());
        transaction.commit();

        // 使用 repository 删除
        studentRepository.deleteAll();
        classRepository.deleteAll();
        lessonRepository.deleteAll();
        teacherRepository.deleteAll();
    }

    /**
     * 根据学生姓名查询所选课程
     *
     * @param studentName 学生姓名
     * @param depth       深度
     * @return 课程列表
     */
    public List<Lesson> findLessonsFromStudent(String studentName, int depth) {
        List<Lesson> lessons = Lists.newArrayList();
        studentRepository.findByName(studentName, depth).ifPresent(student -> lessons.addAll(student.getLessons()));
        return lessons;
    }

    /**
     * 查询全校学生数
     *
     * @return 学生总数
     */
    public Long studentCount(String className) {
        if (StrUtil.isBlank(className)) {
            return studentRepository.count();
        } else {
            return studentRepository.countByClassName(className);
        }
    }

    /**
     * 查询同学关系，根据课程
     *
     * @return 返回同学关系
     */
    public Map<String, List<Student>> findClassmatesGroupByLesson() {
        List<ClassmateInfoGroupByLesson> groupByLesson = studentRepository.findByClassmateGroupByLesson();
        Map<String, List<Student>> result = Maps.newHashMap();

        groupByLesson.forEach(classmateInfoGroupByLesson -> result.put(classmateInfoGroupByLesson.getLessonName(), classmateInfoGroupByLesson.getStudents()));
        return result;
    }

    /**
     * 查询所有师生关系，包括班主任/学生，任课老师/学生
     *
     * @return 师生关系
     */
    public Map<String, Set<Student>> findTeacherStudent() {
        List<TeacherStudent> teacherStudentByClass = studentRepository.findTeacherStudentByClass();
        List<TeacherStudent> teacherStudentByLesson = studentRepository.findTeacherStudentByLesson();
        Map<String, Set<Student>> result = Maps.newHashMap();

        teacherStudentByClass.forEach(teacherStudent -> result.put(teacherStudent.getTeacherName(), Sets.newHashSet(teacherStudent.getStudents())));

        teacherStudentByLesson.forEach(teacherStudent -> result.put(teacherStudent.getTeacherName(), Sets.newHashSet(teacherStudent.getStudents())));

        return result;
    }
}
