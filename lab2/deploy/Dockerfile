#FROM bitnami/wildfly:latest
#COPY target/AreaChecker.war /opt/bitnami/wildfly/standalone/deployments/
#CMD ["/opt/bitnami/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]

# Additional task: move to glassfish
FROM omnifish/glassfish:latest
COPY target/AreaChecker.war $PATH_GF_HOME/glassfish/domains/domain1/autodeploy/
CMD ["asadmin", "start-domain", "-v"]
