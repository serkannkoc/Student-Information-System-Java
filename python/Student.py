import json
from Advisor import Advisor

class Student:
    def __init__(self, studentNumber, studentName, year, advisor, transcriptBefore):
        self.studentNumber = studentNumber
        self.studentName = studentName
        self.year = year
        self.advisor = advisor
        self.transcriptBefore = transcriptBefore

    def createStudentJSON(self):
        with open("students/"+str(self.studentNumber)+ ".json", "w") as outfile:
            jsonForStudent = json.dumps(self.__dict__, indent=6)
            outfile.write(jsonForStudent)
