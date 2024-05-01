#FROM maven:3-openjdk-11

FROM maven:3-openjdk-11

# Add metadata to the image
LABEL maintainer="Tech Team <team14@example.com>"

# Update the package list and install required packages
RUN apt-get update && \
    apt-get install -y openjdk-11-jre curl && \
    rm -rf /var/lib/apt/lists/*

# Create a new app directory for your application
RUN mkdir /app

# Set the directory for executing future commands
WORKDIR /app

# Copy the executable JAR with dependencies to the app directory
COPY Server/target/Server-1.0-SNAPSHOT-jar-with-dependencies.jar /app/robot-worlds.jar

# Expose the required port
EXPOSE 5050

# Set the entrypoint for running the application
ENTRYPOINT ["java", "-jar", "robot-worlds.jar"]

# Set the default command to run the application with specific arguments
CMD ["-s", "2", "-p", "5050"]
