# Use the official WildFly image
FROM bitnami/wildfly:latest

# Copy the WAR file into the WildFly deployments directory
COPY ./../target/AreaChecker.war /opt/bitnami/wildfly/standalone/deployments/

# Modify standalone.xml to set welcome content or additional configurations
#COPY standalone.xml /opt/bitnami/wildfly/standalone/configuration/

# Set the default command to start WildFly in standalone mode
CMD ["/opt/bitnami/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]
