package com.company;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;

public class JAXBExample {

    public static void main(String args[]) throws JAXBException {

try{
        User user = new User();
        user.login = "test";
        user.password = "132";
        File file = new File("C:\\Users\\Luksor\\IdeaProjects\\SSLExample\\src\\com\\company\\database.xml");


        JAXBContext jaxbContext = JAXBContext.newInstance(Users.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        Users users = (Users) jaxbUnmarshaller.unmarshal(file);
        List<User> userList = users.getUsers();
        for (int i = 0; i < userList.size(); i++) {
            if(userList.get(i).login.equals(user.login) && userList.get(i).password.equals(user.password)){
                System.out.println("jest");
            }
        }

    System.out.println("nie ma");
    System.out.println(userList.get(0));


} catch (JAXBException e) {
        e.printStackTrace();
    }

    }
}
