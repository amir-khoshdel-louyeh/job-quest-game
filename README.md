# Job Quest Game 🎮

A professional career simulation game with modern Java Swing GUI

## ✨ Features

- 🎨 **Modern Professional UI** - Material Design inspired interface
- 🔄 **Automatic Database Setup** - No manual configuration needed
- 💼 **Multiple Career Paths** - Choose from Programmer, Doctor, Logo Designer, or Typist
- 📊 **Resource Management** - Track health, energy, and money
- 🛒 **Shopping System** - Buy items and services
- 📚 **Skill Learning** - Upgrade your abilities
- 💾 **Auto-Save** - Your progress is automatically saved
- 🏆 **Professional Architecture** - Built with SOLID principles and design patterns

## 📋 Prerequisites

### 1. Java Development Kit (JDK)
```bash
sudo apt update
sudo apt install openjdk-17-jdk -y
java -version
```

### 2. Apache Maven
```bash
sudo apt install maven -y
mvn -version
```

### 3. MySQL Server
```bash
sudo apt install mysql-server -y
sudo systemctl enable --now mysql
sudo systemctl status mysql
```

## 🚀 Installation and Setup

### Step 1: Clone the Project
```bash
git clone https://github.com/amir-khoshdel-louyeh/job-quest-game.git
cd job-quest-game
```

### Step 2: Database Setup

**Good News:** Database is created automatically! ✨

The application will automatically on first run:
- Create the `job_quest` database
- Create user `jobuser` with password `jobpass`
- Create all required tables

**Just make sure MySQL is installed and running!**

#### (Optional) Manual Database Setup

If you want to create the database manually:

```bash
sudo mysql -e "
CREATE DATABASE IF NOT EXISTS job_quest CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
CREATE USER IF NOT EXISTS 'jobuser'@'localhost' IDENTIFIED BY 'jobpass';
GRANT ALL PRIVILEGES ON job_quest.* TO 'jobuser'@'localhost';
FLUSH PRIVILEGES;
"
```

### Step 3: Compile and Run

```bash
# Compile the project
mvn clean compile

# Run the game
mvn exec:java -Dexec.mainClass="main.Main"
```

**On first run** you will see these messages:
```
Checking and initializing database...
✓ Database 'job_quest' is ready.
✓ Table 'users' is ready.
✓ Table 'items' is ready.
Database initialized successfully.
```

## 🏗️ Build Executable JAR

```bash
# Build JAR
mvn clean package

# Run JAR
java -jar target/job-quest-game-1.0-SNAPSHOT.jar
```

## 🎮 Game Mechanics

### Progression System
1. **Start** with basic stats and $5,000
2. **Complete jobs** to earn money and XP
3. **Learn skills** to unlock higher-paying jobs
4. **Build reputation** for payment bonuses (up to +50%)
5. **Level up** for permanent bonuses (+5% per level)
6. **Maintain streaks** for daily login rewards (up to +30%)
7. **Complete quests** for bonus money and XP
8. **Unlock achievements** for major rewards

### Payment Calculation
```
Final Payment = Base × (1 + Reputation Bonus + Level Bonus + Streak Bonus)
```

**Example:** 
- Level 10 (+ 45%) + Reputation 80 (+40%) + 15-day Streak (+15%)
- $1,000 base job → **$2,000 final payment!**

### Quest Types
- **Daily Quests:** Complete 3 jobs, earn specific amounts
- **Weekly Quests:** Longer-term goals with bigger rewards
- **Main Story:** One-time quests that guide your journey

### Random Events
Events occur randomly and can:
- ✨ Grant bonus money, health, or energy
- ⚠️ Create challenges like equipment repairs
- ℹ️ Provide information and small boosts

See [GAME_LOGIC_GUIDE.md](GAME_LOGIC_GUIDE.md) for detailed mechanics!

## 🔧 Troubleshooting

### Error: "Connection refused"
```bash
# Check MySQL status
sudo systemctl status mysql

# Restart MySQL
sudo systemctl restart mysql
```

### Error: "Access denied for user 'jobuser'"
The application creates the user automatically. If you get an error, create it manually:
```bash
sudo mysql -e "
CREATE USER IF NOT EXISTS 'jobuser'@'localhost' IDENTIFIED BY 'jobpass';
GRANT ALL PRIVILEGES ON job_quest.* TO 'jobuser'@'localhost';
FLUSH PRIVILEGES;
"
```

### Error: "Unknown database 'job_quest'"
The application creates the database automatically. If you get an error, create it manually:
```bash
sudo mysql -e "CREATE DATABASE IF NOT EXISTS job_quest CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;"
```

### Verify Database Connection
```bash
# Connect to MySQL
mysql -u jobuser -p job_quest
# Password: jobpass

# Show tables
SHOW TABLES;

# Exit
exit;
```

## 📁 Project Structure

```
job-quest-game/
├── src/main/java/
│   ├── controller/      # Game controllers
│   ├── database/        # Database management
│   │   ├── DatabaseConnection.java
│   │   ├── DatabaseInitializer.java  # 🆕 Auto-initialization
│   │   ├── DatabaseUtil.java
│   │   └── jobـquest.sql
│   ├── main/           # Application entry point
│   ├── model/          # Data models
│   ├── services/       # Game services
│   ├── utils/          # Utility tools
│   └── view/           # User interface
├── src/main/resources/
│   └── images/         # Game images
├── pom.xml             # Maven configuration
└── README.md
```

## 🛠️ Technologies Used

- **Java 17+**: Main programming language
- **Java Swing**: Graphical user interface
- **MySQL 8.0**: Database
- **JDBC**: Database connection
- **Maven**: Project and dependency management

## 📦 Dependencies

All dependencies are defined in `pom.xml` and Maven automatically downloads them:

- **MySQL Connector/J 8.0.33**: JDBC driver for MySQL

## 🔒 Database Configuration

Default settings:
- **Host**: localhost
- **Port**: 3306
- **Database**: job_quest
- **Username**: jobuser
- **Password**: jobpass

To change these settings, edit these files:
- `src/main/java/database/DatabaseConfig.java`
- `src/main/java/database/DatabaseConnection.java`
- `src/main/java/database/DatabaseInitializer.java`

## 📊 Game Features

- ✅ **Modern Professional UI** with Material Design
- ✅ Registration and login system with smooth animations
- ✅ Multiple career paths with unique skills
- ✅ Interactive identity selection with hover effects
- ✅ Work and income system
- ✅ Health and energy management
- ✅ Shop and inventory system
- ✅ Skill learning system
- ✅ Automatic game progress save
- ✅ Total play time calculation
- ✅ Automatic database initialization
- ✅ Repository pattern for data access
- ✅ SOLID principles and design patterns

## 🏗️ Architecture

The project follows professional software engineering practices:

- **Design Patterns**: Singleton, Repository, Factory, Strategy, Observer
- **SOLID Principles**: All five principles implemented
- **Layered Architecture**: Presentation, Controller, Service, Repository, Data Access
- **Modern UI**: Theme system with consistent styling

For detailed architecture documentation, see:
- `ARCHITECTURE.md` - Design patterns and principles
- `OOP_REFACTORING.md` - Code quality improvements
- `GUI_IMPROVEMENTS.md` - UI/UX enhancements

## 📚 Documentation

- **[README.md](README.md)** - Getting started guide
- **[GAME_LOGIC_GUIDE.md](GAME_LOGIC_GUIDE.md)** - ⭐ Detailed game mechanics and systems
- **[ARCHITECTURE.md](ARCHITECTURE.md)** - Design patterns and architecture
- **[OOP_REFACTORING.md](OOP_REFACTORING.md)** - Code improvements summary
- **[GUI_IMPROVEMENTS.md](GUI_IMPROVEMENTS.md)** - UI/UX enhancements
- **[CHANGES_SUMMARY.md](CHANGES_SUMMARY.md)** - Complete transformation summary

## 🎯 Quick Start Guide

### First Time Playing
1. Register a new account
2. Choose your career identity
3. Complete your first job to earn money
4. Check your daily quests
5. Save money to learn your first skill

### Advanced Strategy
1. **Build your streak** - Login daily for +30% payment bonus
2. **Increase reputation** - Complete jobs consistently
3. **Learn all skills** - Unlock the highest-paying jobs
4. **Complete quests** - Easy money and XP
5. **Unlock achievements** - Earn up to $39,000 in rewards!

See [GAME_LOGIC_GUIDE.md](GAME_LOGIC_GUIDE.md) for detailed strategies!

## 📈 Progression Overview

```
Level 1 → 10:  Build foundation, learn first skills
Level 11 → 30: Increase reputation, complete quests  
Level 31 → 50: Maximize bonuses, unlock achievements
Level 51+:     Master tier - Earn 3x more per job!
```

## 🏆 Achievement System

Unlock 12+ achievements:
- 💰 Money achievements (First Dollar → Millionaire)
- 📊 Job achievements (First Job → Workaholic)  
- 🎓 Skill achievements (Student → Master)
- 🎯 Special achievements (Health, Dedication)

**Total Rewards:** $39,000+

## 📜 Quest System

**Daily Quests:** Reset every 24 hours
- Complete 3 jobs: $500 + 100 XP
- Earn $1,000: $300 + 50 XP

**Weekly Quests:** Reset every 7 days
- Complete 20 jobs: $3,000 + 500 XP
- Learn 2 skills: $2,000 + 400 XP

**Main Story:** Progressive career journey
- 5 main quests with increasing difficulty
- Total rewards: $8,200 + 1,650 XP

## ⚡ Random Events

15+ events can occur:
- ✨ Positive: Bonus money, energy, health
- ⚠️ Negative: Bills, equipment issues  
- ℹ️ Neutral: Information and small boosts

Events trigger randomly with 5-minute cooldown.

## 💰 Payment Bonuses

Jobs pay more as you progress:

**Base Payment Multipliers:**
- Level bonus: +5% per level (up to +500%)
- Reputation bonus: Up to +50% at max
- Streak bonus: +1% per day (max +30%)

**Example:** A $1,000 job can pay $3,000+ with all bonuses!

## 🎨 Professional UI Features

## 👨‍💻 Developer

Amir Khoshdel Louyeh

## 📄 License

This project is released under an open license.

---

## 🎯 Quick Start

```bash
# Clone, compile and run in 3 lines!
git clone https://github.com/amir-khoshdel-louyeh/job-quest-game.git
cd job-quest-game
mvn clean compile && mvn exec:java -Dexec.mainClass="main.Main"
```

**Enjoy!** 🚀
