# Starting out with the openjdk-11-slim image
FROM maven:3-openjdk-11-slim

# Set working directory to /app
WORKDIR /app

# Copy all sources etc
COPY . .

# Make sure ./run has permissions
RUN chmod +x run

# Run it
CMD ["./run"]
