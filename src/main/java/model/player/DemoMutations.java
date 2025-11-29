package model.player;

// Demo-only exact-value mutations used by the developer panel.
// A separate collaborator rather than a nested class: User composes it from
// its own components, so the cheat paths never widen User's public API.
// Game rules never depend on this class.
final class DemoMutations implements DevControls {
    private final Wallet wallet;
    private final Vitals vitals;
    private final CareerProgress progress;
    private final JobRecord jobRecord;
    private final PlayClock playClock;
    private final BlockStatus blockStatus;

    DemoMutations(Wallet wallet, Vitals vitals, CareerProgress progress,
                  JobRecord jobRecord, PlayClock playClock, BlockStatus blockStatus) {
        this.wallet = utils.Require.notNull(wallet, "wallet");
        this.vitals = utils.Require.notNull(vitals, "vitals");
        this.progress = utils.Require.notNull(progress, "progress");
        this.jobRecord = utils.Require.notNull(jobRecord, "jobRecord");
        this.playClock = utils.Require.notNull(playClock, "playClock");
        this.blockStatus = utils.Require.notNull(blockStatus, "blockStatus");
    }

    @Override
    public void setBalance(int amount) {
        wallet.setBalanceForCheat(amount);
    }

    @Override
    public void setHealth(int health) {
        vitals.setHealthForCheat(health);
    }

    @Override
    public void setEnergy(int energy) {
        vitals.setEnergyForCheat(energy);
    }

    @Override
    public void setLevel(int level) {
        progress.setLevelForCheat(level);
    }

    @Override
    public void setReputation(int reputation) {
        progress.setReputationForCheat(reputation);
    }

    @Override
    public void setJobBonus(int jobBonus) {
        jobRecord.setJobBonusForCheat(jobBonus);
    }

    @Override
    public void addPlayTimeMinutes(long minutes) {
        playClock.addMillis(minutes * utils.DurationText.MILLIS_PER_MINUTE);
    }

    @Override
    public void setPlayTimeMinutes(long minutes) {
        playClock.setMinutesForCheat(minutes);
    }

    @Override
    public void unblock() {
        blockStatus.unblock();
    }
}
