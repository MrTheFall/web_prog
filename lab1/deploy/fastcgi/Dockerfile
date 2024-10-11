FROM openjdk:17-jdk

WORKDIR /fastcgi-app

COPY . .
RUN javac -cp ".:src/:lib/*" -d classes src/Server.java

# =================COMPILE AS A JAR WITH EXTERNAL LIBS=================
#RUN echo "Main-Class: Server" > MANIFEST.mf
#RUN echo "Class-Path: lib/fastcgi-lib.jar lib/javax.json-1.1.4.jar" >> MANIFEST.mf

#RUN jar -cvfm Server.jar MANIFEST.mf -C classes/ . -C lib/ .
#CMD ["java", "-DFCGI_PORT=9000", "-jar", "Server.jar"]


# ================COMPILE AS A CLASS WITH EXTERNAL LIBS================
#CMD ["java", "-cp", ".:src/:lib/*:classes/", "-DFCGI_PORT=9000", "Server"]


# ==========COMPILE AS A SINGLE JAR INCLUDING DEPENDENCY LIBS==========
RUN mkdir unpacked_libs && \
    cd unpacked_libs && \
    for jar in /fastcgi-app/lib/*.jar; do \
        jar -xf "$jar"; \
    done && \
    cp -r . ../classes

# Create the manifest file
RUN echo "Main-Class: Server" > MANIFEST.mf

# Package the JAR with unpacked libraries
RUN jar -cvfm Server.jar MANIFEST.mf -C classes/ .

CMD ["java", "-DFCGI_PORT=9000", "-jar", "Server.jar"]
