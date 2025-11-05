package provider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.minigame.QuizQuestion;
import model.minigame.QuizQuestion.Category;

/**
 * Work questions for Doctor (diagnosis) and Logo Designer (client brief).
 *
 * <p>To add a question, add one line per category block. All questions are
 * single-best-answer with a short explanation shown after answering.</p>
 */
public final class QuizProvider {

    private static final List<QuizQuestion> DIAGNOSIS = new ArrayList<>();
    private static final List<QuizQuestion> DESIGN = new ArrayList<>();

    static {
        // ---- Doctor: online consultations ----
        DIAGNOSIS.add(new QuizQuestion("dx-1", Category.DIAGNOSIS,
                "Patient: fever 38.5°C, sore throat, no cough. Rapid strep test is positive. Best next step?",
                List.of("Prescribe amoxicillin and rest", "Order a chest X-ray", "Start insulin therapy"),
                0, "Positive strep + classic signs → antibiotics, not imaging."));
        DIAGNOSIS.add(new QuizQuestion("dx-2", Category.DIAGNOSIS,
                "Patient: crushing chest pain radiating to the left arm, sweating. First action in a video triage?",
                List.of("Recommend antacids and observe", "Call emergency services immediately", "Schedule a routine checkup"),
                1, "Classic heart-attack signs → emergency, every minute counts."));
        DIAGNOSIS.add(new QuizQuestion("dx-3", Category.DIAGNOSIS,
                "Child with itchy red eyes and sneezing every spring, no fever. Most likely cause?",
                List.of("Bacterial conjunctivitis", "Seasonal allergy", "Migraine"),
                1, "Seasonal pattern + itch + sneeze → allergy."));
        DIAGNOSIS.add(new QuizQuestion("dx-4", Category.DIAGNOSIS,
                "Patient on a new drug develops a widespread rash and lip swelling. What do you do?",
                List.of("Double the dose to build tolerance", "Stop the drug and seek urgent care", "Add another new drug"),
                1, "Possible drug allergy/angioedema → stop + urgent care."));
        DIAGNOSIS.add(new QuizQuestion("dx-5", Category.DIAGNOSIS,
                "Tired patient, pale skin, low hemoglobin on labs. Which advice fits best?",
                List.of("Iron-rich diet and follow-up labs", "Stop drinking water", "Avoid all exercise forever"),
                0, "Anemia pattern → iron + recheck, not extremes."));

        // ---- Logo Designer: client briefs ----
        DESIGN.add(new QuizQuestion("lg-1", Category.DESIGN,
                "Brief: eco-friendly baby-food startup, warm and trustworthy. Best concept?",
                List.of("Neon skull with sharp spikes", "Soft green leaf cradling a smile", "Black gothic letters on red"),
                1, "Soft organic shapes + green signal eco + care."));
        DESIGN.add(new QuizQuestion("lg-2", Category.DESIGN,
                "Brief: fintech app promising speed and security. Best concept?",
                List.of("Fast arrow locked in a shield", "Comic cat with a fish", "Watercolor flowers"),
                0, "Arrow = speed, shield = security, one idea per mark."));
        DESIGN.add(new QuizQuestion("lg-3", Category.DESIGN,
                "Client logo must work as a 16px favicon. What matters most?",
                List.of("Tiny serif details", "Bold simple silhouette", "A full landscape scene"),
                1, "Small sizes need bold simplicity, details vanish."));
        DESIGN.add(new QuizQuestion("lg-4", Category.DESIGN,
                "Brief: luxury watch brand, minimal and timeless. Best direction?",
                List.of("Six clashing neon colors", "Single thin monogram in black and gold", "Cartoon mascot waving"),
                1, "Luxury = restraint: one mark, premium palette."));
        DESIGN.add(new QuizQuestion("lg-5", Category.DESIGN,
                "Client asks for the logo text in 12 fonts at once. Best response?",
                List.of("Agree — more fonts look richer", "Explain hierarchy: max 2 fonts, show proof", "Quit the project silently"),
                1, "Professionals educate: 1–2 typefaces keep brands readable."));
    }

    private QuizProvider() {
    }

    public static List<QuizQuestion> getDiagnosisQuestions() {
        return Collections.unmodifiableList(DIAGNOSIS);
    }

    public static List<QuizQuestion> getDesignQuestions() {
        return Collections.unmodifiableList(DESIGN);
    }
}
