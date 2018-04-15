package cting.com.robineeee.ui.lib.clock.utils;

public class AnalogDecor {

    private int dialBg;
    private int handHour;
    private int handMinute;
    private int handSecond;

    public AnalogDecor() {
    }

    public AnalogDecor(AnalogDecor decor) {
        dialBg = decor.dialBg;
        handHour = decor.handHour;
        handMinute = decor.handMinute;
        handSecond = decor.handSecond;
    }

    public int getDialBg() {
        return dialBg;
    }

    public void setDialBg(int dialBg) {
        if (dialBg != -1) {
            this.dialBg = dialBg;
        }
    }

    public int getHandHour() {
        return handHour;
    }

    public void setHandHour(int handHour) {
        if (handHour != -1) {
            this.handHour = handHour;
        }
    }

    public int getHandMinute() {
        return handMinute;
    }

    public void setHandMinute(int handMinute) {
        if (handMinute != -1) {
            this.handMinute = handMinute;
        }
    }

    public int getHandSecond() {
        return handSecond;
    }

    public void setHandSecond(int handSecond) {
        if (handSecond != -1) {
            this.handSecond = handSecond;
        }
    }

    public static class Builder {
        AnalogDecor decor = new AnalogDecor();

        public AnalogDecor build() {
            return decor;
        }

        public Builder dialBg(int dialBg) {
            decor.dialBg = dialBg;
            return this;
        }

        public Builder handHour(int handHour) {
            decor.handHour = handHour;
            return this;
        }

        public Builder handMinute(int handMinute) {
            decor.handMinute = handMinute;
            return this;
        }

        public Builder handSecond(int handSecond) {
            decor.handSecond = handSecond;
            return this;
        }

    }
}
