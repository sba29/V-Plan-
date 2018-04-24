package vplan;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
@SuppressWarnings("unused")
class Task implements Serializable {
String date, title, subject;
int difficulty;
Task(String title, String subject, String date, int difficulty) {
this.title = title;
this.subject = subject;
this.date = date;
this.difficulty = difficulty;
}
String getTitle(){
	return this.title;
}

String getDate(){
	return this.date;
}
String getSubject(){
	return this.subject;
}
int getDifficulty(){
	return this.difficulty;
}

@Override
public String toString() {
    String x = this.getDifficulty() + " - " + this.getTitle() + " Due: " + this.getDate();
    return x;
}


}
