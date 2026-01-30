FROM eclipse-temurin:17-jdk

# Set environment variables
ENV ANDROID_HOME=/opt/android-sdk
ENV ANDROID_SDK_ROOT=/opt/android-sdk
ENV PATH="${PATH}:${ANDROID_HOME}/cmdline-tools/latest/bin:${ANDROID_HOME}/platform-tools"

# Install required packages
RUN apt-get update && apt-get install -y \
    curl \
    unzip \
    git \
    && rm -rf /var/lib/apt/lists/*

# Download and install Android command line tools
RUN mkdir -p ${ANDROID_HOME}/cmdline-tools && \
    curl -o /tmp/commandlinetools.zip https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip && \
    unzip /tmp/commandlinetools.zip -d ${ANDROID_HOME}/cmdline-tools && \
    mv ${ANDROID_HOME}/cmdline-tools/cmdline-tools ${ANDROID_HOME}/cmdline-tools/latest && \
    rm /tmp/commandlinetools.zip

# Accept licenses and install SDK components
RUN yes | sdkmanager --licenses && \
    sdkmanager "platform-tools" \
    "platforms;android-34" \
    "build-tools;34.0.0"

# Set working directory
WORKDIR /app

# Copy Gradle configuration files for dependency caching
COPY gradle/ gradle/
COPY gradlew gradlew
COPY gradle.properties gradle.properties
COPY settings.gradle.kts settings.gradle.kts
COPY build.gradle.kts build.gradle.kts
COPY app/build.gradle.kts app/build.gradle.kts
COPY gradle/libs.versions.toml gradle/libs.versions.toml

# Pre-download dependencies (cached in image layer)
# Clean up locks and project .gradle after to avoid conflicts when source is mounted
RUN ./gradlew dependencies --no-daemon || true && \
    find /root/.gradle -name "*.lock" -delete 2>/dev/null || true && \
    rm -rf /app/.gradle

# Default command
CMD ["./gradlew", "build"]
