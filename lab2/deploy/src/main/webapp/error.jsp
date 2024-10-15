<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%! 
@Override
public void jspInit() {
    System.out.println("=========================");
    System.out.println("=========================");
    System.out.println("=========================");
    System.out.println("JSP Initialization: jspInit() method invoked.");
}

/*@Override
public void _jspService() {
    System.out.println("JSP WORK."); // But is we override default _jspService() it will craash ofc.
}
*/

@Override
public void jspDestroy() {
    System.out.println("JSP Cleanup: jspDestroy() method invoked.");
}
%>

<%-- docker exec -it 2c35ab84c9a9 bash /opt/bitnami/wildfly/bin/jboss-cli.sh --connect command=:shutdown --%>
<!-- html comment docker exec -it 2c35ab84c9a9 cat /opt/bitnami/wildfly/standalone/tmp/AreaChecker.war/org/apache/jsp/error_jsp.java -->
<!DOCTYPE html>
<html lang="en">
<head>
</head>
<body>
<h1>No luck today</h1>
<%
System.out.println("Request Processing: _jspService() method invoked.");
String error = (String) request.getAttribute("error");
%>
<h2><%= error %></h2>
</body>
</html>
