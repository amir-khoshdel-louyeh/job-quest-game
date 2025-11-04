#!/bin/bash

# Job Quest Game Launcher
# This script handles library conflicts and runs the game properly


# ================= CONFIG =================
# UI scaling factor - EDIT THIS VALUE to change zoom:
#   1.0  = 100% (small / Fedora default)
#   1.25 = 125% (like Windows)
#   1.5  = 150% (recommended)
#   2.0  = 200% (large / HiDPI)
UI_SCALE="2.0"
# ============================================

echo "🎮 Starting Job Quest Game (UI scale: $UI_SCALE)..."
echo ""

# Clear any conflicting library paths from snap
export LD_LIBRARY_PATH=""
# Suppress Guice/sun.misc.Unsafe warnings from Maven on Java 25
export MAVEN_OPTS="--sun-misc-unsafe-memory-access=allow"

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

    echo "🚀 Launching game (sun.java2d.uiScale=$UI_SCALE)..."
    env -i \
        HOME="$HOME" \
        USER="$USER" \
        JAVA_HOME="$JAVA_HOME" \
        PATH="$JAVA_HOME/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin" \
        DISPLAY="$DISPLAY" \
        WAYLAND_DISPLAY="$WAYLAND_DISPLAY" \
        XDG_RUNTIME_DIR="$XDG_RUNTIME_DIR" \
        XAUTHORITY="$XAUTHORITY" \
        java -Dsun.java2d.uiScale="$UI_SCALE" \
             -Dawt.useSystemAAFontSettings=on \
             -Dswing.aatext=true \
             -cp target/classes:target/dependency/* main.Main
else
    echo "❌ Compilation failed. Please check the errors above."
    exit 1
fi
