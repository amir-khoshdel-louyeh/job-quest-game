package view;

import view.theme.AppTheme;
import javax.swing.*;
import java.awt.*;

/**
 * About and Help dialog with game instructions and information.
 */
public class AboutDialog extends JDialog {
    
    // Dialog constructor
    public AboutDialog(JFrame parent) {
        super(parent, "📖 About Job Quest Game", true);
        setSize(900, 700);
        setLocationRelativeTo(parent);
        setResizable(false);
        
        initComponents();
    }
    
    // Initialize dialog components and layout
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(AppTheme.BACKGROUND_COLOR);
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(AppTheme.PRIMARY_COLOR);
        JLabel titleLabel = new JLabel("🎮 Job Quest - Career Simulation Game");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);
        
        // Tabbed content
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // Tab 1: How to Play
        tabbedPane.addTab("🎯 How to Play", createHowToPlayPanel());
        
        // Tab 2: Game Mechanics
        tabbedPane.addTab("⚙️ Game Mechanics", createMechanicsPanel());
        
        // Tab 3: Progression
        tabbedPane.addTab("📈 Progression", createProgressionPanel());
        
        // Tab 4: Tips & Tricks
        tabbedPane.addTab("💡 Tips & Tricks", createTipsPanel());
        
        // Tab 5: About
        tabbedPane.addTab("ℹ️ About", createAboutPanel());
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Close button at the bottom. Kept simple for beginners: click to close.
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(AppTheme.BACKGROUND_COLOR);
        JButton closeButton = AppTheme.createPrimaryButton("Close");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    // Create "How to Play" tab panel
    private JPanel createHowToPlayPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        String content = """
            <html>
            <body style='font-family: Segoe UI; padding: 15px;'>
            <h2>🎮 How to Play Job Quest</h2>
            
            <h3>🏁 Getting Started</h3>
            <ol>
                <li><b>Choose Your Career:</b> Select from Programmer, Doctor, Logo Designer, or Typist</li>
                <li><b>Complete Jobs:</b> Click on the Workshop (center) in the World View to work</li>
                <li><b>Manage Resources:</b> Keep track of your Health, Energy, and Money</li>
                <li><b>Level Up:</b> Gain XP from jobs to increase your level</li>
            </ol>
            
            <h3>🗺️ World View Map</h3>
            <p>Click on different buildings in the World View:</p>
            <ul>
                <li><b>🏥 Hospital (Left):</b> Restore health and get medical services</li>
                <li><b>🏢 Workshop (Center-Left):</b> Complete jobs to earn money and XP</li>
                <li><b>🏪 Supermarket (Center-Right):</b> Buy food to restore energy</li>
                <li><b>🎓 School (Right):</b> Learn new skills to unlock better jobs</li>
            </ul>
            
            <h3>💰 Earning Money</h3>
            <ol>
                <li>Click on the Workshop to see available jobs</li>
                <li>Select a job based on your energy and skills</li>
                <li>Complete the job to earn money and gain XP</li>
                <li>Higher level and reputation = more money!</li>
            </ol>
            
            <h3>⚡ Managing Energy & Health</h3>
            <ul>
                <li><b>Energy:</b> Decreases over time and when working. Buy food to restore.</li>
                <li><b>Health:</b> Decreases slowly. Visit hospital or rest to restore.</li>
                <li><b>Warning:</b> Low health or energy reduces your work efficiency!</li>
            </ul>
            
            <h3>🎓 Learning Skills</h3>
            <ol>
                <li>Visit the School (right side of World View)</li>
                <li>Purchase skills with your earned money</li>
                <li>Skills unlock higher-paying jobs</li>
                <li>Learn all 6 skills to become a Master!</li>
            </ol>
            
            <h3>💾 Saving Your Progress</h3>
            <p>Click "Save & Exit" button to save your game progress and exit safely.</p>
            </body>
            </html>
            """;
        
        // Use an editor pane to render simple HTML content and make it scrollable.
        panel.add(createHtmlScroll(content), BorderLayout.CENTER);
        return panel;
    }
    
    // Create "Game Mechanics" tab panel
    private JPanel createMechanicsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        String content = """
            <html>
            <body style='font-family: Segoe UI; padding: 15px;'>
            <h2>⚙️ Game Mechanics</h2>
            
            <h3>⭐ Level & Experience System</h3>
            <ul>
                <li><b>Max Level:</b> 100</li>
                <li><b>XP Required:</b> Level × 1,000 (e.g., Level 5 needs 5,000 XP)</li>
                <li><b>Gaining XP:</b> Complete jobs (Energy Cost ÷ 10), complete challenges</li>
                <li><b>Level Up Bonus:</b> Full health and energy restoration!</li>
                <li><b>Payment Bonus:</b> +5% per level (Level 10 = +50% earnings!)</li>
            </ul>
            
            <h3>🏆 Reputation System</h3>
            <table border='1' cellpadding='5'>
                <tr><th>Score</th><th>Title</th><th>Bonus</th></tr>
                <tr><td>0-19</td><td>Newbie</td><td>0-9%</td></tr>
                <tr><td>20-39</td><td>Beginner</td><td>10-19%</td></tr>
                <tr><td>40-59</td><td>Skilled</td><td>20-29%</td></tr>
                <tr><td>60-74</td><td>Professional</td><td>30-37%</td></tr>
                <tr><td>75-89</td><td>Expert</td><td>38-44%</td></tr>
                <tr><td>90-100</td><td>Legendary</td><td>45-50%</td></tr>
            </table>
            <p><b>How to Gain:</b> Complete jobs (30% chance), learn skills (+2), complete challenges (+2)</p>
            
            <h3>🔥 Daily Streak System</h3>
            <ul>
                <li><b>Login Daily:</b> Increases your streak counter</li>
                <li><b>Miss a Day:</b> Streak resets after 48 hours</li>
                <li><b>Bonus:</b> +1% payment per day (max +30% at 30-day streak!)</li>
                <li><b>Track:</b> Current streak and personal best displayed</li>
            </ul>
            
            <h3>💰 Payment Calculation</h3>
            <p style='background: #f0f0f0; padding: 10px; border-left: 4px solid #3f51b5;'>
                <b>Final Payment = Base Payment × (1 + Level% + Reputation% + Streak%)</b>
            </p>
            <p><b>Example:</b> $1,000 base job at Level 10, Reputation 80, 15-day streak:</p>
            <ul>
                <li>Level 10: +45% ($450)</li>
                <li>Reputation 80: +40% ($400)</li>
                <li>Streak 15: +15% ($150)</li>
                <li><b>Total: $2,000!</b> (100% bonus!)</li>
            </ul>
            
            <h3>🎖️ Achievements (12 Total)</h3>
            <ul>
                <li><b>Money:</b> First Dollar, Money Maker, Wealthy, Millionaire</li>
                <li><b>Jobs:</b> First Job, Hard Worker, Workaholic</li>
                <li><b>Skills:</b> Student, Skill Collector, Master</li>
                <li><b>Special:</b> Healthy Lifestyle, Dedicated Player</li>
                <li><b>Total Rewards:</b> $39,000+</li>
            </ul>
            
            <h3>📜 Challenge System</h3>
            <ul>
                <li><b>Daily Challenges:</b> Reset every 24 hours (Complete 3 jobs, Earn $1,000)</li>
                <li><b>Weekly Challenges:</b> Reset every 7 days (Complete 20 jobs, Learn skills)</li>
                <li><b>Main Story:</b> One-time progressive challenges</li>
                <li><b>Total Value:</b> $56,200 + 10,950 XP</li>
            </ul>
            
            <h3>⚡ Random Events</h3>
            <ul>
                <li><b>Trigger:</b> 20% chance every 5 minutes</li>
                <li><b>Types:</b> Positive (bonuses), Negative (challenges), Neutral (info)</li>
                <li><b>Effects:</b> Can gain/lose money, health, or energy</li>
                <li><b>Examples:</b> Lucky Find (+$500), Unexpected Bill (-$800)</li>
            </ul>
            </body>
            </html>
            """;
        
        panel.add(createHtmlScroll(content), BorderLayout.CENTER);
        return panel;
    }
    
    // Create "Progression" tab panel
    private JPanel createProgressionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        String content = """
            <html>
            <body style='font-family: Segoe UI; padding: 15px;'>
            <h2>📈 Progression System</h2>
            
            <h3>🎯 Your Journey</h3>
            
            <h4>Week 1: The Beginner (Level 1-5)</h4>
            <ul>
                <li>✅ Complete your first jobs</li>
                <li>✅ Learn your first skill (Typing Proficiency)</li>
                <li>✅ Start building your login streak</li>
                <li>✅ Complete daily challenges</li>
                <li>💰 <b>Expected Earnings:</b> $5,000-$10,000</li>
            </ul>
            
            <h4>Week 2-3: The Learner (Level 6-15)</h4>
            <ul>
                <li>✅ Learn 3-5 skills</li>
                <li>✅ Complete weekly challenges</li>
                <li>✅ Build reputation to 40+</li>
                <li>✅ Maintain 10+ day streak</li>
                <li>✅ Unlock first achievements</li>
                <li>💰 <b>Expected Earnings:</b> $20,000-$40,000</li>
            </ul>
            
            <h4>Month 2: The Professional (Level 16-30)</h4>
            <ul>
                <li>✅ Master all 6 skills</li>
                <li>✅ Reputation 60-80</li>
                <li>✅ Complete main story challenges</li>
                <li>✅ 20+ achievements unlocked</li>
                <li>✅ Earn $100,000+</li>
                <li>💰 <b>Expected Earnings:</b> $100,000+</li>
            </ul>
            
            <h4>Month 3+: The Master (Level 31-100)</h4>
            <ul>
                <li>✅ Level 50+</li>
                <li>✅ Reputation 90-100 (Legendary!)</li>
                <li>✅ 30-day streaks</li>
                <li>✅ All achievements unlocked</li>
                <li>✅ Maximum bonuses active</li>
                <li>💰 <b>Expected Earnings:</b> $200,000+ per month</li>
            </ul>
            
            <h3>💸 Payment Growth Example</h3>
            <table border='1' cellpadding='8' style='border-collapse: collapse; width: 100%;'>
                <tr style='background: #3f51b5; color: white;'>
                    <th>Stage</th>
                    <th>Level</th>
                    <th>Rep</th>
                    <th>Streak</th>
                    <th>$1,000 Job Pays</th>
                    <th>Growth</th>
                </tr>
                <tr>
                    <td>Beginner</td>
                    <td>1</td>
                    <td>0</td>
                    <td>0</td>
                    <td><b>$1,000</b></td>
                    <td>-</td>
                </tr>
                <tr style='background: #f0f0f0;'>
                    <td>Learner</td>
                    <td>10</td>
                    <td>50</td>
                    <td>7</td>
                    <td><b>$1,770</b></td>
                    <td>+77%</td>
                </tr>
                <tr>
                    <td>Professional</td>
                    <td>30</td>
                    <td>80</td>
                    <td>20</td>
                    <td><b>$3,050</b></td>
                    <td>+205%</td>
                </tr>
                <tr style='background: #f0f0f0;'>
                    <td>Master</td>
                    <td>50+</td>
                    <td>100</td>
                    <td>30</td>
                    <td><b>$4,250</b></td>
                    <td>+325%</td>
                </tr>
            </table>
            
            <h3>🏆 Achievement Hunter's Path</h3>
            <p><b>Easy (Week 1):</b></p>
            <ul>
                <li>✅ First Job - Complete 1 job → $100</li>
                <li>✅ First Dollar - Earn $100 → $50</li>
                <li>✅ Student - Learn 1 skill → $200</li>
            </ul>
            
            <p><b>Medium (Week 2-4):</b></p>
            <ul>
                <li>✅ Hard Worker - Complete 50 jobs → $1,000</li>
                <li>✅ Skill Collector - Learn 5 skills → $1,000</li>
                <li>✅ Money Maker - Earn $5,000 → $500</li>
            </ul>
            
            <p><b>Hard (Month 2+):</b></p>
            <ul>
                <li>✅ Millionaire - Have $100,000 → $20,000</li>
                <li>✅ Master - Learn all skills → $5,000</li>
                <li>✅ Dedicated Player - Play 30 days → $3,000</li>
            </ul>
            
            <h3>🎯 Optimization Strategy</h3>
            <ol>
                <li><b>Login Daily:</b> Never break your streak! (+30% max bonus)</li>
                <li><b>Learn Skills Early:</b> They unlock better jobs quickly</li>
                <li><b>Build Reputation:</b> 50% payment bonus at max is HUGE!</li>
                <li><b>Complete Challenges:</b> Easy money and XP source</li>
                <li><b>Level Up Fast:</b> Each level = +5% permanent bonus</li>
                <li><b>Maintain Health:</b> Low health reduces efficiency</li>
                <li><b>Save Strategically:</b> Don't overspend early game</li>
            </ol>
            </body>
            </html>
            """;
        
        panel.add(createHtmlScroll(content), BorderLayout.CENTER);
        return panel;
    }
    
    // Create "Tips & Tricks" tab panel
    private JPanel createTipsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        String content = """
            <html>
            <body style='font-family: Segoe UI; padding: 15px;'>
            <h2>💡 Tips & Tricks</h2>
            
            <h3>💰 Money Making Tips</h3>
            <ul>
                <li>🎯 <b>Focus on Bonuses:</b> Level + Rep + Streak can triple your earnings!</li>
                <li>🎯 <b>Learn Skills ASAP:</b> Higher-paying jobs require skills</li>
                <li>🎯 <b>Complete Challenges:</b> Free money and XP - don't ignore them!</li>
                <li>🎯 <b>Watch Events:</b> Random events can give free money</li>
                <li>🎯 <b>Build Streak:</b> 30-day streak = permanent +30% income</li>
            </ul>
            
            <h3>⚡ Resource Management</h3>
            <ul>
                <li>💡 <b>Energy Efficiency:</b> Buy food in bulk when cheap</li>
                <li>💡 <b>Health Priority:</b> Keep health above 80 to avoid penalties</li>
                <li>💡 <b>Timing:</b> Energy decreases every 5 seconds, plan accordingly</li>
                <li>💡 <b>Rest:</b> Sometimes resting is better than working with low stats</li>
            </ul>
            
            <h3>📈 Fast Leveling</h3>
            <ul>
                <li>⭐ <b>High Energy Jobs:</b> More energy = more XP (Energy ÷ 10)</li>
                <li>⭐ <b>Challenge Rewards:</b> Challenges give bonus XP</li>
                <li>⭐ <b>Skill Learning:</b> Each skill = reputation + achievement progress</li>
                <li>⭐ <b>Consistent Play:</b> Daily challenges add up quickly</li>
            </ul>
            
            <h3>🏆 Achievement Hunting</h3>
            <ul>
                <li>🎖️ <b>Start Early:</b> Some achievements track lifetime stats</li>
                <li>🎖️ <b>Workaholic:</b> Complete 10 jobs in one session</li>
                <li>🎖️ <b>Healthy Lifestyle:</b> Maintain 100 health for 7 days</li>
                <li>🎖️ <b>Master:</b> Plan your skill purchases to afford all 6</li>
            </ul>
            
            <h3>🎯 Strategic Planning</h3>
            <ul>
                <li>📊 <b>Early Game (Lv 1-10):</b> Focus on learning first 3 skills</li>
                <li>📊 <b>Mid Game (Lv 11-30):</b> Build reputation to 60+</li>
                <li>📊 <b>Late Game (Lv 31+):</b> Maximize all bonuses, farm achievements</li>
            </ul>
            
            <h3>⚠️ Common Mistakes to Avoid</h3>
            <ul>
                <li>❌ <b>Breaking Streak:</b> Login daily even if just for a minute!</li>
                <li>❌ <b>Ignoring Health:</b> Low health reduces work efficiency</li>
                <li>❌ <b>Overspending:</b> Save money for skills, they're investments</li>
                <li>❌ <b>Skipping Challenges:</b> They're the easiest money + XP source</li>
                <li>❌ <b>Working at Low Energy:</b> Rest/eat first for better results</li>
            </ul>
            
            <h3>🔥 Pro Player Strategies</h3>
            <ul>
                <li>🚀 <b>Morning Routine:</b> Login → Check challenges → Complete daily challenge</li>
                <li>🚀 <b>Skill Order:</b> Typing → Graphic Design → Writing → Web Dev → SEO → Management</li>
                <li>🚀 <b>Resource Cycle:</b> Work → Earn → Buy food → Restore → Repeat</li>
                <li>🚀 <b>Reputation Farm:</b> Do many small jobs vs few big ones</li>
                <li>🚀 <b>Achievement Synergy:</b> Complete jobs that progress multiple achievements</li>
            </ul>
            
            <h3>📱 Keyboard Shortcuts</h3>
            <ul>
                <li>⌨️ <b>ESC:</b> Exit fullscreen / Close game (with confirmation)</li>
                <li>⌨️ <b>F11:</b> Toggle fullscreen mode</li>
            </ul>
            
            <h3>💎 Hidden Mechanics</h3>
            <ul>
                <li>🔍 <b>Event Timing:</b> Events check every 5 minutes with 20% chance</li>
                <li>🔍 <b>Reputation Gain:</b> 30% chance on job completion</li>
                <li>🔍 <b>Level Up Heal:</b> Level up fully restores health & energy</li>
                <li>🔍 <b>Streak Grace:</b> You have 48 hours before streak breaks</li>
            </ul>
            
            <h3>🎮 Endgame Content</h3>
            <p>Once you reach Level 50+ with maxed reputation:</p>
            <ul>
                <li>✨ You earn <b>4x more</b> than beginners for same jobs</li>
                <li>✨ You have <b>all skills</b> unlocked</li>
                <li>✨ You maintain <b>30+ day streaks</b></li>
                <li>✨ You've completed <b>all achievements</b></li>
                <li>✨ You have <b>$100,000+</b> saved</li>
            </ul>
            
            <p style='background: #4caf50; color: white; padding: 15px; border-radius: 5px; text-align: center;'>
                <b>🏆 Remember: Consistency beats intensity! Login daily and you'll become a master!</b>
            </p>
            </body>
            </html>
            """;
        
        panel.add(createHtmlScroll(content), BorderLayout.CENTER);
        return panel;
    }
    
    // Create "About" tab panel
    private JPanel createAboutPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        String content = """
            <html>
            <body style='font-family: Segoe UI; padding: 15px;'>
            <h2 style='text-align: center;'>🎮 Job Quest Game</h2>
            <h3 style='text-align: center; color: #3f51b5;'>Professional Career Simulation RPG</h3>
            
            <hr>
            
            <h3>📖 About the Game</h3>
            <p>
                Job Quest is a comprehensive career simulation game where you start as a freelancer 
                and work your way up to becoming a legendary professional. The game features 
                multiple progression systems, achievements, challenges, and random events to keep 
                your journey exciting and rewarding.
            </p>
            
            <h3>✨ Key Features</h3>
            <ul>
                <li>🎨 <b>Modern Professional UI</b> - Material Design inspired interface</li>
                <li>⭐ <b>100 Levels</b> of progression with XP system</li>
                <li>🏆 <b>Reputation System</b> from Newbie to Legendary</li>
                <li>🔥 <b>Daily Streak</b> rewards for consistent play</li>
                <li>🎖️ <b>12 Achievements</b> worth $39,000+ in rewards</li>
                <li>📜 <b>Challenge System</b> with daily, weekly, and story challenges</li>
                <li>⚡ <b>15+ Random Events</b> for variety</li>
                <li>💰 <b>Dynamic Payment</b> system with multiple bonuses</li>
                <li>🎓 <b>6 Learnable Skills</b> to master</li>
                <li>💼 <b>4 Career Paths</b> to choose from</li>
            </ul>
            
            <h3>🎯 Game Statistics</h3>
            <table border='1' cellpadding='8' style='width: 100%; border-collapse: collapse;'>
                <tr><td><b>Total Jobs:</b></td><td>16+</td></tr>
                <tr><td><b>Skills to Learn:</b></td><td>6</td></tr>
                <tr><td><b>Achievements:</b></td><td>12</td></tr>
                <tr><td><b>Challenge Types:</b></td><td>10</td></tr>
                <tr><td><b>Random Events:</b></td><td>15</td></tr>
                <tr><td><b>Max Level:</b></td><td>100</td></tr>
                <tr><td><b>Career Paths:</b></td><td>4</td></tr>
                <tr><td><b>Playtime Content:</b></td><td>30+ hours</td></tr>
            </table>
            
            <h3>🛠️ Technologies</h3>
            <ul>
                <li>☕ <b>Java 17+</b> - Modern Java features</li>
                <li>🖼️ <b>Java Swing</b> - Professional GUI</li>
                <li>🗄️ <b>MySQL 8.0</b> - Database backend</li>
                <li>📦 <b>Maven</b> - Build automation</li>
            </ul>
            
            <h3>🏗️ Architecture</h3>
            <ul>
                <li>✅ <b>SOLID Principles</b> - All five principles implemented</li>
                <li>✅ <b>Design Patterns</b> - Singleton, Repository, Factory, Strategy, Observer</li>
                <li>✅ <b>Layered Architecture</b> - Presentation, Controller, Service, Repository</li>
                <li>✅ <b>Clean Code</b> - Professional practices throughout</li>
            </ul>
            
            <h3>👨‍💻 Developer</h3>
            <p>
                <b>Amir Khoshdel Louyeh</b><br>
                GitHub: <a href='https://github.com/amir-khoshdel-louyeh'>amir-khoshdel-louyeh</a><br>
                Repository: <a href='https://github.com/amir-khoshdel-louyeh/job-quest-game'>job-quest-game</a>
            </p>
            
            <h3>📚 Documentation</h3>
            <p>Comprehensive documentation available in the project folder:</p>
            <ul>
                <li>📘 <b>README.md</b> - Getting started guide</li>
                <li>📗 <b>GAME_LOGIC_GUIDE.md</b> - Detailed mechanics (400+ lines)</li>
                <li>📙 <b>ARCHITECTURE.md</b> - Design patterns and architecture</li>
                <li>📕 <b>COMPLETE_FEATURES.md</b> - Full feature list</li>
                <li>And 5 more documentation files!</li>
            </ul>
            
            <h3>🎉 Version Information</h3>
            <table border='1' cellpadding='8' style='width: 100%; border-collapse: collapse;'>
                <tr><td><b>Version:</b></td><td>1.0-SNAPSHOT</td></tr>
                <tr><td><b>Release Date:</b></td><td>October 2025</td></tr>
                <tr><td><b>Java Files:</b></td><td>66</td></tr>
                <tr><td><b>Code Lines:</b></td><td>10,000+</td></tr>
                <tr><td><b>Documentation Lines:</b></td><td>2,000+</td></tr>
            </table>
            
            <hr>
            
            <p style='text-align: center; background: #3f51b5; color: white; padding: 15px; border-radius: 5px;'>
                <b>🎮 Thank you for playing Job Quest! 🎮</b><br>
                <i>May your career be legendary!</i>
            </p>
            
            <p style='text-align: center; color: #666; font-size: 12px;'>
                © 2025 Amir Khoshdel Louyeh. All rights reserved.
            </p>
            </body>
            </html>
            """;
        
        panel.add(createHtmlScroll(content), BorderLayout.CENTER);
        return panel;
    }

    // Small helper that creates a scrollable HTML view from a string.
    private JScrollPane createHtmlScroll(String html) {
        JEditorPane editorPane = new JEditorPane("text/html", html);
        editorPane.setEditable(false);
        editorPane.setCaretPosition(0);
        return new JScrollPane(editorPane);
    }
}
