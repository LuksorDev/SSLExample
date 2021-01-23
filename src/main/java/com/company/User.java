package com.company;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement (name = "user")
public class User {

    String login;
    String password;
    String question;

    public String getLogin() {
        return login;
    }

    @XmlElement
    public void setLogin(String login) {
        this.login = login;
    }

    public String  getPassword() {
        return password;
    }

    @XmlElement
    public void setPassword(String password) {
        this.password = password;
    }

    public String  getQuestion() {
        return question;
    }

    @XmlElement
    public void setQuestion(String question) {
        this.question = question;
    }

}