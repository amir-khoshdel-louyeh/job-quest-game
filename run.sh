#!/bin/bash

# Job Quest Game Launcher
# This script handles library conflicts and runs the game properly

echo "🎮 Starting Job Quest Game..."
echo ""

# Clear any conflicting library paths from snap
export LD_LIBRARY_PATH=""

# Check if MySQL is running
if ! systemctl is-active --quiet mysql; then
    echo "⚠️  MySQL is not running. Starting MySQL..."
    sudo systemctl start mysql
    sleep 2
fi

# Check if Java is available
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed. Please install Java 17 or higher."
    exit 1
fi

# Check if Maven is available
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven is not installed. Please install Maven."
    exit 1
fi

echo "✓ Dependencies check passed"
echo ""

# Compile and run the project
echo "📦 Compiling project..."
mvn clean compile -q

if [ $? -eq 0 ]; then
    echo "✓ Compilation successful"
    echo ""
    echo "🚀 Launching game..."
    echo ""
    
    # Copy dependencies and run with system Java (cleared env to avoid snap conflicts)
    echo "📂 Copying dependencies..."
    mvn dependency:copy-dependencies -q -DoutputDirectory=target/dependency

    echo "🚀 Launching game..."
    env -i \
        HOME="$HOME" \
        USER="$USER" \
        PATH="$JAVA_HOME/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin" \
        DISPLAY="$DISPLAY" \
        XAUTHORITY="$XAUTHORITY" \
        java -cp target/classes:target/dependency/* main.Main
else
    echo "❌ Compilation failed. Please check the errors above."
    exit 1
fi
