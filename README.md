# Job Quest Game 🎮

A career-simulation game built with **Java 17** and **Swing**.
Pick a profession, work jobs, pass the quiz mini-game, learn skills, and grow a
career from your first dollar to a high-level professional.

The game is intentionally **in-memory**: there is no database, login, or save
file. Progress lasts for the current run, which keeps the project focused on
object-oriented design.

## ✨ Features

- 🎨 Swing UI with a shared theme (`view/theme/AppTheme`)
- 💼 Four careers: Programmer, Doctor, Logo Designer, Typist
- 🔨 Jobs with a short quiz mini-game (pass score: 50/100)
- 📈 Levels, XP, reputation, and a job bonus
- 🎓 Six learnable skills with playtime requirements
- 🛒 Hospital and Market services for health and energy
- 🏆 Seven story challenges and 21 achievements
- ⏱️ Passive energy/health drain, sickness checks, and knockout penalties
- 🛠️ Developer panel for demos (change stats instantly)
- 🧪 45 unit tests in four test classes (`mvn test`)

## 📋 Requirements

- **JDK 17+**
- **Maven 3.8+**

No database or external service is needed.

## 🚀 Run

```bash
# Run the tests
mvn clean test

# Start the game
mvn exec:java -Dexec.mainClass="main.Main"
```

Build a runnable JAR:

```bash
mvn clean package
java -jar target/job-quest-game-1.0-SNAPSHOT.jar
```

## 🎮 Game mechanics

### Progression

- Choose a career with its own starting balance and job list.
- Every job plays a 3-question quiz round with a 90-second par time.
- A successful job pays money, grants XP, and raises reputation.
- XP needed for the next level is `level × 200`.
- Leveling up refills health and energy.

### Payment

```
final pay = base × (1 + level bonus + reputation bonus + job bonus) × quiz multiplier
```

| Bonus | Rule | Cap |
| --- | --- | --- |
| Level | +5% per level above 1 | +500% |
| Reputation | +0.5% per point | +50% |
| Job bonus | +2% per job-bonus point, +1 point per 10 jobs | +30% |
| Quiz | 0.5 + score / 100 (score 50+ passes) | 1.5× |

XP is `25 + base pay / 8`, also scaled by the quiz multiplier. The quiz score is
80% accuracy + 20% speed.

### Resources

- **Health** (0–100) is restored at the Hospital.
- **Energy** (0–100,000) is restored at the Market.
- Energy drains by 1 every 30 seconds; health drains by 1 every 60 seconds.
- Low health doubles the energy drain; low energy triples the health drain.
- Reaching 0 health or 0 energy costs $200 and blocks jobs for 5 minutes.
- A sickness check runs hourly with a 10% chance to lose 5–10 health.

### Skills and jobs

- Jobs require a profession and, for advanced work, specific skills.
- Skills unlock after a playtime requirement (0 to 110 minutes) and cost money.
- The School screen shows available, locked, and learned skills.
- The Workshop shows available jobs first and locked jobs with the missing skills.

## 🏗️ Architecture

Dependencies point one way:

```
main → view → controller → services / providers → model
                                  ↘ observer
```

- **model** — entities and value objects (`User`, `Wallet`, `Job`, `JobRewards`,
  `QuizRound`, `Achievement`, ...)
- **services** — pure game rules (`JobService`, `JobGate`, `ChallengeService`,
  `AchievementService`, `RoutineService`, `QuizScorer`)
- **provider** — read-only game catalogs (`JobProvider`, `SkillProvider`, ...)
- **controller** — application actions (`GameController`), read queries
  (`GameQueries`), and observer notifications
- **view** — Swing screens; it renders and forwards clicks
- **observer** — `Subject` / `Observer` / `ChangeType` for stat updates
- **utils** — formatting and small shared helpers

### OOP concepts in this project

| Concept | Where to look |
| --- | --- |
| Encapsulation | `User` composition; `QuizRound` state; `Wallet`/`Vitals` setter guards; defensive `ChallengeBook` copies |
| Information hiding | Package-private `UserController`/`PlayerReadModel`/`SkillBook`/rule controllers; `PlayerView`/`Observable`-only view surface; `DevControls` demo port |
| Abstraction | `Identity`, `SkillSet`, `PaymentStats`, `ServiceTarget`, `ScoreStrategy`, `DevControls` |
| Inheritance | `AbstractCatalog<T>`, `UnknownIdentityException` |
| Polymorphism | `LearnableSkill.Availability`, `Purchasable`, `MiniGameLauncher` |
| Composition | `User` owns `Wallet`, `Vitals`, `CareerProgress`, `PlayClock`, books |
| Immutability / value objects | `Job`, `JobRewards`, `Service`, `MiniGameResult`, `QuizQuestion` |
| Observer pattern | `Subject`, `SimpleSubject`, `Observer`, `ChangeType` |
| Strategy pattern | `MiniGameLauncher`/`QuizLauncher`, `ScoreStrategy`/`QuizScorer` |
| Factory methods | `Outcome.success`, `MiniGameResult.quiz`, `QuizRound.random` |
| Dependency inversion | `JobService` depends on `PaymentStats`; services/controllers depend on `*Catalog` contracts; controllers depend on `GamePlayer`/`JobPlayer`/`SkillLearner`/`PurchaseTarget` ports |
| Single responsibility | `PlayerReadModel` (reads) vs `UserController` (commands/notifications); `JobGate` (eligibility) vs `JobController` (outcomes); rule calculators each own one rule |
| Open/closed | Add a mini-game or achievement without editing the existing flow |
| Interface segregation | Role views (`JobBoardView`, `SkillBoardView`, ...), command ports (`GamePlayer`, `JobPlayer`, `SkillLearner`, `PurchaseTarget`), `SkillSet`, `PaymentStats`, goal ports, and narrow upkeep targets for consumers |

See [docs/OOP_DESIGN.md](docs/OOP_DESIGN.md) for a file-by-file tour.

## 📁 Project structure

```
job-quest-game/
├── src/main/java/
│   ├── main/            # Application entry point
│   ├── model/           # Entities, value objects, interfaces
│   │   ├── player/      # User, Wallet, Vitals, CareerProgress, PlayClock, ...
│   │   ├── career/      # Job, JobRewards, Profession, LearnableSkill
│   │   ├── goals/       # Challenge, Achievement and their books
│   │   ├── store/       # Service, Purchasable, ServiceTarget
│   │   └── minigame/    # QuizRound, QuizQuestion, MiniGameResult, ScoreStrategy
│   ├── services/        # Rule composers + job/ and upkeep/ rule classes
│   ├── provider/        # Catalogs of jobs, skills, services, achievements
│   ├── controller/      # GameSession, controllers, SessionTracker, Scheduler port
│   ├── view/            # Swing panels/dialogs, WorldViewPanel, NotificationToast (+ theme, minigame)
│   ├── observer/        # Observer pattern
│   └── utils/           # Lists, DurationText, Require, text helpers, balance
├── src/test/java/       # Domain unit tests
├── docs/                # Design notes
└── pom.xml
```

## 🧪 Tests

```bash
mvn test
```

The suite lives in `ModelRulesTest`, `GameRulesTest`, `UserControllerTest`, and
`PlayerViewBoundaryTest`. It covers progression, resource caps, job rewards,
mini-game scaling, quiz rounds, skill availability, services, routine upkeep,
play-time accrual, purchase outcomes, challenge isolation, session summaries,
and the read-only view boundary. Tests never open a window.

## 🛠️ Developer panel

The **Dev** button opens demo controls for balance, health, energy, level,
reputation, playtime, and job bonus. These mutations live in `CheatService` and
reach the player only through the narrow `DevControls` port, so the aggregate's
`Wallet`, `Vitals`, `CareerProgress`, `JobRecord`, and `PlayClock` stay private.
Upkeep runs on an injected `Scheduler` (`SwingScheduler` in the app), so the
controller layer has no Swing dependency.

## 👨‍💻 Author

Amir Khoshdel Louyeh

## 📄 License

Released under an open license.
