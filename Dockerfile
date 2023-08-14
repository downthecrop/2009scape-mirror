# Starting out with the openjdk-11-slim image
FROM maven:3-openjdk-11-slim

# Set working directory to /app
WORKDIR /app

# Update apt; install git and git-lfs 
RUN apt-get update && apt-get -qq -y install git git-lfs

# Clone the 2009scape repository
RUN git clone --depth=1 https://gitlab.com/2009scape/2009scape.git

# Fake it til you make it - let's go home
WORKDIR /app/2009scape

# Make sure ./run has permissions
RUN chmod +x run

# Run it
CMD ["./run"]
