package provider;

import java.util.ArrayList;
import java.util.List;

import model.minigame.QuizQuestion;

// Holds the quiz questions for the Doctor and Designer mini-games.
// Instance repository (not static holder): the catalog comes from outside,
// so launchers can inject/mock it (DIP) — same pattern as the others.
public final class QuizProvider {

    private final ArrayList<QuizQuestion> diagnosis;
    private final ArrayList<QuizQuestion> design;

    public QuizProvider() {
        this(buildDiagnosisCatalog(), buildDesignCatalog());
    }

    // Test seam: custom catalogs make every run reproducible.
    public QuizProvider(List<QuizQuestion> diagnosis, List<QuizQuestion> design) {
        if (diagnosis == null || design == null) {
            throw new IllegalArgumentException("quiz catalogs must not be null");
        }
        this.diagnosis = new ArrayList<QuizQuestion>(diagnosis);
        this.design = new ArrayList<QuizQuestion>(design);
    }

    private static ArrayList<QuizQuestion> buildDiagnosisCatalog() {
        ArrayList<QuizQuestion> diagnosis = new ArrayList<QuizQuestion>();
        // Doctor questions
        diagnosis.add(new QuizQuestion("dx-1", QuizQuestion.Category.DIAGNOSIS,
                "Patient: fever 38.5, sore throat, no cough. Rapid strep test is positive. Best next step?",
                makeList3("Prescribe amoxicillin and rest", "Order a chest X-ray", "Start insulin therapy"),
                0, "Positive strep + classic signs -> antibiotics, not imaging."));
        diagnosis.add(new QuizQuestion("dx-2", QuizQuestion.Category.DIAGNOSIS,
                "Patient: crushing chest pain radiating to the left arm, sweating. First action in a video triage?",
                makeList3("Recommend antacids and observe", "Call emergency services immediately", "Schedule a routine checkup"),
                1, "Classic heart-attack signs -> emergency, every minute counts."));
        diagnosis.add(new QuizQuestion("dx-3", QuizQuestion.Category.DIAGNOSIS,
                "Child with itchy red eyes and sneezing every spring, no fever. Most likely cause?",
                makeList3("Bacterial conjunctivitis", "Seasonal allergy", "Migraine"),
                1, "Seasonal pattern + itch + sneeze -> allergy."));
        diagnosis.add(new QuizQuestion("dx-4", QuizQuestion.Category.DIAGNOSIS,
                "Patient on a new drug develops a widespread rash and lip swelling. What do you do?",
                makeList3("Double the dose to build tolerance", "Stop the drug and seek urgent care", "Add another new drug"),
                1, "Possible drug allergy -> stop + urgent care."));
        diagnosis.add(new QuizQuestion("dx-5", QuizQuestion.Category.DIAGNOSIS,
                "Tired patient, pale skin, low hemoglobin on labs. Which advice fits best?",
                makeList3("Iron-rich diet and follow-up labs", "Stop drinking water", "Avoid all exercise forever"),
                0, "Anemia pattern -> iron + recheck, not extremes."));
        return diagnosis;
    }

    private static ArrayList<QuizQuestion> buildDesignCatalog() {
        ArrayList<QuizQuestion> design = new ArrayList<QuizQuestion>();
        // Designer questions
        design.add(new QuizQuestion("lg-1", QuizQuestion.Category.DESIGN,
                "Brief: eco-friendly baby-food startup, warm and trustworthy. Best concept?",
                makeList3("Neon skull with sharp spikes", "Soft green leaf cradling a smile", "Black gothic letters on red"),
                1, "Soft organic shapes + green signal eco + care."));
        design.add(new QuizQuestion("lg-2", QuizQuestion.Category.DESIGN,
                "Brief: fintech app promising speed and security. Best concept?",
                makeList3("Fast arrow locked in a shield", "Comic cat with a fish", "Watercolor flowers"),
                0, "Arrow = speed, shield = security, one idea per mark."));
        design.add(new QuizQuestion("lg-3", QuizQuestion.Category.DESIGN,
                "Client logo must work as a 16px favicon. What matters most?",
                makeList3("Tiny serif details", "Bold simple silhouette", "A full landscape scene"),
                1, "Small sizes need bold simplicity, details vanish."));
        design.add(new QuizQuestion("lg-4", QuizQuestion.Category.DESIGN,
                "Brief: luxury watch brand, minimal and timeless. Best direction?",
                makeList3("Six clashing neon colors", "Single thin monogram in black and gold", "Cartoon mascot waving"),
                1, "Luxury = restraint: one mark, premium palette."));
        design.add(new QuizQuestion("lg-5", QuizQuestion.Category.DESIGN,
                "Client asks for the logo text in 12 fonts at once. Best response?",
                makeList3("Agree — more fonts look richer", "Explain hierarchy: max 2 fonts, show proof", "Quit the project silently"),
                1, "Professionals educate: 1-2 typefaces keep brands readable."));
        return design;
    }

    private static ArrayList<String> makeList3(String a, String b, String c) {
        ArrayList<String> list = new ArrayList<String>();
        list.add(a);
        list.add(b);
        list.add(c);
        return list;
    }

    public List<QuizQuestion> getDiagnosisQuestions() {
        ArrayList<QuizQuestion> copy = new ArrayList<QuizQuestion>();
        for (int i = 0; i < diagnosis.size(); i++) {
            copy.add(diagnosis.get(i));
        }
        return copy;
    }

    public List<QuizQuestion> getDesignQuestions() {
        ArrayList<QuizQuestion> copy = new ArrayList<QuizQuestion>();
        for (int i = 0; i < design.size(); i++) {
            copy.add(design.get(i));
        }
        return copy;
    }
}
