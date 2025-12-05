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
- 🧩 Layered OOP design with read-only views and narrow ports

## 📋 Requirements

- **JDK 17+**
- **Maven 3.8+**

No database or external service is needed.

## 🚀 Run

The easiest way is the launcher script, which also handles UI scaling:

```bash
./run.sh          # Linux / macOS
python3 main.py   # same launcher, via Python
```

Or run with Maven directly:

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="main.Main"
```

Build a runnable JAR:

```bash
mvn clean package
java -jar target/job-quest-game-1.0-SNAPSHOT.jar
```

### Controls

- **F11** — toggle fullscreen / windowed mode
- **ESC** — ask to exit the game

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
- Below 30 health the energy drain doubles; below 25,000 energy the health
  drain triples.
- When a drain brings health or energy to 0, the player pays $200 and cannot
  work for 5 minutes.
- A sickness check runs hourly with a 10% chance to lose 5–10 health.

### Skills and jobs

- Jobs require a profession and, for advanced work, specific skills.
- Skills unlock after a playtime requirement (0 to 110 minutes) and cost money.
- The School screen shows available, locked, and learned skills.
- The Workshop shows available jobs first and locked jobs with the missing skills.

## 🏗️ Architecture

راهنمای فارسیِ ساختار packageها و کلاس‌ها در
[ARCHITECTURE_FA.md](./ARCHITECTURE_FA.md) و نسخه‌ی تعاملی راست‌به‌چپ، همراه با
جست‌وجو، فیلتر، ارتباط‌ها، مفاهیم OOP و کد کامل فایل‌ها در
[ARCHITECTURE_FA.html](./ARCHITECTURE_FA.html) قرار دارد.

Dependencies point one way only. A layer may use the layer below it, but never
the other way around:

```
main → view → controller → services / providers → model
                              ↘ observer
```

- **main** — starts Swing and opens the main window (`MainFrame`)
- **view** — Swing screens; renders data and forwards clicks. It never
  calculates a game rule
- **controller** — application actions (`GameController`), read queries
  (`GameQueries`), and observer notifications. It never paints on screen
- **services** — pure game rules (`JobRewardService`, `JobGate`, `ChallengeService`,
  `AchievementService`, `RoutineService`, `QuizScorer`). No UI, no database
- **provider** — read-only catalogs of jobs, skills, services, achievements,
  challenges, quizzes, and careers. These are data, not game logic
- **model** — entities, value objects, small rules, and the ports that connect
  the layers (`Player`, `Wallet`, `Job`, `JobRewards`, `QuizRound`, ...)
- **observer** — `Subject` / `Observer` / `ChangeType` for stat updates
- **utils** — formatting and small shared helpers

Four rules keep the design honest:

- A view never calculates a game rule.
- A controller never paints on screen.
- A service never opens a dialog.
- A model object never reads the keyboard or the screen.

`model` is the bottom layer and depends only on `utils`. That is why the
dependency arrows never point back up.

### How a click travels

```text
JobDialog (view)
  → GameController
    → JobController
      → JobGate (can this job start?)
      → JobRewardService → JobPaymentCalculator + JobRewardCalculator
      → PlayerController (commands + notifications)
        → Player → Wallet / Vitals / CareerProgress / books
  → Outcome (success/failure + message) back to the view
  → PlayerController notifies observers → GamePanel refreshes the stats panel
```

Views never receive the mutable `Player`. `GameSession` hands them a read-only
`PlayerView` and a subscribe-only `Observable`, so a cast cannot reach a command
port or the aggregate.

### 🧭 Where to start reading

| Order | File | Why |
| --- | --- | --- |
| 1 | `model/career/Profession.java` | The profession contract |
| 2 | `model/player/Wallet.java` | Encapsulation with guarded commands |
| 3 | `model/player/Vitals.java` | Clamping inside private setters |
| 4 | `model/player/CareerProgress.java` | Multi-level XP in one call |
| 5 | `model/player/Player.java` | Aggregate root built by composition |
| 6 | `model/career/Job.java` + `JobRewards.java` | Immutable value objects |
| 7 | `model/minigame/QuizRound.java` | A small state machine |
| 8 | `provider/AbstractCatalog.java` | Generics + inheritance |
| 9 | `services/JobRewardService.java` | Composition of two rule classes |
| 10 | `observer/SimpleSubject.java` | Defensive snapshot + safe notify |
| 11 | `controller/PlayerController.java` | Adapter, ports, projections |
| 12 | `controller/GameSession.java` | Composition root |
| 13 | `controller/GameController.java` + `GameQueries.java` | Action/read split |

## 🧠 OOP concepts in this project

| Concept | Where to look |
| --- | --- |
| Encapsulation | `Player` composition; `QuizRound` state; `Wallet`/`Vitals` setter guards; defensive `ChallengeBook` copies |
| Information hiding | Package-private `PlayerController`/`SkillBook`/rule controllers; views get read-only `PlayerView` and `Observable` projections, so no cast back to a command port or `Player`; `DevControls` demo port |
| Abstraction | `Profession`, `SkillSet`, `PaymentStats`, `ShopItemTarget`, `ScoreStrategy`, `PlayerView`, `UpkeepTarget`, `DevControls` |
| Inheritance | `AbstractCatalog<T>` (template method), `AbstractIdentified` (shared identity), `UnknownProfessionException` |
| Polymorphism | Ad-hoc: `GameController.doJob` overloads; Coercion: `ProgressMath.percent(double, double)` called with `int` values; Inclusion: `ScoreStrategy`/`QuizScorer`, `ShopItem.apply(ShopItemTarget)`, `Profession`/`ProfessionDefinition`; Parametric: `Catalog<T>`/`AbstractCatalog<T>` |
| Composition | `Player` owns `Wallet`, `Vitals`, `CareerProgress`, `PlayClock`, books |
| Immutability / value objects | `Job`, `JobRewards`, `ShopItem`, `MiniGameResult`, `QuizQuestion` |
| Observer pattern | `Subject`, `SimpleSubject`, `Observer`, `ChangeType` |
| Strategy pattern | `MiniGameLauncher`/`QuizLauncher`, `ScoreStrategy`/`QuizScorer` |
| Factory methods | `Outcome.success`, `MiniGameResult.quiz`, `QuizRound.random` |
| Dependency inversion | `JobRewardService` depends on `PaymentStats`; upkeep rules on `UpkeepTarget`; goal services on `PlayerProgress`; controllers on `JobPlayer`/`SkillLearner`/`PurchaseTarget`; services and views on `*Catalog` contracts |
| Single responsibility | `JobGate` (eligibility) vs `JobController` (outcomes); rule calculators each own one rule; `GameController` (actions) vs `GameQueries` (reads) |
| Open/closed | Add a mini-game or achievement without editing the existing flow |
| Interface segregation | Read surface (`PlayerView`), goal surface (`PlayerProgress`), per-use-case ports (`JobPlayer`, `SkillLearner`, `PurchaseTarget`), plus `SkillSet`, `PaymentStats`, `AchievementStats`, and `UpkeepTarget` |
| Adapter / port | `PlayerController` (adapter over `Player`); `Scheduler`/`SwingScheduler`; `MessageSink`/`GamePanel` |
| Composition root | `GameSession` builds the player, services, controllers, and queries |
| Defensive copying | `utils.Lists`, `AbstractCatalog.getAll()`, `ChallengeProvider`, `QuizProvider` |

## 📁 Project structure

```text
job-quest-game/
├── src/main/java/
│   ├── main/            # Application entry point
│   ├── model/           # Entities, value objects, ports, small rules
│   │   ├── career/      # Job, JobRewards, Profession, ProfessionDefinition, LearnableSkill
│   │   ├── goals/       # Challenge, Achievement and their books
│   │   ├── minigame/    # QuizRound, QuizQuestion, MiniGameResult, ScoreStrategy
│   │   ├── player/      # Player, Wallet, Vitals, CareerProgress, ports
│   │   └── store/       # ShopItem, ShopItemTarget
│   ├── services/        # Rule composers
│   │   ├── job/         # JobGate, payment and reward calculators
│   │   ├── minigame/    # QuizScorer
│   │   └── upkeep/      # Drain, sickness, knockout rules
│   ├── provider/        # Read-only catalogs and profession registry
│   ├── controller/      # GameSession, controllers, queries, ports
│   ├── view/            # Swing screens, dialogs, theme
│   │   ├── minigame/    # QuizLauncher, QuizGameDialog
│   │   └── theme/       # AppTheme
│   ├── observer/        # Observer pattern
│   └── utils/           # Lists, formatting, GameBalance, logging
├── src/main/resources/images/
├── pom.xml
├── run.sh
└── main.py
```

## 🧪 Testing

`mvn test` runs the JUnit 5 suite under `src/test/java`:

- `PlayerTest` — player aggregate rules.
- `JobGateTest`, `JobRewardServiceTest` — eligibility and reward math.
- `ChallengeServiceTest`, `AchievementServiceTest` — goal progress and rewards.
- `RoutineServiceTest`, `DrainRuleTest`, `SicknessRuleTest`, `KnockoutPolicyTest` — upkeep rules.
- `AbstractCatalogTest`, `AbstractIdentifiedTest` — abstract base behaviour.

The rules depend on small ports (`PaymentStats`, `UpkeepTarget`,
`PlayerProgress`, `ShopItemTarget`) and time is injected as a `Supplier<Long>`,
so the suite runs headless with fake players and no Swing window.

## 🛠️ Developer panel

The **Dev** button opens demo controls for balance, health, energy, level,
reputation, playtime, and job bonus. `DeveloperController` mutates through the
narrow `DevControls` port only, so the aggregate's `Wallet`, `Vitals`,
`CareerProgress`, `JobRecord`, and `PlayClock` stay private.

Upkeep runs on an injected `Scheduler` (`SwingScheduler` in the app), so the
controller layer has no Swing dependency.

## 👨‍💻 Author

Amir Khoshdel Louyeh

## 📄 License

Released under an open license.
