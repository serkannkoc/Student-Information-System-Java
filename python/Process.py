# import required packages
import json
from random import randint
from Student import Student
from Advisor import Advisor

def getAdvisorsFromJSON():
    json_file = open("input.json")

    with open("input.json", encoding='utf-8') as data_file:
        data=json.load(data_file)

    variables = json.load(json_file)
    json_file.close()

    advisorList = []

    for advisor in variables['advisors']:
        newAdvisor = Advisor(advisor['advisorName'], advisor['department'], advisor['rank'])
        advisorList.append(newAdvisor)
        
    return advisorList


def getSemesterCourses(semester):
    json_file = open("input.json")
    variables = json.load(json_file)
    json_file.close()

    myCourseList = []
    
    for courses in variables['courses']:
        for semester in courses[str(semester)]:
            myCourseList.append(semester['courseName'])

    return myCourseList

def getElectiveCourses(electiveType):
    json_file = open("input.json")
    variables = json.load(json_file)
    json_file.close()

    myCourseList = []
    
    for courses in variables['courses']:
        for semester in courses[electiveType]:
            myCourseList.append(semester['courseName'])

    return myCourseList

def getSemesterFromJSON():
    json_file = open("input.json")
    variables = json.load(json_file)
    json_file.close()

    return variables['semester']

studentArrayList = []
advisorArrayList = getAdvisorsFromJSON()
semester = getSemesterFromJSON()

def semesterToInt(year):
    if(semester == "FALL"):
        return year*2-1
    else:
        return year*2

def createTranscript(year):
    courseList = []

    for semester in range(1, semesterToInt(year)):
        for course in getSemesterCourses(semester):
            courseList.append({"courseName" : course, "letterGrade" : generateRandomLetterGrade(), "semester" : semester})
        if(semester == 2):
            while(True):
                electiveCourse = getElectiveCourses("NTE")[randint(0, len(getElectiveCourses("NTE"))-1)]
                if courseList.count(electiveCourse) == 0:
                    courseList.append({"courseName" : electiveCourse, "letterGrade" : generateRandomLetterGrade(), "semester" : semester})
                    break

    return courseList

def getCreateNewStudentFromJSON():
    json_file = open("input.json")
    variables = json.load(json_file)
    json_file.close()

    return True if variables['createNewStudents'] == True else False 

def generateStudentNumber(year, count):
    numberStart = str(150122 - year)
    numberStart += "0"
    
    if count>=9:
        studentNumber = str(numberStart) + str(count+1)

    else:
        studentNumber = str(numberStart) + str(count//10) + str(count+1)

    return studentNumber

def generateRandomName():
    firstNames = ("Ahmet", "Mehmet", "Mustafa", "Zeynep", "Elif", "Defne", "Kerem", "Azra", "Miran",
                "Asya", "Hamza", "Öykü", "Ömer Asaf", "Ebrar", "Ömer", "Eylül", "Eymen", "Murat", "Hesna", "Ali", "Nuri",
                "Muhammed", "Gökay", "Koray", "Esra", "Bihter", "Ceyda", "Özge", "Özlem", "Önder", "Ramazan", "Recep",
                "Rabia", "Hilal", "Buse", "Başak", "Serkan", "Ulaş", "Deniz", "Kardelen", "Mervan", "Kübra", "Atilla",
                "İlhan", "Fatih", "Asena", "Ahsen", "Ferit", "Kemal")

    lastNames = ("Yılmaz", "Deniz", "Karakurt", "Şahin", "Türkay", "Akyıldız", "Yıldız", "Güçlü",
                "Temur", "Duraner", "Yılmazer", "Ekşi", "Ballıca", "Erdoğmuş", "Erdoğan", "Korkmaz", "Çubukluöz",
                "Hüyüktepe", "Zengin", "Büyük", "Küçük", "Türk", "Turgut", "Boz", "Açıkgöz", "Öztürk", "Beler",
                "Çetin", "Bilgin", "Yalçınkaya", "Adıgüzel", "Kartal", "Dinç", "Genç", "Kuruçay", "Parlak", "Karakaya",
                "Kaya")

    fullName = firstNames[randint(0, len(firstNames)-1)] + " " + lastNames[randint(0, len(lastNames)-1)]

    return fullName

def generateRandomLetterGrade():
    grades = ("AA", "BA", "BB", "BB", "CB", "CC", "DC", "DD", "FD", "FF")

    return grades[randint(0, len(grades)-1)]

def createJSONForAllStudents():
    for student in studentArrayList:
        student.createStudentJSON()

def getNumericGradeFromLetterGrade(letterGrade):

    match letterGrade:
        case 'AA':
            return 4.0
        case 'BA':
            return 3.5
        case 'BB':
            return 3.0
        case 'CB':
            return 2.5
        case 'CC':
            return 2.0
        case 'DC':
            return 1.5
        case 'DD':
            return 1.0
        case 'FD':
            return 0.5 
        case _:        
            return 0.0

def initializeStudents():
    for year in range(1,4):
        advisor = advisorArrayList[randint(0, len(advisorArrayList)-1)]
        for y in range(70):
            student = Student(generateStudentNumber(year,y), generateRandomName(), year, "Erdi", createTranscript(year))
            studentArrayList.append(student)

initializeStudents()
createJSONForAllStudents()