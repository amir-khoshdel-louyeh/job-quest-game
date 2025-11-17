package model;

// Which mini-game a profession plays. Replaces the old "TYPING"/"CODE"/
// "DIAGNOSIS"/"DESIGN" magic strings: the compiler now checks every
// routing decision, and typos fail at compile time instead of silently
// falling through to "unknown game kind" at runtime.
public enum WorkGameKind {
    TYPING,
    CODE,
    DIAGNOSIS,
    DESIGN
}
