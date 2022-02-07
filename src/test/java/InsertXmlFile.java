import org.springframework.util.ObjectUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class InsertXmlFile {
    private static final String USER_FILENAME = "src/test/resources/user.xml";
    private static final String DEPT_FILENAME = "src/test/resources/dept.xml";
    private static final String COMP_FILENAME = "src/test/resources/comp.xml";


    private Document getDocument(String fileName) {
        // Instantiate the Factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            // optional, but recommended
            // process XML securely, avoid attacks like XML External Entities (XXE)
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            // parse XML file
            DocumentBuilder db = dbf.newDocumentBuilder();

            return db.parse(new File(fileName));

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void insertCompney(Document doc) {
        doc.getDocumentElement().normalize();
        NodeList list = doc.getElementsByTagName("company");
        System.out.println("insert into msg_comp (scompany_code,company_code,company_name,domain_name,locale,sort_seq) values");

        for (int temp = 0; temp < list.getLength();) {
            Node node = list.item(temp);

            if (node.getNodeType() == Node.ELEMENT_NODE) {

                Element element = (Element) node;

                // get text
                String scompCode = element.getElementsByTagName("scompCode").item(0).getTextContent();
                String companyCode = element.getElementsByTagName("companyCode").item(0).getTextContent();
                String companyName = element.getElementsByTagName("companyName").item(0).getTextContent();
                String domainName = element.getElementsByTagName("domainName").item(0).getTextContent();

                System.out.print("('"+scompCode + "','" + companyCode + "','" + companyName + "','" + domainName + "','ko_KR'," + ++temp + ")");

                if(temp < list.getLength()) {
                    System.out.println(",");
                } else {
                    System.out.println(";");
                }

            }
        }
    }

    private void insertDept(Document doc) {
        doc.getDocumentElement().normalize();
        NodeList list = doc.getElementsByTagName("dept");
        System.out.println("\n\ninsert into msg_dept (company_code,dept_code,pdept_code,dept_name,locale,sort_seq) values");

        for (int temp = 0; temp < list.getLength();) {
            Node node = list.item(temp);

            if (node.getNodeType() == Node.ELEMENT_NODE) {

                Element element = (Element) node;

                // get text
                String companyCode = element.getElementsByTagName("companyCode").item(0).getTextContent();
                String pdeptCode = element.getElementsByTagName("pdeptCode").item(0).getTextContent();
                String deptCode = element.getElementsByTagName("deptCode").item(0).getTextContent();
                String deptName = element.getElementsByTagName("deptName").item(0).getTextContent();

                System.out.print("('"+companyCode + "','" + pdeptCode + "','" + deptCode + "','" + deptName + "','ko_KR'," + ++temp + ")");

                if(temp < list.getLength()) {
                    System.out.println(",");
                } else {
                    System.out.println(";");
                    System.out.println(";");
                }

            }
        }
    }
    private void insertUser(Document doc) {
        doc.getDocumentElement().normalize();
        NodeList list = doc.getElementsByTagName("user");
        System.out.println("\n\ninsert into msg_user (locale,userindex,empno,userid,password,user_name,company_code,dept_code,grade_code,grade_name,duty_code,duty_name,pos_code,pos_name,sex,email,domain_name,compphone_number,greetings,online_id,sort_seq,mobile_number) values");

        for (int temp = 0; temp < list.getLength();) {
            Node node = list.item(temp);

            if (node.getNodeType() == Node.ELEMENT_NODE) {

                Element element = (Element) node;

                // get text
                String userindex = element.getElementsByTagName("userindex").item(0).getTextContent();
                String userid = element.getElementsByTagName("userid").item(0).getTextContent();
                String userName = element.getElementsByTagName("userName").item(0).getTextContent();
                String companyCode = element.getElementsByTagName("companyCode").item(0).getTextContent();
                String deptCode = element.getElementsByTagName("deptCode").item(0).getTextContent();
                String gradeCode = element.getElementsByTagName("gradeCode").item(0).getTextContent();
                String gradeName = element.getElementsByTagName("gradeName").item(0).getTextContent();
                String dutyCode = element.getElementsByTagName("dutyCode").item(0).getTextContent();
                String dutyName = element.getElementsByTagName("dutyName").item(0).getTextContent();
                String sex = element.getElementsByTagName("sex").item(0).getTextContent();
                String email = element.getElementsByTagName("email").item(0).getTextContent();
                String domainName = element.getElementsByTagName("domainName").item(0).getTextContent();
                Node compPhoneNumber = element.getElementsByTagName("compPhoneNumber").item(0);
                Node mobileNumber = element.getElementsByTagName("mobileNumber").item(0);
                String empno = element.getElementsByTagName("empno").item(0).getTextContent();
                Node posName = element.getElementsByTagName("posName").item(0);
                Node posCode = element.getElementsByTagName("posCode").item(0);
                Node greetings = element.getElementsByTagName("greetings").item(0);

                System.out.print("('ko_KR','" + userindex + "','"+empno + "','"+userid + "', '','"+userName + "','"+companyCode + "','"+deptCode + "','"+gradeCode + "','"+
                        gradeName + "','"+dutyCode + "','"+dutyName + "','"+(posCode == null ? "" : posCode.getTextContent()) + "','"+(posName == null ? "" : posName.getTextContent()) + "','"+sex + "','"+email + "','"+domainName + "','"+
                        (compPhoneNumber == null ? "" : compPhoneNumber.getTextContent()) + "','"+ (greetings == null ? "" : greetings.getTextContent()) + "',null,'"+  ++temp + "','"+(mobileNumber == null ? "" : mobileNumber.getTextContent()) + "')");

                if(temp < list.getLength()) {
                    System.out.println(",");
                } else {
                    System.out.println(";");
                }

            }
        }
    }

    public static void main(String[] args) {
        InsertXmlFile xml = new InsertXmlFile();

        try {
            Document compDoc = xml.getDocument(COMP_FILENAME);
            Document deptDoc = xml.getDocument(DEPT_FILENAME);
            Document userDoc = xml.getDocument(USER_FILENAME);

            xml.insertCompney(compDoc);
            xml.insertDept(deptDoc);
            xml.insertUser(userDoc);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
